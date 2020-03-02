package com.gritta.doctorsdiary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

public class Statistiken extends AppCompatActivity {

    public static final String GEWICHT = "Gewicht";
    public static final String SCHLAF = "Schlaf";
    public static final String WOHL = "Wohlbefinden";

    public static final String EINMONAT = "1 Monat";
    public static final String DREIMONAT = "3 Monate";
    public static final String JAHR = "1 Jahr";

    private static final String LOG_TAG = Statistiken.class.getSimpleName();

    private int zeitraum;

    private GraphicalView chartView;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(3);
    private XYSeriesRenderer schlafRenderer;
    private XYSeries schlafSeries;
    private XYSeriesRenderer gewichtRenderer;
    private XYSeries gewichtSeries;
    private XYSeriesRenderer wohlRenderer;
    private XYSeries wohlSeries;

    private NotizDatenQuelle notitzDb;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ein_Monat:
                    zeitraum = 30;
                    showStatistics();
                    return true;
                case R.id.drei_Monate:
                    zeitraum = 90;
                    showStatistics();
                    return true;
                case R.id.ein_Jahr:
                    zeitraum = 365;
                    showStatistics();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistik);
        notitzDb = new NotizDatenQuelle(this);
        zeitraum = 30;
        showStatistics();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.statistikBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Statistiken.this, Start.class));
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void addData(){
        schlafSeries.clear();
        gewichtSeries.clear();
        wohlSeries.clear();

        int z = zeitraum;

        notitzDb.open();
        List<Notiz> notizen = notitzDb.getAllNotizen();
        notitzDb.close();
        int x = 0;
        int e;
        if (notizen.size() < z) e = notizen.size() - 1;
        else e = z;
        int heute = notizen.get(e).getDatum_int();
        Log.d(LOG_TAG, "Heute ist " + String.valueOf(heute));
        for (int i = e; i >= 0; --i){
            Notiz n = notizen.get(i);

            Log.d(LOG_TAG, String.valueOf(n.getDatum_int()) + " bei " + String.valueOf(x));
            schlafSeries.add(x, n.getSchlaf());
            gewichtSeries.add(x, n.getGewicht());
            wohlSeries.add(x, n.getWohlbefinden());
            heute = heute + 1;
            x = x + 1;
        }

        mRenderer.setXAxisMax(zeitraum, 0);
        mRenderer.setXAxisMax(zeitraum, 1);
        mRenderer.setXAxisMax(zeitraum, 2);

        if (zeitraum == 365)
        mRenderer.setXLabels(zeitraum/20);

    }
    private void initChart(){
        schlafSeries = new XYSeries("Schlaf");
        schlafRenderer = new XYSeriesRenderer();
        schlafRenderer.setLineWidth(4);
        schlafRenderer.setColor(Color.BLUE);
        schlafRenderer.setPointStyle(PointStyle.CIRCLE);
        schlafRenderer.setPointStrokeWidth(5);
        schlafRenderer.setDisplayChartValues(true);

        gewichtSeries = new XYSeries("Gewicht");
        gewichtRenderer = new XYSeriesRenderer();
        gewichtRenderer.setLineWidth(4);
        gewichtRenderer.setColor(Color.RED);
        gewichtRenderer.setPointStyle(PointStyle.CIRCLE);
        gewichtRenderer.setPointStrokeWidth(5);
        gewichtRenderer.setDisplayChartValues(true);

        wohlSeries = new XYSeries("Wohlbefinden");
        wohlRenderer = new XYSeriesRenderer();
        wohlRenderer.setLineWidth(4);
        wohlRenderer.setColor(Color.GREEN);
        wohlRenderer.setPointStyle(PointStyle.CIRCLE);
        wohlRenderer.setPointStrokeWidth(5);
        wohlRenderer.setDisplayChartValues(true);

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setMargins(new int[]{20,20,50,50});
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);

        mRenderer.setClickEnabled(false);
        mRenderer.setPanEnabled(true, true);
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setZoomEnabled(true, true);
        mRenderer.setExternalZoomEnabled(false);
        mRenderer.setZoomEnabled(true);
        mRenderer.setInScroll(true);

        mRenderer.setLegendTextSize(30);
        mRenderer.setFitLegend(true);

        mRenderer.setYLabels(10);
        mRenderer.setChartTitleTextSize(15);
        mRenderer.setLabelsTextSize(24);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setShowGrid(true); // we show the grid
        /*
        mRenderer.setYTitle("Schlaf", 0);
        mRenderer.setYTitle("Gewicht", 1);
        mRenderer.setYTitle("Wohlbefinden", 2);
        */
        mRenderer.setXAxisMin(0,0);
        mRenderer.setXAxisMin(0,1);
        mRenderer.setXAxisMin(0,2);

        mRenderer.setYAxisMin(0,0);
        mRenderer.setYAxisMax(80,0); // sollte eigentlich eher 15 sein...

        mRenderer.setYAxisMin(0,1);
        mRenderer.setYAxisMax(80,1);

        mRenderer.setYAxisMin(0,2);
        mRenderer.setYAxisMax(10,2);


        mRenderer.setYLabelsColor(0,Color.BLUE);
        mRenderer.setYLabelsColor(1,Color.RED);
        mRenderer.setYLabelsColor(2,Color.GREEN);

        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setYLabelsColor(1, Color.BLACK);
        mRenderer.setYLabelsColor(2, Color.BLACK);

        mRenderer.setYAxisAlign(Paint.Align.LEFT, 0);
        mRenderer.setYAxisAlign(Paint.Align.RIGHT, 1);
        mRenderer.setYAxisAlign(Paint.Align.RIGHT, 2);
        mRenderer.setShowAxes(true);

        mDataset.addSeries(0, schlafSeries);
        mDataset.addSeries(1, gewichtSeries);
        mDataset.addSeries(2, wohlSeries);

        mRenderer.addSeriesRenderer(0, schlafRenderer );
        mRenderer.addSeriesRenderer(1, gewichtRenderer);
        mRenderer.addSeriesRenderer(2, wohlRenderer);
    }

    private void showStatistics(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.chart);
        if (chartView == null){
            layout.removeAllViews();
            initChart();
            addData();
            chartView = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
            layout.addView(chartView);

        } else addData(); chartView.repaint();
    }

}
