package com.dev.ekart.pojo

import com.google.gson.annotations.SerializedName

data class Items(

	@field:SerializedName("image")
	var image: String? = null,

	@field:SerializedName("user_id")
	var user_id: String? = null,

	@field:SerializedName("price")
	var price: String? = null,

	@field:SerializedName("category")
	var category: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
