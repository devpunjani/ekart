package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.databinding.FragmentProfileBinding
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.SharedPrefUtil

class ProfileFragment:BaseFragment(){
    private lateinit var user: Users
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = SharedPrefUtil().init(requireContext()).getUser()
        binding.user = user!!

        binding.buttonEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",EditprofileFragment::class.java))
        }

        binding.buttonChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",ChangepasswordFragment::class.java))
        }

        binding.buttonCart.setOnClickListener {
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",CartFragment::class.java))
        }

        binding.buttonOrder.setOnClickListener {
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page", OrderFragment::class.java))
        }

        binding.buttonLogout.setOnClickListener{
            SharedPrefUtil().init(requireContext()).flagLoggedOut()
            activity?.startActivity(
                Intent(context,MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }
}