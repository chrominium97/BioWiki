package kr.kdev.dg1s.biowiki.ui.notifications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Note;
import kr.kdev.dg1s.biowiki.util.BWLinkMovementMethod;
import kr.kdev.dg1s.biowiki.util.HtmlUtils;
import kr.kdev.dg1s.biowiki.util.JSONUtil;

public class BigBadgeFragment extends Fragment implements NotificationFragment {
    private Note mNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        View view = inflater.inflate(R.layout.notifications_big_badge, parent, false);
        NetworkImageView badgeImageView = (NetworkImageView) view.findViewById(R.id.badge);

        TextView bodyTextView = (TextView) view.findViewById(R.id.body);
        bodyTextView.setMovementMethod(BWLinkMovementMethod.getInstance());

        if (getNote() != null) {
            String noteHTML = JSONUtil.queryJSON(getNote().toJSONObject(), "body.html", "");
            if (noteHTML.equals("")) {
                noteHTML = getNote().getSubject();
            }
            Spanned html = HtmlUtils.fromHtml(noteHTML);
            bodyTextView.setText(html);

            // Get the badge
            String iconURL = getNote().getIconURL();
            if (!iconURL.equals(""))
                badgeImageView.setImageUrl(iconURL, BioWiki.imageLoader);

            // if this is a stats-related note, show stats link and enable tapping badge
            // to view stats - but only if the note is for a blog that's visible
            if (isStatsNote()) {
                final int remoteBlogId = getNote().getMetaValueAsInt("blog_id", -1);
                if (BioWiki.wpDB.isDotComAccountVisible(remoteBlogId)) {
                    TextView txtStats = (TextView) view.findViewById(R.id.text_stats_link);
                    txtStats.setVisibility(View.VISIBLE);
                    View.OnClickListener statsListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showStatsActivity(remoteBlogId);
                        }
                    };
                    txtStats.setOnClickListener(statsListener);
                    badgeImageView.setOnClickListener(statsListener);
                }
            }
        }

        return view;
    }

    public Note getNote() {
        return mNote;
    }

    public void setNote(Note note) {
        mNote = note;
    }

    /*
     * returns true if this is a stats-related notification - currently handles these types:
     *   followed_milestone_achievement
     *   post_milestone_achievement
     *   like_milestone_achievement
     *   traffic_surge
     *   best_followed_day_feat
     *   best_liked_day_feat
     *   most_liked_day
     *   most_followed_day
     */
    boolean isStatsNote() {
        if (getNote() == null)
            return false;

        String type = getNote().getType();
        if (type == null)
            return false;

        return (type.contains("_milestone_")
                || type.startsWith("traffic_")
                || type.startsWith("best_")
                || type.startsWith("most_"));
    }

    /*
     * show stats for the passed blog
     */
    private void showStatsActivity(int remoteBlogId) {
        if (getActivity() == null || isRemoving())
            return;

        // stats activity is designed to work with the current blog, so switch blogs if necessary
        if (BioWiki.getCurrentRemoteBlogId() != remoteBlogId) {
            // TODO: should we show a toast to let user know blog was switched?
            int localBlogId = BioWiki.wpDB.getLocalTableBlogIdForRemoteBlogId(remoteBlogId);
            BioWiki.setCurrentBlog(localBlogId);
        }

        /*
        Intent intent = new Intent(getActivity(), StatsActivity.class);
        intent.putExtra(StatsActivity.ARG_NO_MENU_DRAWER, true);
        getActivity().startActivity(intent);
        */
    }
}