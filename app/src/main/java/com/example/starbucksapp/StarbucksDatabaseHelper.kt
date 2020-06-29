package com.example.starbucksapp

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StarbucksDatabaseHelper(
    context: Context,
    val DB_NAME: String = "starbucks",
    val DB_VERSION: Int = 1
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        updateMyDatabse(db as SQLiteDatabase, 0 , DB_VERSION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        updateMyDatabse(db as SQLiteDatabase, oldVersion, newVersion)
    }

    fun insertDrink(db: SQLiteDatabase?, name: String,
                    description: String, resourceId: Int){
        val drinkValues = ContentValues()
        drinkValues.put("NAME", name)
        drinkValues.put("DESCRIPTION", description)
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId)
        db?.insert("DRINK", null, drinkValues)
    }

    fun updateMyDatabse(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        if (oldVersion<1){
            db!!.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, IMAGE_RESOURCE_ID INTEGER);")

            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte)
            insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam",
                R.drawable.cappuccino)
            insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter)
        }
        if (oldVersion<2){
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC")
        }
    }
}