package kr.kdev.dg1s.biowiki.ui.posts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlrpc.android.XMLRPCClientInterface;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Blog;
import kr.kdev.dg1s.biowiki.models.CategoryNode;
import kr.kdev.dg1s.biowiki.ui.PullToRefreshHelper;
import kr.kdev.dg1s.biowiki.ui.PullToRefreshHelper.RefreshListener;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.ListScrollPositionManager;
import kr.kdev.dg1s.biowiki.util.NetworkUtils;
import kr.kdev.dg1s.biowiki.util.StringUtils;
import kr.kdev.dg1s.biowiki.util.ToastUtils;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;

public class SelectCategoriesActivity extends SherlockListActivity {
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            mPullToRefreshHelper.setRefreshing(false);
            if (finalResult.equals("addCategory_success")) {
                populateOrFetchCategories();
                if (!isFinishing()) {
                    ToastUtils.showToast(SelectCategoriesActivity.this, R.string.adding_cat_success, ToastUtils.Duration.SHORT);
                }
            } else if (finalResult.equals("addCategory_failed")) {
                if (!isFinishing()) {
                    ToastUtils.showToast(SelectCategoriesActivity.this, R.string.adding_cat_failed, ToastUtils.Duration.LONG);
                }
            } else if (finalResult.equals("gotCategories")) {
                populateOrFetchCategories();
            } else if (finalResult.equals("FAIL")) {
                if (!isFinishing()) {
                    ToastUtils.showToast(SelectCategoriesActivity.this, R.string.category_refresh_error, ToastUtils.Duration.LONG);
                }
            }
        }
    };
    private final Handler mHandler = new Handler();
    public String categoryErrorMsg = "";
    String finalResult = "";
    XMLRPCClientInterface mClient;
    private Blog blog;
    private ListView mListView;
    private ListScrollPositionManager mListScrollPositionManager;
    private PullToRefreshHelper mPullToRefreshHelper;
    private HashSet<String> mSelectedCategories;
    private CategoryNode mCategories;
    private ArrayList<CategoryNode> mCategoryLevels;
    private Map<String, Integer> mCategoryNames = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.select_categories);
        setTitle(getResources().getString(R.string.select_categories));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mListView = getListView();
        mListScrollPositionManager = new ListScrollPositionManager(mListView, false);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setItemsCanFocus(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (getCheckedItemCount(mListView) > 1) {
                    boolean uncategorizedNeedToBeSelected = false;
                    for (int i = 0; i < mCategoryLevels.size(); i++) {
                        if (mCategoryLevels.get(i).getName().equalsIgnoreCase("uncategorized")) {
                            mListView.setItemChecked(i, uncategorizedNeedToBeSelected);
                        }
                    }
                }
            }
        });

        mSelectedCategories = new HashSet<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int blogId = extras.getInt("id");
            try {
                blog = BioWiki.wpDB.instantiateBlogByLocalId(blogId);
            } catch (Exception e) {
                Toast.makeText(this, getResources().getText(R.string.blog_not_found), Toast.LENGTH_SHORT).show();
                finish();
            }
            if (extras.containsKey("categories"))
                mSelectedCategories = (HashSet<String>) extras.getSerializable("categories");
        }
        if (mSelectedCategories == null) {
            mSelectedCategories = new HashSet<String>();
        }

        // pull to refresh setup
        mPullToRefreshHelper = new PullToRefreshHelper(this, (PullToRefreshLayout) findViewById(R.id.ptr_layout),
                new RefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        if (!NetworkUtils.checkConnection(getBaseContext())) {
                            mPullToRefreshHelper.setRefreshing(false);
                            return;
                        }
                        refreshCategories();
                    }
                }
        );

        populateOrFetchCategories();
    }

    private void populateCategoryList() {
        mCategoryLevels = CategoryNode.getSortedListOfCategoriesFromRoot(mCategories);
        for (int i = 0; i < mCategoryLevels.size(); i++) {
            mCategoryNames.put(StringUtils.unescapeHTML(mCategoryLevels.get(i).getName()), i);
        }

        CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(this, R.layout.categories_row, mCategoryLevels);
        this.setListAdapter(categoryAdapter);
        if (mSelectedCategories != null) {
            ListView lv = getListView();
            for (String selectedCategory : mSelectedCategories) {
                if (mCategoryNames.keySet().contains(selectedCategory)) {
                    lv.setItemChecked(mCategoryNames.get(selectedCategory), true);
                }
            }
        }
        mListScrollPositionManager.restoreScrollOffset();
    }

    private void populateOrFetchCategories() {
        mCategories = CategoryNode.createCategoryTreeFromDB(blog.getLocalTableBlogId());
        if (mCategories.getChildren().size() > 0) {
            populateCategoryList();
        } else {
            mPullToRefreshHelper.setRefreshing(true);
            refreshCategories();
        }
    }

    /**
     * Gets the categories via a xmlrpc call
     *
     * @return result message
     */
    public String fetchCategories() {
        String returnMessage;
        Object result[] = null;
        Object[] params = {blog.getRemoteBlogId(), blog.getUsername(), blog.getPassword(),};
        mClient = XMLRPCFactory.instantiate(blog.getUri(), blog.getHttpuser(), blog.getHttppassword());
        boolean success = false;
        try {
            result = (Object[]) mClient.call("wp.getCategories", params);
            success = true;
        } catch (XMLRPCException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (IOException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (XmlPullParserException e) {
            AppLog.e(AppLog.T.POSTS, e);
        }

        if (success) {
            // wipe out the categories table
            BioWiki.wpDB.clearCategories(blog.getLocalTableBlogId());

            for (Object aResult : result) {
                Map<?, ?> curHash = (Map<?, ?>) aResult;
                String categoryName = curHash.get("categoryName").toString();
                String categoryID = curHash.get("categoryId").toString();
                String categoryParentID = curHash.get("parentId").toString();
                int convertedCategoryID = Integer.parseInt(categoryID);
                int convertedCategoryParentID = Integer.parseInt(categoryParentID);
                BioWiki.wpDB.insertCategory(blog.getLocalTableBlogId(), convertedCategoryID, convertedCategoryParentID, categoryName);
            }
            returnMessage = "gotCategories";
        } else {
            returnMessage = "FAIL";
        }
        return returnMessage;
    }

    /**
     * function addCategory
     *
     * @param String category_name
     * @return
     * @description Adds a new category
     */
    public String addCategory(final String category_name, String category_slug, String category_desc, int parent_id) {
        // Return string
        String returnString = "addCategory_failed";

        // Save selected categories
        updateSelectedCategoryList();
        mListScrollPositionManager.saveScrollOffset();

        // Store the parameters for wp.addCategory
        Map<String, Object> struct = new HashMap<String, Object>();
        struct.put("name", category_name);
        struct.put("slug", category_slug);
        struct.put("description", category_desc);
        struct.put("parent_id", parent_id);
        mClient = XMLRPCFactory.instantiate(blog.getUri(), blog.getHttpuser(), blog.getHttppassword());
        Object[] params = {blog.getRemoteBlogId(), blog.getUsername(), blog.getPassword(), struct};

        Object result = null;
        try {
            result = mClient.call("wp.newCategory", params);
        } catch (XMLRPCException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (IOException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (XmlPullParserException e) {
            AppLog.e(AppLog.T.POSTS, e);
        }

        if (result != null) {
            // Category successfully created. "result" is the ID of the new category
            // Initialize the category database
            // Convert "result" (= category_id) from type Object to int
            int category_id = Integer.parseInt(result.toString());

            // Fetch canonical name, can't to do this asynchronously because the new category_name is needed for
            // insertCategory
            final String new_category_name = getCanonicalCategoryName(category_id);
            if (new_category_name == null) {
                return returnString;
            }
            final Activity that = this;
            if (!new_category_name.equals(category_name)) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(that, String.format(String.valueOf(getText(R.string.category_automatically_renamed)),
                                category_name, new_category_name), Toast.LENGTH_LONG).show();
                    }
                });
            }

            // Insert the new category into database
            BioWiki.wpDB.insertCategory(blog.getLocalTableBlogId(), category_id, parent_id, new_category_name);
            returnString = "addCategory_success";
            // auto select new category
            mSelectedCategories.add(new_category_name);
        }

        return returnString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            final Bundle extras = data.getExtras();

            switch (requestCode) {
                case 0: // Add category
                    // Does the user want to continue, or did he press "dismiss"?
                    if (extras.getString("continue").equals("TRUE")) {
                        // Get name, slug and desc from Intent
                        final String category_name = extras.getString("category_name");
                        final String category_slug = extras.getString("category_slug");
                        final String category_desc = extras.getString("category_desc");
                        final int parent_id = extras.getInt("parent_id");

                        // Check if the category name already exists
                        if (!mCategoryNames.keySet().contains(category_name)) {
                            Thread th = new Thread() {
                                public void run() {
                                    finalResult = addCategory(category_name, category_slug, category_desc, parent_id);
                                    mHandler.post(mUpdateResults);
                                }
                            };
                            th.start();
                        }
                        break;
                    }
            }// end null check
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_new_category) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", blog.getLocalTableBlogId());
            Intent i = new Intent(SelectCategoriesActivity.this, AddCategoryActivity.class);
            i.putExtras(bundle);
            startActivityForResult(i, 0);
            return true;
        } else if (itemId == android.R.id.home) {
            saveAndFinish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getCanonicalCategoryName(int category_id) {
        String new_category_name = null;
        Map<?, ?> result = null;
        Object[] params = {blog.getRemoteBlogId(), blog.getUsername(), blog.getPassword(), "category", category_id};
        mClient = XMLRPCFactory.instantiate(blog.getUri(), blog.getHttpuser(), blog.getHttppassword());
        try {
            result = (Map<?, ?>) mClient.call("wp.getTerm", params);
        } catch (XMLRPCException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (IOException e) {
            AppLog.e(AppLog.T.POSTS, e);
        } catch (XmlPullParserException e) {
            AppLog.e(AppLog.T.POSTS, e);
        }

        if (result != null) {
            if (result.containsKey("name")) {
                new_category_name = result.get("name").toString();
            }
        }
        return new_category_name;
    }

    private void refreshCategories() {
        mListScrollPositionManager.saveScrollOffset();
        updateSelectedCategoryList();
        Thread th = new Thread() {
            public void run() {
                finalResult = fetchCategories();
                mHandler.post(mUpdateResults);
            }
        };
        th.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation change
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
        super.onBackPressed();
    }

    private void updateSelectedCategoryList() {
        SparseBooleanArray selectedItems = mListView.getCheckedItemPositions();
        for (int i = 0; i < selectedItems.size(); i++) {
            String currentName = StringUtils.unescapeHTML(mCategoryLevels.get(selectedItems.keyAt(i)).getName());
            if (selectedItems.get(selectedItems.keyAt(i))) {
                mSelectedCategories.add(currentName);
            } else {
                mSelectedCategories.remove(currentName);
            }
        }
    }

    private void saveAndFinish() {
        Bundle bundle = new Bundle();
        updateSelectedCategoryList();
        bundle.putSerializable("selectedCategories", new ArrayList<String>(mSelectedCategories));
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private int getCheckedItemCount(ListView listView) {
        if (Build.VERSION.SDK_INT >= 11) {
            return listView.getCheckedItemCount();
        } else {
            int count = 0;
            for (int i = listView.getCount() - 1; i >= 0; i--)
                if (listView.isItemChecked(i)) count++;
            return count;
        }
    }
}
