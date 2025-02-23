package com.dev.ekart


import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import com.dev.ekart.api.BaseCallback

import retrofit2.Call

open class BaseFragment : Fragment() {
    lateinit var pd: ProgressDialog

    fun createProgressDialog(){
        pd = ProgressDialog(requireContext())
        pd.setCancelable(false)
    }
}

fun <T> Call<T>.enqueue(callback: BaseCallback<T>.() -> Unit){
    val callbackBk = BaseCallback<T>()
    callback.invoke(callbackBk)
    this.enqueue(callbackBk)
}
