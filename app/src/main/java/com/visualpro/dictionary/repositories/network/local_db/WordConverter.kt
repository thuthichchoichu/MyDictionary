package com.visualpro.dictionary.repositories.network.local_db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.NearByWord
import com.visualpro.myapplication.Model.Word
import com.visualpro.dictionary.model.Example

class WordConverter{


    @TypeConverter
    fun userExampleToString(values:String?):java.util.ArrayList<Example>{
        val listType= object : TypeToken<java.util.ArrayList<Example>>(){}.type
        return Gson().fromJson(values,listType)
    }
    @TypeConverter
    fun stringToUserEx(nbw:java.util.ArrayList<Example>):String{
        return Gson().toJson(nbw)
    }

    @TypeConverter
    fun nearByWordfromString(values:String?):ArrayList<NearByWord>{
        val listType= object : TypeToken<ArrayList<NearByWord>>(){}.type
        return Gson().fromJson(values,listType)
    }
    @TypeConverter
    fun stringFromNearByWord(nbw:ArrayList<NearByWord>):String{
        return Gson().toJson(nbw)
    }

    @TypeConverter
    fun definitionfromString(values:String):ArrayList<Definition>{
        val listType= object : TypeToken<ArrayList<Definition>>(){}.type
        return Gson().fromJson(values,listType)
    }
    @TypeConverter
    fun stringFromDefinition(defList:ArrayList<Definition>?):String{
        return Gson().toJson(defList)
    }

    @TypeConverter
    fun wordfromString(values:String?):Word{
        val listType= object : TypeToken<Word>(){}.type
        return Gson().fromJson(values,listType)
    }
    @TypeConverter
    fun stringFromWord(word:Word):String{
        return Gson().toJson(word)
    }

    @TypeConverter
    fun fromString(values:String?):ArrayList<Word>{
        val listType= object : TypeToken<ArrayList<Word>>(){}.type
        return Gson().fromJson(values,listType)
    }
    @TypeConverter
    fun fromArrayList(list:ArrayList<Word>):String{
        return Gson().toJson(list)
    }
}

