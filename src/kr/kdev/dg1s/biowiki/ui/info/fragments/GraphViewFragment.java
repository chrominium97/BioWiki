package kr.kdev.dg1s.biowiki.ui.info.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;

public class GraphViewFragment extends SherlockFragment {

    Context context;

    OnDatasetRefreshRequest mCallback;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:

            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDatasetRefreshRequest) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDatasetRefreshRequest");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.graph, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setValues();
        getLayoutInflater(savedInstanceState).inflate(R.layout.graph, null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    void setValues() {
        // 표시할 수치값
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{14230, 12300, 14240, 15244, 15900, 19200,
                22030, 21200, 19500, 15500, 12600, 14000});

        /** 그래프 출력을 위한 그래픽 속성 지정객체 */
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // 상단 표시 제목과 글자 크기
        renderer.setChartTitle("2011년도 판매량");
        renderer.setChartTitleTextSize(20);

        // 분류에 대한 이름
        String[] titles = new String[]{"월별 판매량"};

        // 항목을 표시하는데 사용될 색상값
        int[] colors = new int[]{Color.YELLOW};

        // 분류명 글자 크기 및 각 색상 지정
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }

        // X,Y축 항목이름과 글자 크기
        renderer.setXTitle("월");
        renderer.setYTitle("판매량");
        renderer.setAxisTitleTextSize(12);

        // 수치값 글자 크기 / X축 최소,최대값 / Y축 최소,최대값
        renderer.setLabelsTextSize(10);
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(12.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(24000);

        // X,Y축 라인 색상
        renderer.setAxesColor(Color.WHITE);
        // 상단제목, X,Y축 제목, 수치값의 글자 색상
        renderer.setLabelsColor(Color.CYAN);

        // X축의 표시 간격
        renderer.setXLabels(12);
        // Y축의 표시 간격
        renderer.setYLabels(5);

        // X,Y축 정렬방향
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // X,Y축 스크롤 여부 ON/OFF
        renderer.setPanEnabled(false, false);
        // ZOOM기능 ON/OFF
        renderer.setZoomEnabled(false, false);
        // ZOOM 비율
        renderer.setZoomRate(1.0f);
        // 막대간 간격
        renderer.setBarSpacing(0.5f);

        // 설정 정보 설정
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < titles.length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }

        // 그래프 객체 생성
        GraphicalView gv = ChartFactory.getBarChartView(context, dataset,
                renderer, BarChart.Type.STACKED);

        // 그래프를 LinearLayout에 추가
        LinearLayout llBody = (LinearLayout) getActivity().findViewById(R.id.chart);
        if (llBody == null)
            Toast.makeText(context, "NO LAYOUT", Toast.LENGTH_SHORT).show();

        llBody.addView(gv);

        Log.d("Graph", "Added Successfully");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Container Activity must implement this interface
    public interface OnDatasetRefreshRequest {
        public void onDatasetRefreshRequest(ArrayList<String> names);
    }
}
