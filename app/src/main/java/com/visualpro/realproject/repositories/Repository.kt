package com.visualpro.realproject.repositories

import android.media.SoundPool
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.gson.GsonBuilder
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.Model.TranslateItems
import com.visualpro.realproject.Model.WordTypeSeparate
import com.visualpro.realproject.Model.model_relations.Separate_DefinitionList
import com.visualpro.realproject.network.*
import com.visualpro.realproject.network.Api_Configs.Companion.BASE_URL
import com.visualpro.realproject.network.local_db.category_DAO
import com.visualpro.realproject.views.ResultActivity.Companion.SOUND_TEMPORARY_UK
import com.visualpro.realproject.views.ResultActivity.Companion.SOUND_TEMPORARY_US
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.launch as launch1


class Repository(private val categoryDao: category_DAO) {
    private var currentWordLocal=false
    private var soundUk = -1
    private var soundUs = -1
    var allCategory = categoryDao.getAllCategory()

     var localFilePath = ""
    var pool: SoundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val mCoroutine = CoroutineScope(IO)
    private val oxSearchservice: Ox_SearchService = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(Ox_SearchService::class.java)
    private val ggTranslateservice: Gg_TranslateService = Retrofit
        .Builder()
        .baseUrl(Api_Configs.BASE_URL_GG)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(Gg_TranslateService::class.java)

    companion object {
        const val TEMP_URL1 = "https://www.oxfordlearnersdictionaries.com/us/search/english/?q="
    }


    var mainInterface: DataInterface_Main? = null
    var resultInterface: DataInterface_Result? = null
    var usSoundLoaded = false
    var ukSoundLoaded = false

    fun performSearches(word: String) {
        mCoroutine.launch1 {
            val respond = oxSearchservice.search(word, "application/json;charset=utf-8")
            mainInterface!!.setSearchItems(respond)


        }
    }

    fun performTranslate(text: String) {
        mCoroutine.launch1 {
            val respond: TranslateItems =
                ggTranslateservice.translate("gtx", "auto", "vi", "t", "bd", 1, text)
            mainInterface!!.setTranslateItem(respond)
        }

    }

    private fun getOxfordURL(word: String) = TEMP_URL1 + word.replace(" ", "+")


    fun performParseHtml1(word: String, url: String, useUrl: Boolean) {
        usSoundLoaded = false
        ukSoundLoaded = false

        mCoroutine.launch1 {
            val localWord = categoryDao.selectWord(word)
            if (localWord != null) {
                Log.d("test", "get local data")
                currentWordLocal=true
                withContext(Main) {
                    resultInterface!!.setWordItem(localWord)
                }
            } else {
                currentWordLocal=true
                val doc: Document?
                try {
                    doc = if (useUrl && !url.equals("")) Jsoup.connect(url).get() else Jsoup.connect(getOxfordURL(word)).get()
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
                            downloadSound(word1.auUrlUs, word1.auUrlUk)
                            var listX = ArrayList<Separate_DefinitionList?>()
                            var nullDefinition = Definition()

                            nullDefinition.belongToWord = word1.getPK()
                            nullDefinition.isNull = true


                            for (i in listNearByWord.indices) {
                                if (word.trim().equals(listNearByWord.get(i).word.trim())) {
                                    listX.add(returnRelateWord(listNearByWord.get(i).urlToThisWord))
                                }
                            }

                            var separate = WordTypeSeparate()
                            separate.begin = 0
                            separate.end = definitionList.size
                            separate.wordType = word1.wordType
                            separate.wordRef = word1.getPK()

                            categoryDao.insertWord(word1)
                            categoryDao.insertWordTypeSeparate(separate)

                            for (i in definitionList.indices) {
                                if (i == 0) {
                                    categoryDao.insertDefinition(nullDefinition)
                                }
                                definitionList[i]!!.belongToWord = word1.getPK()
                                categoryDao.insertDefinition(definitionList[i])
                            }

                            for (i in listX.indices) {
                                if (listX[i] != null) {
                                    listX[i]?.separate!!.begin = separate.end + 1
                                    listX[i]?.separate!!.end =
                                        listX[i]?.separate!!.begin + listX[i]!!.list.size
                                    listX[i]?.separate!!.wordRef = word1.getPK()

                                    separate = listX[i]?.separate!!
                                    categoryDao.insertWordTypeSeparate(listX[i]?.separate!!)

                                    for (j in listX[i]!!.list.indices) {
                                        if (j == 0) {
                                            categoryDao.insertDefinition(nullDefinition)
                                        }
                                        listX[i]!!.list[j]?.belongToWord = word1.getPK()
                                        categoryDao.insertDefinition(listX[i]!!.list[j])
                                    }
                                }
                            }
                            val localWord1 = categoryDao.selectWord(word)
                            withContext(Main) {

                                if (localWord1 != null) {
                                    resultInterface!!.setWordItem(localWord1)
                                }
                            }

                        } else {
                            resultInterface!!.nextWord(word, false)
//                        master = doc.selectFirst("ul[class=result-list]")
//                        if (master != null) {
//                            val didUMean: ArrayList<NearByWord> = ArrayList<NearByWord>()
//                            for (i in 0 until master.childrenSize()) {
//                                val e = master.child(i)
//                                didUMean.add(NearByWord(e.text(), e.child(0).attr("href"), ""))
//                            }
//
//                        } else {
//
//                        }
                        }
                    }


                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Log.d("fail", "fail: ${word} ")
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

    fun playSoundUs(soundName: String?) {
        if (usSoundLoaded == false) {
            soundUs = pool.load(localFilePath + "/" + SOUND_TEMPORARY_US, 1)
            pool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, i, i2 ->
                usSoundLoaded = true
                pool.play(soundUs, 1f, 1f, 1, 0, 1f)
            })
        } else {
            pool.play(soundUs, 1f, 1f, 1, 0, 1f)
        }


    }

    fun playSoundUk(soundName: String?) {
        if (ukSoundLoaded == false) {
            soundUk = pool.load(localFilePath + "/" + SOUND_TEMPORARY_UK, 1)
            pool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, i, i2 ->
                usSoundLoaded = true
                pool.play(soundUk, 1f, 1f, 1, 0, 1f)
            })
        } else {
            pool.play(soundUk, 1f, 1f, 1, 0, 1f)
        }


    }


    @Suppress("BlockingMethodInNonBlockingContext")
    fun downloadSound(urlUs: String?, urlUk: String?) {
        if (urlUs == null || urlUk == null) {
            return
        }
        mCoroutine.launch1 {
            var bIn: BufferedInputStream? = null
            var bOut: BufferedOutputStream? = null
            var fOut: FileOutputStream? = null
            var http: HttpURLConnection
            try {
                val fiLe = File(localFilePath + "/" + SOUND_TEMPORARY_US)
                if (!fiLe.exists()) {
                    fiLe.createNewFile()
                }
                fOut = FileOutputStream(fiLe)
                http = URL(urlUs).openConnection() as HttpURLConnection
                bIn = BufferedInputStream(http.inputStream)
                bOut = BufferedOutputStream(fOut, 1024)
                var buffer = ByteArray(1024)

//                while ((read >= 0).also { read = bIn!!.read(buffer, 0, 1024) }) {
//                    bOut.write(buffer, 0, read)
//                }
                while (true) {
                    val read = bIn.read(buffer, 0, 1024)
                    if (read == -1) {
                        break
                    }
                    bOut.write(buffer, 0, read)
                }


                Log.d("test", "downloadSound: US")
//                prePairSound(SOUND_TEMPORARY_US, 0)
            } catch (exception: Exception) {
                Log.d("test", "error sound:${exception.message} ")
            } finally {
                if (fOut != null) {
                    fOut.close()
                }
                if (bOut != null) {
                    bOut.close()
                }
                if (bIn != null) {
                    bIn.close()
                }
            }

        }
        mCoroutine.launch1 {
            var bIn: BufferedInputStream? = null
            var bOut: BufferedOutputStream? = null
            var fOut: FileOutputStream? = null
            var http: HttpURLConnection
            try {
                val fiLe = File(localFilePath + "/" + SOUND_TEMPORARY_UK)
                if (!fiLe.exists()) {
                    fiLe.createNewFile()
                }
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
                Log.d("test", "downloadSound: UK")
//                prePairSound(SOUND_TEMPORARY_UK, 1)
            } catch (exception: Exception) {
                Log.getStackTraceString(exception)
            } finally {
                if (fOut != null) {
                    fOut!!.close()
                }
                if (bOut != null) {
                    bOut!!.close()
                }
                if (bIn != null) {
                    bIn!!.close()
                }
            }

        }


    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun addEmptyCategory(item: Category) {
        mCoroutine.launch1 {
            categoryDao.insertCategory(item)
        }
    }

    fun getListCategoryName() {
        mCoroutine.launch1 {
            resultInterface!!.setListCategorysName(categoryDao.getListCategorysName())
        }

    }

    fun addWordToCategory(word: Word, position: Int) {
        mCoroutine.launch1 {
            word.Category_Id = position + 1
            categoryDao.insertUserWord(word)


        }

    }
}

