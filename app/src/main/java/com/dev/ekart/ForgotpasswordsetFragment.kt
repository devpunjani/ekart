package com.dev.ekart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentForgotpasswordsetBinding
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.Constant.Companion.USERS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotpasswordsetFragment : Fragment() {
    private lateinit var binding: FragmentForgotpasswordsetBinding
    private lateinit var user: Users

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotpasswordsetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUpdate.setOnClickListener {
            try {
                Validate(binding.newPasswordEditText).isEmpty("Enter Password").minLength(8,"Password Length Should Be More Than 8 Characters")
                Validate(binding.confirmNewPasswordEditText).isEqualTo(binding.newPasswordEditText,"Password Does Not Match")

                user =SharedPrefUtil().init(requireContext()).getUser()

                user.password=binding.newPasswordEditText.text.toString()

                var id = SharedPrefUtil().init(requireContext()).getUser().id

                APIServiceProvider().getServices(USERS_URL).editUser(id,user).enqueue{
                    Snackbar.make(requireView(),"Password Changed Successfully",Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_ForgotpasswordsetFragment_to_loginFragment)
                }

            }catch (ex: ValidationException){
                Snackbar.make(view,ex.localizedMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}