package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.R
import com.dev.ekart.adapter.HomeAdapter
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentCartBinding
import com.dev.ekart.databinding.FragmentOrderBinding
import com.dev.ekart.pojo.Items
import com.dev.ekart.pojo.Users
import com.dev.ekart.util.Constant
import com.dev.ekart.util.SharedPrefUtil
import com.google.android.material.snackbar.Snackbar

class OrderFragment : Fragment() {
    private lateinit var adapter: HomeAdapter
    private lateinit var list: ArrayList<Items>
    private lateinit var user : Users
    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater,container,false)
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

        APIServiceProvider().getServices(Constant.ORDER_URL).getOrder().enqueue {
            success = { orderItemsResponse ->
                val orderItems = orderItemsResponse.body() ?: emptyList()

                if (orderItems.isNotEmpty()) {
                    APIServiceProvider().getServices(Constant.ITEMS_URL).getItems().enqueue {
                        success = { itemsResponse ->
                            val items = itemsResponse.body() ?: emptyList()

                            val matchingItems = mutableListOf<Items>()

                            for (orderItem in orderItems) {
                                if (orderItem.user_id == SharedPrefUtil().init(requireContext()).getUser().id.toString()) {
                                    for (item in items) {
                                        if (orderItem.item_id == item.id) {
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
                } else {
                    Snackbar.make(requireView(), "Order is empty", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}