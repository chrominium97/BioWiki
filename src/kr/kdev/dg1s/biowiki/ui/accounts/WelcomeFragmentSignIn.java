package kr.kdev.dg1s.biowiki.ui.accounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wordpress.emailchecker.EmailChecker;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.BioWikiDB;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.util.EditTextUtils;
import kr.kdev.dg1s.biowiki.widgets.BWTextView;

//import kr.kdev.dg1s.biowiki.networking.SelfSignedSSLCertsManager;
//import kr.kdev.dg1s.biowiki.networking.SSLCertsViewActivity;

//import kr.kdev.dg1s.biowiki.networking.SelfSignedSSLCertsManager;
//import kr.kdev.dg1s.biowiki.networking.SSLCertsViewActivity;

public class WelcomeFragmentSignIn extends NewAccountAbstractPageFragment implements TextWatcher {
    final private static String DOT_COM_BASE_URL = "http://10.80.121.88/nacl";
    final private static String FORGOT_PASSWORD_RELATIVE_URL = "/wp-login.php?action=lostpassword";
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mUrlEditText;
    private boolean mSelfHosted = true;
    private View.OnClickListener mForgotPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String baseUrl = DOT_COM_BASE_URL;
            if (mSelfHosted && !TextUtils.isEmpty(EditTextUtils.getText(mUrlEditText).trim())) {
                baseUrl = EditTextUtils.getText(mUrlEditText).trim();
                String lowerCaseBaseUrl = baseUrl.toLowerCase(Locale.getDefault());
                if (!lowerCaseBaseUrl.startsWith("https://") && !lowerCaseBaseUrl.startsWith("http://")) {
                    baseUrl = "http://" + baseUrl;
                }
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + FORGOT_PASSWORD_RELATIVE_URL));
            startActivity(intent);
        }
    };
    private TextView.OnEditorActionListener mEditorAction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (mPasswordEditText == v) {
                if (mSelfHosted) {
                    mUrlEditText.requestFocus();
                    return true;
                } else {
                    return onDoneEvent(actionId, event);
                }
            }
            return onDoneEvent(actionId, event);
        }
    };
    private BWTextView mSignInButton;
    private BWTextView mCreateAccountButton;
    private BWTextView mAddSelfHostedButton;
    private BWTextView mProgressTextSignIn;
    private BWTextView mForgotPassword;
    private LinearLayout mBottomButtonsLayout;
    private RelativeLayout mProgressBarSignIn;
    private RelativeLayout mUrlButtonLayout;
    private ImageView mInfoButton;
    private ImageView mInfoButtonSecondary;
    private EmailChecker mEmailChecker;
    private boolean mEmailAutoCorrected;
    private View.OnClickListener mCreateAccountListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newAccountIntent = new Intent(getActivity(), NewAccountActivity.class);
            startActivityForResult(newAccountIntent, WelcomeActivity.CREATE_ACCOUNT_REQUEST);
        }
    };
    private OnClickListener mSignInClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            signin();
        }
    };

    public WelcomeFragmentSignIn() {
        mEmailChecker = new EmailChecker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.nux_fragment_welcome, container, false);
        mUrlButtonLayout = (RelativeLayout) rootView.findViewById(R.id.url_button_layout);
        mUsernameEditText = (EditText) rootView.findViewById(R.id.nux_username);
        mUsernameEditText.addTextChangedListener(this);
        mPasswordEditText = (EditText) rootView.findViewById(R.id.nux_password);
        mPasswordEditText.addTextChangedListener(this);
        mUrlEditText = (EditText) rootView.findViewById(R.id.nux_url);
        mSignInButton = (BWTextView) rootView.findViewById(R.id.nux_sign_in_button);
        mSignInButton.setOnClickListener(mSignInClickListener);
        mProgressBarSignIn = (RelativeLayout) rootView.findViewById(R.id.nux_sign_in_progress_bar);
        mProgressTextSignIn = (BWTextView) rootView.findViewById(R.id.nux_sign_in_progress_text);
        mCreateAccountButton = (BWTextView) rootView.findViewById(R.id.nux_create_account_button);
        mCreateAccountButton.setOnClickListener(mCreateAccountListener);
        mAddSelfHostedButton = (BWTextView) rootView.findViewById(R.id.nux_add_selfhosted_button);
        mAddSelfHostedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUrlButtonLayout.getVisibility() == View.VISIBLE) {
                    mUrlButtonLayout.setVisibility(View.GONE);
                    mAddSelfHostedButton.setText(getString(R.string.nux_add_selfhosted_blog));
                    mSelfHosted = false;
                } else {
                    mUrlButtonLayout.setVisibility(View.VISIBLE);
                    mAddSelfHostedButton.setText(getString(R.string.nux_oops_not_selfhosted_blog));
                    mSelfHosted = true;
                }
            }
        });
        mForgotPassword = (BWTextView) rootView.findViewById(R.id.forgot_password);
        mForgotPassword.setOnClickListener(mForgotPasswordListener);
        mUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    autocorrectUsername();
                }
            }
        });
        mPasswordEditText.setOnEditorActionListener(mEditorAction);
        mUrlEditText.setOnEditorActionListener(mEditorAction);
        mBottomButtonsLayout = (LinearLayout) rootView.findViewById(R.id.nux_bottom_buttons);
        initPasswordVisibilityButton(rootView, mPasswordEditText);
        initInfoButtons(rootView);
        moveBottomButtons();
        return rootView;
    }

    /**
     * Hide toggle button "add self hosted / sign in with WordPress.com" and show self hosted URL
     * edit box
     */
    public void forceSelfHostedMode() {
        mUrlButtonLayout.setVisibility(View.VISIBLE);
        mAddSelfHostedButton.setVisibility(View.GONE);
        mCreateAccountButton.setVisibility(View.GONE);
        mSelfHosted = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        moveBottomButtons();
    }

    private void initInfoButtons(View rootView) {
        OnClickListener infoButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAccountIntent = new Intent(getActivity(), NuxHelpActivity.class);
                startActivity(newAccountIntent);
            }
        };
        mInfoButton = (ImageView) rootView.findViewById(R.id.info_button);
        mInfoButtonSecondary = (ImageView) rootView.findViewById(R.id.info_button_secondary);
        mInfoButton.setOnClickListener(infoButtonListener);
        mInfoButtonSecondary.setOnClickListener(infoButtonListener);
    }

    private void setSecondaryButtonVisible(boolean visible) {
        mInfoButtonSecondary.setVisibility(visible ? View.VISIBLE : View.GONE);
        mInfoButton.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void moveBottomButtons() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBottomButtonsLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (getResources().getInteger(R.integer.isTablet) == 0) {
                setSecondaryButtonVisible(true);
            } else {
                setSecondaryButtonVisible(false);
            }
        } else {
            mBottomButtonsLayout.setOrientation(LinearLayout.VERTICAL);
            setSecondaryButtonVisible(false);
        }
    }

    private void autocorrectUsername() {
        if (mEmailAutoCorrected) {
            return;
        }
        final String email = EditTextUtils.getText(mUsernameEditText).trim();
        // Check if the username looks like an email address
        final Pattern emailRegExPattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = emailRegExPattern.matcher(email);
        if (!matcher.find()) {
            return;
        }
        // It looks like an email address, then try to correct it
        String suggest = mEmailChecker.suggestDomainCorrection(email);
        if (suggest.compareTo(email) != 0) {
            mEmailAutoCorrected = true;
            mUsernameEditText.setText(suggest);
            mUsernameEditText.setSelection(suggest.length());
        }
    }

    protected void onDoneAction() {
        signin();
    }

    private void signin() {
        if (!isUserDataValid()) {
            return;
        }
        new SetupBlogTask().execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (fieldsFilled()) {
            mSignInButton.setEnabled(true);
        } else {
            mSignInButton.setEnabled(false);
        }
        mPasswordEditText.setError(null);
        mUsernameEditText.setError(null);
    }

    private boolean fieldsFilled() {
        return EditTextUtils.getText(mUsernameEditText).trim().length() > 0
                && EditTextUtils.getText(mPasswordEditText).trim().length() > 0;
    }

    protected boolean isUserDataValid() {
        final String username = EditTextUtils.getText(mUsernameEditText).trim();
        final String password = EditTextUtils.getText(mPasswordEditText).trim();
        boolean retValue = true;

        if (username.equals("")) {
            mUsernameEditText.setError(getString(R.string.required_field));
            mUsernameEditText.requestFocus();
            retValue = false;
        }

        if (password.equals("")) {
            mPasswordEditText.setError(getString(R.string.required_field));
            mPasswordEditText.requestFocus();
            retValue = false;
        }
        return retValue;
    }

    private boolean selfHostedFieldsFilled() {
        return fieldsFilled() && EditTextUtils.getText(mUrlEditText).trim().length() > 0;
    }

    private void showPasswordError(int messageId) {
        mPasswordEditText.setError(getString(messageId));
        mPasswordEditText.requestFocus();
    }

    private void showUsernameError(int messageId) {
        mUsernameEditText.setError(getString(messageId));
        mUsernameEditText.requestFocus();
    }

    protected boolean specificShowError(int messageId) {
        switch (getErrorType(messageId)) {
            case USERNAME:
            case PASSWORD:
                showUsernameError(messageId);
                showPasswordError(messageId);
                return true;
        }
        return false;
    }

    public void signInDotComUser() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());
        String username = settings.getString(BioWiki.WPCOM_USERNAME_PREFERENCE, null);
        String password = BioWikiDB.decryptPassword(settings.getString(BioWiki.WPCOM_PASSWORD_PREFERENCE, null));
        if (username != null && password != null) {
            mUsernameEditText.setText(username);
            mPasswordEditText.setText(password);
            new SetupBlogTask().execute();
        }
    }

    protected void startProgress(String message) {
        mProgressBarSignIn.setVisibility(View.VISIBLE);
        mProgressTextSignIn.setVisibility(View.VISIBLE);
        mSignInButton.setVisibility(View.GONE);
        mProgressBarSignIn.setEnabled(false);
        mProgressTextSignIn.setText(message);
        mUsernameEditText.setEnabled(false);
        mPasswordEditText.setEnabled(false);
        mUrlEditText.setEnabled(false);
        mAddSelfHostedButton.setEnabled(false);
        mCreateAccountButton.setEnabled(false);
        mForgotPassword.setEnabled(false);
    }

    protected void endProgress() {
        mProgressBarSignIn.setVisibility(View.GONE);
        mProgressTextSignIn.setVisibility(View.GONE);
        mSignInButton.setVisibility(View.VISIBLE);
        mUsernameEditText.setEnabled(true);
        mPasswordEditText.setEnabled(true);
        mUrlEditText.setEnabled(true);
        mAddSelfHostedButton.setEnabled(true);
        mCreateAccountButton.setEnabled(true);
        mForgotPassword.setEnabled(true);
    }


    protected void askForSslTrust() {
        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.ssl_certificate_error));
        alert.setMessage(getString(R.string.ssl_certificate_ask_trust));
        alert.setPositiveButton(
                R.string.ssl_certificate_trust, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SetupBlogTask setupBlogTask = new SetupBlogTask();
                try {
                    SelfSignedSSLCertsManager selfSignedSSLCertsManager = SelfSignedSSLCertsManager.getInstance(getActivity());
                    selfSignedSSLCertsManager.addCertificates(selfSignedSSLCertsManager.getLastFailureChain());
                } catch (IOException e) {
                    AppLog.e(AppLog.T.NUX, e);
                } catch (GeneralSecurityException e) {
                    AppLog.e(AppLog.T.NUX, e);
                }
                setupBlogTask.execute();
            }
        });
        alert.setNeutralButton(R.string.ssl_certificate_details, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SSLCertsViewActivity.class);
                try {
                    SelfSignedSSLCertsManager selfSignedSSLCertsManager = SelfSignedSSLCertsManager.getInstance(getActivity());
                    String lastFailureChainDescription = "URL: " + EditTextUtils.getText(mUrlEditText).trim() + "<br/><br/>" 
                            + selfSignedSSLCertsManager.getLastFailureChainDescription().replaceAll("\n", "<br/>");
                    intent.putExtra(SSLCertsViewActivity.CERT_DETAILS_KEYS, lastFailureChainDescription);
                    getActivity().startActivityForResult(intent, WelcomeActivity.SHOW_CERT_DETAILS);
                } catch (GeneralSecurityException e) {
                    AppLog.e(AppLog.T.NUX, e);
                } catch (IOException e) {
                    AppLog.e(AppLog.T.NUX, e);
                }
            }
        });
        alert.setNegativeButton(R.string.ssl_certificate_do_not_trust, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
        endProgress();
        */
    }


    private class SetupBlogTask extends AsyncTask<Void, Void, List<Object>> {
        private SetupBlog mSetupBlog;
        private int mErrorMsgId;

        private void setHttpCredentials(String username, String password) {
            if (mSetupBlog == null) {
                mSetupBlog = new SetupBlog();
            }
            mSetupBlog.setHttpUsername(username);
            mSetupBlog.setHttpPassword(password);
        }

        @Override
        protected void onPreExecute() {
            if (mSetupBlog == null) {
                mSetupBlog = new SetupBlog();
            }
            mSetupBlog.setUsername(EditTextUtils.getText(mUsernameEditText).trim());
            mSetupBlog.setPassword(EditTextUtils.getText(mPasswordEditText).trim());
            if (mSelfHosted) {
                mSetupBlog.setSelfHostedURL(EditTextUtils.getText(mUrlEditText).trim());
            } else {
                mSetupBlog.setSelfHostedURL("10.80.121.88/nacl");
            }
            startProgress(selfHostedFieldsFilled() ? getString(R.string.attempting_configure) : getString(
                    R.string.connecting_wpcom));
        }

        @Override
        protected List doInBackground(Void... args) {
            List userBlogList = mSetupBlog.getBlogList();
            mErrorMsgId = mSetupBlog.getErrorMsgId();
            if (userBlogList != null) {
                mSetupBlog.addBlogs(userBlogList);
            }
            return userBlogList;
        }

        private void httpAuthRequired() {
            // Prompt for http credentials
            mSetupBlog.setHttpAuthRequired(false);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(R.string.http_authorization_required);

            View httpAuth = getActivity().getLayoutInflater().inflate(R.layout.alert_http_auth, null);
            final EditText usernameEditText = (EditText) httpAuth.findViewById(R.id.http_username);
            final EditText passwordEditText = (EditText) httpAuth.findViewById(R.id.http_password);
            alert.setView(httpAuth);
            alert.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SetupBlogTask setupBlogTask = new SetupBlogTask();
                    setupBlogTask.setHttpCredentials(EditTextUtils.getText(usernameEditText), EditTextUtils.getText(
                            passwordEditText));
                    setupBlogTask.execute();
                }
            });

            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
            endProgress();
        }


        @Override
        protected void onPostExecute(final List<Object> userBlogList) {
            if (mSetupBlog.isErroneousSslCertificates() && hasActivity()) {
                askForSslTrust();
                return;
            }

            if (mSetupBlog.isHttpAuthRequired() && hasActivity()) {
                httpAuthRequired();
                return;
            }

            if (userBlogList == null && mErrorMsgId != 0 && hasActivity()) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                NUXDialogFragment nuxAlert;
                if (mErrorMsgId == R.string.account_two_step_auth_enabled) {
                    nuxAlert = NUXDialogFragment.newInstance(getString(R.string.nux_cannot_log_in), getString(
                                    mErrorMsgId), getString(R.string.nux_tap_continue), R.drawable.nux_icon_alert, true,
                            getString(R.string.visit_security_settings), NUXDialogFragment.ACTION_OPEN_URL,
                            "https://wordpress.com/settings/security/?ssl=forced"
                    );
                } else {
                    if (mErrorMsgId == R.string.username_or_password_incorrect) {
                        showUsernameError(mErrorMsgId);
                        showPasswordError(mErrorMsgId);
                        mErrorMsgId = 0;
                        endProgress();
                        return;
                    } else {
                        nuxAlert = NUXDialogFragment.newInstance(getString(R.string.nux_cannot_log_in), getString(
                                mErrorMsgId), getString(R.string.nux_tap_continue), R.drawable.nux_icon_alert);
                    }
                }
                ft.add(nuxAlert, "alert");
                ft.commitAllowingStateLoss();
                mErrorMsgId = 0;
                endProgress();
                return;
            }

            /*
            // Update wp.com credentials
            if (mSetupBlog.getXmlrpcUrl().contains("wordpress.com")) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(BioWiki.getContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(BioWiki.WPCOM_USERNAME_PREFERENCE, mSetupBlog.getUsername());
                editor.putString(BioWiki.WPCOM_PASSWORD_PREFERENCE, BioWikiDB.encryptPassword(
                        mSetupBlog.getPassword()));
                editor.commit();
                // Fire off a request to get an access token
                BioWiki.getRestClientUtils().get("me", new RestRequest.Listener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ReaderUserActions.setCurrentUser(jsonObject);
                    }
                }, null);
            }
            */

            if (userBlogList != null) {
                if (getActivity() != null) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            } else {
                endProgress();
            }
        }
    }
}
