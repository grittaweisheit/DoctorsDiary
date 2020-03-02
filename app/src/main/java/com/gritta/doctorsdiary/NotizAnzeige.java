package com.gritta.doctorsdiary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Gritta on 12.03.2018.
 */

public class NotizAnzeige extends AppCompatActivity{

    public NotizDatenQuelle notizdb;
    public Button delete, change;

    long id;
    String dat, zu;
    int wohl;
    double gew, sch;
    int ta, t, ca, fe, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notiz_anzeige);

        notizdb = new NotizDatenQuelle(this);
        notizdb.open();
        Intent intent = getIntent();

        dat = intent.getStringExtra("datum");
        wohl = Integer.parseInt(intent.getStringExtra("wohlbefinden"));
        zu = intent.getStringExtra("zusatz");
        gew = Double.parseDouble(intent.getStringExtra("gewicht"));
        sch = Double.parseDouble(intent.getStringExtra("schlaf"));
        ta = Integer.parseInt(intent.getStringExtra("tage"));
        t = Integer.parseInt(intent.getStringExtra("t"));
        ca = Integer.parseInt(intent.getStringExtra("ca"));
        fe = Integer.parseInt(intent.getStringExtra("fe"));
        b = Integer.parseInt(intent.getStringExtra("b"));

        id = intent.getLongExtra("id", 1L);

        String kg = intent.getStringExtra("gewicht") + "kg",  h = intent.getStringExtra("schlaf") + " h";
        ((TextView)(findViewById(R.id.Datum))).setText(intent.getStringExtra("datum"));
        ((TextView)(findViewById(R.id.Gewicht))).setText(kg);
        ((TextView)(findViewById(R.id.Wohlbefinden))).setText(intent.getStringExtra("wohlbefinden"));
        ((TextView)(findViewById(R.id.Schlaf))).setText(h);
        if (ta == 0) ((CheckedTextView)findViewById(R.id.Tage)).setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        else ((CheckedTextView)findViewById(R.id.Tage)).setCheckMarkDrawable(android.R.drawable.checkbox_on_background);

        if (t == 0) ((CheckedTextView)findViewById(R.id.T)).setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        else ((CheckedTextView)findViewById(R.id.T)).setCheckMarkDrawable(android.R.drawable.checkbox_on_background);

        if (ca == 0) ((CheckedTextView)findViewById(R.id.Ca)).setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        else ((CheckedTextView)findViewById(R.id.Ca)).setCheckMarkDrawable(android.R.drawable.checkbox_on_background);

        if (fe == 0) ((CheckedTextView)findViewById(R.id.Fe)).setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        else ((CheckedTextView)findViewById(R.id.Fe)).setCheckMarkDrawable(android.R.drawable.checkbox_on_background);

        if (b == 0) ((CheckedTextView)findViewById(R.id.B)).setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        else ((CheckedTextView)findViewById(R.id.B)).setCheckMarkDrawable(android.R.drawable.checkbox_on_background);

        ((TextView)(findViewById(R.id.Zusatz))).setText(intent.getStringExtra("zusatz"));


        notizdb.close();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.anzeigeBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotizAnzeige.this, History.class));
            }
        });

        delete = (Button)findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                notizdb.open();
                notizdb.eintragLoeschen(id);

                Intent in = new Intent(NotizAnzeige.this, History.class);
                startActivity(in);

                notizdb.close();
            }
        });

        change = (Button)findViewById(R.id.Change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notizdb.open();
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".NotizEintrag");
                intent.putExtra("datum", dat);
                intent.putExtra("gewicht", Double.toString(gew));
                intent.putExtra("wohlbefinden", Integer.toString(wohl));
                intent.putExtra("schlaf", Double.toString(sch));
                intent.putExtra("tage", Integer.toString(ta));
                intent.putExtra("t", Integer.toString(t));
                intent.putExtra("ca", Integer.toString(ca));
                intent.putExtra("fe", Integer.toString(fe));
                intent.putExtra("b", Integer.toString(b));
                intent.putExtra("zusatz", zu);
                intent.putExtra("id", id);
                intent.putExtra("aendern", 1);
                startActivity(intent);
                notizdb.close();

            }
        });


    }
}
