package com.example.cameraandgalery.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cameraandgalery.model.Gallery

class MyDbHelper(context: Context):SQLiteOpenHelper(context,"db_name",null,1),DbInterface {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table gallery_table(id integer not null primary key autoincrement unique, name text not null, imageLink text not null)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun addPhoto(gallery: Gallery) {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put("name", gallery.name)
        contentValue.put("imageLink",gallery.imageLink)
        database.insert("gallery_table",null, contentValue)
        database.close()
    }

    override fun showPhoto(): ArrayList<Gallery> {
        val list = ArrayList<Gallery>()
        val database = this.readableDatabase
        val cursor = database.rawQuery("select * from gallery_table", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Gallery(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        return list
    }
}