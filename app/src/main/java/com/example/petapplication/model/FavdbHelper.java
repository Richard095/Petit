package com.example.petapplication.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavdbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE = "favs.db";
    public FavdbHelper(Context context){
        super(context,DATABASE,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String FAVS_DATABASE = "CREATE TABLE " + FavoritesContract.FavoritesColumns.TABLE_NAME + " (" +
                FavoritesContract.FavoritesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                FavoritesContract.FavoritesColumns.FAV + " INTEGER NOT NULL " + " ) ";

        sqLiteDatabase.execSQL(FAVS_DATABASE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE);
        onCreate(sqLiteDatabase);

    }


    public void delete(int petpostId){
        SQLiteDatabase db = getWritableDatabase();
        if (db != null){
            db.execSQL("DELETE FROM "+FavoritesContract.FavoritesColumns.TABLE_NAME+" WHERE "+FavoritesContract.FavoritesColumns.FAV+ "= '"+petpostId+"' ");
            db.close();
        }


    }
    public void insert(int postId){
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (db != null){
                db.execSQL("INSERT INTO "+FavoritesContract.FavoritesColumns.TABLE_NAME+" VALUES(null,'"+postId+"')  ");
                db.close();
            }
        }catch (SQLException E){E.printStackTrace();}finally {
            db.close();
        }


    }

}
