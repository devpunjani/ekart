package com.dev.ekart.pojo

import com.google.gson.annotations.SerializedName

data class Wishlist(

	@field:SerializedName("user_id")
	var user_id: String? = null,

	@field:SerializedName("item_id")
    var item_id: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
