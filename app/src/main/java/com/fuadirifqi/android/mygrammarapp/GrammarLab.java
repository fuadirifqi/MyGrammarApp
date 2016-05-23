package com.fuadirifqi.android.mygrammarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.fuadirifqi.android.mygrammarapp.database.GrammarBaseHelper;
import com.fuadirifqi.android.mygrammarapp.database.GrammarCursorWrapper;
import com.fuadirifqi.android.mygrammarapp.database.GrammarDbSchema;
import com.fuadirifqi.android.mygrammarapp.database.GrammarDbSchema.GrammarTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by fuadirifqi on 5/16/16.
 */
public class GrammarLab {

    private static GrammarLab sGrammarLab;

    //private List<Grammar> mGrammars;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static GrammarLab get(Context context){
        if(sGrammarLab == null){
            sGrammarLab = new GrammarLab(context);
        }

        return sGrammarLab;
    }

    private GrammarLab(Context context){

        mContext = context.getApplicationContext();
        mDatabase = new GrammarBaseHelper(mContext).getWritableDatabase();
        //mGrammars = new ArrayList<>();

    }

    public void addGrammar(Grammar grammar){
        //mGrammars.add(grammar);
        ContentValues cv = getContentValues(grammar);

        mDatabase.insert(GrammarTable.NAME, null, cv);
    }

    public void deleteGrammar(Grammar grammar) {
        String uuidString = grammar.getId().toString();
        //mGrammars.remove(grammar);
        ContentValues cv = getContentValues(grammar);

        mDatabase.delete(GrammarTable.NAME, GrammarTable.Cols.UUID + " = ? ", new String[]{uuidString});
    }

    public void updateGrammar(Grammar grammar){
        String uuidString = grammar.getId().toString();
        ContentValues cv = getContentValues(grammar);

        mDatabase.update(GrammarTable.NAME, cv, GrammarTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public List<Grammar> getGrammars(){
        //return mGrammars;

        List<Grammar> grammars = new ArrayList<>();

        GrammarCursorWrapper cursorWrapper = queryGrammars(null, null);

        try{
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                grammars.add(cursorWrapper.getGrammar());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }

        return grammars;
    }

    public Grammar getGrammar(UUID id){
        //for (Grammar grammar: mGrammars){
        //    if(grammar.getId().equals(id)){
        //        return grammar;
        //    }
        //}

        GrammarCursorWrapper cursorWrapper = queryGrammars(GrammarTable.Cols.UUID + " = ?", new String[] {id.toString()});

        try{
            if(cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getGrammar();
        }finally {
            cursorWrapper.close();
        }
    }

    public File getPhotoFile(Grammar grammar){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFileDir == null){
            return null;
        }

        return new File(externalFileDir, grammar.getPhotoFilename());

    }

    public static ContentValues getContentValues(Grammar grammar){

        ContentValues cv = new ContentValues();
        cv.put(GrammarTable.Cols.UUID, grammar.getId().toString());
        cv.put(GrammarTable.Cols.TITLE, grammar.getTitle());
        cv.put(GrammarTable.Cols.DATE, grammar.getDate().getTime());
        cv.put(GrammarTable.Cols.VALID, grammar.isValid()? 1 : 0 );
        cv.put(GrammarTable.Cols.SUSPECT, grammar.getSuspect());

        return cv;
    }


    //private Cursor queryGrammars(String whereClause, String[] whereArgs){
    private GrammarCursorWrapper queryGrammars(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(GrammarTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //GroupBy
                null, //having
                null  //orderBy
        );

        return new GrammarCursorWrapper(cursor);
    }

}
