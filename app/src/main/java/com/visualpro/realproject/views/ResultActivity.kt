package com.visualpro.realproject.views

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemNearByWordClick
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemRecyclerViewCkick
import com.visualpro.realproject.adapter.DefinitionAdapter
import com.visualpro.realproject.adapter.NearByWordAdapter
import com.visualpro.realproject.databinding.ActivityResultBinding
import com.visualpro.realproject.viewmodel.ResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Reader

class ResultActivity : AppCompatActivity(), onItemRecyclerViewCkick, onItemNearByWordClick,
    dataSave {
    companion object {

        var localFilePath = ""
        const val DATA_DEFINITION = "load definition"
        const val INTENT_ID = "intent id"
        const val SOUND_TEMPORARY_US = "temporary.us"
        const val SOUND_TEMPORARY_UK = "temporary.uk"
    }

    private val mViewModel: ResultViewModel by viewModels {
        ResultViewModel.ResultViewModelFactory(
            (application as MainApplication).repository,
            filesDir.toString()
        )
    }
    private var isSimpTransShow: Boolean = false
    private var currentItem_NearByWord = -1

    private lateinit var bind: ActivityResultBinding
    private lateinit var mNearByWordAdapter: NearByWordAdapter
    private lateinit var mDefinitionAdapter: DefinitionAdapter
    private var dialog: SaveWordDialog? = null
    private lateinit var mViewBottomDialog: View
    private lateinit var mGoButton_NearByWord: Button

    private var mCoroutine: CoroutineScope? = null
    private var mCoroutine2: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "onCreate: ")
        super.onCreate(savedInstanceState)
        mCoroutine = CoroutineScope(Dispatchers.Main)
        mCoroutine2 = CoroutineScope(Dispatchers.IO)
        val job = mCoroutine!!.launch {
            bind = ActivityResultBinding.inflate(layoutInflater)
//                bind.lifecycleOwner = this
            setContentView(bind.root)
            initView()
            onNewIntent(intent)
            localFilePath = filesDir.toString()


        }

    }

    override fun onNewIntent(intent: Intent?) {
        mCoroutine!!.launch {
            Log.d("test", "onNewIntwente: ")
            if (intent != null && intent.hasExtra(INTENT_ID)) {
                when (intent.getIntExtra(INTENT_ID, 0)) {
                    0 -> print("DF")
                    1 -> {
                        mViewModel.getWordFromServer(intent.getStringExtra(DATA_DEFINITION), "", false, 0, 0)
                    }
                    2 -> print("D")
                    else -> {}
                }


            }

        }
        return super.onNewIntent(intent)


    }

    private fun initView() {
        bind.viewModel = mViewModel

        bind.reCyclerViewList.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        bind.reCyclerViewList.itemAnimator = null
        mDefinitionAdapter = DefinitionAdapter(this)
        bind.reCyclerViewList.adapter = mDefinitionAdapter


        var srcLang = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.countries_array)
        )
        var desLang = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.countries_array)
        )
        bind.spinnerSourceLang.adapter = srcLang
        bind.spinnerDestinationLang.adapter = desLang

        bind.txtTransoutput.movementMethod = ScrollingMovementMethod()


        initListeners()
    }
    fun getFileContent(fis: FileInputStream?): String? {
        val sb = StringBuilder()
        val r: Reader = InputStreamReader(fis, "UTF-8") //or whatever encoding
        val buf = CharArray(1024)
        var amt: Int = r.read(buf)
        while (amt > 0) {
            sb.append(buf, 0, amt)
            amt = r.read(buf)
        }
        return sb.toString()
    }

    private fun initListeners() {
        mNearByWordAdapter = NearByWordAdapter(this)
        mViewBottomDialog = LayoutInflater.from(applicationContext).inflate(R.layout.layout_nearbyword, null, false)
        mGoButton_NearByWord = mViewBottomDialog.findViewById(R.id.btn_Go)

        mGoButton_NearByWord.setOnClickListener(View.OnClickListener {
            mViewModel!!.performLoadNearByWord(
                mNearByWordAdapter.mList.get(currentItem_NearByWord).word,
                mNearByWordAdapter.mList.get(currentItem_NearByWord).urlToThisWord
        )
        })
//        bind.reload.setOnClickListener(View.OnClickListener {
//            mCoroutine2!!.launch {
//                mViewModel.mRepo.performSearches()
//                var time= measureTimeMillis {
//                    var x =mViewModel!!.mRepo.performParseHtml(bind.ttt.text.toString().trim().toLowerCase(), "", false,false,0,0)
////                   for (i in x.defList.indices){
////                       Log.d("test", "${x.defList[i]}")
////                   }
//                }
//                Log.d("test", "${time}")
//
//
//            }
//            val file= File(filesDir.toString()+"/list.txt")
//            var content= getFileContent(file.inputStream())
//            var c=content!!.toCharArray()
//            var set=HashSet<String>()
//            var listSorted=ArrayList<String>()
//            var i=0
//            var s=""
//            while (i<c.size-1){
//                if(c[i].isLetter()){
//                    s+=c[i]
//                    i++
//                }else{
//                    listSorted.add(s.toLowerCase())
//                    s=""
//                    while (i<c.size-1 && !c[i].isLetter()){
//                        i++
//                    }
//                }
//
//
//            }
//         val x= listSorted.toSortedSet()
//            var processed=0
//            var size =x.size-1
//
//         mCoroutine2!!.launch {
//          x.forEach {
//              t->
//              if(t.length>1)
//              processed++
//              mViewModel.getWordFromServer(t, "", false, processed, size)
//              delay(50)
//
//          }
//
//         }
//            print(listSorted.size)



//            val rcv = mViewBottomDialog.findViewById<RecyclerView>(R.id.rcv_nearby_word)
//            rcv.adapter = mNearByWordAdapter
//            rcv.layoutManager = LinearLayoutManager(this, VERTICAL, false)
//            rcv.setHasFixedSize(true)
//            val dialog = BottomSheetDialog(this)
//
//            mNearByWordAdapter.mList = mViewModel.getWord()!!.nearByWord
//            dialog.setContentView(mViewBottomDialog)
//            dialog.show()
//        }})
        bind.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        bind.btnExpandEdtTranInput.setOnClickListener { view ->
            val expand =
                bind.edtTransInput.visibility == VISIBLE
            bind.edtTransInput.setVisibility(if (expand) GONE else VISIBLE)
            bind.btnExpandEdtTranInput.setImageResource(if (expand) R.drawable.ic_baseline_keyboard_arrow_up_24 else R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

        bind.speakerUK.setOnClickListener({ mViewModel!!.playSoundUk() })

        bind.speakerUS.setOnClickListener { mViewModel!!.playSoundUs() }
        bind.spinnerDestinationLang.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val strArr = resources.getStringArray(R.array.countries_array)
                    TODO()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        bind.btnClearText.setOnClickListener({ bind.edtTransInput.setText("") })

        bind.edtTransInput.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_DONE) {
                    TODO("Import room")
                }
                return true
            }

        })

        bind.btnExitfullscreen.setOnClickListener({ view ->

        })
        bind.btnSave2.setOnClickListener { view ->
            mViewModel.mRepo.getListCategoryName()
            val name = mViewModel.getWord()?.word
            mViewModel.mListCategoryName.observe(this, Observer {
                if (dialog == null) {
                    dialog = SaveWordDialog(name, it, this)
                    dialog!!.show(supportFragmentManager, "S")
                }

            })
        }
    }


    override fun clickAtSearchItem(position: Int) {
        TODO("Not yet implemented")
    }

    private fun setSimpleTransHide1() {

        isSimpTransShow = false
        bind.btnClearText.setVisibility(GONE)
        bind.edtTransInput.setVisibility(GONE)
        bind.txtTransoutput.setVisibility(GONE)
        bind.relativeLayout2.setVisibility(GONE)
        bind.btnCopy.setVisibility(GONE)
        bind.btnExpandEdtTranInput.setVisibility(GONE)
        setDefinition_views_Show()
    }

    private fun setSimpleTransShow1() {
        setDefinition_views_Hide()
        isSimpTransShow = true
        bind.btnClearText.setVisibility(VISIBLE)
        bind.edtTransInput.setVisibility(VISIBLE)
        bind.txtTransoutput.setVisibility(VISIBLE)
        bind.relativeLayout2.setVisibility(VISIBLE)
        bind.btnCopy.setVisibility(VISIBLE)
        bind.btnExpandEdtTranInput.setVisibility(VISIBLE)
    }

    private fun setDefinition_views_Hide() {
        bind.linearLayout2.setVisibility(GONE)
        bind.reCyclerViewList.setVisibility(GONE)
//        bind.rcvNearbyWord.setVisibility(GONE)
        bind.layoutExpanableDefinition.setVisibility(GONE)
        bind.layoutHeader.setVisibility(GONE)
        bind.txtDefinitionOf.setVisibility(GONE)
        bind.txtDefinition1.visibility = GONE
    }

    private fun setDefinition_views_Show() {
        bind.linearLayout2.setVisibility(VISIBLE)
        bind.reCyclerViewList.setVisibility(VISIBLE)
        bind.layoutExpanableDefinition.setVisibility(VISIBLE)
        bind.txtDefinitionOf.setVisibility(VISIBLE)
        bind.layoutExpanableDefinition.setVisibility(VISIBLE)
        bind.layoutHeader.setVisibility(VISIBLE)
    }

    override fun nearByWordClick(position: Int) {
        currentItem_NearByWord = position

    }

    override fun saveCurrentOpeningWord(position: Int) {
        if (position == -1) {
            dialog = null
            return
        }
        mViewModel.saveWordToDb(position)
        dialog = null
    }


}


