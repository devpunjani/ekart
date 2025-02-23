package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentChangepasswordBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar

class ChangepasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangepasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangepasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUpdate.setOnClickListener {
            try {

                Validate(binding.oldPasswordEditText).isEmpty("Enter Old Password").minLength(8,"Password Length Should Be More Than 8 Characters")
                Validate(binding.newPasswordEditText).isEmpty("Enter New Password").minLength(8,"Password Length Should Be More Than 8 Characters")
                Validate(binding.confirmNewPasswordEditText).isEqualTo(binding.newPasswordEditText,"Password Does Not Match")

                val oldPassword = binding.oldPasswordEditText.text.toString()

                var userDetails = Users()
                userDetails = SharedPrefUtil().init(requireContext()).getUser()

                APIServiceProvider().getServices(Constant.USERS_URL).getUsers().enqueue {
                    success={
                        for (user in it.body()!!){
                            if (user.id?.equals(userDetails.id)!!){
                                if (user.password?.equals(oldPassword)!!){

                                    userDetails.password=binding.newPasswordEditText.text.toString()

                                    var userId = SharedPrefUtil().init(requireContext()).getUser().id

                                    APIServiceProvider().getServices(Constant.USERS_URL).editUser(userId,userDetails)
                                        .enqueue{
                                        Snackbar.make(requireView(),"Password Changed Successfully",Snackbar.LENGTH_SHORT).show()
                                        SharedPrefUtil().init(requireContext()).saveUser(user)
                                        activity?.startActivity(
                                            Intent(context,HomeActivity::class.java)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                        }
                                    break
                                }else{
                                    Snackbar.make(requireView(),"Old Password Does Not Match",Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

            }catch (ex: ValidationException){
                Snackbar.make(view,ex.localizedMessage, Snackbar.LENGTH_LONG).show()
            }
        }

    }

}