package kr.kdev.dg1s.biowiki.ui.posts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Post;
import kr.kdev.dg1s.biowiki.ui.comments.CommentActions;
import kr.kdev.dg1s.biowiki.util.BWHtml;
import kr.kdev.dg1s.biowiki.util.BWMobileStatsUtil;
import kr.kdev.dg1s.biowiki.util.BWWebViewClient;
import kr.kdev.dg1s.biowiki.util.EditTextUtils;
import kr.kdev.dg1s.biowiki.util.NetworkUtils;
import kr.kdev.dg1s.biowiki.util.StringUtils;
import kr.kdev.dg1s.biowiki.util.ToastUtils;

public class ViewPostFragment extends Fragment {
    PostsActivity parentActivity;
    boolean mIsCommentBoxShowing = false;
    boolean mIsSubmittingComment = false;
    /**
     * Called when the activity is first created.
     */

    private OnDetailPostActionListener onDetailPostActionListener;
    private ViewGroup mLayoutCommentBox;
    private EditText mEditComment;
    private ImageButton mAddCommentButton;

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (BioWiki.currentPost != null)
            loadPost(BioWiki.currentPost);

        parentActivity = (PostsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.posts_viewpost_fragment, container, false);

        // comment views
        mLayoutCommentBox = (ViewGroup) v.findViewById(R.id.layout_comment_box);
        mEditComment = (EditText) mLayoutCommentBox.findViewById(R.id.edit_comment);
        mEditComment.setHint(R.string.reader_hint_comment_on_post);

        // button listeners here
        ImageButton editPostButton = (ImageButton) v
                .findViewById(R.id.editPost);
        editPostButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                if (BioWiki.currentPost != null && !parentActivity.isRefreshing()) {
                    onDetailPostActionListener.onDetailPostAction(
                            PostsActivity.POST_EDIT, BioWiki.currentPost);
                    Intent i = new Intent(
                            getActivity().getApplicationContext(),
                            EditPostActivity.class);
                    i.putExtra(EditPostActivity.EXTRA_IS_PAGE, BioWiki.currentPost.isPage());
                    i.putExtra(EditPostActivity.EXTRA_POSTID, BioWiki.currentPost.getLocalTablePostId());
                    getActivity().startActivityForResult(i, PostsActivity.ACTIVITY_EDIT_POST);
                }

            }
        });

        ImageButton shareURLButton = (ImageButton) v
                .findViewById(R.id.sharePostLink);
        shareURLButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                if (!parentActivity.isRefreshing()) {
                    onDetailPostActionListener.onDetailPostAction(PostsActivity.POST_SHARE, BioWiki.currentPost);
                }
            }
        });

        ImageButton deletePostButton = (ImageButton) v
                .findViewById(R.id.deletePost);
        deletePostButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                if (!parentActivity.isRefreshing()) {
                    onDetailPostActionListener.onDetailPostAction(PostsActivity.POST_DELETE, BioWiki.currentPost);
                }
            }
        });

        ImageButton viewPostButton = (ImageButton) v
                .findViewById(R.id.viewPost);
        viewPostButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                onDetailPostActionListener.onDetailPostAction(PostsActivity.POST_VIEW, BioWiki.currentPost);
                if (!parentActivity.isRefreshing()) {
                    loadPostPreview();
                }
            }
        });

        mAddCommentButton = (ImageButton) v.findViewById(R.id.addComment);
        // Tint the comment icon to match the other icons in the toolbar
        mAddCommentButton.setColorFilter(Color.argb(255, 132, 132, 132));
        mAddCommentButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                if (!parentActivity.isRefreshing()) {
                    toggleCommentBox();
                }
            }
        });

        return v;

    }

    protected void loadPostPreview() {
        if (BioWiki.currentPost != null && !TextUtils.isEmpty(BioWiki.currentPost.getPermaLink())) {
            Intent i = new Intent(getActivity(), PreviewPostActivity.class);
            startActivity(i);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // check that the containing activity implements our callback
            onDetailPostActionListener = (OnDetailPostActionListener) activity;
        } catch (ClassCastException e) {
            activity.finish();
            throw new ClassCastException(activity.toString()
                    + " must implement Callback");
        }
    }

    public void loadPost(final Post post) {
        // Don't load if the Post object or title are null, see #395
        if (post == null || post.getTitle() == null)
            return;
        if (!hasActivity() || getView() == null)
            return;

        // create handler on UI thread
        final Handler handler = new Handler();

        // locate views and determine content in the background to avoid ANR - especially
        // important when using BWHtml.fromHtml() for drafts that contain images since
        // thumbnails may take some time to create
        final WebView webView = (WebView) getView().findViewById(R.id.viewPostWebView);
        webView.setWebViewClient(new BWWebViewClient(BioWiki.getCurrentBlog()));
        new Thread() {
            @Override
            public void run() {
                final TextView txtTitle = (TextView) getView().findViewById(R.id.postTitle);
                final TextView txtContent = (TextView) getView().findViewById(R.id.viewPostTextView);
                final TextView tagView = (TextView) getView().findViewById(R.id.tagView);
                final ImageButton btnShareUrl = (ImageButton) getView().findViewById(R.id.sharePostLink);
                final ImageButton btnViewPost = (ImageButton) getView().findViewById(R.id.viewPost);
                final ImageButton btnAddComment = (ImageButton) getView().findViewById(R.id.addComment);

                final String title = (TextUtils.isEmpty(post.getTitle())
                        ? "(" + getResources().getText(R.string.untitled) + ")"
                        : StringUtils.unescapeHTML(post.getTitle()));

                final String postContent = post.getDescription() + "\n\n" + post.getMoreText();

                final Spanned draftContent;
                final String htmlContent;
                if (post.isLocalDraft()) {
                    draftContent = BWHtml.fromHtml(postContent.replaceAll("\uFFFC", ""), getActivity(), post);
                    htmlContent = null;
                } else {
                    draftContent = null;
                    htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                            + "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\" /></head>"
                            + "<body><div id=\"container\">"
                            + StringUtils.addPTags(postContent)
                            + "</div></body></html>";
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // make sure activity is still valid
                        if (!hasActivity())
                            return;

                        txtTitle.setText(title);

                        if (post.isLocalDraft()) {
                            txtContent.setVisibility(View.VISIBLE);
                            webView.setVisibility(View.GONE);
                            btnShareUrl.setVisibility(View.GONE);
                            btnViewPost.setVisibility(View.GONE);
                            btnAddComment.setVisibility(View.GONE);
                            txtContent.setText(draftContent);
                        } else {
                            txtContent.setVisibility(View.GONE);
                            webView.setVisibility(View.VISIBLE);
                            btnShareUrl.setVisibility(View.VISIBLE);
                            btnViewPost.setVisibility(View.VISIBLE);
                            btnAddComment.setVisibility(post.isAllowComments() ? View.VISIBLE : View.GONE);
                            webView.loadDataWithBaseURL("file:///android_asset/",
                                    htmlContent,
                                    "text/html",
                                    "utf-8",
                                    null);
                        }
                        if (!post.getKeywords().equals("")) {
                            Log.d("Tags", "Tags are visible");
                            String tags = post.getKeywords();
                            tags = (tags.replaceAll(", ", "\n")).replace("，", ", ");
                            tagView.setText(tags);
                            tagView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }.start();
    }

    public void clearContent() {
        TextView txtTitle = (TextView) getView().findViewById(R.id.postTitle);
        WebView webView = (WebView) getView().findViewById(R.id.viewPostWebView);
        TextView txtContent = (TextView) getView().findViewById(R.id.viewPostTextView);
        txtTitle.setText("");
        txtContent.setText("");
        String htmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                + "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\" /></head>"
                + "<body><div id=\"container\"></div></body></html>";
        webView.loadDataWithBaseURL("file:///android_asset/", htmlText,
                "text/html", "utf-8", null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState.isEmpty()) {
            outState.putBoolean("bug_19917_fix", true);
        }
        super.onSaveInstanceState(outState);
    }

    private boolean hasActivity() {
        return (getActivity() != null && !isRemoving());
    }

    private void showCommentBox() {
        // skip if it's already showing or a comment is being submitted
        if (mIsCommentBoxShowing || mIsSubmittingComment)
            return;
        if (!hasActivity())
            return;

        BWMobileStatsUtil.flagProperty(BWMobileStatsUtil.StatsEventPostsClosed,
                BWMobileStatsUtil.StatsPropertyPostDetailClickedComment);

        // show the comment box in, force keyboard to appear and highlight the comment button
        mLayoutCommentBox.setVisibility(View.VISIBLE);
        mEditComment.requestFocus();

        // submit comment when done/send tapped on the keyboard
        mEditComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
                    submitComment();
                return false;
            }
        });

        // submit comment when send icon tapped
        final ImageView imgPostComment = (ImageView) mLayoutCommentBox.findViewById(R.id.image_post_comment);
        imgPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditComment, InputMethodManager.SHOW_IMPLICIT);
        mIsCommentBoxShowing = true;
    }

    private void hideCommentBox() {
        if (!mIsCommentBoxShowing)
            return;
        if (!hasActivity())
            return;

        EditTextUtils.hideSoftInput(mEditComment);
        mLayoutCommentBox.setVisibility(View.GONE);

        mIsCommentBoxShowing = false;
    }

    private void toggleCommentBox() {
        if (mIsCommentBoxShowing) {
            hideCommentBox();
        } else {
            showCommentBox();
        }
    }

    private void submitComment() {
        if (!hasActivity() || mIsSubmittingComment)
            return;

        if (!NetworkUtils.checkConnection(getActivity()))
            return;

        final String commentText = EditTextUtils.getText(mEditComment);
        if (TextUtils.isEmpty(commentText))
            return;

        final ImageView imgPostComment = (ImageView) mLayoutCommentBox.findViewById(R.id.image_post_comment);
        final ProgressBar progress = (ProgressBar) mLayoutCommentBox.findViewById(R.id.progress_submit_comment);

        // disable editor & comment button, hide soft keyboard, hide submit icon, and show progress spinner while submitting
        mEditComment.setEnabled(false);
        mAddCommentButton.setEnabled(false);
        EditTextUtils.hideSoftInput(mEditComment);
        imgPostComment.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        CommentActions.CommentActionListener actionListener = new CommentActions.CommentActionListener() {
            @Override
            public void onActionResult(boolean succeeded) {
                mIsSubmittingComment = false;
                if (!hasActivity())
                    return;

                parentActivity.attemptToSelectPost();

                mEditComment.setEnabled(true);
                mAddCommentButton.setEnabled(true);
                imgPostComment.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                if (succeeded) {
                    ToastUtils.showToast(getActivity(), R.string.comment_added);
                    hideCommentBox();
                    mEditComment.setText(null);
                    parentActivity.refreshComments();
                } else {
                    ToastUtils.showToast(getActivity(), R.string.reader_toast_err_comment_failed, ToastUtils.Duration.LONG);
                }
            }
        };

        int accountId = BioWiki.getCurrentLocalTableBlogId();
        CommentActions.addComment(accountId, BioWiki.currentPost.getRemotePostId(), commentText, actionListener);
    }

    public interface OnDetailPostActionListener {
        public void onDetailPostAction(int action, Post post);
    }

}
