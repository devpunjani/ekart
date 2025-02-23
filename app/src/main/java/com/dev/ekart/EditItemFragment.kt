package com.dev.ekart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentEditItemBinding
import com.dev.ekart.pojo.Items
import com.dev.ekart.util.Constant.Companion.ITEMS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class EditItemFragment() : BaseFragment() {

    private lateinit var binding: FragmentEditItemBinding
    private lateinit var item: Items

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentEditItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = Gson().fromJson(requireArguments().getString("item"),Items::class.java)

        binding.btnSubmit.setOnClickListener{
            try {
                Validate(binding.editTextName).isEmptyWithTrim("Please Enter Item Name")
                Validate(binding.editTextDescription).isEmptyWithTrim("Please Enter Description")
                    .minLength(8,"Please Enter 8 Characters In Description")
                Validate(binding.editTextPrice).isEmpty("Please Enter Price")

                item.name=binding.editTextName.text.toString()
                item.description=binding.editTextDescription.text.toString()
                item.price=binding.editTextPrice.text.toString().toInt().toString()
                item.user_id= SharedPrefUtil().init(requireContext()).getUser().id
                APIServiceProvider().getServices(ITEMS_URL).editItem(item.id,item).enqueue{
                    Snackbar.make(requireView(),"Item Updated Successfully",Snackbar.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }catch (e: ValidationException){
                Snackbar.make(requireView(),e.localizedMessage,Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}