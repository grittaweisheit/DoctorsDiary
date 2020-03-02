package com.gritta.doctorsdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import android.util.Log;
import android.widget.Toast;

import android.os.Build.VERSION;


/**
 * Created by Gritta on 12.03.2018.
 */


public class NotizEintrag extends AppCompatActivity{

    Button ok;
    String dat, zu, heute;
    int ta, fe,t,b,ca, w;
    double s, g;
    boolean aendern;
    long id;

    public static final String LOG_TAG = NotizEintrag.class.getSimpleName();

    private NotizDatenQuelle notizDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notiz_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        VERSION version = new Build.VERSION();
        //Datum auf heute setzen
        if (version.SDK_INT < 26){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            heute = dateFormat.format(calendar.getTime());
        }
        else {
            LocalDate h = LocalDate.now();
            heute = String.valueOf(h.getDayOfMonth()) + "." + String.valueOf(h.getMonthValue()) + "." + String.valueOf(h.getYear());
            ((EditText) (findViewById(R.id.DatumE))).setText(heute);
        }
        ((EditText)findViewById(R.id.DatumE)).setText(heute);

        Intent intent = getIntent();
        if(intent.getIntExtra("aendern", 0) == 1) aendern = true; else aendern = false;
        if (aendern){
            ((EditText)findViewById(R.id.DatumE)).setText(intent.getStringExtra("datum"));
            ((EditText)findViewById(R.id.GewichtE)).setText(intent.getStringExtra("gewicht"));

            ((SeekBar)findViewById(R.id.WohlbefindenE)).setProgress(Integer.parseInt(intent.getStringExtra("wohlbefinden")));

            ((EditText)findViewById(R.id.SchlafE)).setText(intent.getStringExtra("schlaf"));

            if (Integer.parseInt(intent.getStringExtra("tage")) == 1){
                ((CheckBox)findViewById(R.id.TageE)).setChecked(true);
            }
            if (Integer.parseInt(intent.getStringExtra("t")) == 1){
                ((CheckBox)findViewById(R.id.TE)).setChecked(true);
            }
            if (Integer.parseInt(intent.getStringExtra("fe")) == 1){
                ((CheckBox)findViewById(R.id.FeE)).setChecked(true);
            }
            if (Integer.parseInt(intent.getStringExtra("ca")) == 1){
                ((CheckBox)findViewById(R.id.CaE)).setChecked(true);
            }
            if (Integer.parseInt(intent.getStringExtra("b")) == 1){
                ((CheckBox)findViewById(R.id.BE)).setChecked(true);
            }
            ((EditText)findViewById(R.id.ZusatzE)).setText(intent.getStringExtra("zusatz"));
            id = intent.getLongExtra("id", 1L);
        }

        //OK Button
        ok = (Button) findViewById(R.id.NotizEintragErstellen);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText d = (EditText)findViewById(R.id.DatumE);
                if(d.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                dat = d.getText().toString();

                EditText ge = (EditText)findViewById(R.id.GewichtE);
                if(ge.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                g = Double.parseDouble((ge.getText()).toString());

                SeekBar o = (SeekBar) findViewById(R.id.WohlbefindenE);
                w = o.getProgress();

                EditText sc = (EditText)findViewById(R.id.SchlafE);
                if(sc.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                s = Double.parseDouble((sc.getText()).toString());

                CheckBox l = (CheckBox) findViewById(R.id.TageE);
                if(l.isChecked()){
                    ta = 1;
                }else ta = 0;

                l = (CheckBox) findViewById(R.id.CaE);
                if(l.isChecked()){
                    ca = 1;
                }else ca = 0;

                l = (CheckBox) findViewById(R.id.FeE);
                if(l.isChecked()){
                    fe = 1;
                }else fe = 0;

                l = (CheckBox) findViewById(R.id.BE);
                if(l.isChecked()){
                    b = 1;
                }else b = 0;

                l = (CheckBox) findViewById(R.id.TE);
                if(l.isChecked()){
                    t = 1;
                }else t = 0;

                EditText a = (EditText)findViewById(R.id.ZusatzE);
                if(a.getText().toString().equals("")){
                    zu = "...nothing to see here...";
                }else zu = a.getText().toString();

                notizDb = new NotizDatenQuelle(NotizEintrag.this);

                Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
                notizDb.open();

                if (aendern) notizDb.updateNotiz(id, dat, g, w, s, ta, t, ca, fe, b, zu);
                else notizDb.erstelleNotizeintrag(dat, g, w, s, ta, t, ca, fe, b, zu);

                Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
                notizDb.close();

                Log.d(LOG_TAG, "Datum ist " + dat);

                startActivity(new Intent(NotizEintrag.this, History.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void fehlenderEintragToast(){
        Context context = getApplicationContext();
        CharSequence text = "Eingabe nicht vorllständig!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
