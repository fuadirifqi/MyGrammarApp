package com.fuadirifqi.android.mygrammarapp.database;

/**
 * Created by fuadirifqi on 5/17/16.
 */
public class GrammarDbSchema {

    public static final class GrammarTable{
        public static final String NAME = "grammars";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String VALID = "valid";
            public static final String SUSPECT = "suspect";

        }
    }

}
