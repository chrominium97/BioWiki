package kr.kdev.dg1s.biowiki.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;

public class CategoryViewerActivity extends BWActionBarActivity {

    GridView gridView;

    String parent = "";
    String last_tag = "";
    Source source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.category);
        gridView = (GridView) findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        try {
            source = new Source(getResources().openRawResource(R.raw.categories));
            parseXML("", -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view;
                try {
                    Log.d("XML", textView.getText().toString());
                    parseXML(textView.getText().toString(), position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseXML(String tag, int position) throws IOException{

        List<Element> elements;
        ArrayList<String> names = new ArrayList<String>();
        if (tag.equals("")) {
            elements = source.getAllElements("flower");
        } else {
            elements = source.getAllElements(tag);
        }
        for (Element element : elements) {
            names.add(element.getAttributeValue("name"));
            Log.d("gridView", element.getAttributeValue("name"));
        }
        gridView.invalidateViews();
        gridView.setAdapter(new ElementAdapter(this, names));
        getSupportActionBar().setTitle(tag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
