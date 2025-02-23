package com.dev.ekart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentAddItemBinding
import com.dev.ekart.pojo.Items
import com.dev.ekart.util.Constant.Companion.ITEMS_URL
import com.dev.ekart.util.SharedPrefUtil
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar

class AddItemFragment : BaseFragment() {

    private lateinit var binding: FragmentAddItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener{
            try {
                Validate(binding.editTextName).isEmptyWithTrim("Please Enter Item Name")
                Validate(binding.editTextDescription).isEmptyWithTrim("Please Enter Description")
                    .minLength(8,"Please Enter 8 Characters In Description")
                Validate(binding.editTextPrice).isEmpty("Please Enter Price")
                var item = Items()
                item.name=binding.editTextName.text.toString()
                item.description=binding.editTextDescription.text.toString()
                item.price=binding.editTextPrice.text.toString().toInt().toString()

                val selectedCategoryPosition = binding.spCategory.selectedItemPosition
                val categoriesArray = resources.getStringArray(R.array.category)
                val selectedCategory = categoriesArray[selectedCategoryPosition]
                item.category = selectedCategory.toString()


                if(selectedCategory.toString() == "Phone")
                {
                    item.image="https://icon-library.com/images/iphone-icon-png/iphone-icon-png-22.jpg"
                }
                else if(selectedCategory.toString() == "Laptop")
                {
                    item.image="https://icon-library.com/images/laptop-icon-png/laptop-icon-png-25.jpg"
                }
                else if(selectedCategory.toString() == "Smartwatch")
                {
                    item.image="https://icon-library.com/images/smart-watch-icon/smart-watch-icon-9.jpg"
                }
                else if (selectedCategory.toString() == "Headphone")
                {
                    item.image="https://icon-library.com/images/headphone-icon-png/headphone-icon-png-16.jpg"
                }

                item.user_id= SharedPrefUtil().init(requireContext()).getUser().id
                APIServiceProvider().getServices(ITEMS_URL).addItem(item).enqueue{
                    Snackbar.make(requireView(),"Item Added Successfully",Snackbar.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }catch (e: ValidationException){
                Snackbar.make(requireView(),e.localizedMessage,Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}