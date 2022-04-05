package com.example.sqlitefullexample_multiprogrammingideas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var dbHandler: DBHandler
    }

    var customerslist = ArrayList<Customer>()
    lateinit var adapter : RecyclerView.Adapter<*>
    lateinit var rv : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)

        viewCustomers()
        fab.setOnClickListener{
            val i = Intent(this, AddCustomerActivity::class.java)
            startActivity(i)
        }

        editSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var filteredCustomers = ArrayList<Customer>()
                if (!editSearch.text.isEmpty()){
                    for (i in 0..customerslist.size - 1){
                        if (customerslist.get(i).customerName!!.toLowerCase().contains(p0.toString().toLowerCase()))
                            filteredCustomers.add(customerslist[i])
                    }
                    adapter = CustomerAdapter(this@MainActivity,filteredCustomers)
                    rv.adapter = adapter
                } else { //if it is empty.
                    adapter = CustomerAdapter(this@MainActivity,customerslist)
                    rv.adapter = adapter
                }

            }
        })

    }

    private fun viewCustomers(){
        //customerslist: ArrayList<Customer> = dbHandler.getCustomers(this)
        customerslist = dbHandler.getCustomers(this)
        adapter = CustomerAdapter(this, customerslist)
        rv = findViewById(R.id.rv)
        rv. layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    override fun onResume() {
        viewCustomers()
        super.onResume()
    }
}