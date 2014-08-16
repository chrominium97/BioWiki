package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.ArrayUtils;
import uk.ac.cam.cl.dtg.snowdon.LineGraphView;

public class ChartViewerActivity extends BIActionBarActivity {

    ArrayUtils arrayUtils;
    TextView startingDateText;
    TextView endDateText;
    LineGraphView lineGraph;
    Random random;

    boolean startingDateSet = false;
    private DatePickerDialog.OnDateSetListener startDayListener;
    private DatePickerDialog.OnDateSetListener endDayListener;

    void recalculate(int dataSize) {
        ArrayList<Integer> randomIntegerArray = new ArrayList<Integer>();
        
        int maxInt1 = arrayUtils.regenerateRandomIntegerValues(randomIntegerArray, dataSize, random);
        float[] integerArray1 = new float[randomIntegerArray.size()];
        arrayUtils.migrateValues(randomIntegerArray, integerArray1);

        int maxInt2 = arrayUtils.regenerateRandomIntegerValues(randomIntegerArray, dataSize, random);
        float[] integerArray2 = new float[randomIntegerArray.size()];
        arrayUtils.migrateValues(randomIntegerArray, integerArray2);

        int maxInt3 = arrayUtils.regenerateRandomIntegerValues(randomIntegerArray, dataSize, random);
        float[] integerArray3 = new float[randomIntegerArray.size()];
        arrayUtils.migrateValues(randomIntegerArray, integerArray3);

        float[][] data1 = {arrayUtils.genericPositions(dataSize), integerArray1};
        float[][] data2 = {arrayUtils.genericPositions(dataSize), integerArray2};
        float[][] data3 = {arrayUtils.genericPositions(dataSize), integerArray3};

        int maxValueRounded = (int) (Math.ceil(
                Collections.max(Arrays.asList(org.apache.commons.lang.ArrayUtils.toObject(new float[]{maxInt1, maxInt2, maxInt3}))).floatValue() * 1.1f));
        lineGraph.setData(new float[][][]{data1, data2, data3}, 0, dataSize - 1, 0, maxValueRounded);
        lineGraph.setYLabels(arrayUtils.genericLabels(maxValueRounded));
        lineGraph.setYLabelPositions(arrayUtils.genericAxisIndex(maxValueRounded));
        findViewById(R.id.info_text).setVisibility(View.GONE);

        lineGraph.redraw();
    }

    void initializeGlobalVariables() {
        arrayUtils = new ArrayUtils();

        random = new Random();
        lineGraph = (LineGraphView) findViewById(R.id.graph);

        startingDateText = (TextView) findViewById(R.id.startDate);
        startingDateText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Calendar calendar = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(ChartViewerActivity.this,
                        startDayListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)
                );
                mDialog.show();
            }
        });

        endDateText = (TextView) findViewById(R.id.endDate);
        endDateText.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        String[] dateParts = startingDateText.getText().toString().split("-");
                        try {
                            Dialog mDialog;
                            mDialog = new DatePickerDialog(ChartViewerActivity.this,
                                    endDayListener, Integer.valueOf(dateParts[0]), Integer.valueOf(dateParts[1]) - 1,
                                    Integer.valueOf(dateParts[2]));
                            mDialog.show();
                        } catch (NumberFormatException e) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ChartViewerActivity.this);
                            alertDialogBuilder.setTitle(getResources().getString(R.string.error));
                            alertDialogBuilder
                                    .setMessage(getResources().getString(R.string.select_start_date))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }

                }
        );
        startDayListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                arg2++;
                String date = arg1 + "-" + arg2 + "-" + arg3;
                startingDateText.setText(date);
                startingDateText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                startingDateSet = true;
                recalculate(6);
            }
        };
        endDayListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                String date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
                endDateText.setText(date);
                endDateText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                lineGraph.setVisibility(View.VISIBLE);
                recalculate(6);
            }
        };
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = getString(R.string.habitatTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        initializeGlobalVariables();
    }
}