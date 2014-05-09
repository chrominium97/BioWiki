package kr.kdev.dg1s.biowiki.ui.media;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.models.Blog;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.VolleyUtils;

/**
 * provides the ImageLoader and backing RequestQueue for media image requests - necessary because
 * images in protected blogs need to be authenticated, which requires a separate RequestQueue
 */
class MediaImageLoader {
    private MediaImageLoader() {
        throw new AssertionError();
    }

    static ImageLoader getInstance() {
        return getInstance(BioWiki.getCurrentBlog());
    }

    static ImageLoader getInstance(Blog blog) {
        if (blog != null && VolleyUtils.isCustomHTTPClientStackNeeded(blog)) {
            // use ImageLoader with authenticating request queue for protected blogs
            AppLog.d(AppLog.T.MEDIA, "using custom imageLoader");
            Context context = BioWiki.getContext();
            RequestQueue authRequestQueue = Volley.newRequestQueue(context, VolleyUtils.getHTTPClientStack(context, blog));
            ImageLoader imageLoader = new ImageLoader(authRequestQueue, BioWiki.getBitmapCache());
            imageLoader.setBatchedResponseDelay(0);
            return imageLoader;
        } else {
            // use default ImageLoader for all others
            AppLog.d(AppLog.T.MEDIA, "using default imageLoader");
            return BioWiki.imageLoader;
        }
    }
}
