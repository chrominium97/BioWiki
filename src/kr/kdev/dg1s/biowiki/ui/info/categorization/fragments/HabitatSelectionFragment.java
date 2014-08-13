package kr.kdev.dg1s.biowiki.ui.info.categorization.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.networking.CachedDownloader;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.InformationAdapter;

public class HabitatSelectionFragment extends SherlockFragment {

    public Element currentElement;
    GridView gridView;
    Random random = new Random();
    int downloadQueue;
    List<Element> displayedElements;

    Context context;

    Source source;

    OnPlantSelectedListener mCallback;
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
            mCallback = (OnPlantSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlantSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.selector_vertical_single, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViews();

        try {
            initializeCategory();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to initialize a blank linearlayout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public void setupViews() {
        // Lists plants and their categories
        gridView = (GridView) getView().findViewById(R.id.selector);
    }

    public void initializeCategory() throws IOException {
        // Instance of ImageAdapter Class
        //setSource(getString(R.string.biowiki_address) + "/xml/categories", Constants.FILE_XML_CATEGORY);
        source = new Source(getResources().openRawResource(R.raw.categories));
        Log.d("XML", source.toString());
        currentElement = source.getFirstElement("repo");
        parseXML(null, -1);
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

    public ArrayList<String> getDetails(String name) throws IOException {
        ArrayList<String> export = new ArrayList<String>();

        Source plantInfo = new Source(getResources().getAssets().open("xmls/kingdom.xml"));
        Log.d("XML", "Searching for details on " + name);
        Element element = plantInfo.getFirstElement("name", name, false);
        if (element == null) {
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
                Toast.makeText(context, "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            mCallback.onPlantSelected(tag);
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
        gridView.setAdapter(new InformationAdapter(context, names));
        if (tag != null) {
            getSherlockActivity().getSupportActionBar().setTitle(tag);
        } else if (!(currentElement.getAttributeValue("name") == null)) {
            getSherlockActivity().getSupportActionBar().setTitle(currentElement.getAttributeValue("name"));
        } else {
            getSherlockActivity().getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Container Activity must implement this interface
    public interface OnPlantSelectedListener {
        public void onPlantSelected(String name);
    }

    class BufferedSource extends Thread {
        public BufferedSource(String url, String fileName) {
            int identifier = random.nextInt();
            CachedDownloader downloader = new CachedDownloader(identifier);
            downloadQueue = downloader.prepareFile(url, fileName, "XML");
        }
    }
}
