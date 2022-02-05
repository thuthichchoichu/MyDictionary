package com.visualpro.realproject.network.submodel

import com.google.gson.annotations.SerializedName

class SearchItemResponse(@SerializedName("results") var resposeList: ArrayList<SearchText>)

