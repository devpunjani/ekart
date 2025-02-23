package com.dev.ekart.api

import com.dev.ekart.pojo.Cart
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Order
import com.dev.ekart.pojo.Users
import com.dev.ekart.pojo.Wishlist
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //Register
    @POST("users")
    fun performRegister(@Body user: Users): Call<Users>

    //Login
    @GET("users")
    fun performLogin(): Call<List<Users>>

    //Get Users
    @GET("users")
    fun getUsers(): Call<List<Users>>

    //Edit Profile
    @PUT("users/{user_id}")
    fun editUser(@Path("user_id")id: String?, @Body user: Users): Call<Users>


    //Get All Items
    @GET("items")
    fun getItems(): Call<List<Items>>

    //Get 1 Item
    @GET("items/{item_id}")
    fun getItem(@Path("item_id") id: String): Call<Items>

    //Add Item
    @POST("items")
    fun addItem(@Body item: Items): Call<Items>

    //Edit Item
    @PUT("items/{item_id}")
    fun editItem(@Path("item_id") id: String?, @Body item: Items): Call<Items>

    //Delete Item
    @DELETE("items/{item_id}")
    fun deleteItem(@Path("item_id") id: String?): Call<Items>


    //Get ALl Wishlist
    @GET("wishlist")
    fun getWishlist(): Call<List<Wishlist>>

    //Add To Wishlist
    @POST("wishlist")
    fun addToWishlist(@Body wishlist: Wishlist): Call<Wishlist>

    //Delete From Wishlist
    @DELETE("wishlist/{id}")
    fun deleteFromWishlist(@Path("id") id : String?): Call<Wishlist>


    //Get ALl Cart
    @GET("cart")
    fun getCart(): Call<List<Cart>>

    //Add To Cart
    @POST("cart")
    fun addToCart(@Body cart: Cart): Call<Cart>

    //Delete From Cart
    @DELETE("cart/{id}")
    fun deleteFromCart(@Path("id") id : String?): Call<Cart>


    //Get ALl Order
    @GET("order")
    fun getOrder(): Call<List<Order>>

    //Add To Order
    @POST("order")
    fun addToOrder(@Body order: Order): Call<Order>
}
