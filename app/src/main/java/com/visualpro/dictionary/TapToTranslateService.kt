package com.visualpro.dictionary


import android.animation.ObjectAnimator
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.Intent.*
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.visualpro.dictionary.ui.float_translate.FloatTranslate
import com.visualpro.dictionary.ui.float_translate.FloatTranslate.Companion.ACTION_COPY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class TapToTranslateService() : Service(), ClipboardManager.OnPrimaryClipChangedListener {
    private val IBinder = mBinder()
    private var clipboardText1 = ""

    inner class mBinder() : Binder() {
        fun getInstance() = this@TapToTranslateService
    }

    companion object {
        const val CHANNEL_ID="c id"
        const val HIDE_FLOAT_ICON = "hide"
        const val SHOW_FLOAT_ICON = "show"
        const val DATA_CLIPBOARD = "data clipboard"
        var currentClipboardDataNeedToTranslate = true
    }

    private val mCorountine = CoroutineScope(IO)
    private lateinit var image: ImageView

    private var clipboard: ClipboardManager? = null
    private lateinit var mWindowManager: WindowManager
    private lateinit var paramsF: WindowManager.LayoutParams
    private var mHandler = Handler(Looper.myLooper()!!)

    private fun initFloatBubble() {
        image = ImageView(this)
        image.setImageResource(com.visualpro.dictionary.R.drawable.aic_main_icon1)
        image.setVisibility(View.VISIBLE)
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paramsF = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            paramsF = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        paramsF.gravity = Gravity.TOP or Gravity.START
        paramsF.x = 0
        paramsF.y = 100
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(this)
            } else {
                TODO("VERSION.SDK_INT < M")
            }

        mWindowManager.addView(image, paramsF)
        hideIconWithAlphaAnim(3000)
        try {
            image.setOnTouchListener(object : View.OnTouchListener {

                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = paramsF.x
                            initialY = paramsF.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                        }
                        MotionEvent.ACTION_MOVE -> {
                            paramsF.x = initialX + (event.rawX - initialTouchX).toInt()
                            paramsF.y = initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager.updateViewLayout(v, paramsF)
                        }
                    }
                    return false
                }
            })
            image.setOnClickListener { v ->
                hideIconWithAlphaAnim(0)
                hideNotification()
                mCorountine.launch {
                    Intent(this@TapToTranslateService, FloatTranslate::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(if (FloatTranslate.isStarted) FLAG_ACTIVITY_SINGLE_TOP else Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        putExtra(DATA_CLIPBOARD, clipboardText1)
                        startActivity(this)

                    }

                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideIconWithAlphaAnim(delay: Long) {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({

            mHandler.postDelayed({
                image.visibility = View.GONE
                displayNotification()
            }, delay + 500)

            ObjectAnimator.ofFloat(image, "alpha", 0.0f).apply {
                duration = 500
                start()
            }
        }, delay)

    }

    override fun onCreate() {
        super.onCreate()
        registerClipBoardChange()
        initFloatBubble()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        displayNotification()
        if (intent != null) {
            if (intent.hasExtra(HIDE_FLOAT_ICON)) {
                hideIconWithAlphaAnim(0)
            } else if (intent.hasExtra(SHOW_FLOAT_ICON)) {
                image.alpha = 1f
                image.visibility = View.VISIBLE
                hideIconWithAlphaAnim(3000)
            }

            if(intent.hasExtra(ACTION_COPY)){
                currentClipboardDataNeedToTranslate=false
                clipboard!!.setPrimaryClip(ClipData(ClipData.newPlainText("",intent.getStringExtra(ACTION_COPY))))
            }
        }


        return START_REDELIVER_INTENT
    }

    fun displayNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, FloatTranslate::class.java).let { notificationIntent ->
                notificationIntent.putExtra(DATA_CLIPBOARD, clipboardText1)
                notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK)
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.fserviceruuning))
            .setContentText(clipboardText1)
            .setColor(ContextCompat.getColor(this, R.color.orange2))
            .setSmallIcon(R.drawable.aic_main_icon)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    fun hideNotification() {
        stopForeground(true)
    }

    fun updateText() {
        val pendingIntent: PendingIntent =
            Intent(this, FloatTranslate::class.java).let { notificationIntent ->
                notificationIntent.putExtra(DATA_CLIPBOARD, clipboardText1)
                notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                notificationIntent.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK)

                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        val snoozeIntent = Intent(this, FloatTranslate::class.java).apply {
//            action = ACTION_SNOOZE
//            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, snoozeIntent, 0)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.word_detect))
            .setContentText(clipboardText1)
            .setSmallIcon(R.drawable.aic_main_icon)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.aic_main_icon, "TRANSLATE",
                snoozePendingIntent)
            .build()

        val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(1, notification)
    }

    private fun registerClipBoardChange() {
        if (clipboard == null) {
            clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard!!.addPrimaryClipChangedListener(this)
        }
    }

    override fun onDestroy() {
        mWindowManager.removeView(image)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?) = IBinder


    var clipboardText = MutableSharedFlow<String>()
    override fun onPrimaryClipChanged() {
        if (currentClipboardDataNeedToTranslate){
            if(clipboard!!.hasPrimaryClip()) {
                val data = clipboard!!.getPrimaryClip();
                val text = data?.getItemAt(0)?.getText().toString().trim();
                clipboardText1 = text
                image.alpha = 1f
                image.visibility = View.VISIBLE
                Log.d("test", "onPrimaryClipChanged: ")

                ObjectAnimator.ofFloat(
                    image, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f
                ).setDuration(500).start();

                hideIconWithAlphaAnim(3000)
                updateText()
//            }


            }
        } else{
            currentClipboardDataNeedToTranslate=true
        }



    }

}