package com.dev.ekart

import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentSignupBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.Constant.Companion.USERS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar

class SignupFragment : BaseFragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.buttonSignup.setOnClickListener {
                try {
                    Validate(binding.editTextUsername).isEmptyWithTrim("Please Enter Username").minLength(8,"Enter 8 Characters Username")
                    Validate(binding.editTextFirstName).isEmpty("Please Enter First Name").isCharacter("First Name Must Contain Only Characters")
                    Validate(binding.editTextLastName).isEmpty("Please Enter Last Name").isCharacter("Last Name Must Contain Only Characters")
                    Validate(binding.editTextEmail).isEmpty("Please Enter Email").isEmail("Enter Proper Email")
                    Validate(binding.editTextPhone).isEmptyWithTrim("Please Enter Phone Number").minLength(10,"Enter 10 Digit Number").isNumber("Phone Number Must Contain Only Number")
                    Validate(binding.editTextPassword).isEmpty("Enter Password").minLength(8,"Password Length Should Be More Than 8 Characters")
                    Validate(binding.editTextConfirmPassword).isEqualTo(binding.editTextPassword,"Password Does Not Match")

                    createProgressDialog()
                    pd.show()

                    var user = Users()
                    user.username = binding.editTextUsername.text.toString()
                    user.firstname = binding.editTextFirstName.text.toString()
                    user.lastname = binding.editTextLastName.text.toString()
                    user.email = binding.editTextEmail.text.toString()
                    user.phone = binding.editTextPhone.text.toString()
                    user.password = binding.editTextPassword.text.toString()
                    APIServiceProvider().getServices(USERS_URL).performRegister(user)
                        .enqueue(){
                            success = {
                                Snackbar.make(requireView(), "User Created Successfully!", Snackbar.LENGTH_LONG).show()
                                pd.hide()
                                SharedPrefUtil().init(requireContext()).flagLoggedIn()
                                SharedPrefUtil().init(requireContext()).saveUser(it.body()!!)
                                startActivity(Intent(requireContext(),HomeActivity::class.java)
                                    .setFlags(FLAG_ACTIVITY_CLEAR_TOP))
                            }
                        }

                }catch (ex: ValidationException){
                    Snackbar.make(view,ex.localizedMessage, Snackbar.LENGTH_LONG).show()
                }
            }
            binding.textViewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
    }