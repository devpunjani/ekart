package com.dev.ekart

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.adapter.HomeAdapter
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentCartBinding
import com.dev.ekart.databinding.FragmentWishlistBinding
import com.dev.ekart.pojo.Cart
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Order
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.Constant.Companion.CART_URL
import com.dev.ekart.util.SharedPrefUtil
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CartFragment : Fragment() {
    private lateinit var adapter: HomeAdapter
    private lateinit var list: ArrayList<Items>
    private lateinit var user : Users
    private lateinit var binding: FragmentCartBinding
    private lateinit var order : Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater,container,false)
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

        APIServiceProvider().getServices(Constant.CART_URL).getCart().enqueue {
            success = { cartItemsResponse ->
                val cartItems = cartItemsResponse.body() ?: emptyList()

                if (cartItems.isNotEmpty()) {
                    APIServiceProvider().getServices(Constant.ITEMS_URL).getItems().enqueue {
                        success = { itemsResponse ->
                            val items = itemsResponse.body() ?: emptyList()

                            val matchingItems = mutableListOf<Items>()
                            var totalSum = 0

                            for (cartItem in cartItems) {
                                if (cartItem.user_id == SharedPrefUtil().init(requireContext()).getUser().id.toString()) {
                                    for (item in items) {
                                        if (cartItem.item_id == item.id) {
                                            matchingItems.add(item)
                                            totalSum += item.price?.toInt() ?:
                                            break // Break the inner loop once a match is found
                                        }
                                    }
                                }
                            }

                            if (matchingItems.isNotEmpty()) {
                                list.addAll(matchingItems)
                                adapter.notifyDataSetChanged()


                                binding.textViewTotalsum.text = "$totalSum"
                            }
                        }
                    }
                } else {
                    Snackbar.make(requireView(), "Cart is empty", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonPlaceOrder.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setMessage("Are You Sure You Want To Order All Items In Cart ?")
                .setPositiveButton("Yes",DialogInterface.OnClickListener{dialog, which ->
                    APIServiceProvider().getServices(Constant.CART_URL).getCart().enqueue {
                        success = { cartItemsResponse ->
                            val cartItems = cartItemsResponse.body() ?: emptyList()

                            if (cartItems.isNotEmpty()) {

                                APIServiceProvider().getServices(Constant.ITEMS_URL).getItems().enqueue {
                                    success = { itemsResponse ->
                                        val items = itemsResponse.body() ?: emptyList()
                                        var orderPlaced = false

                                        for (cartItem in cartItems) {
                                            if (cartItem.user_id == SharedPrefUtil().init(requireContext()).getUser().id.toString()) {
                                                for (item in items) {
                                                    if (cartItem.item_id == item.id) {

                                                        var order = Order()
                                                        order.item_id=cartItem.item_id
                                                        order.user_id=cartItem.user_id

                                                        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                                                            Date()
                                                        )
                                                        order.date = currentDate
                                                        orderPlaced=true
                                                        APIServiceProvider().getServices(Constant.ORDER_URL).addToOrder(order).enqueue{
                                                            orderPlaced=true
                                                        }
                                                        APIServiceProvider().getServices(CART_URL).deleteFromCart(cartItem.id).enqueue {

                                                        }
                                                        break // Break the inner loop once a match is found
                                                    }
                                                }
                                            }
                                        }
                                        if (orderPlaced) {
                                            Snackbar.make(requireView(), "Order Placed", Snackbar.LENGTH_SHORT).show()
                                            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                                                .putExtra("page", HomeFragment::class.java))

                                            requireActivity().finish()
                                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                                        }
                                    }
                                }
                            }
                        }
                    }


                })
                .setNegativeButton("No", DialogInterface.OnClickListener{dialog, which ->
                    dialog.dismiss()
                }).setCancelable(false).create().show()
        }
    }
}