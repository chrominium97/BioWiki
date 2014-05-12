package kr.kdev.dg1s.biowiki.ui.posts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Post;
import kr.kdev.dg1s.biowiki.models.PostStatus;
import kr.kdev.dg1s.biowiki.ui.AuthenticatedWebViewActivity;
import kr.kdev.dg1s.biowiki.util.StringUtils;

/**
 * Activity for previewing a post or page in a webview.
 */
public class PreviewPostActivity extends AuthenticatedWebViewActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        boolean isPage = getIntent().getBooleanExtra("isPage", false);
        if (isPage) {
            this.setTitle(StringUtils.unescapeHTML(BioWiki.getCurrentBlog().getBlogName())
                    + " - " + getResources().getText(R.string.preview_page));
        } else {
            this.setTitle(StringUtils.unescapeHTML(BioWiki.getCurrentBlog().getBlogName())
                    + " - " + getResources().getText(R.string.preview_post));
        }

        mWebView.getSettings().setJavaScriptEnabled(true);

        if (extras != null) {
            long mPostID = extras.getLong("postID");

            Post post = BioWiki.wpDB.getPostForLocalTablePostId(mPostID);
            if (post == null)
                Toast.makeText(this, R.string.post_not_found, Toast.LENGTH_SHORT).show();
            else
                loadPostPreview(post);
        } else if (BioWiki.currentPost != null) {
            loadPostPreview(BioWiki.currentPost);
        } else {
            Toast.makeText(this, R.string.post_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load the post preview. If the post is in a non-public state (e.g. draft status, part of a
     * non-public blog, etc), load the preview as an authenticated URL. Otherwise, just load the
     * preview normally.
     *
     * @param post Post to load the preview for.
     */
    private void loadPostPreview(Post post) {
        if (post != null) {
            String url = post.getPermaLink();

            if (BioWiki.getCurrentBlog().isPrivate() //blog private
                    || post.isLocalDraft()
                    || post.isLocalChange()
                    || post.getStatusEnum() != PostStatus.PUBLISHED) {
                if (-1 == url.indexOf('?')) {
                    url = url.concat("?preview=true");
                } else {
                    url = url.concat("&preview=true");
                }
                loadAuthenticatedUrl(url);
            } else {
                loadUrl(url);
            }
        }
    }
}
