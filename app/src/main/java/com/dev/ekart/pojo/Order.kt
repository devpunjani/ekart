package com.dev.ekart.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(

	@field:SerializedName("date")
    var date: String? = null,

	@field:SerializedName("user_id")
	var user_id: String? = null,

	@field:SerializedName("item_id")
    var item_id: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Serializable
