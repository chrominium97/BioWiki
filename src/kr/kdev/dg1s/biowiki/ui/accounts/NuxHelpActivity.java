package kr.kdev.dg1s.biowiki.ui.accounts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.AppLogViewerActivity;
import kr.kdev.dg1s.biowiki.widgets.BWTextView;

public class NuxHelpActivity extends SherlockFragmentActivity {
    final private static String FAQ_URL = "http://android.wordpress.org/faq/";
    final private static String FORUM_URL = "http://android.forums.wordpress.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nux_help);

        BWTextView version = (BWTextView) findViewById(R.id.nux_help_version);
        version.setText(getString(R.string.version) + " " + BioWiki.versionName);

        BWTextView helpCenterButton = (BWTextView) findViewById(R.id.help_button);
        helpCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FAQ_URL)));
            }
        });

        BWTextView forumButton = (BWTextView) findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FORUM_URL)));
            }
        });

        BWTextView applogButton = (BWTextView) findViewById(R.id.applog_button);
        applogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AppLogViewerActivity.class));
            }
        });
    }
}