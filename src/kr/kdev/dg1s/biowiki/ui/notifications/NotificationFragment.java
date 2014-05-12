/**
 * Provides a list view and list adapter to display a note. It will have a header view to show
 * the avatar and other details for the post.
 *
 * More specialized note adapters will need to be made to provide the correct views for the type
 * of note/note template it has.
 */
package kr.kdev.dg1s.biowiki.ui.notifications;

import kr.kdev.dg1s.biowiki.models.Note;

public interface NotificationFragment {

    public Note getNote();

    public void setNote(Note note);

    public static interface OnPostClickListener {
        public void onPostClicked(Note note, int remoteBlogId, int postId);
    }

    public static interface OnCommentClickListener {
        public void onCommentClicked(Note note, int remoteBlogId, long commentId);
    }
}