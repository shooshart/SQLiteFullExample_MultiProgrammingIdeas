package com.example.sqlitefullexample_multiprogrammingideas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_customer.*

class AddCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

        btnSave.setOnClickListener{
            if (editCustomerName.text.isEmpty()){
                Toast.makeText(this, "Enter Customer Name", Toast.LENGTH_SHORT).show()
                editCustomerName.requestFocus()
            } else
            {
                val customer = Customer()
                customer.customerName = editCustomerName.text.toString()
                if (editMaxCredit.text.isEmpty())
                    customer.maxCredit = 0.0 else
                    customer.maxCredit = editMaxCredit.text.toString().toDouble()
                customer.phoneNumber = editPhoneNumber.text.toString()
                MainActivity.dbHandler.addCustomer(this, customer)
                clearEdit()
                editCustomerName.requestFocus()
            }
        }

        btnCancel.setOnClickListener{
            clearEdit()
            finish()
        }
    }

    private fun clearEdit(){
        editCustomerName.text.clear()
        editMaxCredit.text.clear()
        editPhoneNumber.text.clear()
    }

}