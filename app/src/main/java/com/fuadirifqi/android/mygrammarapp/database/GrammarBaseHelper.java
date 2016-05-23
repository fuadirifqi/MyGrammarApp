package com.fuadirifqi.android.mygrammarapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fuadirifqi.android.mygrammarapp.database.GrammarDbSchema.GrammarTable;

/**
 * Created by fuadirifqi on 5/17/16.
 */
public class GrammarBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "grammarBase.db";

    public GrammarBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ GrammarTable.NAME + "(" + "_id integer primary key autoincrement, " +
        GrammarTable.Cols.UUID + ", " +
        GrammarTable.Cols.TITLE + ", " +
        GrammarTable.Cols.DATE + ", " +
        GrammarTable.Cols.VALID + ", " +
        GrammarTable.Cols.SUSPECT +  ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldver, int newver){

    }

}
