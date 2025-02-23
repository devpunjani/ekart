package com.dev.ekart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class IsolatedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isolated)
        openFragment()
    }

    private fun openFragment(){
        when(intent.getSerializableExtra("page")){
            AddItemFragment::class.java->{
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    AddItemFragment(),"").commitNow()
            }
            ItemDetailsFragment::class.java->{
                val bundle = Bundle()
                bundle.putString("id",intent.getStringExtra("id"))
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    ItemDetailsFragment()::class.java,bundle,"").commitNow()
            }
            EditprofileFragment::class.java->{
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    EditprofileFragment(),"").commitNow()
            }
            ChangepasswordFragment::class.java->{
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    ChangepasswordFragment(),"").commitNow()
            }
            CartFragment::class.java->{
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    CartFragment(),"").commitNow()
            }
            OrderFragment::class.java->{
                supportFragmentManager.beginTransaction().add(R.id.place_holder,
                    OrderFragment(),"").commitNow()
            }
        }
    }
}