package kr.kdev.dg1s.biowiki.ui.dictionary;

import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;

public class DictionaryViewerActivity extends BIActionBarActivity {



    /*

    String flowerType;
    String leafType;
    String fruitType;

    GridView flowerView;
    GridView leafView;
    GridView fruitView;
    RelativeLayout attributeSelector;

    LinearLayout plantInfo;

    ViewPager imagePager = null;
    MainPagerAdapter imagePagerAdapter = null;
    ViewPager attributePager = null;
    MainPagerAdapter attributePagerAdapter = null;

    Element flowerElement;
    Element leafElement;
    Element fruitElement;
    List<Element> flowerElements;
    List<Element> leafElements;
    List<Element> fruitElements;

    ImageLoaderConfiguration config;
    DisplayImageOptions options;

    Context context;

    Source source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.activity_dictionary);
        context = getApplicationContext();
        setupViews();
        try {
            initializeCategory();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to initialize category", Toast.LENGTH_SHORT).show();
        }
        setAuilAttributes();
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
                if (plantInfo.getVisibility() == View.VISIBLE) {
                    plantInfo.setVisibility(View.GONE);
                    attributeSelector.setVisibility(View.VISIBLE);
                    return true;
                }
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
        if (plantInfo.getVisibility() == View.VISIBLE) {
            plantInfo.setVisibility(View.GONE);
            attributeSelector.setVisibility(View.VISIBLE);
            return;
        } else {
            if (!(currentElement.getAttributeValue("name") == null)) {
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

    public void attributeSelection(View view) {
        findViewById(R.id.button_flower).setBackgroundColor(R.color.transparent);
        findViewById(R.id.button_leaf).setBackgroundColor(R.color.transparent);
        findViewById(R.id.button_fruit).setBackgroundColor(R.color.transparent);
        switch (view.getId()) {
            case R.id.button_flower:
                findViewById(R.id.button_flower).setBackgroundColor(R.color.blue_dark);
                imagePager.setCurrentItem(0, true);
            case R.id.button_leaf:
                findViewById(R.id.button_flower).setBackgroundColor(R.color.blue_dark);
                imagePager.setCurrentItem(1, true);
            case R.id.button_fruit:
                findViewById(R.id.button_flower).setBackgroundColor(R.color.blue_dark);
                imagePager.setCurrentItem(2, true);
        }
    }

    public void setAuilAttributes() {
        // Create global configuration and initialize ImageLoader with this configuration
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(4)
                .discCache(new UnlimitedDiscCache(getCacheDir()))
                .discCacheSize(200 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .discCacheFileCount(1000)
                .build();
    }

    public void setupViews() {
        // Container of pager containing attribute selectors
        attributeSelector = (RelativeLayout) findViewById(R.id.attributeSelector);
        // Details of plants
        plantInfo = (LinearLayout) findViewById(R.id.showdetails);
        // Set gridViews
        flowerView = (GridView) findViewById(R.id.flowerView);
        leafView = (GridView) findViewById(R.id.leafView);
        fruitView = (GridView) findViewById(R.id.fruitView);
        // Image pagers
        imagePagerAdapter = new MainPagerAdapter();
        imagePager = (ViewPager) findViewById(R.id.viewpager);
        imagePager.setAdapter(imagePagerAdapter);
        imagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:

                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                imagePager.getParent().requestDisallowInterceptTouchEvent(true);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        // Attribute Selector adapters
        attributePagerAdapter = new MainPagerAdapter();
        attributePager = (ViewPager) findViewById(R.id.viewpager);
        attributePager.setAdapter(attributePagerAdapter);
        attributePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:

                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                attributePager.getParent().requestDisallowInterceptTouchEvent(true);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        setAttributeSelectorListener();
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addImage(View newPage) {
        imagePagerAdapter.addView(newPage);
        imagePagerAdapter.notifyDataSetChanged();
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = imagePagerAdapter.removeView(imagePager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == imagePagerAdapter.getCount())
            pageIndex--;
        imagePager.setCurrentItem(pageIndex);
        imagePagerAdapter.notifyDataSetChanged();
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return imagePagerAdapter.getView(imagePager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        imagePager.setCurrentItem(imagePagerAdapter.getItemPosition(pageToShow), true);
    }

    public void initializeCategory() throws IOException {
        source = new Source(getAssets().open("xmls/dictionary.xml"));
        Log.d("XML", source.toString());
        flowerElement = source.getFirstElement("repo").getFirstElement("flower");
        leafElement = source.getFirstElement("repo").getFirstElement("leaf");
        fruitElement = source.getFirstElement("repo").getFirstElement("fruit");
        parseXML(null, -1, flowerView);
        parseXML(null, -1, leafView);
        parseXML(null, -1, fruitView);
    }

    public void setAttributeSelectorListener() {
        flowerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        leafView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        fruitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }

    public ArrayList<String> getDetails(String name, Element input) throws IOException {
        ArrayList<String> export = new ArrayList<String>();
        Log.d("XML", "Searching for details on " + name);
        Element element = input.getFirstElement("name", name, false);
        if (element == null) {
            return export;
        } Attributes attributes = element.getAttributes();
        for (Attribute attribute : attributes) {
            export.add(attribute.getName());
            export.add(attribute.getValue());
        } return export;
    }

    View plantDetails(String token, String value) {
        LinearLayout plantDetails = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.plant_detail_adapter, null);
        TextView name = (TextView) plantDetails.findViewById(R.id.name);
        TextView details = (TextView) plantDetails.findViewById(R.id.details);
        if (token.equals("name")) {
            name.setText("이름");
        } else if (token.equals("stump")) {
            name.setText("줄기");
        } else if (token.equals("leaf")) {
            name.setText("잎");
        } else if (token.equals("flower")) {
            name.setText("꽃");
        } else if (token.equals("fruit")) {
            name.setText("열매");
        } else if (token.equals("chromo")) {
            name.setText("핵형");
        } else if (token.equals("place")) {
            name.setText("서식지");
        } else if (token.equals("horizon")) {
            name.setText("수평분포");
        } else if (token.equals("vertical")) {
            name.setText("수직분포");
        } else if (token.equals("geograph")) {
            name.setText("식생지리");
        } else if (token.equals("vegetat")) {
            name.setText("식생형");
        } else if (token.equals("preserve")) {
            name.setText("종보존등급");
        } else {
            name.setText("기타");
        }
        name.setTextColor(getResources().getColor(R.color.black));
        details.setText(value);
        details.setTextColor(getResources().getColor(R.color.black));
        return plantDetails;
    }

    public void displayPlant() {
        plantInfo.removeViews(2, plantInfo.getChildCount() - 2);
        for (int i = 0; i < (details.size() / 2); i++) {
            if (details.get(2 * i).equals("image")) {
                if (!(details.get(2 * i + 1).equals(""))) {
                    imagePager.setVisibility(View.VISIBLE);
                    for (; 0 != imagePagerAdapter.getCount(); ) {
                        imagePagerAdapter.removeView(imagePager, 0);
                    }
                    imagePagerAdapter.notifyDataSetChanged();
                    for (String filename : Arrays.asList(details.get(2 * i + 1).split(" "))) {
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(config);
                        imageLoader.displayImage(BioWiki.getCurrentBlog().getHomeURL() + "repo/IMG/" + filename, imageView);
                        addImage(imageView);
                    }
                } else {
                    imagePager.setVisibility(View.GONE);
                }
            } else {
                plantInfo.addView(plantDetails(details.get(2 * i), details.get(2 * i + 1)));
            }
        }
        TextView plantName = (TextView) plantInfo.findViewById(R.id.plant_name);
        plantName.setText(tag);
        attributeSelector.setVisibility(View.GONE);
        plantInfo.setVisibility(View.VISIBLE);
    }



    public void parseXML(String tag, int position, View view) throws IOException {
        ArrayList<String> names = new ArrayList<String>();
        List<Element> elements;
        Element element = null;
        if (view == flowerView) {
            elements = flowerElements;
            element = flowerElement;
        } else if (view == leafView) {
            elements = leafElements;
            element = leafElement;
        } else if (view == fruitView) {
            elements = fruitElements;
            element = fruitElement;
        }
        if (position == -1) { // Initialize child elements
            elements = element.getChildElements();
        } else if (position == -2) { // Go up one element
            if (!element.getName().equals("flower") && !element.getName().equals("leaf")
                    && !element.getName().equals("fruit")) { // When element is not on the highest hierarchy
                element = element.getParentElement();
            } elements = element.getChildElements();
        } else if (element.getFirstElement("name", tag, false).getName().equals("type")) { // When the clicked object is an attribute

            ArrayList<String> details = getDetails(element.getFirstElement("name", tag, false).getAttributeValue("type"), element);
            if (details.size() == 0) {
                Toast.makeText(context, "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            displayPlant();
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

    */
}
