package com.example.sqlitefullexample_multiprogrammingideas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import kotlin.Exception

class DBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 2

        val CUSTOMER_TABLE_NAME = "Customers"
        val COLUMN_CUSTOMERID = "customerid"
        val COLUMN_CUSTOMERNAME = "customername"
        val COLUMN_MAXCREDIT = "maxcredit"
        val COLUMN_PHONENO = "phoneno"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CUSTOMERS_TABLE : String = ("CREATE TABLE $CUSTOMER_TABLE_NAME (" +
                "$COLUMN_CUSTOMERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CUSTOMERNAME TEXT," +
                "$COLUMN_MAXCREDIT DOUBLE DEFAULT 0)")
        db?.execSQL(CREATE_CUSTOMERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <2){
                db?.execSQL("Alter Table $CUSTOMER_TABLE_NAME " +
                        "Add $COLUMN_PHONENO TEXT NULL")
        }
    }

    fun getCustomers(mCtx: Context): ArrayList<Customer> {
        val gry = "Select * From $CUSTOMER_TABLE_NAME"
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery(gry, null)
        val customers = ArrayList<Customer>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show() else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()) {
                val customer = Customer()
                customer.customerID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMERID))
                customer.customerName = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERNAME))
                customer.maxCredit = cursor.getDouble(cursor.getColumnIndex(COLUMN_MAXCREDIT))
                customer.phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONENO))
                customers.add(customer)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT)
                .show()
        }
        cursor.close()
        db.close()
        return customers
    }

    fun addCustomer(mCtx: Context, customer: Customer){
        val values = ContentValues()
        values.put(COLUMN_CUSTOMERNAME, customer.customerName)
        values.put(COLUMN_MAXCREDIT, customer.maxCredit)
        values.put(COLUMN_PHONENO, customer.phoneNumber)
        val db: SQLiteDatabase = this.writableDatabase
        try {
            db.insert(CUSTOMER_TABLE_NAME, null, values)
//Can use this line
//          db.rawQuery("Insert Into $CUSTOMER_TABLE_NAME ($COLUMN_CUSTOMERNAME, $COLUMN_MAXCREDIT) Values(?, ?)")
            Toast.makeText(mCtx, "Customer Added", Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteCustomer(customerID: Int): Boolean{
        val qry = "Delete From $CUSTOMER_TABLE_NAME where $COLUMN_CUSTOMERID = $customerID"
        val db: SQLiteDatabase = this.writableDatabase
        var result: Boolean = false
        try {
//            val cursor: Int = db.delete(CUSTOMER_TABLE_NAME, "$COLUMN_CUSTOMERID = ?", arrayOf(customerID.toString())))
            val cursor: Unit = db.execSQL(qry)
            result = true
        } catch (e: Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }

    fun updateCustomer(id: String, customerName: String, maxCredit: String, phoneNumber: String): Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        var result: Boolean = false
        contentValues.put(COLUMN_CUSTOMERNAME, customerName)
        contentValues.put(COLUMN_MAXCREDIT, maxCredit.toDouble())
        contentValues.put(COLUMN_PHONENO, phoneNumber)
        try {
            db.update(CUSTOMER_TABLE_NAME, contentValues, "$COLUMN_CUSTOMERID = ?", arrayOf(id))
            result = true
        } catch ( e: Exception ){
            Log.e(ContentValues.TAG, "Error Updating")
            result = false
        }
        return result
    }

}