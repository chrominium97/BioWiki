package kr.kdev.dg1s.biowiki.ui.posts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import org.wordpress.passcodelock.AppLockManager;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlrpc.android.ApiHelper;
import org.xmlrpc.android.XMLRPCClientInterface;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFactory;

import java.io.IOException;
import java.util.Iterator;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Blog;
import kr.kdev.dg1s.biowiki.models.Post;
import kr.kdev.dg1s.biowiki.models.PostStatus;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.MenuDrawerItem;
import kr.kdev.dg1s.biowiki.ui.notifications.NotificationsActivity;
import kr.kdev.dg1s.biowiki.util.AlertUtil;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.BWAlertDialogFragment;
import kr.kdev.dg1s.biowiki.util.BWMeShortlinks;
import kr.kdev.dg1s.biowiki.util.BWMobileStatsUtil;

public class PostsActivity extends BWActionBarActivity
        implements PostsListFragment.OnPostSelectedListener, PostsListFragment.OnSinglePostLoadedListener, PostsListFragment.OnPostActionListener,
        ViewPostFragment.OnDetailPostActionListener, BWAlertDialogFragment.OnDialogConfirmListener {

    public static final String EXTRA_VIEW_PAGES = "viewPages";
    public static final String EXTRA_ERROR_MSG = "errorMessage";
    public static final String EXTRA_ERROR_INFO_TITLE = "errorInfoTitle";
    public static final String EXTRA_ERROR_INFO_LINK = "errorInfoLink";

    public static final int POST_DELETE = 0, POST_SHARE = 1, POST_EDIT = 2, POST_CLEAR = 3, POST_VIEW = 5;
    public static final int ACTIVITY_EDIT_POST = 0;
    private static final int ID_DIALOG_DELETING = 1, ID_DIALOG_SHARE = 2;
    public ProgressDialog mLoadingDialog;
    public boolean mIsPage = false;
    public String mErrorMsg = "";
    private PostsListFragment mPostList;
    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                mMenuDrawer.setDrawerIndicatorEnabled(true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Special check for a null database (see #507)
        if (BioWiki.wpDB == null) {
            Toast.makeText(this, R.string.fatal_db_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Check if we came from a notification, if so let's launch NotificationsActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean(NotificationsActivity.FROM_NOTIFICATION_EXTRA)) {
            startNotificationsActivity(extras);
            return;
        }

        // Restore last selection on app creation
        if (BioWiki.shouldRestoreSelectedActivityBIOWIKI && BioWiki.getCurrentBlog() != null &&
                !(this instanceof PagesActivity)) {
            // Refresh blog content when returning to the app
            new ApiHelper.RefreshBlogContentTask(this, BioWiki.getCurrentBlog(), new RefreshBlogContentCallback())
                    .execute(false);

            BioWiki.shouldRestoreSelectedActivityBIOWIKI = false;
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            int lastActivitySelection = settings.getInt(LAST_ACTIVITY_PREFERENCE, -1);
            if (lastActivitySelection > MenuDrawerItem.NO_ITEM_ID &&
                    lastActivitySelection != BWActionBarActivity.DASHBOARD_ACTIVITY) {
                Iterator<MenuDrawerItem> itemIterator = mMenuItems.iterator();
                while (itemIterator.hasNext()) {
                    MenuDrawerItem item = itemIterator.next();
                    // if we have a matching item id, and it's not selected and it's visible, call it
                    if (item.hasItemId() && item.getItemId() == lastActivitySelection && !item.isSelected() &&
                            item.isVisible()) {
                        mFirstLaunch = true;
                        item.selectItem();
                        finish();
                        return;
                    }
                }
            }
        }

        createMenuDrawer(R.layout.posts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(mOnBackStackChangedListener);
        mPostList = (PostsListFragment) fm.findFragmentById(R.id.postList);

        if (extras != null) {
            mIsPage = extras.getBoolean(EXTRA_VIEW_PAGES);
            showErrorDialogIfNeeded(extras);
        }

        if (mIsPage)
            setTitle(getString(R.string.pages));
        else
            setTitle(getString(R.string.posts));

        BioWiki.currentPost = null;

        if (savedInstanceState != null)
            popPostDetail();

        attemptToSelectPost();

        BWMobileStatsUtil.trackEventForWPCom(statEventForViewOpening());
    }

    private void showPostUploadErrorAlert(String errorMessage, String infoTitle,
                                          final String infoURL) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostsActivity.this);
        dialogBuilder.setTitle(getResources().getText(R.string.error));
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Just close the window.
                    }
                }
        );
        if (infoTitle != null && infoURL != null) {
            dialogBuilder.setNeutralButton(infoTitle,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(infoURL)));
                        }
                    }
            );
        }
        dialogBuilder.setCancelable(true);
        if (!isFinishing())
            dialogBuilder.create().show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Check if we came from a notification, if so let's launch NotificationsActivity
            if (extras.getBoolean(NotificationsActivity.FROM_NOTIFICATION_EXTRA)) {
                startNotificationsActivity(extras);
                return;
            }
        }
    }

    private void showErrorDialogIfNeeded(Bundle extras) {
        if (extras == null) {
            return;
        }
        String errorMessage = extras.getString(EXTRA_ERROR_MSG);
        if (!TextUtils.isEmpty(errorMessage)) {
            String errorInfoTitle = extras.getString(EXTRA_ERROR_INFO_TITLE);
            String errorInfoLink = extras.getString(EXTRA_ERROR_INFO_LINK);
            showPostUploadErrorAlert(errorMessage, errorInfoTitle, errorInfoLink);
        }
    }

    private void startNotificationsActivity(Bundle extras) {
        // Manually set last selection to notifications
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LAST_ACTIVITY_PREFERENCE, NOTIFICATIONS_ACTIVITY);
        editor.commit();

        Intent i = new Intent(this, NotificationsActivity.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public boolean isRefreshing() {
        return mPostList.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        mPostList.setRefreshing(refreshing);
    }

    public void checkForLocalChanges(boolean shouldPrompt) {
        if (BioWiki.getCurrentBlog() == null) {
            return;
        }
        boolean hasLocalChanges = BioWiki.wpDB.findLocalChanges(BioWiki.getCurrentBlog().getLocalTableBlogId(),
                mIsPage);
        if (hasLocalChanges) {
            if (!shouldPrompt) {
                return;
            }
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostsActivity.this);
            dialogBuilder.setTitle(getResources().getText(R.string.local_changes));
            dialogBuilder.setMessage(getResources().getText(R.string.remote_changes));
            dialogBuilder.setPositiveButton(getResources().getText(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            popPostDetail();
                            attemptToSelectPost();
                            mPostList.requestPosts(false);
                        }
                    }
            );
            dialogBuilder.setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mPostList.setRefreshing(false);
                }
            });
            dialogBuilder.setCancelable(true);
            if (!isFinishing()) {
                dialogBuilder.create().show();
            }
        } else {
            popPostDetail();
            mPostList.requestPosts(false);
            mPostList.setRefreshing(true);
        }
    }

    protected void popPostDetail() {
        FragmentManager fm = getSupportFragmentManager();
        ViewPostFragment f = (ViewPostFragment) fm.findFragmentById(R.id.postDetail);
        if (f == null) {
            try {
                fm.popBackStack();
            } catch (RuntimeException e) {
                AppLog.e(AppLog.T.POSTS, e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BioWiki.isSignedIn(PostsActivity.this)) {
            // showReaderIfNoBlog();
        }
        if (BioWiki.postsShouldRefresh) {
            checkForLocalChanges(false);
            mPostList.setRefreshing(true);
            BioWiki.postsShouldRefresh = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        BWMobileStatsUtil.trackEventForWPComWithSavedProperties(statEventForViewClosing());
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.posts, menu);
        if (mIsPage) {
            menu.findItem(R.id.menu_new_post).setTitle(R.string.new_page);
        }
        return true;
    }

    public void newPost() {
        BWMobileStatsUtil.trackEventForWPCom(statEventForNewPost());
        if (BioWiki.getCurrentBlog() == null) {
            if (!isFinishing())
                Toast.makeText(this, R.string.blog_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a new post object
        Post newPost = new Post(BioWiki.getCurrentBlog().getLocalTableBlogId(), mIsPage);
        BioWiki.wpDB.savePost(newPost);
        Intent i = new Intent(this, EditPostActivity.class);
        i.putExtra(EditPostActivity.EXTRA_POSTID, newPost.getLocalTablePostId());
        i.putExtra(EditPostActivity.EXTRA_IS_PAGE, mIsPage);
        i.putExtra(EditPostActivity.EXTRA_IS_NEW_POST, true);
        startActivityForResult(i, ACTIVITY_EDIT_POST);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_new_post) {
            newPost();
            return true;
        } else if (itemId == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                popPostDetail();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == ACTIVITY_EDIT_POST && resultCode == RESULT_OK) {
                if (data.getBooleanExtra("shouldRefresh", false)) {
                    mPostList.getPostListAdapter().loadPosts();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void attemptToSelectPost() {
        FragmentManager fm = getSupportFragmentManager();
        ViewPostFragment f = (ViewPostFragment) fm.findFragmentById(R.id.postDetail);
        if (f != null && f.isInLayout()) {
            mPostList.setShouldSelectFirstPost(true);
        }
    }

    @Override
    public void onPostSelected(Post post) {
        FragmentManager fm = getSupportFragmentManager();
        ViewPostFragment f = (ViewPostFragment) fm
                .findFragmentById(R.id.postDetail);

        if (post != null) {

            BioWiki.currentPost = post;
            if (f == null || !f.isInLayout()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(mPostList);
                f = new ViewPostFragment();
                ft.add(R.id.postDetailFragmentContainer, f);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                mMenuDrawer.setDrawerIndicatorEnabled(false);
            } else {
                f.loadPost(post);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        mLoadingDialog = new ProgressDialog(this);
        if (id == ID_DIALOG_DELETING) {
            mLoadingDialog.setMessage(getResources().getText(
                    mIsPage ? R.string.deleting_page : R.string.deleting_post));
            mLoadingDialog.setCancelable(false);
            return mLoadingDialog;
        } else if (id == ID_DIALOG_SHARE) {
            mLoadingDialog.setMessage(mIsPage ? getString(R.string.share_url_page) : getString(
                    R.string.share_url_post));
            mLoadingDialog.setCancelable(false);
            return mLoadingDialog;
        }

        return super.onCreateDialog(id);
    }

    protected String statEventForViewOpening() {
        return BWMobileStatsUtil.StatsEventPostsOpened;
    }

    protected String statEventForViewClosing() {
        return BWMobileStatsUtil.StatsEventPostsClosed;
    }

    protected String statEventForNewPost() {
        return BWMobileStatsUtil.StatsEventPostsClickedNewPost;
    }

    protected void refreshComments() {
        new refreshCommentsTask().execute();
    }

    @Override
    public void onPostAction(int action, final Post post) {
        // No post? No service.
        if (post == null) {
            Toast.makeText(PostsActivity.this, R.string.post_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        if (action == POST_DELETE) {
            BWMobileStatsUtil.flagProperty(statEventForViewClosing(), BWMobileStatsUtil.StatsPropertyPostDetailClickedDelete);
            if (post.isLocalDraft()) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        PostsActivity.this);
                dialogBuilder.setTitle(getResources().getText(
                        R.string.delete_draft));
                dialogBuilder.setMessage(getResources().getText(
                        R.string.delete_sure)
                        + " '" + post.getTitle() + "'?");
                dialogBuilder.setPositiveButton(
                        getResources().getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                BioWiki.wpDB.deletePost(post);
                                popPostDetail();
                                attemptToSelectPost();
                                mPostList.getPostListAdapter().loadPosts();
                            }
                        }
                );
                dialogBuilder.setNegativeButton(
                        getResources().getText(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Just close the window.

                            }
                        }
                );
                dialogBuilder.setCancelable(true);
                if (!isFinishing()) {
                    dialogBuilder.create().show();
                }

            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        PostsActivity.this);
                dialogBuilder.setTitle(getResources().getText(
                        (post.isPage()) ? R.string.delete_page
                                : R.string.delete_post
                ));
                dialogBuilder.setMessage(getResources().getText(
                        (post.isPage()) ? R.string.delete_sure_page
                                : R.string.delete_sure_post
                )
                        + " '" + post.getTitle() + "'?");
                dialogBuilder.setPositiveButton(
                        getResources().getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                new deletePostTask().execute(post);
                            }
                        }
                );
                dialogBuilder.setNegativeButton(
                        getResources().getText(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Just close the window.

                            }
                        }
                );
                dialogBuilder.setCancelable(true);
                if (!isFinishing()) {
                    dialogBuilder.create().show();
                }

            }
        } else if (action == POST_SHARE) {
            // Only share published posts
            if (post.getStatusEnum() != PostStatus.PUBLISHED && post.getStatusEnum() != PostStatus.SCHEDULED) {
                AlertUtil.showAlert(this, R.string.error,
                        post.isPage() ? R.string.page_not_published : R.string.post_not_published);
                return;
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
            String shortlink = BWMeShortlinks.getPostShortlink(BioWiki.getCurrentBlog(), post);
            share.putExtra(Intent.EXTRA_TEXT, shortlink != null ? shortlink : post.getPermaLink());
            startActivity(Intent.createChooser(share, getResources()
                    .getText(R.string.share_url)));
            AppLockManager.getInstance().setExtendedTimeout();

            BWMobileStatsUtil.flagProperty(statEventForViewClosing(), BWMobileStatsUtil.StatsPropertyPostDetailClickedShare);
        } else if (action == POST_CLEAR) {
            FragmentManager fm = getSupportFragmentManager();
            ViewPostFragment f = (ViewPostFragment) fm
                    .findFragmentById(R.id.postDetail);
            if (f != null) {
                f.clearContent();
            }
        } else if (action == POST_EDIT) {
            BWMobileStatsUtil.flagProperty(statEventForViewClosing(), BWMobileStatsUtil.StatsPropertyPostDetailClickedEdit);
        } else if (action == POST_VIEW) {
            BWMobileStatsUtil.flagProperty(statEventForViewClosing(), BWMobileStatsUtil.StatsPropertyPostDetailClickedPreview);
        }
    }

    @Override
    public void onDetailPostAction(int action, Post post) {
        onPostAction(action, post);
    }

    @Override
    public void onDialogConfirm() {
        mPostList.requestPosts(true);
        mPostList.setRefreshing(true);
    }

    @Override
    public void onSinglePostLoaded() {
        popPostDetail();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState.isEmpty()) {
            outState.putBoolean("bug_19917_fix", true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBlogChanged() {
        super.onBlogChanged();
        popPostDetail();
        attemptToSelectPost();
        mPostList.clear();
        mPostList.getPostListAdapter().loadPosts();
        new ApiHelper.RefreshBlogContentTask(this, BioWiki.getCurrentBlog(), null).execute(false);
        mPostList.onBlogChanged();
    }

    public class deletePostTask extends AsyncTask<Post, Void, Boolean> {

        Post post;

        @Override
        protected void onPreExecute() {
            // pop out of the detail view if on a smaller screen
            popPostDetail();
            showDialog(ID_DIALOG_DELETING);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissDialog(ID_DIALOG_DELETING);
            attemptToSelectPost();
            if (result) {
                Toast.makeText(PostsActivity.this, getResources().getText((mIsPage) ?
                                R.string.page_deleted : R.string.post_deleted),
                        Toast.LENGTH_SHORT
                ).show();
                checkForLocalChanges(false);
                BioWiki.wpDB.deletePost(post);
                mPostList.requestPosts(false);
                mPostList.setRefreshing(true);
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostsActivity.this);
                dialogBuilder.setTitle(getResources().getText(R.string.connection_error));
                dialogBuilder.setMessage(mErrorMsg);
                dialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Just close the window.
                            }
                        }
                );
                dialogBuilder.setCancelable(true);
                if (!isFinishing()) {
                    dialogBuilder.create().show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Post... params) {
            boolean result = false;
            post = params[0];
            Blog blog = BioWiki.currentBlog;
            XMLRPCClientInterface client = XMLRPCFactory.instantiate(blog.getUri(), blog.getHttpuser(),
                    blog.getHttppassword());

            Object[] postParams = {"", post.getRemotePostId(),
                    BioWiki.currentBlog.getUsername(),
                    BioWiki.currentBlog.getPassword()};
            Object[] pageParams = {BioWiki.currentBlog.getRemoteBlogId(),
                    BioWiki.currentBlog.getUsername(),
                    BioWiki.currentBlog.getPassword(), post.getRemotePostId()};

            try {
                client.call((mIsPage) ? "wp.deletePage" : "blogger.deletePost", (mIsPage) ? pageParams : postParams);
                result = true;
            } catch (final XMLRPCException e) {
                mErrorMsg = prepareErrorMessage(e);
            } catch (IOException e) {
                mErrorMsg = prepareErrorMessage(e);
            } catch (XmlPullParserException e) {
                mErrorMsg = prepareErrorMessage(e);
            }
            return result;
        }

        private String prepareErrorMessage(Exception e) {
            AppLog.e(AppLog.T.POSTS, "Error while deleting post or page", e);
            return String.format(getResources().getString(R.string.error_delete_post),
                    (mIsPage) ? getResources().getText(R.string.page)
                            : getResources().getText(R.string.post)
            );
        }
    }

    public class refreshCommentsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Object[] commentParams = {BioWiki.currentBlog.getRemoteBlogId(),
                    BioWiki.currentBlog.getUsername(),
                    BioWiki.currentBlog.getPassword()};

            try {
                ApiHelper.refreshComments(PostsActivity.this, BioWiki.currentBlog, commentParams);
            } catch (final Exception e) {
                mErrorMsg = getResources().getText(R.string.error_generic).toString();
            }
            return null;
        }
    }

    public class RefreshBlogContentCallback implements ApiHelper.GenericCallback {
        @Override
        public void onSuccess() {
            if (isFinishing()) {
                return;
            }
            updateMenuDrawer();
            mPostList.setRefreshing(false);
        }

        @Override
        public void onFailure(ApiHelper.ErrorType errorType, String errorMessage, Throwable throwable) {
            if (isFinishing()) {
                return;
            }
            mPostList.setRefreshing(false);
        }
    }
}
