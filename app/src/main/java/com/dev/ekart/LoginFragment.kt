package com.dev.ekart

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.api.ApiService
import com.dev.ekart.databinding.FragmentLoginBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant.Companion.USERS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment:Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener{
            try {

                Validate(binding.editTextUsername).isEmptyWithTrim("Please Enter Username").minLength(8,"Enter 8 Characters Username")
                Validate(binding.editTextPassword).isEmpty("Enter Password").minLength(8,"Password Length Should Be More Than 8 Characters")

                val username = binding.editTextUsername.text.toString()
                val password = binding.editTextPassword.text.toString()

                APIServiceProvider().getServices(USERS_URL).performLogin().enqueue {
                    success={
                        for (user in it.body()!!){
                            if (user.username?.equals(username)!!){
                                if (user.password?.equals(password)!!){
                                    Snackbar.make(requireView(),"User Login Successfully!",Snackbar.LENGTH_SHORT).show()
                                    SharedPrefUtil().init(requireContext()).saveUser(user)
                                    SharedPrefUtil().init(requireContext()).flagLoggedIn()
                                    activity?.startActivity(Intent(context,HomeActivity::class.java)
                                            .setFlags(FLAG_ACTIVITY_CLEAR_TOP))
                                }
                            }
                            else{
                                Snackbar.make(requireView(),"Invalid Username or Password",Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }catch (ex:ValidationException){
                Snackbar.make(view,ex.localizedMessage,Snackbar.LENGTH_LONG).show()
            }
        }
        binding.textViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.textViewForgotPassword.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_forgotpasswordverifyFragment)
        }

    }

}