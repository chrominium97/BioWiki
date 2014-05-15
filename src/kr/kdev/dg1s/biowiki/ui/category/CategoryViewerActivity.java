package kr.kdev.dg1s.biowiki.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.simonvt.menudrawer.MenuDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.MenuDrawerItem;
import kr.kdev.dg1s.biowiki.ui.dictionary.DictionaryViewerActivity;
import kr.kdev.dg1s.biowiki.widgets.BWTextView;

public class CategoryViewerActivity extends BIActionBarActivity {

    GridView gridView;
    LinearLayout layout;

    Element currentElement;
    List<Element> displayedElements;

    Source source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.category);
        gridView = (GridView) findViewById(R.id.list_view);
        layout = (LinearLayout) findViewById(R.id.showdetails);
        // Instance of ImageAdapter Class
        try {
            source = new Source(getResources().openRawResource(R.raw.categories));
            Log.d("XML", source.toString());
            currentElement = source.getFirstElement("repo");
            parseXML(null, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("", "Setting gridView listener");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view;
                Log.d("Button", "Clicked " + position);
                try {
                    parseXML(textView.getText().toString(), position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.hierarchy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.go_up:
                try {
                    parseXML(null, -2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (mMenuDrawer != null) {
            final int drawerState = mMenuDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                mMenuDrawer.closeMenu();
                return;
            }
        }
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            return;
        } else {
            if (!(currentElement.getAttributeValue("name")==null)) {
                try {
                    parseXML(null, -2);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        super.onBackPressed();
    }

    public ArrayList<String> getDetails(String name) throws IOException {
        ArrayList<String> export = new ArrayList<String>();
        Source source1 = new Source(getAssets().open("xmls/kingdom.xml"));
        Log.d("XML", "Searching for details on " + name);
        Element element = source1.getFirstElement("name", name, false);
        if (element==null) {
            return export;
        }
        Attributes attributes = element.getAttributes();
            for (Attribute attribute : attributes) {
                export.add(attribute.getName());
                export.add(attribute.getValue());
            }
        return export;
    }

    public void parseXML(String tag, int position) throws IOException {
        ArrayList<String> names = new ArrayList<String>();

        if (position == -1) {
            displayedElements = currentElement.getChildElements();
        } else if (position == -2) {
            if (!currentElement.getName().equals("repo"))
                currentElement = currentElement.getParentElement();
            displayedElements = currentElement.getChildElements();
        } else if (currentElement.getFirstElement("name", tag, false).getName().equals("what")) {
            ArrayList<String> details = getDetails(currentElement.getFirstElement("name", tag, false).getAttributeValue("name"));
            if (details.size() == 0) {
                Toast.makeText(getApplicationContext(), "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < (details.size() / 2); i++) {
                LinearLayout plantDetails = (LinearLayout) LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.plant_detail_adapter, null);
                TextView textView = (TextView) plantDetails.findViewById(R.id.name);
                String token = details.get(2 * i);
                if (token.equals("name")) {
                    textView.setText("이름");
                } else if (token.equals("stump")) {
                    textView.setText("줄기");
                } else if (token.equals("leaf")) {
                    textView.setText("잎");
                } else if (token.equals("flower")) {
                    textView.setText("꽃");
                } else if (token.equals("fruit")) {
                    textView.setText("열매");
                } else if (token.equals("chromo")) {
                    textView.setText("핵형");
                } else if (token.equals("place")) {
                    textView.setText("서식지");
                } else if (token.equals("horizon")) {
                    textView.setText("수평분포");
                } else if (token.equals("vertical")) {
                    textView.setText("수직분포");
                } else if (token.equals("geograph")) {
                    textView.setText("식생지리");
                } else if (token.equals("vegetat")) {
                    textView.setText("식생형");
                } else if (token.equals("preserve")) {
                    textView.setText("종보존등급");
                } else {
                    textView.setText("기타");
                } textView.setTextColor(getResources().getColor(R.color.black));
                Log.d("TextView", "Set textView to " + textView.getText());
                textView = (TextView) plantDetails.findViewById(R.id.details);
                textView.setText(details.get((2 * i) +1));
                textView.setTextColor(getResources().getColor(R.color.black));
                layout.addView(plantDetails);
            }
            TextView textView = (TextView) findViewById(R.id.plant_name);
            textView.setText(tag);
            gridView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            return;
        } else {
            currentElement = currentElement.getFirstElement("name", tag, false);
            displayedElements = currentElement.getChildElements();
        }
        for (Element element : displayedElements) {
            names.add(element.getAttributeValue("name"));
            Collections.sort(names);
        }
        gridView.invalidateViews();
        gridView.setAdapter(new ElementAdapter(this, names));
        if (tag != null) {
            getSupportActionBar().setTitle(tag);
        } else if (!(currentElement.getAttributeValue("name") == null)) {
            getSupportActionBar().setTitle(currentElement.getAttributeValue("name"));
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
