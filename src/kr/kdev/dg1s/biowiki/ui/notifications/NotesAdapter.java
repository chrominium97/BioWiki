package kr.kdev.dg1s.biowiki.ui.notifications;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Note;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.PhotonUtils;
import kr.kdev.dg1s.biowiki.util.StringUtils;
import kr.kdev.dg1s.biowiki.widgets.BWNetworkImageView;

/**
 * Created by nbradbury on 3/10/14.
 */
class NotesAdapter extends BaseAdapter {
    private final int mAvatarSz;
    private final LayoutInflater mInflater;
    private final ArrayList<Note> mNotes = new ArrayList<Note>();
    private final DataLoadedListener mDataLoadedListener;
    // HashMap of drawables for note types
    private final HashMap<String, Drawable> mNoteIcons = new HashMap<String, Drawable>();
    private boolean mIsAddingNotes;

    NotesAdapter(Context context, DataLoadedListener dataLoadedListener) {
        mInflater = LayoutInflater.from(context);
        mAvatarSz = context.getResources().getDimensionPixelSize(R.dimen.avatar_sz_medium);
        mDataLoadedListener = dataLoadedListener;
    }

    boolean isAddingNotes() {
        return mIsAddingNotes;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isValidPosition(int position) {
        return (position >= 0 && position < mNotes.size());
    }

    @Override
    public Note getItem(int position) {
        if (isValidPosition(position))
            return mNotes.get(position);
        return null;
    }

    private int indexOfNote(Note note) {
        if (note == null)
            return -1;
        for (int i = 0; i < mNotes.size(); i++) {
            if (StringUtils.equals(mNotes.get(i).getId(), note.getId()))
                return i;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note note = getItem(position);
        final NoteViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.note_list_item, null);
            holder = new NoteViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        if (note.isCommentType()) {
            holder.txtDetail.setText(note.getCommentPreview());
            holder.txtDetail.setVisibility(View.VISIBLE);
        } else {
            holder.txtDetail.setVisibility(View.GONE);
        }

        holder.txtLabel.setText(note.getSubject());
        holder.txtDate.setText(note.getTimeSpan());
        holder.imgNoteIcon.setImageDrawable(getDrawableForType(convertView.getContext(), note.getType()));

        String avatarUrl = PhotonUtils.fixAvatar(note.getIconURL(), mAvatarSz);
        holder.imgAvatar.setImageUrl(avatarUrl, BWNetworkImageView.ImageType.AVATAR);

        holder.unreadIndicator.setVisibility(note.isUnread() ? View.VISIBLE : View.INVISIBLE);
        holder.placeholderLoading.setVisibility(note.isPlaceholder() ? View.VISIBLE : View.GONE);

        return convertView;
    }

    public void addAll(List<Note> notes, boolean clearBeforeAdding) {
        if (notes.size() > 0) {
            Collections.sort(notes, new Note.TimeStampComparator());
            mIsAddingNotes = true;
            try {
                if (clearBeforeAdding)
                    mNotes.clear();
                mNotes.addAll(notes);
            } finally {
                notifyDataSetChanged();
                mIsAddingNotes = false;
            }
        }

        if (mDataLoadedListener != null)
            mDataLoadedListener.onDataLoaded(isEmpty());
    }

    /*
     * called by activity when a note has changed - passed note will already contain the changes
     * but will still have the same note id
     */
    protected void updateNote(Note updatedNote) {
        int index = indexOfNote(updatedNote);
        if (index == -1)
            return;
        mNotes.set(index, updatedNote);
        notifyDataSetChanged();
    }

    private Drawable getDrawableForType(Context context, String noteType) {
        if (context == null || noteType == null)
            return null;

        // use like icon for comment likes
        if (noteType.equals(Note.NOTE_COMMENT_LIKE_TYPE))
            noteType = Note.NOTE_LIKE_TYPE;

        Drawable icon = mNoteIcons.get(noteType);
        if (icon != null)
            return icon;

        int imageId = context.getResources().getIdentifier("note_icon_" + noteType, "drawable", context.getPackageName());
        if (imageId == 0) {
            AppLog.i(AppLog.T.NOTIFS, "unknown note type - " + noteType);
            return null;
        }

        icon = context.getResources().getDrawable(imageId);
        if (icon == null)
            return null;

        mNoteIcons.put(noteType, icon);
        return icon;
    }

    public interface DataLoadedListener {
        public void onDataLoaded(boolean isEmpty);
    }

    private static class NoteViewHolder {
        private final TextView txtLabel;
        private final TextView txtDetail;
        private final TextView unreadIndicator;
        private final TextView txtDate;
        private final ProgressBar placeholderLoading;
        private final BWNetworkImageView imgAvatar;
        private final ImageView imgNoteIcon;

        NoteViewHolder(View view) {
            txtLabel = (TextView) view.findViewById(R.id.note_label);
            txtDetail = (TextView) view.findViewById(R.id.note_detail);
            unreadIndicator = (TextView) view.findViewById(R.id.unread_indicator);
            txtDate = (TextView) view.findViewById(R.id.text_date);
            placeholderLoading = (ProgressBar) view.findViewById(R.id.placeholder_loading);
            imgAvatar = (BWNetworkImageView) view.findViewById(R.id.note_avatar);
            imgNoteIcon = (ImageView) view.findViewById(R.id.note_icon);
        }
    }
}