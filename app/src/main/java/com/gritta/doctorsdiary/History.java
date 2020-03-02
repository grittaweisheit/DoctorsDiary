package com.gritta.doctorsdiary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Intent;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Gritta on 12.03.2018.
 */


public class History extends AppCompatActivity {
    private final String LOG = History.class.getSimpleName();
    private NotizDatenQuelle notizDB;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notiz_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notizDB = new NotizDatenQuelle(this);

        final String ordnerPfad = Environment.getExternalStorageDirectory().getPath() + "/Backup/";

        ImageButton fab = (ImageButton) findViewById(R.id.plusNotiz);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(History.this, NotizEintrag.class);
                in.putExtra("aendern", 0);
                startActivity(in);
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.notizHistoryBack);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(History.this, Start.class);
                startActivity(in);
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.notizToExcel);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NotizDbHelfer notizDbHelfer = new NotizDbHelfer(getApplicationContext());
                final Cursor cu = notizDbHelfer.getNotiz();

                notizDB.open();
                List<Notiz> notizen = notizDB.getAllNotizen();
                notizDB.close();

                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "Notizeintraege.xls";
                File ordner = new File(sd.getAbsolutePath());
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    String[] s = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(History.this, s, 0);
                    return;
                } else {}

                if (!ordner.isDirectory()){
                    ordner.mkdirs();
                }try {
                    File file = new File(ordner, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("de", "DE"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    WritableSheet sheet = workbook.createSheet("NotizList", 0);

                    sheet.addCell(new Label(0,0,"Datum"));
                    sheet.addCell(new Label(1,0,"Gewicht"));
                    sheet.addCell(new Label(2,0,"Wohlbefinden"));
                    sheet.addCell(new Label(3,0,"Schlaf (in h)"));
                    sheet.addCell(new Label(4,0,"Tage"));
                    sheet.addCell(new Label(5,0,"T"));
                    sheet.addCell(new Label(6,0,"Ca"));
                    sheet.addCell(new Label(7,0,"Fe"));
                    sheet.addCell(new Label(8,0,"B"));
                    sheet.addCell(new Label(9,0,"Ergänzungen"));

                    for (int i = 0; i < notizen.size(); ++i){
                        Notiz n = notizen.get(i);
                        String d = n.getDatum();
                        String g = String.valueOf(n.getGewicht());
                        String w = String.valueOf(n.getWohlbefinden());
                        String ta, t, ca, fe, b;
                        if(n.getTage() == 0) ta = "nein"; else ta = "ja";
                        if(n.getT() == 0) t = "nein"; else t = "ja";
                        if(n.getCa() == 0) ca = "nein"; else ca = "ja";
                        if(n.getFe() == 0) fe = "nein"; else fe = "ja";
                        if(n.getB() == 0) b = "nein"; else b = "ja";
                        String s = String.valueOf(n.getSchlaf());
                        String z = n.getZusatz();

                        sheet.addCell(new Label(0,i+1, d));
                        sheet.addCell(new Label(1,i+1, g));
                        sheet.addCell(new Label(2,i+1, w));
                        sheet.addCell(new Label(3,i+1, s));
                        sheet.addCell(new Label(4,i+1, ta));
                        sheet.addCell(new Label(5,i+1, t));
                        sheet.addCell(new Label(6,i+1, ca));
                        sheet.addCell(new Label(7,i+1, fe));
                        sheet.addCell(new Label(8,i+1, b));
                        sheet.addCell(new Label(9,i+1, z));
                    }
                    workbook.write();
                    workbook.close();
                    Log.d(LOG, file.getAbsolutePath());
                    Toast.makeText(getApplication(), "Dateien in Excel Sheet exportiert", Toast.LENGTH_SHORT).show();

                } catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "Fehler!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        initializeContextualActionBar();

        notizDB.open();
        List<Notiz> notizen = notizDB.getAllNotizen();
        notizDB.close();

        ListAdapter adapter = new ArrayAdapter<Notiz>(getApplicationContext(), R.layout.notizeintrag_element, notizen);

        final ListView lv = (ListView)findViewById(R.id.list_view);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {

                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".NotizAnzeige");
                Notiz n = (Notiz)lv.getAdapter().getItem(arg2);
                intent.putExtra("datum", n.getDatum());
                intent.putExtra("gewicht", Double.toString(n.getGewicht()));
                intent.putExtra("wohlbefinden", Integer.toString(n.getWohlbefinden()));
                intent.putExtra("schlaf", Double.toString(n.getSchlaf()));
                intent.putExtra("tage", Integer.toString(n.getTage()));
                intent.putExtra("t", Integer.toString(n.getT()));
                intent.putExtra("ca", Integer.toString(n.getCa()));
                intent.putExtra("fe", Integer.toString(n.getFe()));
                intent.putExtra("b", Integer.toString(n.getB()));
                intent.putExtra("zusatz", n.getZusatz());
                intent.putExtra("id", n.getId());
                startActivity(intent);
            }
        });
    }

    private void initializeContextualActionBar(){

        final ListView notizenListView = (ListView) findViewById(R.id.list_view);
        notizenListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        notizenListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.cab_delete:
                        SparseBooleanArray touchedShoppingMemosPositions = notizenListView.getCheckedItemPositions();
                        notizDB.open();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                Notiz notiz = (Notiz) notizenListView.getItemAtPosition(postitionInListView);
                                Log.d(NotizEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + notiz.toString());
                                notizDB.einitragLoeschen(notiz);
                            }
                        }
                        showAllListEntries();
                        notizDB.close();
                        mode.finish();
                        return true;
                    case R.id.cab_change:
                        SparseBooleanArray touchedNotizPositions1 = notizenListView.getCheckedItemPositions();

                        for (int i=0; i < touchedNotizPositions1.size(); i++) {
                            boolean isChecked = touchedNotizPositions1.valueAt(i);
                            if(isChecked) {
                                notizDB.open();
                                int postitionInListView = touchedNotizPositions1.keyAt(i);
                                Notiz notiz = (Notiz) notizenListView.getItemAtPosition(postitionInListView);
                                Log.d(NotizEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + notiz.toString());

                                Intent inte = new Intent();
                                inte.setClassName(getPackageName(), getPackageName()+".NotizEintrag");

                                inte.putExtra("datum", notiz.getDatum());
                                inte.putExtra("gewicht", String.valueOf(notiz.getGewicht()));
                                inte.putExtra("wohlbefinden", String.valueOf(notiz.getWohlbefinden()));
                                inte.putExtra("schlaf", String.valueOf(notiz.getSchlaf()));
                                inte.putExtra("tage", String.valueOf(notiz.getTage()));
                                inte.putExtra("t", String.valueOf(notiz.getT()));
                                inte.putExtra("ca", String.valueOf(notiz.getCa()));
                                inte.putExtra("fe", String.valueOf(notiz.getFe()));
                                inte.putExtra("b", String.valueOf(notiz.getB()));
                                inte.putExtra("zusatz", notiz.getZusatz());
                                inte.putExtra("id", notiz.getId());
                                inte.putExtra("aendern", 1);
                                startActivity(inte);
                                notizDB.close();
                            }
                        }
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }
    private void showAllListEntries () {
        List<Notiz> notizL = notizDB.getAllNotizen();

        ArrayAdapter<Notiz> notizArrayAdapter = new ArrayAdapter<> (
                this,
                R.layout.notizeintrag_element,
                notizL);

        ListView notizListView = (ListView) findViewById(R.id.list_view);
        notizListView.setAdapter(notizArrayAdapter);
    }

}