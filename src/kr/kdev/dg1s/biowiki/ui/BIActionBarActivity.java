package kr.kdev.dg1s.biowiki.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Blog;
import kr.kdev.dg1s.biowiki.ui.accounts.WelcomeActivity;
import kr.kdev.dg1s.biowiki.ui.category.CategoryViewerActivity;
import kr.kdev.dg1s.biowiki.ui.prefs.PreferencesActivity;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.DisplayUtils;
import kr.kdev.dg1s.biowiki.util.StringUtils;
import kr.kdev.dg1s.biowiki.util.ToastUtils;

/**
 * Base class for Activities that include a standard action bar and menu drawer.
 */
public abstract class BIActionBarActivity extends SherlockFragmentActivity {
    /**
     * Used to restore active activity on app creation
     */
    protected static final int INTRO_ACTIVITY = 0;
    protected static final int CATEGORIZATION_ACTIVITY = 1;
    protected static final String LAST_ACTIVITY_PREFERENCE = "bi_pref_last_activity";
    private static final String TAG = "BWActionBarActivity";
    /**
     * AuthenticatorRequest code used when no accounts exist, and user is prompted to add an
     * account.
     */
    private static final int ADD_ACCOUNT_REQUEST = 100;
    /**
     * AuthenticatorRequest code for reloading menu after returning from  the PreferencesActivity.
     */
    private static final int SETTINGS_REQUEST = 200;

    private static int[] blogIDs;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null)
                return;
            if (intent.getAction().equals(BioWiki.BROADCAST_ACTION_SIGNOUT)) {
                onSignout();
            }
            if (intent.getAction().equals(BioWiki.BROADCAST_ACTION_XMLRPC_INVALID_CREDENTIALS)) {
                ToastUtils.showAuthErrorDialog(BIActionBarActivity.this);
            }
            if (intent.getAction().equals(BioWiki.BROADCAST_ACTION_XMLRPC_TWO_FA_AUTH)) {
                // TODO: add a specific message like "you must use a specific app password"
                ToastUtils.showAuthErrorDialog(BIActionBarActivity.this);
            }
            if (intent.getAction().equals(BioWiki.BROADCAST_ACTION_XMLRPC_INVALID_SSL_CERTIFICATE)) {
                // SelfSignedSSLCertsManager.askForSslTrust(BWActionBarActivity.this);
            }
            if (intent.getAction().equals(BioWiki.BROADCAST_ACTION_XMLRPC_LOGIN_LIMIT)) {
                ToastUtils.showToast(context, R.string.limit_reached, ToastUtils.Duration.LONG);
            }
        }
    };
    protected MenuDrawer mMenuDrawer;
    protected boolean isAnimatingRefreshButton;
    protected boolean mShouldFinish;
    protected List<MenuDrawerItem> mMenuItems = new ArrayList<MenuDrawerItem>();
    protected boolean mFirstLaunch = false;
    private boolean mBlogSpinnerInitialized;
    private IcsAdapterView.OnItemSelectedListener mItemSelectedListener = new IcsAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(IcsAdapterView<?> arg0, View arg1, int position, long arg3) {
            // http://stackoverflow.com/questions/5624825/spinner-onitemselected-executes-when-it-is-not-suppose-to/5918177#5918177
            if (!mBlogSpinnerInitialized) {
                mBlogSpinnerInitialized = true;
            } else {
                BioWiki.setCurrentBlog(blogIDs[position]);
                updateMenuDrawer();
                onBlogChanged();
            }
        }

        @Override
        public void onNothingSelected(IcsAdapterView<?> arg0) {
        }
    };

    private MenuAdapter mAdapter;
    private ListView mListView;
    private IcsSpinner mBlogSpinner;

    /**
     * Get the names of all the blogs configured within the application. If a
     * blog does not have a specific name, the blog URL is returned.
     *
     * @return array of blog names
     */
    private static String[] getBlogNames() {
        List<Map<String, Object>> accounts = BioWiki.wpDB.getVisibleAccounts();

        int blogCount = accounts.size();
        blogIDs = new int[blogCount];
        String[] blogNames = new String[blogCount];

        for (int i = 0; i < blogCount; i++) {
            Map<String, Object> account = accounts.get(i);
            String name;
            if (account.get("blogName") != null) {
                name = StringUtils.unescapeHTML(account.get("blogName").toString());
                if (name.trim().length() == 0) {
                    name = StringUtils.getHost(account.get("url").toString());
                }
            } else {
                name = StringUtils.getHost(account.get("url").toString());
            }
            blogNames[i] = name;
            blogIDs[i] = Integer.valueOf(account.get("id").toString());
        }

        return blogNames;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configure all the available menu items

        mMenuItems.add(new CategoryItem());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();

        if (isAnimatingRefreshButton) {
            isAnimatingRefreshButton = false;
        }
        if (mShouldFinish) {
            overridePendingTransition(0, 0);
            finish();
        } else {
            BioWiki.shouldRestoreSelectedActivity = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        refreshMenuDrawer();
    }

    protected void refreshMenuDrawer() {
        // the current blog may have changed while we were away
        setupCurrentBlog();
        if (mMenuDrawer != null) {
            updateMenuDrawer();
        }

        Blog currentBlog = BioWiki.getCurrentBlog();

        if (currentBlog != null && mListView != null && mListView.getHeaderViewsCount() > 0) {
            for (int i = 0; i < blogIDs.length; i++) {
                if (blogIDs[i] == currentBlog.getLocalTableBlogId()) {
                    if (mBlogSpinner != null) {
                        mBlogSpinner.setSelection(i);
                    }
                }
            }
        }
    }

    /**
     * Create a menu drawer and attach it to the activity.
     *
     * @param contentViewID {@link android.view.View} of the main content for the activity.
     */
    protected void createMenuDrawer(int contentViewID) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMenuDrawer = attachMenuDrawer();
        mMenuDrawer.setContentView(contentViewID);

        initMenuDrawer();
    }

    /**
     * Create a menu drawer and attach it to the activity.
     *
     * @param contentView {@link android.view.View} of the main content for the activity.
     */
    protected void createMenuDrawer(View contentView) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMenuDrawer = attachMenuDrawer();
        mMenuDrawer.setContentView(contentView);

        initMenuDrawer();
    }

    /**
     * returns true if this is an extra-large device in landscape mode
     */
    protected boolean isXLargeLandscape() {
        return isXLarge() && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    protected boolean isXLarge() {
        return ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    protected boolean isLargeOrXLarge() {
        int mask = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        return (mask == Configuration.SCREENLAYOUT_SIZE_LARGE
                || mask == Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    /**
     * Attach a menu drawer to the Activity
     * Set to be a static drawer if on a landscape x-large device
     */
    private MenuDrawer attachMenuDrawer() {
        final MenuDrawer menuDrawer;
        if (isStaticMenuDrawer()) {
            menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.STATIC, Position.LEFT);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            menuDrawer.setDrawerIndicatorEnabled(true);
        }

        int shadowSizeInPixels = getResources().getDimensionPixelSize(R.dimen.menu_shadow_width);
        menuDrawer.setDropShadowSize(shadowSizeInPixels);
        menuDrawer.setDropShadowColor(getResources().getColor(R.color.md__shadowColor));
        menuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        return menuDrawer;
    }

    public boolean isStaticMenuDrawer() {
        return isXLargeLandscape();
    }

    private void initMenuDrawer() {
        initMenuDrawer(-1);
    }

    /**
     * Create menu drawer ListView and listeners
     */
    private void initMenuDrawer(int blogSelection) {
        mListView = new ListView(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setCacheColorHint(android.R.color.transparent);

        // if the ActionBar overlays window content, we must insert a view which is the same
        // height as the ActionBar as the first header in the ListView - without this the
        // ActionBar will cover the first item
        if (DisplayUtils.hasActionBarOverlay(getWindow())) {
            final int actionbarHeight = DisplayUtils.getActionBarHeight(this);
            RelativeLayout header = new RelativeLayout(this);
            header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, actionbarHeight));
            mListView.addHeaderView(header, null, false);
        }

        mAdapter = new MenuAdapter(this);
        String[] blogNames = getBlogNames();
        if (blogNames.length > 1) {
            addBlogSpinner(blogNames);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // account for header views
                int menuPosition = position - mListView.getHeaderViewsCount();
                // bail if the adjusted position is out of bounds for the adapter
                if (menuPosition < 0 || menuPosition >= mAdapter.getCount())
                    return;
                MenuDrawerItem item = mAdapter.getItem(menuPosition);
                // if the item has an id, remember it for launch
                if (item.hasItemId()) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(BIActionBarActivity.this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(LAST_ACTIVITY_PREFERENCE, item.getItemId());
                    editor.commit();
                }
                // only perform selection if the item isn't already selected
                if (!item.isSelected())
                    item.selectItem();
                // save the last activity preference
                // close the menu drawer
                mMenuDrawer.closeMenu();
                // if we have an intent, start the new activity
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                mMenuDrawer.invalidate();
            }
        });

        mMenuDrawer.setMenuView(mListView);
        mListView.setAdapter(mAdapter);
        if (blogSelection != -1 && mBlogSpinner != null) {
            mBlogSpinner.setSelection(blogSelection);
        }
        updateMenuDrawer();
    }

    private void addBlogSpinner(String[] blogNames) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout spinnerWrapper = (LinearLayout) layoutInflater.inflate(R.layout.blog_spinner, null);
        if (spinnerWrapper != null) {
            spinnerWrapper.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mBlogSpinner != null) {
                        mBlogSpinner.performClick();
                    }
                }
            });
        }
        mBlogSpinner = (IcsSpinner) spinnerWrapper.findViewById(R.id.blog_spinner);
        mBlogSpinner.setOnItemSelectedListener(mItemSelectedListener);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(
                getSupportActionBar().getThemedContext(),
                R.layout.spinner_menu_dropdown_item,
                R.id.menu_text_dropdown,
                blogNames
        );

        mBlogSpinner.setAdapter(mSpinnerAdapter);
        mListView.addHeaderView(spinnerWrapper);
    }

    protected void startActivityWithDelay(final Intent i) {
        if (isXLargeLandscape()) {
            // Tablets in landscape don't need a delay because the menu drawer doesn't close
            startActivity(i);
        } else {
            // When switching to LAST_ACTIVITY_PREFERENCE onCreate we don't need to delay
            if (mFirstLaunch) {
                startActivity(i);
                return;
            }
            // Let the menu animation finish before starting a new activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                }
            }, 400);
        }
    }

    /**
     * Update all of the items in the menu drawer based on the current active
     * blog.
     */
    public void updateMenuDrawer() {
        mAdapter.clear();
        // iterate over the available menu items and only show the ones that should be visible
        Iterator<MenuDrawerItem> availableItems = mMenuItems.iterator();
        while (availableItems.hasNext()) {
            MenuDrawerItem item = availableItems.next();
            if (item.isVisible()) {
                mAdapter.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * If the activity has a menu drawer attached that is opened or in the
     * process of opening, the back button press closes it. Otherwise, the
     * normal back action is taken.
     */
    @Override
    public void onBackPressed() {
        if (mMenuDrawer != null) {
            final int drawerState = mMenuDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                mMenuDrawer.closeMenu();
                return;
            }
        }

        super.onBackPressed();
    }

    private boolean askToSignInIfNot() {

        if (!BioWiki.isSignedIn(BIActionBarActivity.this)) {
            AppLog.d(AppLog.T.NUX, "No accounts configured.  Sending user to set up an account");
            mShouldFinish = false;
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("request", WelcomeActivity.SIGN_IN_REQUEST);
            startActivityForResult(intent, ADD_ACCOUNT_REQUEST);
            return false;
        }
        return true;
    }

    /**
     * Setup the global state tracking which blog is currently active if the user is signed in.
     */
    public void setupCurrentBlog() {
        if (askToSignInIfNot()) {
            BioWiki.getCurrentBlog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SETTINGS_REQUEST:
                // user returned from settings - skip if user signed out
                if (mMenuDrawer != null && resultCode != PreferencesActivity.RESULT_SIGNED_OUT) {
                    updateMenuDrawer();
                    String[] blogNames = getBlogNames();
                    // If we need to add or remove the blog spinner, init the drawer again
                    if ((blogNames.length > 1 && mListView.getHeaderViewsCount() == 0)
                            || (blogNames.length == 1 && mListView.getHeaderViewsCount() > 0)
                            || blogNames.length == 0) {
                        initMenuDrawer();
                    } else if (blogNames.length > 1 && mBlogSpinner != null) {
                        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(
                                getSupportActionBar().getThemedContext(),
                                R.layout.spinner_menu_dropdown_item,
                                R.id.menu_text_dropdown,
                                blogNames
                        );
                        mBlogSpinner.setAdapter(mSpinnerAdapter);
                    }

                    if (blogNames.length >= 1) {
                        setupCurrentBlog();
                        onBlogChanged();
                    } else {
                        // user has hidden all blogs
                        onBlogChanged();
                    }
                }
                break;

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mMenuDrawer != null) {
                mMenuDrawer.toggleMenu();
                return true;
            }
        } else if (item.getItemId() == R.id.menu_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivityForResult(i, SETTINGS_REQUEST);
        } else if (item.getItemId() == R.id.menu_signout) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getText(R.string.sign_out));
            dialogBuilder.setMessage(getString(R.string.sign_out_confirm));
            dialogBuilder.setPositiveButton(R.string.sign_out,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            BioWiki.signOut(BIActionBarActivity.this);
                            refreshMenuDrawer();
                        }
                    }
            );
            dialogBuilder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // Just close the window.
                        }
                    }
            );
            dialogBuilder.setCancelable(true);
            if (!isFinishing())
                dialogBuilder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the user changes the active blog or hides all blogs
     */
    public void onBlogChanged() {
        BioWiki.wpDB.updateLastBlogId(BioWiki.getCurrentLocalTableBlogId());
        // the menu may have changed, we need to change the selection if the selected item
        // is not available in the menu anymore
        Iterator<MenuDrawerItem> itemIterator = mMenuItems.iterator();
        while (itemIterator.hasNext()) {
            MenuDrawerItem item = itemIterator.next();
            // if the item is selected, but it's no longer visible we need to
            // select the first available item from the adapter
            if (item.isSelected() && !item.isVisible()) {
                // then select the first item and activate it
                if (mAdapter.getCount() > 0) {
                    mAdapter.getItem(0).selectItem();
                }
                // if it has an item id save it to the preferences
                if (item.hasItemId()) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                            BIActionBarActivity.this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(LAST_ACTIVITY_PREFERENCE, item.getItemId());
                    editor.commit();
                }
                break;
            }
        }
    }

    /**
     * this method is called when the user signs out of the app - descendants should override
     * this to perform activity-specific cleanup upon signout
     */
    public void onSignout() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (isXLarge()) {
            if (mMenuDrawer != null) {
                // Re-attach the drawer if an XLarge device is rotated, so it can be static if in landscape
                View content = mMenuDrawer.getContentContainer().getChildAt(0);
                if (content != null) {
                    mMenuDrawer.getContentContainer().removeView(content);
                    mMenuDrawer = attachMenuDrawer();
                    mMenuDrawer.setContentView(content);
                    if (mBlogSpinner != null) {
                        initMenuDrawer(mBlogSpinner.getSelectedItemPosition());
                    } else {
                        initMenuDrawer();
                    }
                }
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * broadcast receiver which detects when user signs out of the app and calls onSignout()
     * so descendants of this activity can do cleanup upon signout
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BioWiki.BROADCAST_ACTION_SIGNOUT);
        filter.addAction(BioWiki.BROADCAST_ACTION_XMLRPC_TWO_FA_AUTH);
        filter.addAction(BioWiki.BROADCAST_ACTION_XMLRPC_INVALID_CREDENTIALS);
        filter.addAction(BioWiki.BROADCAST_ACTION_XMLRPC_INVALID_SSL_CERTIFICATE);
        filter.addAction(BioWiki.BROADCAST_ACTION_XMLRPC_LOGIN_LIMIT);
        registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            // exception occurs if receiver already unregistered (safe to ignore)
        }
    }

    public static class MenuAdapter extends ArrayAdapter<MenuDrawerItem> {

        MenuAdapter(Context context) {
            super(context, R.layout.menu_drawer_row, R.id.menu_row_title, new ArrayList<MenuDrawerItem>());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            MenuDrawerItem item = getItem(position);

            TextView titleTextView = (TextView) view.findViewById(R.id.menu_row_title);
            titleTextView.setText(item.getTitleRes());

            ImageView iconImageView = (ImageView) view.findViewById(R.id.menu_row_icon);
            iconImageView.setImageResource(item.getIconRes());
            // Hide the badge always
            view.findViewById(R.id.menu_row_badge).setVisibility(View.GONE);

            if (item.isSelected()) {
                // http://stackoverflow.com/questions/5890379/setbackgroundresource-discards-my-xml-layout-attributes
                int bottom = view.getPaddingBottom();
                int top = view.getPaddingTop();
                int right = view.getPaddingRight();
                int left = view.getPaddingLeft();
                view.setBackgroundResource(R.color.blue_dark);
                view.setPadding(left, top, right, bottom);
            } else {
                view.setBackgroundResource(R.drawable.md_list_selector);
            }
            // allow the menudrawer item to configure the view
            item.configureView(view);

            return view;
        }
    }

    private class CategoryItem extends MenuDrawerItem {
        CategoryItem() {
            super(CATEGORIZATION_ACTIVITY, R.string.dictionary_menu, R.drawable.dashicon_edit);
        }

        @Override
        public Boolean isSelected() {
            return BIActionBarActivity.this instanceof CategoryViewerActivity;
        }

        @Override
        public void onSelectItem() {
            if (!(BIActionBarActivity.this instanceof CategoryViewerActivity))
                mShouldFinish = true;
            Intent intent = new Intent(BIActionBarActivity.this, CategoryViewerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityWithDelay(intent);
        }

        @Override
        public Boolean isVisible() {
            return BioWiki.wpDB.getNumVisibleAccounts() != 0;
        }
    }

}
