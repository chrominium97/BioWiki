package kr.kdev.dg1s.biowiki.ui.info.distribution.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;

public class MapViewerFragment extends SupportMapFragment implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        LocationListener,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final LatLng LOCATION_DEFAULT = new LatLng(35.886826, 128.721226);
    private static final LatLng DG1S = new LatLng(35.886545, 128.722626);
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private final List<Marker> mDistributionStats = new ArrayList<Marker>();
    private final List<MarkerOptions> mMarkerOptions = new ArrayList<MarkerOptions>();
    OnPlantSelectedListener mCallback;
    Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 573) {
                loadMarkers(mMarkerOptions);
                if (context != null) {
                    Toast.makeText(context, "Loaded locations.", Toast.LENGTH_SHORT).show();
                }
            }
            if (message.getData().getStringArrayList("position") != null) {
                List<String> mImport = message.getData().getStringArrayList("position");
                Log.d("Handler", "Message received and initializing");
                String hueIndex;
                if (mImport.get(3) == null) {
                    hueIndex = "";
                } else {
                    hueIndex = mImport.get(3);
                }
                mMarkerOptions.add(new MarkerOptions()
                                .position(new LatLng(
                                        Double.parseDouble(mImport.get(1)),
                                        Double.parseDouble(mImport.get(2))))
                                .title(mImport.get(0))
                                .icon(BitmapDescriptorFactory.defaultMarker(getHue(hueIndex)))
                );
                Log.d("Marker", "Added Marker : " + mImport.get(0) + " @" + mImport.get(1) + ", " + mImport.get(2));
            }
        }
    };
    SeekBar mRotationBar;
    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private Marker mDG1S;
    private Marker mUser;
    private boolean loadedMarkers = false;
    private TextView mTopText;
    private CheckBox mFlatBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.maps_marker, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews();
        setUpMapIfNeeded();
    }

    /**
     * Callback called when connected to GCore. Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks}.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
    }

    /**
     * Callback called when disconnected from GCore. Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks}.
     */
    @Override
    public void onDisconnected() {
        // Do nothing
    }

    /**
     * Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener}.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
        // Do nothing
    }

    private void setUpMapIfNeeded() {
        Log.d("Map", "Checking if needed");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = MapViewerFragment.this.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                setUpMap();
            }
        } else {
            setUpMap();
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    context,
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    private Source bringSource(URL url) throws IOException {
        Log.d("Source Input", "Sending source");
        return new Source(new InputStreamReader(url.openStream(), "UTF-8"));
    }

    private List<Message> parseMarkers(Source source) {
        Log.d("ParseMarkers", "XML Input : " + source);
        List<Message> messages = new ArrayList<Message>();
        List<Element> speciesArray = source.getAllElements("species");
        for (Element species : speciesArray) {
            for (Element location : species.getAllElements("location")) {
                ArrayList<String> attributes = new ArrayList<String>();
                attributes.add(0, species.getAttributeValue("name"));
                attributes.add(1, location.getAttributeValue("latitude"));
                attributes.add(2, location.getAttributeValue("longitude"));
                attributes.add(3, species.getAttributeValue("color"));
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("position", attributes);
                Message message = new Message();
                message.setData(bundle);
                messages.add(message);
                Log.d("Messages", "Added Message to array : " + species.getAttributeValue("name") + " @"
                        + location.getAttributeValue("latitude") + ", " + location.getAttributeValue("longitude"));
                Log.d("Message", "Detail : " + messages);
            }
        }
        Message endTagMessage = new Message();
        endTagMessage.what = 573;
        messages.add(endTagMessage);
        return messages;
    }

    private void loadMarkers(List<MarkerOptions> mMarkerOptions) {
        for (MarkerOptions markerOptions : mMarkerOptions) {
            Log.d("Markers", "Added markers to map : " + markerOptions.getTitle() + " @" +
                    markerOptions.getPosition().latitude + ", " + markerOptions.getPosition().longitude);
            mDistributionStats.add(mMap.addMarker(markerOptions));
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = MapViewerFragment.this.getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(LOCATION_DEFAULT)
                            .include(DG1S)
                            .build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
    }

    private void addMarkersToMap() {
        // Start thread for updating markers
        if (loadedMarkers) {
            updateMarkers updateMarkers = new updateMarkers();
            updateMarkers.setContextClassLoader(getClass().getClassLoader());
            updateMarkers.start();
        }
        loadedMarkers = true;

        // Uses a colored icon.
        mDG1S = mMap.addMarker(new MarkerOptions()
                .position(LOCATION_DEFAULT)
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Creates a draggable marker. Long press to drag.
        mUser = mMap.addMarker(new MarkerOptions()
                .position(DG1S)
                .title("Daegu Il Science High School")
                .snippet("!!!")
                .draggable(true));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(context, "Map isn't ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private float getHue(String config) {
        Log.d("COLOR", config);
        if (config.equals("azure")) {
            return BitmapDescriptorFactory.HUE_AZURE;
        } else if (config.equals("blue")) {
            return BitmapDescriptorFactory.HUE_BLUE;
        } else if (config.equals("cyan")) {
            return BitmapDescriptorFactory.HUE_CYAN;
        } else if (config.equals("green")) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (config.equals("magenta")) {
            return BitmapDescriptorFactory.HUE_MAGENTA;
        } else if (config.equals("orange")) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (config.equals("red")) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (config.equals("rose")) {
            return BitmapDescriptorFactory.HUE_ROSE;
        } else if (config.equals("violet")) {
            return BitmapDescriptorFactory.HUE_VIOLET;
        } else if (config.equals("yellow")) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return 0;
        }
    }

    /**
     * Called when the Clear button is clicked.
     */
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /**
     * Called when the Reset button is clicked.
     */
    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    /**
     * Called when the Reset button is clicked.
     */
    public void onToggleFlat(View view) {
        if (!checkReady()) {
            return;
        }
        boolean flat = mFlatBox.isChecked();
        for (Marker marker : mDistributionStats) {
            marker.setFlat(flat);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!checkReady()) {
            return;
        }
        float rotation = seekBar.getProgress();
        for (Marker marker : mDistributionStats) {
            marker.setRotation(rotation);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        /*
        if (marker.equals(mPerth)) {
            // This causes the marker at Perth to bounce into position when it is clicked.
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator
                            .getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
        else if (marker.equals(mAdelaide)) {
            // This causes the marker at Adelaide to change color and alpha.
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom.nextFloat() * 360));
            marker.setAlpha(mRandom.nextFloat());
        }
        */
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(context, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    //
    // Marker related listeners.
    //

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

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

    public void setupViews() {
        context = getActivity().getApplicationContext();
    }

    public ArrayList<String> getDetails(String name) throws IOException {
        ArrayList<String> export = new ArrayList<String>();
        Source source1 = new Source(getResources().getAssets().open("xmls/kingdom.xml"));
        Log.d("XML", "Searching for details on " + name);
        Element element = source1.getFirstElement("name", name, false);
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

    @Override
    public void onResume() {
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
        super.onResume();
    }

    // Container Activity must implement this interface
    public interface OnPlantSelectedListener {
        public void onPlantSelected(String name);
    }

    /**
     * Demonstrates customizing the info window and/or its contents.
     */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater(getArguments()).inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater(getArguments()).inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(DG1S)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mUser)) {
                badge = R.drawable.badge_victoria;
            } else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    class updateMarkers extends Thread {
        public void run() {
            try {
                Log.d("Network", "Initiating...");
                URL url = new URL(BioWiki.getCurrentBlog().getHomeURL() + getString(R.string.server_subdomain)
                        + getString(R.string.distribution_sample));
                List<Message> messages = parseMarkers(bringSource(url));
                Log.d("Network", "Received source");
                if (messages == null) {
                    Log.d("Network", "SOURCE IS NULL!");
                } else for (Message message : messages) {
                    handler.sendMessage(message);
                    Log.d("Handler", "Message sent");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
