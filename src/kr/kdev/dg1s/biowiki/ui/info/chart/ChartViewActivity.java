package kr.kdev.dg1s.biowiki.ui.info.chart;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Random;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.common.utils.AutoResizingTextView;
import uk.ac.cam.cl.dtg.snowdon.HeatMapView;
import uk.ac.cam.cl.dtg.snowdon.LineGraphView;

public class ChartViewActivity extends BIActionBarActivity {

    String xData;
    String yData;

    boolean startDaySet = false;
    boolean endDaySet = false;

    AutoResizingTextView startDay;
    AutoResizingTextView endDay;

    LineGraphView mGraph;
    Random random;

    void recalculate() {
        float[] firstDataSet = {random.nextFloat()*100, random.nextFloat()*100};
        mGraph.setData(new float[][][]{{firstDataSet}}, 0, 50, 0, 100);
        mGraph.redraw();
        mGraph.setVisibility(View.VISIBLE);
    }

    void setData(String data, boolean isXAxis) {
        if (isXAxis && (xData == null || !xData.equals(data))) {
            xData = data;
        } else if (!isXAxis && (yData == null || !yData.equals(data))) {
            yData = data;
        } else {
            return;
        }
        recalculate();
    }

    private DatePickerDialog.OnDateSetListener startDayListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2++;
            String date = arg1 + "-" + arg2 + "-" + arg3;
            final String DOUBLE_BYTE_SPACE = "\u3000";
            String fixString = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
                    && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                fixString = DOUBLE_BYTE_SPACE;
            }
            startDay.setText(fixString + date + fixString);
            startDaySet = true;
            recalculate();
        }
    };

    private DatePickerDialog.OnDateSetListener endDayListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;

            String date = arg1 + "-" + arg2 + "-" + arg3;
            final String DOUBLE_BYTE_SPACE = "\u3000";
            String fixString = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
                    && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                fixString = DOUBLE_BYTE_SPACE;
            }
            endDay.setText(fixString + date + fixString);
            endDaySet = true;
            recalculate();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "떡우";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        random = new Random();

        mGraph = (LineGraphView) findViewById(R.id.graph);

        float[][] data1 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, {10.0f, 8.0f, 12.0f, 12.0f, 6.0f, 5.0f}};
        float[][] data2 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f}, {8.0f, 4.0f, 11.0f, 10.0f, 5.0f, 2.0f}};
        float[][] data3 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f}, {3.0f, 1.0f, 3.0f, Float.NaN, 2.0f, 7.0f}};

        // The first dataset must be inputted into the graph using setData to replace the placeholder data already there
        mGraph.setData(new float[][][]{data1}, 0, 5, 0, 15);

        // We want to add the second data set, but only adjust the max x value as all the other stay the same, so we input NaNs in their place
        mGraph.addData(data2, Float.NaN, 7, Float.NaN, Float.NaN);

        // Add the third dataset, which includes NaNs to signify a gap in the data
        mGraph.addData(data3, Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        mGraph.redraw();

        startDay = (AutoResizingTextView) findViewById(R.id.startDate);
        startDay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Calendar calender = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(ChartViewActivity.this,
                        startDayListener, calender.get(Calendar.YEAR),
                        calender.get(Calendar.MONTH), calender
                        .get(Calendar.DAY_OF_MONTH)
                );

                mDialog.show();
            }
        });

        endDay = (AutoResizingTextView) findViewById(R.id.endDate);
        endDay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Calendar calender = Calendar.getInstance();
                Dialog mDialog;
                if (endDaySet) {
                    String[] strings = startDay.getText().toString().split("-");
                    mDialog = new DatePickerDialog(ChartViewActivity.this,
                            endDayListener, Integer.valueOf(strings[0]), Integer.valueOf(strings[1]) - 1,
                            Integer.valueOf(strings[2]));
                } else {
                    mDialog = new DatePickerDialog(ChartViewActivity.this,
                            endDayListener, calender.get(Calendar.YEAR),
                            calender.get(Calendar.MONTH), calender
                            .get(Calendar.DAY_OF_MONTH)
                    );
                }

                mDialog.show();
            }
        });
    }
}