package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentEditprofileBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar

class EditprofileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditprofileBinding
    private lateinit var user: Users

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditprofileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUpdate.setOnClickListener {
            try {

                Validate(binding.editTextUsername).isEmptyWithTrim("Please Enter Username").minLength(8,"Enter 8 Characters Username")
                Validate(binding.editTextFirstName).isEmpty("Please Enter First Name").isCharacter("First Name Must Contain Only Characters")
                Validate(binding.editTextLastName).isEmpty("Please Enter Last Name").isCharacter("Last Name Must Contain Only Characters")
                Validate(binding.editTextPhone).isEmptyWithTrim("Please Enter Phone Number").minLength(10,"Enter 10 Digit Number").isNumber("Phone Number Must Contain Only Number")


                user=SharedPrefUtil().init(requireContext()).getUser()
                user.username = binding.editTextUsername.text.toString()
                user.firstname = binding.editTextFirstName.text.toString()
                user.lastname = binding.editTextLastName.text.toString()
                user.phone = binding.editTextPhone.text.toString()

                var id = SharedPrefUtil().init(requireContext()).getUser().id

                APIServiceProvider().getServices(Constant.USERS_URL).editUser(id,user)
                    .enqueue{
                        success={
                            Snackbar.make(requireView(),"Profile Updated !!",Snackbar.LENGTH_SHORT).show()
                            SharedPrefUtil().init(requireContext()).saveUser(it.body()!!)
                            startActivity(
                                Intent(requireContext(),HomeActivity::class.java)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        }
                    }


            }catch (ex: ValidationException){
                Snackbar.make(view,ex.localizedMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}