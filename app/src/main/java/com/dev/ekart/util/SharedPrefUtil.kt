package com.dev.ekart.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.dev.ekart.pojo.Users
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SharedPrefUtil {
    private lateinit var sharedPref: SharedPreferences
    private val LOGIN="login"
    private val USER="user"

    fun init(context: Context):SharedPrefUtil {
            sharedPref =
                context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        return this
    }

    fun getString(name:String): String? {
        return sharedPref.getString(name,"")
    }
    fun getBoolean(name:String): Boolean {
        return sharedPref.getBoolean(name,false)
    }
    fun getInt(name:String): Int {
        return sharedPref.getInt(name,0)
    }

    fun putString(name:String,value:String){
        sharedPref.edit().putString(name,value).apply()
    }
    fun putBoolean(name:String,value:Boolean){
        sharedPref.edit().putBoolean(name,value).apply()
    }
    fun putInt(name:String,value:Int){
        sharedPref.edit().putInt(name,value).apply()
    }

    fun clearPref(){
        sharedPref.all.clear()
    }
    fun remove(name:String){
        sharedPref.edit().remove(name)
    }

    fun flagLoggedIn(){
        sharedPref.edit().putBoolean(LOGIN,true).apply()
    }
    fun flagLoggedOut(){
        sharedPref.edit().putBoolean(LOGIN,false).apply()
    }

    fun isLogin(): Boolean {
        return sharedPref.getBoolean(LOGIN,false)
    }

    fun saveUser(user: Users){
        putString(USER, Gson().toJson(user))
    }

    fun getUser(): Users {
        return Gson().fromJson(getString(USER),Users::class.java)
    }

    fun removeUser(){
        remove(USER)
    }
}