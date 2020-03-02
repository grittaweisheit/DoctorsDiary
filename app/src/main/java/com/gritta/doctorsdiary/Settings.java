package com.gritta.doctorsdiary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

public class Settings extends AppCompatActivity {
    static final String LOG = Settings.class.getSimpleName();

            NotizDatenQuelle notizDatenQuelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button ex = (Button)findViewById(R.id.importData);
        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    String[] s = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(Settings.this, s, 0);
                    return;
                } else {}

                importData(getBaseContext(), "Notizeintraege.xls");
            }
        });

    }

    private void importData(Context context, String filename){
        notizDatenQuelle = new NotizDatenQuelle(this);
        String fn = filename;

      /*  if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            String[] s = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(Settings.this, s, 0);
            return;
        } else {}*/

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e(LOG, "Storage not available or read only");
            return;
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fn);
            FileInputStream myInput = new FileInputStream(file);

            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            HSSFWorkbook myWorkbook = new HSSFWorkbook(myFileSystem);

            HSSFSheet mySheet = myWorkbook.getSheetAt(0);

            Iterator rowIterator = mySheet.rowIterator();
            Row r1 = (Row)rowIterator.next();

            int i = 0;

            while (rowIterator.hasNext()){
                Row row = (Row)rowIterator.next();
                i = 0;
                String datum = "";
                double gewicht = 0;
                int wohlbefinden = 0;
                double schlaf = 0;
                int tage = 0, t = 0, ca = 0, fe = 0, b = 0;
                String zusatz = "";
                notizDatenQuelle.open();

                Iterator cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = (Cell)cellIterator.next();
                    switch (i){
                        case 0: datum = cell.toString();break;
                        case 1: gewicht = Double.parseDouble(cell.toString());break;
                        case 2: wohlbefinden = Integer.parseInt(cell.toString());break;
                        case 3: schlaf = Double.parseDouble(cell.toString());break;
                        case 4: if(cell.toString().equals("ja")) tage = 1; else tage = 0; break;
                        case 5:  if(cell.toString().equals("ja")) t = 1; else t = 0; break;
                        case 6:  if(cell.toString().equals("ja")) ca = 1; else ca = 0; break;
                        case 7:  if(cell.toString().equals("ja")) fe = 1; else fe = 0; break;
                        case 8:  if(cell.toString().equals("ja")) b = 1; else b = 0; break;
                        case 9: zusatz = cell.toString();break;
                    }
                    ++i;
                }
                notizDatenQuelle.erstelleNotizeintrag(datum, gewicht, wohlbefinden,schlaf, tage, t, ca, fe, b, zusatz);
                notizDatenQuelle.close();
            }
            notizDatenQuelle.open();
            List<Notiz> notizen = notizDatenQuelle.getAllNotizen();
            for (int j = 0; j < notizen.size(); ++j){
                Notiz jn = notizen.get(j);
                for (int k = j+1; k < notizen.size(); ++k){
                    Notiz kn = notizen.get(k);
                    if (jn.getDatum_int() == kn.getDatum_int()) notizDatenQuelle.eintragLoeschen(kn.getId());
                }
            }
            notizDatenQuelle.close();

            Toast.makeText(context, "Hat geklappt!", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Hat nicht geklappt!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
