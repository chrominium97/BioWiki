package kr.kdev.dg1s.biowiki.ui.themes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.wordpress.rest.RestRequest.ErrorListener;
import com.wordpress.rest.RestRequest.Listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.HorizontalTabView;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.BWAlertDialogFragment;
import kr.kdev.dg1s.biowiki.util.Utils;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Theme;
import kr.kdev.dg1s.biowiki.ui.posts.PostsActivity;
import kr.kdev.dg1s.biowiki.ui.themes.ThemeDetailsFragment.ThemeDetailsFragmentCallback;
import kr.kdev.dg1s.biowiki.ui.themes.ThemePreviewFragment.ThemePreviewFragmentCallback;
import kr.kdev.dg1s.biowiki.ui.themes.ThemeTabFragment.ThemeSortType;
import kr.kdev.dg1s.biowiki.ui.themes.ThemeTabFragment.ThemeTabFragmentCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * The theme browser. Accessible via side menu drawer.
 */
public class ThemeBrowserActivity extends BWActionBarActivity implements
        ThemeTabFragmentCallback, ThemeDetailsFragmentCallback, ThemePreviewFragmentCallback,
        HorizontalTabView.TabListener {

    public static final String THEME_REFRESH_INTENT_NOTIFICATION = "THEME_REFRESH_INTENT_NOTIFICATION";
    public static final String THEME_REFRESH_PARAM_REFRESHING = "THEME_REFRESH_PARAM_REFRESHING";

    private HorizontalTabView mTabView;
    private ThemePagerAdapter mThemePagerAdapter;
    private ViewPager mViewPager;
    private ThemeSearchFragment mSearchFragment;
    private ThemePreviewFragment mPreviewFragment;
    private ThemeDetailsFragment mDetailsFragment;
    private boolean mFetchingThemes = false;
    private boolean mIsRunning;

    private boolean mIsActivatingTheme = false;
    private static final String KEY_IS_ACTIVATING_THEME = "is_activating_theme";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BioWiki.wpDB == null) {
            Toast.makeText(this, R.string.fatal_db_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setTitle(R.string.themes);

        createMenuDrawer(R.layout.theme_browser_activity);

        mThemePagerAdapter = new ThemePagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.theme_browser_pager);
        mViewPager.setAdapter(mThemePagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabView.setSelectedTab(position);
            }
        });

        mTabView = (HorizontalTabView) findViewById(R.id.horizontalTabView1);
        mTabView.setTabListener(this);

        int count = ThemeSortType.values().length;
        for (int i = 0; i < count; i++) {
            String title = ThemeSortType.values()[i].getTitle();

            mTabView.addTab(mTabView.newTab().setText(title));
        }
        mTabView.setSelectedTab(0);

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(mOnBackStackChangedListener);
        setupBaseLayout();
        mPreviewFragment = (ThemePreviewFragment) fm.findFragmentByTag(ThemePreviewFragment.TAG);
        mDetailsFragment = (ThemeDetailsFragment) fm.findFragmentByTag(ThemeDetailsFragment.TAG);
        mSearchFragment = (ThemeSearchFragment) fm.findFragmentByTag(ThemeSearchFragment.TAG);
    }

    private boolean areThemesAccessible() {
        // themes are only accessible to admin wordpress.com users
        if (BioWiki.getCurrentBlog() != null && !BioWiki.getCurrentBlog().isDotcomFlag()) {
            Intent intent = new Intent(ThemeBrowserActivity.this, PostsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityWithDelay(intent);
            return false;
        }
        return true;
    }

    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        public void onBackStackChanged() {
            setupBaseLayout();
        }
    };

    private void setupBaseLayout() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mMenuDrawer.setDrawerIndicatorEnabled(true);
            mViewPager.setVisibility(View.VISIBLE);
            mTabView.setVisibility(View.VISIBLE);
        } else {
            mMenuDrawer.setDrawerIndicatorEnabled(false);
            mViewPager.setVisibility(View.GONE);
            mTabView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (areThemesAccessible()) {
            mIsRunning = true;

            // fetch themes if we don't have any
            if (BioWiki.getCurrentBlog() != null && BioWiki.wpDB.getThemeCount(getBlogId()) == 0) {
                fetchThemes();
                setRefreshing(true);
            }
        }
    }

    @Override
    public void onTabSelected(HorizontalTabView.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    public class ThemePagerAdapter extends FragmentStatePagerAdapter {
        public ThemePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ThemeTabFragment.newInstance(ThemeSortType.getTheme(i));
        }

        @Override
        public int getCount() {
            return ThemeSortType.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ThemeSortType.getTheme(position).getTitle();
        }
    }

    public void fetchThemes() {
        if (mFetchingThemes) {
            return;
        }
        String siteId = getBlogId();
        mFetchingThemes = true;
        BioWiki.getRestClientUtils().getThemes(siteId, 0, 0, new Listener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new FetchThemesTask().execute(response);
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError response) {
                        if (response.toString().equals(AuthFailureError.class.getName())) {
                            String errorTitle = getString(R.string.theme_auth_error_title);
                            String errorMsg = getString(R.string.theme_auth_error_message);

                            if (mIsRunning) {
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                BWAlertDialogFragment fragment = BWAlertDialogFragment.newAlertDialog(errorMsg,
                                        errorTitle);
                                ft.add(fragment, "alert");
                                ft.commitAllowingStateLoss();
                            }
                            AppLog.d(AppLog.T.THEMES, "Failed to fetch themes: failed authenticate user");
                        } else {
                            Toast.makeText(ThemeBrowserActivity.this, R.string.theme_fetch_failed, Toast.LENGTH_LONG)
                                 .show();
                            AppLog.d(AppLog.T.THEMES, "Failed to fetch themes: " + response.toString());
                        }

                        mFetchingThemes = false;
                        setRefreshing(false);
                    }
                }
        );
    }

    private void fetchCurrentTheme() {
        final String siteId = getBlogId();

        BioWiki.getRestClientUtils().getCurrentTheme(siteId, new Listener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Theme theme = Theme.fromJSON(response);
                            if (theme != null) {
                                BioWiki.wpDB.setCurrentTheme(siteId, theme.getThemeId());
                                setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            AppLog.e(AppLog.T.THEMES, e);
                        }
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError response) {
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.theme, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                setupBaseLayout();
                return true;
            }
        } else if (itemId == R.id.menu_search) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (mSearchFragment == null) {
                mSearchFragment = ThemeSearchFragment.newInstance();
            }
            ft.add(R.id.theme_browser_container, mSearchFragment, ThemeSearchFragment.TAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (mMenuDrawer.isMenuVisible()) {
            super.onBackPressed();
        } else if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            setupBaseLayout();
        } else {
            super.onBackPressed();
        }
    }

    private String getBlogId() {
        if (BioWiki.getCurrentBlog() == null)
            return "0";
        return String.valueOf(BioWiki.getCurrentBlog().getRemoteBlogId());
    }

    public class FetchThemesTask extends AsyncTask<JSONObject, Void, ArrayList<Theme>> {

        @Override
        protected ArrayList<Theme> doInBackground(JSONObject... args) {
            JSONObject response = args[0];

            final ArrayList<Theme> themes = new ArrayList<Theme>();

            if (response != null) {
                JSONArray array = null;
                try {
                    array = response.getJSONArray("themes");

                    if (array != null) {

                        int count = array.length();
                        for (int i = 0; i < count; i++) {
                            JSONObject object = array.getJSONObject(i);
                            Theme theme = Theme.fromJSON(object);
                            if (theme != null) {
                                theme.save();
                                themes.add(theme);
                            }
                        }
                    }
                } catch (JSONException e) {
                    AppLog.e(AppLog.T.THEMES, e);
                }
            }

            fetchCurrentTheme();

            if (themes != null && themes.size() > 0) {
                return themes;
            }

            return null;

        }

        @Override
        protected void onPostExecute(final ArrayList<Theme> result) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mFetchingThemes = false;
                    if (result == null) {
                        Toast.makeText(ThemeBrowserActivity.this, R.string.theme_fetch_failed, Toast.LENGTH_SHORT).show();
                    }
                    setRefreshing(false);
                }
            });

        }

    }

    @Override
    public void onThemeSelected(String themeId) {
        FragmentManager fm = getSupportFragmentManager();

        if (!Utils.isXLarge(ThemeBrowserActivity.this)) {

            // show details as a fragment on top
            FragmentTransaction ft = fm.beginTransaction();

            if (mSearchFragment != null && mSearchFragment.isVisible())
                fm.popBackStack();

            setupBaseLayout();
            mDetailsFragment = ThemeDetailsFragment.newInstance(themeId);
            ft.add(R.id.theme_browser_container, mDetailsFragment, ThemeDetailsFragment.TAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } else {

            // show details as a dialog
            mDetailsFragment = ThemeDetailsFragment.newInstance(themeId);
            mDetailsFragment.show(getSupportFragmentManager(), ThemeDetailsFragment.TAG);
            getSupportFragmentManager().executePendingTransactions();
            Display display = getWindowManager().getDefaultDisplay();
            int minWidth = getResources().getDimensionPixelSize(R.dimen.theme_details_dialog_min_width);
            int height = getResources().getDimensionPixelSize(R.dimen.theme_details_dialog_height);
            int width = Math.max((int) (display.getWidth() * 0.6), minWidth);
            mDetailsFragment.getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume(Fragment fragment) {
        invalidateOptionsMenu();
    }

    @Override
    public void onPause(Fragment fragment) {
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsRunning = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (Utils.isXLarge(ThemeBrowserActivity.this) && mDetailsFragment != null) {
            mDetailsFragment.dismiss();
        }
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_ACTIVATING_THEME, mIsActivatingTheme);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(KEY_IS_ACTIVATING_THEME) && mDetailsFragment!=null)
            mDetailsFragment.setIsActivatingTheme(true);
    }

    @Override
    public void onActivateThemeClicked(String themeId, final Fragment fragment) {
        final String siteId = getBlogId();
        if (themeId == null) {
            themeId = mPreviewFragment.getThemeId();
        }

        final String newThemeId = themeId;
        final WeakReference<ThemeBrowserActivity> ref = new WeakReference<ThemeBrowserActivity>(this);
        mIsActivatingTheme = true;

        BioWiki.getRestClientUtils().setTheme(siteId, themeId, new Listener() {

                    @Override
                    public void onResponse(JSONObject arg0) {
                        mIsActivatingTheme = false;
                        Toast.makeText(ThemeBrowserActivity.this, R.string.theme_set_success, Toast.LENGTH_LONG).show();

                        BioWiki.wpDB.setCurrentTheme(siteId, newThemeId);
                        if (mDetailsFragment != null) {
                            mDetailsFragment.onThemeActivated(true);
                        }
                        setRefreshing(false);

                        if (ref.get() != null && mIsRunning && fragment instanceof ThemePreviewFragment) {
                            FragmentManager fm = ref.get().getSupportFragmentManager();

                            if (fm.getBackStackEntryCount() > 0) {
                                fm.popBackStack();
                                setupBaseLayout();
                                invalidateOptionsMenu();
                            }
                        }
                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        mIsActivatingTheme = false;
                        if (mDetailsFragment != null && mDetailsFragment.isVisible()) mDetailsFragment.onThemeActivated(
                                false);
                        if (ref.get() != null) Toast.makeText(ref.get(), R.string.theme_set_failed, Toast.LENGTH_LONG)
                                                    .show();
                    }
                }
        );

    }

    @Override
    public void onBlogChanged() {
        super.onBlogChanged();
        if (areThemesAccessible()) {
            fetchThemes();
            setRefreshing(true);
        }
    };

    @Override
    public void onLivePreviewClicked(String themeId, String previewURL) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (mPreviewFragment == null) {
            mPreviewFragment = ThemePreviewFragment.newInstance(themeId, previewURL);
        } else {
            mPreviewFragment.load(themeId, previewURL);
        }

        if (mDetailsFragment != null) {
            if (Utils.isXLarge(ThemeBrowserActivity.this)) {
                mDetailsFragment.dismiss();
            } else {
                ft.hide(mDetailsFragment);
            }
        }
        ft.add(R.id.theme_browser_container, mPreviewFragment, ThemePreviewFragment.TAG);
        ft.addToBackStack(null);
        ft.commit();
        setupBaseLayout();
    }

    private void setRefreshing(boolean refreshing) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(THEME_REFRESH_INTENT_NOTIFICATION);
        intent.putExtra(THEME_REFRESH_PARAM_REFRESHING, refreshing);
        lbm.sendBroadcast(intent);
    }
}
