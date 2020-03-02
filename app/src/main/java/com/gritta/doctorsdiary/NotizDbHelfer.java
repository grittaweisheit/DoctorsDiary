package com.gritta.doctorsdiary;

/**
 * Created by Gritta on 13.03.2018.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class NotizDbHelfer extends SQLiteOpenHelper{

    private HashMap hp;

    public static final String LOG_TAG = NotizDbHelfer.class.getSimpleName();
    public static final String DB_NAME = "notizeintraege.db";
    public static final int DB_VERSION = 1;
    public static final String NOTIZEINTRAEGE = "notizeintraege";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATUM = "datum";
    public static final String COLUMN_GEWICHT = "gewicht";
    public static final String COLUMN_WOHLBEFINDEN = "wohlbefinden";
    public static final String COLUMN_SCHLAF = "schlaf";
    public static final String COLUMN_TAGE = "tage";
    public static final String COLUMN_T = "t";
    public static final String COLUMN_CA = "ca";
    public static final String COLUMN_FE = "fe";
    public static final String COLUMN_B = "b";
    public static final String COLUMN_ZUSATZ = "zusatz";
    public static final String COLUMN_DATINT = "datum_int";

    public static final String SQL_CREATE =
            "CREATE TABLE " + NOTIZEINTRAEGE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATUM + " TEXT NOT NULL, " +
                    COLUMN_GEWICHT + " DOUBLE NOT NULL, " + COLUMN_WOHLBEFINDEN + " INTEGER NOT NULL, " + COLUMN_SCHLAF + " DOUBLE NOT NULL, "
                    + COLUMN_TAGE + " INTEGER NOT NULL, " + COLUMN_T + " INTEGER NOT NULL, "
                    + COLUMN_CA + " INTEGER NOT NULL, " + COLUMN_FE + " INTEGER NOT NULL, " + COLUMN_B + " INTEGER NOT NULL, " + COLUMN_ZUSATZ + " TEXT," + COLUMN_DATINT + " INTEGER NOT NULL);";


    public NotizDbHelfer(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelfer hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTIZEINTRAEGE);
    }

    public Cursor getNotiz(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NOTIZEINTRAEGE + " ", null);
        return res;
    }

}
