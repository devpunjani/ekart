package com.dev.ekart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.ekart.adapter.HomeAdapter
import com.dev.ekart.api.APIServiceProvider
import com.dev.ekart.databinding.FragmentHomeBinding
import com.dev.ekart.pojo.Items
import com.dev.ekart.util.Constant
import com.dev.ekart.util.Constant.Companion.ITEMS_URL

class HomeFragment:BaseFragment(){
    private lateinit var adapter: HomeAdapter
    private lateinit var list: ArrayList<Items>
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list= arrayListOf()
        adapter=HomeAdapter(list){ item->
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",ItemDetailsFragment::class.java)
                .putExtra("id",item.id))
        }

        binding.recyclerHome.adapter = adapter
        createProgressDialog()
        pd.show()
        APIServiceProvider().getServices(ITEMS_URL).getItems().enqueue{
            success={
                list.addAll(it.body()!!)
                adapter.notifyDataSetChanged()
                pd.hide()
            }
        }

        binding.fabAdd.setOnClickListener{
            startActivity(Intent(requireContext(),IsolatedActivity::class.java)
                .putExtra("page",AddItemFragment::class.java))
        }

        binding.fabRefresh.setOnClickListener{
            requireActivity().finish()
            startActivity(Intent(requireContext(), HomeActivity::class.java))
        }
    }
}