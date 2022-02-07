package com.visualpro.realproject.Model.User

import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word

class WordWithDefinitions(
    @Embedded var word: Word,
    @Relation(parentColumn = "word_ID",
    entityColumn = "word_ID", entity = Definition::class)
    var userDefinition: List<Definition>
)


