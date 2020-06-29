package com.example.starbucksapp

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CursorAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_top_level.*

class TopLevelActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase
    lateinit var favoriteCursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_level)
        setupOptionsListView()
        setupFavoriteListView()

    }

    fun setupOptionsListView(){
        list_options.setOnItemClickListener { adapterView, view, i, l ->
            if (i == 0) {
                val intent = Intent(this, DrinkCategoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun setupFavoriteListView(){
        try {
            val starbucksDatabaseHelper = StarbucksDatabaseHelper(this)
            db = starbucksDatabaseHelper.getReadableDatabase()
            favoriteCursor = db.query("DRINK",
                                        arrayOf("_id", "NAME"),
                                        "FAVORITE = 1",
                null, null, null, null)

            val favoriteAdapter = SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                favoriteCursor,
                arrayOf("NAME"),
                intArrayOf(android.R.id.text1, 0))
            list_favorites.setAdapter(favoriteAdapter)
        }catch (e: SQLiteException){
            val toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
            toast.show()
        }

        list_favorites.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val intent = Intent(this, DrinkActivity::class.java)
            intent.putExtra(DrinkActivity.EXTRA_DRINKID, l)
            startActivity(intent)
        }

    }

    override fun onRestart() {
        super.onRestart()
        val newCursor = db.query("DRINK",
                            arrayOf("_id", "NAME"),
                    "FAVORITE = 1",
                null, null, null, null)
        val adapter = list_favorites.adapter as CursorAdapter
        adapter.changeCursor(newCursor)
        favoriteCursor = newCursor
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteCursor.close()
        db.close()
    }

}
