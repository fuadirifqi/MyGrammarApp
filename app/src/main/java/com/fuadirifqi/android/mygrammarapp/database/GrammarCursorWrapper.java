package com.fuadirifqi.android.mygrammarapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.fuadirifqi.android.mygrammarapp.Grammar;
import com.fuadirifqi.android.mygrammarapp.database.GrammarDbSchema.GrammarTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by fuadirifqi on 5/17/16.
 */
public class GrammarCursorWrapper extends CursorWrapper {

    public GrammarCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Grammar getGrammar(){
        String uuidString = getString(getColumnIndex(GrammarTable.Cols.UUID));
        String title = getString(getColumnIndex(GrammarTable.Cols.TITLE));
        long date = getLong(getColumnIndex(GrammarTable.Cols.DATE));
        int isValid = getInt(getColumnIndex(GrammarTable.Cols.VALID));
        String suspect = getString(getColumnIndex(GrammarTable.Cols.SUSPECT));

        Grammar grammar = new Grammar(UUID.fromString(uuidString));
        grammar.setTitle(title);
        grammar.setDate(new Date(date));
        grammar.setValid(isValid != 0);
        grammar.setSuspect(suspect);

        return grammar;
    }

}
