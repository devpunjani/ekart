package com.dev.ekart

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentItemDetailsBinding
import com.dev.ekart.util.SharedPrefUtil
import android.opengl.Visibility
import android.view.View.VISIBLE
import android.widget.Button
import androidx.core.content.ContextCompat
import com.dev.ekart.pojo.Cart
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Wishlist
import com.dev.ekart.util.Constant
import com.dev.ekart.util.Constant.Companion.CART_URL
import com.dev.ekart.util.Constant.Companion.ITEMS_URL
import com.dev.ekart.util.Constant.Companion.ORDER_URL
import com.dev.ekart.util.Constant.Companion.WISHLIST_URL
import com.dev.ekart.util.Validate
import com.dev.ekart.util.ValidationException
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ItemDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private lateinit var item: Items
    private lateinit var wishlistItem : Wishlist
    private lateinit var cartItem: Cart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        APIServiceProvider().getServices(ITEMS_URL).getItem(requireArguments().getString("id")!!).enqueue {
            success = {
                binding.item = it.body()!!
                item = it.body()!!
                if (it.body()?.user_id == SharedPrefUtil().init(requireContext()).getUser().id) {
                    binding.btnEdit.visibility = VISIBLE
                    binding.btnDelete.visibility = VISIBLE
                }
            }
        }

        val userId = SharedPrefUtil().init(requireContext()).getUser().id

        APIServiceProvider().getServices(WISHLIST_URL).getWishlist().enqueue {
            success = { wishlistResponse ->
                val wishlistItems = wishlistResponse.body()
                for (it in wishlistItems!!)
                {   if (it.user_id==userId)
                    {   if(it.item_id==item.id) {
                            binding.buttonWishlist.setImageResource(R.drawable.ic_like_fill)
                            wishlistItem = it
                            break
                        } else{
                            binding.buttonWishlist.setImageResource(R.drawable.ic_like_empty)
                        }
                    }
                }
            }
        }

        APIServiceProvider().getServices(ORDER_URL).getOrder().enqueue {
            success = { orderResponse ->
                val orderItems = orderResponse.body()

                for (it in orderItems!!) {
                    if (it.user_id == userId && it.item_id == item.id) {
                        // Matching order found
                        binding.textViewOrderHistory.text = "Order Date : ${it.date}"
                        binding.textViewOrderHistory.visibility = VISIBLE
                        break
                    }
                }
            }
        }


        APIServiceProvider().getServices(CART_URL).getCart().enqueue {
            success = { cartResponse ->
                val cartItems = cartResponse.body()
                var cartItemFound = false

                for (it in cartItems!!) {
                    if (it.user_id == userId) {
                        if (it.item_id == item.id) {
                            binding.buttonCart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.teal_700))
                            cartItem = it
                            cartItemFound = true
                            break
                        }
                    }
                }

                // Set background color if no matching cart item is found
                if (!cartItemFound) {
                    binding.buttonCart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.dcd9d9))
                }
            }
        }


        binding.btnEdit.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("item", Gson().toJson(item, Items::class.java))
            requireActivity().supportFragmentManager
                .beginTransaction().replace(R.id.place_holder,EditItemFragment::class.java,bundle,"")
                .disallowAddToBackStack().commitNow()
        }

        binding.btnDelete.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setMessage("Are You Sure You Want To Delete This Item ?")
                .setPositiveButton("Yes",DialogInterface.OnClickListener{dialog, which ->
                    APIServiceProvider().getServices(ITEMS_URL).deleteItem(item.id).enqueue{
                        success={
                            Snackbar.make(requireView(),"Item Deleted!!", Snackbar.LENGTH_SHORT).show()
                            requireActivity().finish()
                        }
                    }
                }).setNegativeButton("Cancel",DialogInterface.OnClickListener{dialog, which ->
                    dialog.dismiss()
                }).setCancelable(false).create().show()
        }

        binding.buttonWishlist.setOnClickListener {
            if (::wishlistItem.isInitialized && wishlistItem.id != null && wishlistItem.id != "0") {
                APIServiceProvider().getServices(WISHLIST_URL).deleteFromWishlist(wishlistItem.id)
                    .enqueue {
                        Snackbar.make(
                            requireView(),
                            "Item Removed From Wishlist",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.buttonWishlist.setImageResource(R.drawable.ic_like_empty)
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                    }
            } else {
                val wishlist = Wishlist()
                wishlist.item_id = item.id
                wishlist.user_id = SharedPrefUtil().init(requireContext()).getUser().id
                APIServiceProvider().getServices(WISHLIST_URL).addToWishlist(wishlist).enqueue {
                    Snackbar.make(requireView(), "Item Added To Wishlist", Snackbar.LENGTH_SHORT).show()
                    binding.buttonWishlist.setImageResource(R.drawable.ic_like_fill)
                    wishlistItem = wishlist // Update wishlistItem after adding it
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                }
            }
        }


        binding.buttonCart.setOnClickListener {
            if (::cartItem.isInitialized && cartItem.id != null && cartItem.id != "0") {
                APIServiceProvider().getServices(CART_URL).deleteFromCart(cartItem.id)
                    .enqueue {
                        Snackbar.make(
                            requireView(),
                            "Item Removed From Cart",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.buttonCart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.dcd9d9))
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                    }
            } else {
                val cart = Cart()
                cart.item_id = item.id
                cart.user_id = SharedPrefUtil().init(requireContext()).getUser().id
                APIServiceProvider().getServices(CART_URL).addToCart(cart).enqueue {
                    Snackbar.make(requireView(), "Item Added To Cart", Snackbar.LENGTH_SHORT).show()
                    cartItem = cart // Update cartItem after adding it
                    binding.buttonCart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.teal_700))
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), HomeActivity::class.java))

                }
            }
        }


    }
}