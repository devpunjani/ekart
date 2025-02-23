package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentForgotpasswordverifyBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant.Companion.USERS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotpasswordverifyFragment : Fragment() {
    private lateinit var binding: FragmentForgotpasswordverifyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotpasswordverifyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.verifyButton.setOnClickListener{
            try {


                Validate(binding.editEmailVerify).isEmpty("Please Enter Email").isEmail("Enter Proper Email")

                val enteredEmail = binding.editEmailVerify.text.toString()



                APIServiceProvider().getServices(USERS_URL).getUsers().enqueue {
                    success = { response ->
                        var emailExists = false
                        var user: Users? = null

                        for (userResponse in response.body()!!) {
                            if (userResponse.email?.equals(enteredEmail)!!) {
                                emailExists = true
                                user = userResponse
                                break
                            }
                        }

                        if (emailExists) {
                            Snackbar.make(requireView(), "Email Exist. Set New Password", Snackbar.LENGTH_SHORT).show()
                            SharedPrefUtil().init(requireContext()).saveUser(user!!)
                            findNavController().navigate(R.id.action_ForgotpasswordverifyFragment_to_ForgotpasswordsetFragment)
                        } else {
                            Snackbar.make(requireView(), "Email Does Not Exist", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (ex: ValidationException){
                Snackbar.make(view,ex.localizedMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}