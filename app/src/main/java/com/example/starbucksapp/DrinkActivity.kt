package com.example.starbucksapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drink.*

class DrinkActivity : AppCompatActivity() {

    companion object {
        val EXTRA_DRINKID: String = "drinkId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val drinkId = getIntent().getExtras()?.get(EXTRA_DRINKID)

        val starbucksDatabaseHelper: SQLiteOpenHelper = StarbucksDatabaseHelper(this)
        try {
            val db = starbucksDatabaseHelper.getReadableDatabase()
            val cursor: Cursor = db.query("DRINK",
                arrayOf ("NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"),
                "_id = ?",
                arrayOf(drinkId.toString()),
                null, null, null
                )

            if(cursor.moveToFirst()){
                val nameText = cursor.getString(0)
                val descriptionText = cursor.getString(1)
                val photoId = cursor.getInt(2)
                val isFavorite = (cursor.getInt(3) == 1)

                name.setText(nameText)
                description.setText(descriptionText)
                photo.setImageResource(photoId)
                photo.setContentDescription(nameText)
                favorite.setChecked(isFavorite)
            }
            cursor.close()
            db.close()
        }
        catch (e: SQLiteException){
            val toast = Toast.makeText(this,
                                        "Database unavailable",
                                        Toast.LENGTH_SHORT)
            toast.show()
        }

        val favorite_p = findViewById<CheckBox>(R.id.favorite)
        favorite_p.setOnClickListener {
            UpdateDrinkTask().execute(drinkId as Int)
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class UpdateDrinkTask: AsyncTask<Int, Void, Boolean>(){
        lateinit var drinkValues: ContentValues

        override fun onPreExecute() {
            val favorite_p = findViewById<CheckBox>(R.id.favorite)
            drinkValues = ContentValues()
            drinkValues.put("FAVORITE", favorite_p.isChecked())
        }

        override fun doInBackground(vararg params: Int?): Boolean {
            val drinkId = params[0]
            val starbucksDatabaseHelper = StarbucksDatabaseHelper(this@DrinkActivity)
            try {
                val db = starbucksDatabaseHelper.getWritableDatabase()
                db.update(
                    "DRINK",
                    drinkValues,
                    "_id = ?",
                    arrayOf(drinkId.toString())
                )
                db.close()
                return true
            }catch (e: SQLiteException){
                return false
            }
        }

        override fun onPostExecute(result: Boolean) {
            if (!result){
                val toast = Toast.makeText(this@DrinkActivity, "Database Unavailable", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    }

}
