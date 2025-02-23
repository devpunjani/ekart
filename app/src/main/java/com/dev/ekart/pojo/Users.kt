package com.dev.ekart.pojo
import com.google.gson.annotations.SerializedName

data class Users(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    var username: String? = null,

    @field:SerializedName("firstname")
    var firstname: String? = null,

    @field:SerializedName("lastname")
    var lastname: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("phone")
    var phone: String? = null,

    @field:SerializedName("password")
    var password: String? = null
)
