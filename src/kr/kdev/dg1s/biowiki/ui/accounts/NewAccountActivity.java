package kr.kdev.dg1s.biowiki.ui.accounts;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import kr.kdev.dg1s.biowiki.R;

// TODO: merge it with WelcomeFragmentSignIn
public class NewAccountActivity extends SherlockFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_account);
    }
}