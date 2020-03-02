package com.gritta.doctorsdiary;

/**
 * Created by Gritta on 13.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotizDatenQuelle {

    private static final String LOG_TAG = NotizDatenQuelle.class.getSimpleName();

    private SQLiteDatabase notizDb;
    private NotizDbHelfer dbHelfer;

    private String[] columns =  {
            NotizDbHelfer.COLUMN_ID,
            NotizDbHelfer.COLUMN_DATUM,
            NotizDbHelfer.COLUMN_GEWICHT,
            NotizDbHelfer.COLUMN_WOHLBEFINDEN,
            NotizDbHelfer.COLUMN_SCHLAF,
            NotizDbHelfer.COLUMN_TAGE,
            NotizDbHelfer.COLUMN_T,
            NotizDbHelfer.COLUMN_CA,
            NotizDbHelfer.COLUMN_FE,
            NotizDbHelfer.COLUMN_B,
            NotizDbHelfer.COLUMN_ZUSATZ,
            NotizDbHelfer.COLUMN_DATINT
    };

    public NotizDatenQuelle(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelfer.");
        dbHelfer = new NotizDbHelfer(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        notizDb = dbHelfer.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + notizDb.getPath());

        //Cursor c = notizDb.query(DbHelfer.TANKEINTRAEGE, IWAS , null, null, null, null, DbHelfer.COLUMN_DATINT + " DESC");
        // notizDb.query(DbHelfer.TANKEINTRAEGE + "ORDER BY " + DbHelfer.COLUMN_DATINT, columns, null, null, null, null, null);
    }

    public void close() {
        dbHelfer.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Notiz erstelleNotizeintrag(String d, Double g, int w, double s, int ta, int t, int ca, int fe, int b, String z){
        ContentValues werte = new ContentValues();
        werte.put(NotizDbHelfer.COLUMN_DATUM, d);
        werte.put(NotizDbHelfer.COLUMN_GEWICHT, g);
        werte.put(NotizDbHelfer.COLUMN_WOHLBEFINDEN, w);
        werte.put(NotizDbHelfer.COLUMN_SCHLAF, s);
        werte.put(NotizDbHelfer.COLUMN_TAGE, ta);
        werte.put(NotizDbHelfer.COLUMN_T, t);
        werte.put(NotizDbHelfer.COLUMN_CA, ca);
        werte.put(NotizDbHelfer.COLUMN_FE, fe);
        werte.put(NotizDbHelfer.COLUMN_B, b);
        werte.put(NotizDbHelfer.COLUMN_ZUSATZ, z);
        werte.put(NotizDbHelfer.COLUMN_DATINT, datToInt(d));

        long gibId = notizDb.insert(NotizDbHelfer.NOTIZEINTRAEGE, null, werte);

        Cursor cursor = notizDb.query(NotizDbHelfer.NOTIZEINTRAEGE, columns, NotizDbHelfer.COLUMN_ID + "=" + gibId, null, null, null, NotizDbHelfer.COLUMN_DATINT + " DESC");
        cursor.moveToFirst();
        Notiz n = cursorToNotiz(cursor);
        cursor.close();

        return n;
    }

    private Notiz cursorToNotiz(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(NotizDbHelfer.COLUMN_ID);
        int idDatum = cursor.getColumnIndex(NotizDbHelfer.COLUMN_DATUM);
        int idGewicht = cursor.getColumnIndex(NotizDbHelfer.COLUMN_GEWICHT);
        int idWohlbefinden = cursor.getColumnIndex(NotizDbHelfer.COLUMN_WOHLBEFINDEN);
        int idSchlaf = cursor.getColumnIndex(NotizDbHelfer.COLUMN_SCHLAF);
        int idTage = cursor.getColumnIndex(NotizDbHelfer.COLUMN_TAGE);
        int idT = cursor.getColumnIndex(NotizDbHelfer.COLUMN_T);
        int idCa = cursor.getColumnIndex(NotizDbHelfer.COLUMN_CA);
        int idFe = cursor.getColumnIndex(NotizDbHelfer.COLUMN_FE);
        int idB = cursor.getColumnIndex(NotizDbHelfer.COLUMN_B);
        int idZusatz = cursor.getColumnIndex(NotizDbHelfer.COLUMN_ZUSATZ);
        int idDatumInt = cursor.getColumnIndex(NotizDbHelfer.COLUMN_DATINT);

        long id = cursor.getLong(idIndex);
        String d = cursor.getString(idDatum);
        double g = cursor.getDouble(idGewicht);
        int w = cursor.getInt(idWohlbefinden);
        double s = cursor.getDouble(idSchlaf);
        int ta = cursor.getInt(idTage);
        int t = cursor.getInt(idT);
        int ca= cursor.getInt(idCa);
        int fe = cursor.getInt(idFe);
        int b = cursor.getInt(idB);
        String z = cursor.getString(idZusatz);
        int di = cursor.getInt(idDatumInt);

        Notiz n = new Notiz(id, d, g, w, s, ta, t, ca, fe, b, z);

        return n;
    }

    public List<Notiz> getAllNotizen() {
        List<Notiz> notizListe = new ArrayList<>();

        Cursor cursor = notizDb.query(NotizDbHelfer.NOTIZEINTRAEGE, columns, null, null, null, null, NotizDbHelfer.COLUMN_DATINT +  " DESC");

        cursor.moveToFirst();
        Notiz notiz;

        while(!cursor.isAfterLast()) {
            notiz = cursorToNotiz(cursor);
            notizListe.add(notiz);
            Log.d(LOG_TAG, "ID: " + notiz.getId() + ", Inhalt: " + notiz.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return notizListe;
    }

    public void einitragLoeschen(Notiz n){
        long id = n.getId();

        notizDb.delete(NotizDbHelfer.NOTIZEINTRAEGE, NotizDbHelfer.COLUMN_ID + "=" + id, null);
    }
    public void eintragLoeschen(long od){
        notizDb.delete(NotizDbHelfer.NOTIZEINTRAEGE, NotizDbHelfer.COLUMN_ID + "=" + od, null);
    }

    public Notiz updateNotiz(long id, String d, double g, int w, double s, int ta, int t, int ca, int fe, int b, String z) {
        ContentValues werte = new ContentValues();
        werte.put(NotizDbHelfer.COLUMN_DATUM, d);
        werte.put(NotizDbHelfer.COLUMN_GEWICHT, g);
        werte.put(NotizDbHelfer.COLUMN_WOHLBEFINDEN, w);
        werte.put(NotizDbHelfer.COLUMN_SCHLAF, s);
        werte.put(NotizDbHelfer.COLUMN_TAGE, ta);
        werte.put(NotizDbHelfer.COLUMN_T, t);
        werte.put(NotizDbHelfer.COLUMN_CA, ca);
        werte.put(NotizDbHelfer.COLUMN_FE, fe);
        werte.put(NotizDbHelfer.COLUMN_B, b);
        werte.put(NotizDbHelfer.COLUMN_ZUSATZ, z);
        werte.put(NotizDbHelfer.COLUMN_DATINT, datToInt(d));

        notizDb.update(NotizDbHelfer.NOTIZEINTRAEGE,
                werte,
                NotizDbHelfer.COLUMN_ID + "=" + id,
                null);

        Cursor cursor = notizDb.query(NotizDbHelfer.NOTIZEINTRAEGE, columns, NotizDbHelfer.COLUMN_ID + "=" + id, null, null, null, NotizDbHelfer.COLUMN_DATINT + " DESC");

        cursor.moveToFirst();
        Notiz n = cursorToNotiz(cursor);
        cursor.close();

        return n;
    }
    public int datToInt(String d){
        String dat1 = d;
        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "", datumString = "";
        int datumInt = 0;

        int i = 0;
        while (dat1.charAt(i) != '.'){
            d1 += dat1.charAt(i);
            ++i;
        } ++i; if(d1.length() < 2) d1 ="0" + d1;
        while (dat1.charAt(i) != '.'){
            m1 += dat1.charAt(i);
            ++i;
        } ++i; if(m1.length() < 2) m1 ="0" + m1;
        while (i < dat1.length()){
            y1 += dat1.charAt(i);
            ++i;
        }if (y1.length() < 4) y1 = "20" + y1;
        datumString = y1 + m1 + d1;
        datumInt = Integer.parseInt(datumString);
        return datumInt;
    }
}
