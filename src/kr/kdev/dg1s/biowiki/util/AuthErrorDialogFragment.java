package kr.kdev.dg1s.biowiki.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.actionbarsherlock.app.SherlockDialogFragment;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.prefs.BlogPreferencesActivity;

/**
 * An alert dialog fragment for XML-RPC authentication failures
 * Created by @roundhill on 2/17/14.
 */
public class AuthErrorDialogFragment extends SherlockDialogFragment {

    private static boolean mIsWPCom;

    public static AuthErrorDialogFragment newInstance(boolean isWPCom) {
        mIsWPCom = isWPCom;
        return new AuthErrorDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(R.string.connection_error);
        if (mIsWPCom) {
            /*
            b.setMessage(getResources().getText(R.string.incorrect_credentials) + " " + getResources().getText(R.string.please_sign_in));
            b.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent authIntent = new Intent(getActivity(), BWComLoginActivity.class);
                    authIntent.putExtra("wpcom", true);
                    authIntent.putExtra("auth-only", true);
                    getActivity().startActivity(authIntent);
                }
            });
            */
        } else {
            b.setMessage(getResources().getText(R.string.incorrect_credentials) + " " + getResources().getText(R.string.load_settings));
            b.setCancelable(true);
            b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(getActivity(), BlogPreferencesActivity.class);
                    settingsIntent.putExtra("id", BioWiki.getCurrentBlog().getLocalTableBlogId());
                    getActivity().startActivity(settingsIntent);
                }
            });
            b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        return b.create();
    }
}