/**
 * Behaves much list a ListFragment
 */
package kr.kdev.dg1s.biowiki.ui.notifications;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Note;
import kr.kdev.dg1s.biowiki.util.JSONUtil;

public class NoteSingleLineListFragment extends ListFragment implements NotificationFragment {
    private Note mNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_follow_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ListView list = getListView();
        list.setDivider(getResources().getDrawable(R.drawable.list_divider));
        list.setDividerHeight(1);
        list.setHeaderDividersEnabled(false);

        // No note? No service.
        if (getNote() == null)
            return;

        // set the header
        final DetailHeader noteHeader = (DetailHeader) getView().findViewById(R.id.header);
        noteHeader.setText(JSONUtil.getStringDecoded(getNote().queryJSON("subject", new JSONObject()), "text"));
        String footerUrl = getNote().queryJSON("body.header_link", "");
        noteHeader.setNote(getNote(), footerUrl);

        if (getActivity() instanceof OnPostClickListener) {
            noteHeader.setOnPostClickListener(((OnPostClickListener) getActivity()));
        }
        if (getActivity() instanceof OnCommentClickListener) {
            noteHeader.setOnCommentClickListener(((OnCommentClickListener) getActivity()));
        }

        setListAdapter(new NoteFollowAdapter(getActivity(), getNote(), false));
    }

    @Override
    public Note getNote() {
        return mNote;
    }

    @Override
    public void setNote(Note note) {
        mNote = note;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState.isEmpty()) {
            outState.putBoolean("bug_19917_fix", true);
        }
        super.onSaveInstanceState(outState);
    }
}