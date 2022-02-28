package com.visualpro.dictionary.repositories

import android.content.SharedPreferences
import android.media.SoundPool
import android.util.Log
import com.google.gson.GsonBuilder
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.dictionary.R
import com.visualpro.dictionary.TapToTranslateService
import com.visualpro.dictionary.interfaces.CallbackSoundLoaded
import com.visualpro.dictionary.interfaces.Ox_SearchService
import com.visualpro.dictionary.model.*
import com.visualpro.dictionary.model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.dictionary.model.model_relations.Word_DefinitionList_Ref
import com.visualpro.dictionary.repositories.network.Api_Configs.Companion.BASE_URL
import com.visualpro.dictionary.repositories.network.local_db.User_DAO
import com.visualpro.dictionary.repositories.network.submodel.SearchText
import com.visualpro.dictionary.ui.MainActivity2.Companion.SOUND_TEMPORARY_UK
import com.visualpro.dictionary.ui.MainActivity2.Companion.SOUND_TEMPORARY_US
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.launch as launch1


class Repository(private val userDao: User_DAO) : Serializable {
    private var soundUk = -1
    private var soundUs = -1
    var categoryData = userDao.getAllCategory()
    var recentItems = userDao.get6ItemRecent()

    //    var listCategoryName = userDao.getListCategorysName()
    fun getListCategoryName(callback: (List<String>) -> Unit) {
        mCoroutine.launch1 {
            val list = userDao.getListCategorysName()
            withContext(Main) {
                callback(list)
            }
        }

    }

    var dailyWord = userDao.getDailyWord()
    var sharedPreferences: SharedPreferences? = null

    var soundCallBack: CallbackSoundLoaded? = null
    var localFilePath = ""

    var service: TapToTranslateService? = null


    private var pool: SoundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val mCoroutine = CoroutineScope(IO)
    private val oxSearchservice: Ox_SearchService = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
        .create(Ox_SearchService::class.java)
//    private val ggTranslateservice: Gg_TranslateService =
//        Retrofit.Builder().baseUrl(Api_Configs.BASE_URL_GG)
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
//            .create(Gg_TranslateService::class.java)

    companion object {
        const val LANGUAGE_INPUT_INDEX = "input index"
        const val LANGUAGE_OUTPUT_INDEX = "output index"
        const val TEMP_URL1 = "https://www.oxfordlearnersdictionaries.com/us/search/english/?q="
        const val isAutoDetectInputlanguge = "detect"
        const val isFirstSelectLanguageSucess = "first select language"
        const val TAP_TO_TRANSLATE_ENABLE = "tttl enable"
    }

    private val URL1 = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl="
    private val URL2 = "&dt=t&dt=bd&dj=1&q="
    var usSoundLoaded = false
    var ukSoundLoaded = false


//    fun performTranslate1(text: String) {
//        mCoroutine.launch1 {
//            val respond: TranslateItems =
//                ggTranslateservice.translate("gtx", "auto", "vi", "t", "bd", 1, text)
////            withContext(Main){
////                translateInterface!!.translateResponse(respond)
////            }
//
//        }
//
//    }

    fun getDailyWord() {
        mCoroutine.launch1 {
            isDailyWordLoaded {
                if (!it) {
                    val doc: Document?
                    try {
                        doc = Jsoup.connect("https://www.oxfordlearnersdictionaries.com").get()
                        if (doc != null) {
                            val master = doc.selectFirst("a[class=headword]")
                            val text = master!!.text()
                            val link = master.attr("href")
                            loadWord(text, link, true, false)
                        }
                    } catch (ex: java.lang.Exception) {
                    }
                }

            }
        }
    }

    fun setDailyWordLoaded() {
        val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        sharedPreferences!!.edit().putBoolean(currentDate, true).apply()

    }

    fun isDailyWordLoaded(callback: (isLoad: Boolean) -> Unit) {
        val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        callback(sharedPreferences!!.getBoolean(currentDate, false))
    }

    fun firstSetUpSelectLanguageDone() {
        mCoroutine.launch1 {
            sharedPreferences?.apply {
                edit().putBoolean(isFirstSelectLanguageSucess, true).apply()
            }
        }

    }

    fun getFirstSetUpSelectLanguageSuccess(callback: (isDone: Boolean) -> Unit) {
        mCoroutine.launch1 {
            val isDone = sharedPreferences!!.getBoolean(isFirstSelectLanguageSucess, false)
            withContext(Main) {
                callback(isDone)
            }
        }
    }


    fun search(word: String, callback: (listHint: ArrayList<SearchText>) -> Unit) {
        mCoroutine.launch1 {
            val respond = oxSearchservice.search(word, "application/json;charset=utf-8")
            withContext(Main) {
                callback(respond.resposeList)
            }


        }
    }

    fun translate(
        word: String, destinationLang: String, callback: (response: TranslateItems) -> Unit
    ) {
        val text = word.replace(" ", "%20")
        mCoroutine.launch1 {
            val con: HttpURLConnection
            var `in`: BufferedReader? = null
            try {
                con =
                    URL(URL1 + destinationLang + URL2 + text).openConnection() as HttpURLConnection

                con.setRequestProperty("User-Agent", R.string.request_property.toString())
                `in` = BufferedReader(InputStreamReader(con.inputStream))
                var inputLine: String?
                val response = StringBuilder()
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                val json = JSONObject(response.toString())
                val arr = json.getJSONArray("sentences")
                val result = StringBuilder()
                for (i in 0 until arr.length()) {
                    val obj = arr[i] as JSONObject
                    result.append(obj.getString("trans"))
                }
                withContext(Main) {
                    callback(TranslateItems(word, result.toString()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    `in`?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun getOxfordURL(word: String) = TEMP_URL1 + word.replace(" ", "+")
    fun retrieveLastestQueryWord(callBack: (lastWord: Word_DefinitionList_Ref?) -> Unit) {
        mCoroutine.launch1 {
            val word = userDao.getItemRecent()
            if (word != null) {
                withContext(Main) {
                    callBack(word)
                }
            }
        }
    }

    fun loadWord(
        word: String,
        url: String,
        useUrl: Boolean,
        updateToDefinition: Boolean,
        callback: ((response: Word_DefinitionList_Ref?) -> Unit)? = null
    ) {
        usSoundLoaded = false
        ukSoundLoaded = false
        mCoroutine.launch1 {
            val localWord = userDao.selectWord(word)
            if (localWord != null) {
                if (updateToDefinition) {
                    withContext(Main) {
                        callback?.invoke(localWord)
                    }
                } else {
                    val day = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(Date())
                    val definition = localWord.defList
                    val dailyWord = DailyWord(day, word, definition[1]!!.definition)
                    userDao.insertDailyWord(dailyWord)
                    setDailyWordLoaded()
                    Log.d("test", "get daily word ")
                }

            } else {
                val doc: Document?
                try {
                    doc =
                        if (useUrl && !url.equals("")) Jsoup.connect(url).get() else Jsoup.connect(
                            getOxfordURL(word)
                        ).get()
                    if (doc != null) {
                        val master = doc.selectFirst("div[class=responsive_entry_center_wrap]")
                        if (master != null) {
                            val parser = HtmlParseHelper(master)
                            val allNearByWord = async { parser.parseNearByWord() }
                            val audioUrlArray = async { parser.parseSoundUrl_Pron() }
                            val defList = async { parser.parseDefinition() }
                            val type = async { parser.parseType() }

                            val definitionList = defList.await()
                            val listNearByWord = allNearByWord.await()

                            var word1 = Word(
                                word,
                                type.await(),
                                audioUrlArray.await().get(0),
                                audioUrlArray.await().get(1),
                                audioUrlArray.await().get(2),
                                audioUrlArray.await().get(3),
                                allNearByWord.await()
                            )
//                            downloadSound(word1.auUrlUs, word1.auUrlUk, word1.word)
                            val listX = ArrayList<Separate_DefinitionList?>()
                            val nullDefinition = Definition()

                            nullDefinition.belongToWord = word1.getPK()
                            nullDefinition.isNull = true


                            for (i in listNearByWord.indices) {
                                if (word.trim().equals(listNearByWord.get(i).word.trim())) {
                                    listX.add(returnRelateWord(listNearByWord.get(i).urlToThisWord))
                                }
                            }

                            var separate = WordTypeSeparate().apply {
                                begin = 0
                                end = definitionList.size
                                wordType = word1.wordType
                                wordRef = word1.getPK()
                            }


                            userDao.insertWord(word1)
                            userDao.insertWordTypeSeparate(separate)

                            for (i in definitionList.indices) {
                                if (i == 0) {
                                    userDao.insertDefinition(nullDefinition)
                                }
                                definitionList[i]!!.belongToWord = word1.getPK()
                                userDao.insertDefinition(definitionList[i])
                            }

                            for (i in listX.indices) {
                                if (listX[i] != null) {
                                    listX[i]?.separate!!.begin = separate.end + 1
                                    listX[i]?.separate!!.end =
                                        listX[i]?.separate!!.begin + listX[i]!!.list.size
                                    listX[i]?.separate!!.wordRef = word1.getPK()

                                    separate = listX[i]?.separate!!
                                    userDao.insertWordTypeSeparate(listX[i]?.separate!!)

                                    for (j in listX[i]!!.list.indices) {
                                        if (j == 0) {
                                            userDao.insertDefinition(nullDefinition)
                                        }
                                        listX[i]!!.list[j]?.belongToWord = word1.getPK()
                                        userDao.insertDefinition(listX[i]!!.list[j])
                                    }
                                }
                            }

                            val localWord1 = userDao.selectWord(word)
                            if (localWord1 != null) {
                                if (updateToDefinition) {
                                    withContext(Main) {
                                        callback?.invoke(localWord1)
                                    }

                                } else {
                                    val day = SimpleDateFormat(
                                        "dd.MM.yy", Locale.getDefault()
                                    ).format(Date())
                                    val definition = localWord1.defList
                                    val dailyWord = DailyWord(day, word, definition[1]!!.definition)
                                    userDao.insertDailyWord(dailyWord)
                                    setDailyWordLoaded()
                                    Log.d("test", "get daily word ")
                                }


                            }

                        }
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
    }

    fun returnRelateWord(url: String): Separate_DefinitionList? {
        val doc: Document?
        try {
            doc = Jsoup.connect(url).get()
            if (doc != null) {
                val master = doc.selectFirst("div[class=responsive_entry_center_wrap]")
                if (master != null) {
                    val parser = HtmlParseHelper(master)
                    val defList = parser.parseDefinition()
                    val type = parser.parseType()
                    val separate = WordTypeSeparate()
                    separate.wordType = type
                    separate.begin = 0
                    separate.end = defList.size
                    return Separate_DefinitionList(separate, defList)

                }
            }


        } catch (exception: Exception) {

        }
        return null
    }

    fun playSoundUs(soundLink: String?) {
        if (!usSoundLoaded && !usLoading) {
            downloadSound(soundLink, null)


        } else if (usSoundLoaded) {
            pool.play(soundUs, 1f, 1f, 1, 0, 1f)
        }
    }


    fun playSoundUk(soundLink: String?) {
        if (!ukSoundLoaded && !ukLoading) {
            downloadSound(null, soundLink)
        } else if (ukSoundLoaded) {
            pool.play(soundUk, 1f, 1f, 1, 0, 1f)
        }


    }

    private var ukLoading = false
    private var usLoading = false

    @Suppress("BlockingMethodInNonBlockingContext")
    fun downloadSound(urlUs: String?, urlUk: String?) {
        if (urlUs != null) {
            usLoading = true
            mCoroutine.launch1 {
                var bIn: BufferedInputStream? = null
                var bOut: BufferedOutputStream? = null
                var fOut: FileOutputStream? = null
                val http: HttpURLConnection

                val fiLe = File(localFilePath + "/" + SOUND_TEMPORARY_US)
                if (!fiLe.exists()) {
                    fiLe.createNewFile()
                }
                try {
                    fOut = FileOutputStream(fiLe)
                    http = URL(urlUs).openConnection() as HttpURLConnection
                    bIn = BufferedInputStream(http.inputStream)
                    bOut = BufferedOutputStream(fOut, 1024)
                    var buffer = ByteArray(1024)
                    while (true) {
                        val read = bIn.read(buffer, 0, 1024)
                        if (read == -1) {
                            break
                        }
                        bOut.write(buffer, 0, read)
                    }

                    soundUs = pool.load(localFilePath + "/" + SOUND_TEMPORARY_US, 1)
                    pool.setOnLoadCompleteListener({ soundPool, i, i2 ->
                        usSoundLoaded = true
                        pool.play(soundUs, 1f, 1f, 1, 0, 1f)
                    })
                    withContext(Main) {
                        soundCallBack!!.onSoundLoadComplete("US")
                    }
                } catch (exception: Exception) {
                    Log.d("test", "downloadSound: ${exception.message}")
                    withContext(Main) {
                        soundCallBack!!.onSoundLoadFail("US")
                    }

                } finally {
                    usLoading = false
                    try {
                        if (fOut != null) {
                            fOut.close()
                        }
                        if (bOut != null) {
                            bOut.close()
                        }
                        if (bIn != null) {
                            bIn.close()
                        }
                    } catch (ex: Exception) {

                    }


                }

            }
        }
        if (urlUk != null) {
            ukLoading = true
            mCoroutine.launch1 {
                var bIn: BufferedInputStream? = null
                var bOut: BufferedOutputStream? = null
                var fOut: FileOutputStream? = null

                val fiLe = File(localFilePath + "/" + SOUND_TEMPORARY_UK)
                if (!fiLe.exists()) {
                    fiLe.createNewFile()
                }
                try {
                    fOut = FileOutputStream(fiLe)
                    bIn = BufferedInputStream(URL(urlUk).openStream())
                    bOut = BufferedOutputStream(fOut, 1024)
                    var buffer = ByteArray(1024)
                    while (true) {
                        val read = bIn!!.read(buffer, 0, 1024)
                        if (read == -1) {
                            break
                        }
                        bOut!!.write(buffer, 0, read)
                    }
                    withContext(Main) {
                        soundCallBack!!.onSoundLoadComplete("US")
                    }
                    soundUk = pool.load(localFilePath + "/" + SOUND_TEMPORARY_UK, 1)
                    pool.setOnLoadCompleteListener({ soundPool, i, i2 ->
                        ukSoundLoaded = true
                        pool.play(soundUk, 1f, 1f, 1, 0, 1f)
                    })

                } catch (exception: Exception) {
                    Log.d("test", "downloadSound: ${exception.message}")
                    withContext(Main) {
                        soundCallBack!!.onSoundLoadFail("US")
                    }

                } finally {
                    ukLoading = false
                    try {
                        if (fOut != null) {
                            fOut!!.close()
                        }
                        if (bOut != null) {
                            bOut!!.close()
                        }
                        if (bIn != null) {
                            bIn!!.close()
                        }
                    } catch (ex1: Exception) {

                    }
                }


            }
        }


    }


    fun addEmptyCategory(item: Category) {
        mCoroutine.launch1 {
            userDao.insertCategory(item)
        }
    }

    fun getWordsByCategory(
        CategoryName: String, callback: (words: List<UserWord_DefinitionList_Ref>) -> Unit
    ) {
        mCoroutine.launch1 {
            val x = userDao.selectWordsByCategory(CategoryName)
            withContext(Main) {
                callback(x)
            }

        }

    }

    fun addWordToCategory(word: Word) {
        mCoroutine.launch1 { userDao.UserSaveWord(word) }
    }

    fun addWordToRecent(word: RecentItem) {
        mCoroutine.launch1 {
            userDao.insertRecentWord(word)
        }
    }

    fun getLanguageInputIndex() = sharedPreferences!!.getInt(LANGUAGE_INPUT_INDEX, 0)
    fun getLanguageOutputIndex() = sharedPreferences!!.getInt(LANGUAGE_OUTPUT_INDEX, 20)
    fun updateLanguageInputIndex(position: Int) {
        sharedPreferences!!.edit().apply {
            putInt(LANGUAGE_INPUT_INDEX, position)
            apply()
        }
    }

    fun updateLanguageOutputIndex(position: Int) {
        sharedPreferences!!.edit().apply {
            putInt(LANGUAGE_OUTPUT_INDEX, position)
            apply()
        }
    }

    fun autoDetectEnable() = sharedPreferences!!.getBoolean(isAutoDetectInputlanguge, true)
    fun setAutoDetect(enable: Boolean) {
        sharedPreferences!!.edit().apply {
            putBoolean(isAutoDetectInputlanguge, enable)
            apply()
        }
    }

    fun deleteCategoryWithName(categoryName: String) {
        mCoroutine.launch1 {
            userDao.delecategory(categoryName)
        }

    }

    fun changeCategoryname(oldCategoryNameTemp: String, newCategoryName: String) {
        mCoroutine.launch1 {
            Log.d("test", "changeCategoryname: cost ${
                measureTimeMillis {
                    val newCategory = userDao.getCategoryWithName(oldCategoryNameTemp)
                    newCategory.categoryName = newCategoryName
                    userDao.insertCategory(newCategory)
                    userDao.delecategory(oldCategoryNameTemp)
                }
            } ")

        }
        mCoroutine.launch1 {
            userDao.changeWordOwner(newCategoryName, oldCategoryNameTemp)
        }

    }

    fun updateCatoryColor(categoryName: String, color: Int) {
        mCoroutine.launch1 {
            val category = userDao.updateCategoryColor(categoryName, color)
//            category.color=color
//          userDao.updateCategory(category)

        }


    }

    fun onTabToTranslateCofigChange(checked: Boolean) {
        mCoroutine.launch1 {
            sharedPreferences!!.edit().apply {
                putBoolean(TAP_TO_TRANSLATE_ENABLE, checked)
                apply()
            }
        }
    }

    var clipBoardData: MutableSharedFlow<String>? = null
    fun setClipBoardListener() {
        clipBoardData = service!!.clipboardText
    }

    fun addNewUserDefinition(definition: Definition) {
        mCoroutine.launch1 {
            userDao.upDateDefinition(definition)
        }
    }

    fun checkTapTranslateEnable(callback1: (isDone: Boolean) -> Unit) {
        mCoroutine.launch1 {
            val isDone=sharedPreferences!!.getBoolean(TAP_TO_TRANSLATE_ENABLE, false)
            Log.d("test", "checkTapToTranslate1: $isDone")
                withContext(Main){
                    callback1(isDone)
                }

        }
    }


//    suspend fun clipboardDataFromService(text: String) {
//        clipBoardData.emit(text)
//    }
//
//    val clipBoardData = MutableSharedFlow<String>()


}


