package kr.kdev.dg1s.biowiki.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;

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

    Element currentElement;
    List<Element> displayedElements;

    Source source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.category);

        gridView = (GridView) findViewById(R.id.list_view);
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

        if (!(currentElement.getAttributeValue("name")==null)) {
            try {
                parseXML(null, -2);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onBackPressed();
    }

    public void parseXML(String tag, int position) throws IOException {
        ArrayList<String> names = new ArrayList<String>();

        if (position == -1) {
            displayedElements = currentElement.getChildElements();
        } else if (position == -2) {
            if (!currentElement.getName().equals("repo"))
                currentElement = currentElement.getParentElement();
            displayedElements = currentElement.getChildElements();
        } else if (currentElement.getName().equals("what")) {
            Intent intent = new Intent(get, DictionaryViewerActivity.class);
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
