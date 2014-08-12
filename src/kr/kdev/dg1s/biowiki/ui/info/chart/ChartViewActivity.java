package kr.kdev.dg1s.biowiki.ui.info.chart;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import uk.ac.cam.cl.dtg.snowdon.HeatMapLegendView;
import uk.ac.cam.cl.dtg.snowdon.HeatMapView;

import android.os.Bundle;

public class ChartViewActivity extends BIActionBarActivity {
    private HeatMapView mGraph;
    private HeatMapLegendView mapLegendView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        mGraph = (HeatMapView) findViewById(R.id.graph);
        mapLegendView = (HeatMapLegendView) findViewById(R.id.legend);

        // Our datasets
        float[] data1 = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 100.0f};
        float[] data2 = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f};
        float[][] data3 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f}, {3.0f, 1.0f, 3.0f, Float.NaN, 2.0f, 7.0f}};

        // The first dataset must be inputted into the graph using setData to replace the placeholder data already there
        mGraph.setData(new float[][]{data1, data2});

        /**
        // We want to add the second data set, but only adjust the max x value as all the other stay the same, so we input NaNs in their place
        mGraph.addData(data2, Float.NaN, 7, Float.NaN, Float.NaN);

        // Add the third dataset, which includes NaNs to signify a gap in the data
        mGraph.addData(data3, Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        mGraph.setOverlay1Text("Snowdon", 0.1f, 0.1f);
         */
    }
}