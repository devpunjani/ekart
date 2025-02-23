package com.dev.ekart

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.adapter.HomeAdapter
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentWishlistBinding
import com.dev.ekart.pojo.Cart
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant.Companion.CART_URL
import com.dev.ekart.util.Constant.Companion.ITEMS_URL
import com.dev.ekart.util.Constant.Companion.WISHLIST_URL
import com.dev.ekart.util.SharedPrefUtil
import com.google.android.material.snackbar.Snackbar

class WishlistFragment : Fragment() {
    private lateinit var adapter: HomeAdapter
    private lateinit var list: ArrayList<Items>
    private lateinit var binding: FragmentWishlistBinding
    private lateinit var user : Users

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWishlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list= arrayListOf()
        adapter= HomeAdapter(list){ item->
            startActivity(
                Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",ItemDetailsFragment::class.java)
                .putExtra("id",item.id))
        }

        binding.recyclerHome.adapter = adapter

        user = SharedPrefUtil().init(requireContext()).getUser()

        APIServiceProvider().getServices(WISHLIST_URL).getWishlist().enqueue {
            success = { wishlistItemsResponse ->
                val wishlistItems = wishlistItemsResponse.body() ?: emptyList()

                if (wishlistItems.isNotEmpty()) {
                    APIServiceProvider().getServices(ITEMS_URL).getItems().enqueue {
                        success = { itemsResponse ->
                            val items = itemsResponse.body() ?: emptyList()

                            // Check for matching item IDs
                            val matchingItems = mutableListOf<Items>()

                            for (wishlistItem in wishlistItems) {
                                if (wishlistItem.user_id == SharedPrefUtil().init(requireContext()).getUser().id.toString()) {
                                    for (item in items) {
                                        if (wishlistItem.item_id == item.id) {
                                            matchingItems.add(item)
                                            break // Break the inner loop once a match is found
                                        }
                                    }
                                }
                            }

                            if (matchingItems.isNotEmpty()) {
                                list.addAll(matchingItems)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }

        binding.buttonAddAllToCart.setOnClickListener{
            APIServiceProvider().getServices(WISHLIST_URL).getWishlist().enqueue {
                success = { wishlistItemsResponse ->
                    val wishlistItems = wishlistItemsResponse.body() ?: emptyList()

                    if (wishlistItems.isNotEmpty()) {

                        APIServiceProvider().getServices(ITEMS_URL).getItems().enqueue {
                            success = { itemsResponse ->
                                val items = itemsResponse.body() ?: emptyList()
                                var addedToCart = false

                                for (wishlistItem in wishlistItems) {
                                    if (wishlistItem.user_id == SharedPrefUtil().init(requireContext()).getUser().id.toString()) {
                                        for (item in items) {
                                            if (wishlistItem.item_id == item.id) {

                                                var cart = Cart()
                                                cart.item_id=wishlistItem.item_id
                                                cart.user_id=wishlistItem.user_id

                                                APIServiceProvider().getServices(CART_URL).addToCart(cart).enqueue{
                                                    addedToCart=true
                                                }
                                                break // Break the inner loop once a match is found
                                            }
                                        }
                                    }
                                }
                                if (addedToCart) {
                                    Snackbar.make(requireView(), "Items Added To Cart", Snackbar.LENGTH_SHORT).show()
                                    requireActivity().finish()
                                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




