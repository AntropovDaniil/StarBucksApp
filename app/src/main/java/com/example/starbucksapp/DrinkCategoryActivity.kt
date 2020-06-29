package com.example.starbucksapp

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drink_category.*


class DrinkCategoryActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_category)


        val starbucksDatabaseHelper = StarbucksDatabaseHelper(this)
        try{
            db = starbucksDatabaseHelper.getReadableDatabase()
            cursor = db.query("DRINK",
                arrayOf("_id", "NAME"),
                null, null, null, null, null)
            val listAdapter = SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                arrayOf("NAME"),
                intArrayOf(android.R.id.text1), /////// используется именно intArrayOf место IntArray
                0)

            listDrinks.setAdapter(listAdapter)

        } catch (e: SQLiteException){
            val toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
            toast.show()
        }


        listDrinks.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DrinkActivity::class.java)
            intent.putExtra(DrinkActivity.EXTRA_DRINKID, l)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cursor.close()
        db.close()
    }

}
