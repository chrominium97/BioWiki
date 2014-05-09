package kr.kdev.dg1s.biowiki.ui.accounts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockDialogFragment;

import kr.kdev.dg1s.biowiki.widgets.BWTextView;
import kr.kdev.dg1s.biowiki.R;

public class NUXDialogFragment extends SherlockDialogFragment {
    private static String ARG_TITLE = "title";
    private static String ARG_DESCRIPTION = "message";
    private static String ARG_FOOTER = "footer";
    private static String ARG_IMAGE = "image";
    private static String ARG_TWO_BUTTONS = "two-buttons";
    private static String ARG_SECOND_BUTTON_LABEL = "second-btn-label";
    private static String ARG_SECOND_BUTTON_ACTION = "second-btn-action";
    private static String ARG_SECOND_BUTTON_PARAM = "second-btn-param";

    private ImageView mImageView;
    private BWTextView mTitleTextView;
    private BWTextView mDescriptionTextView;
    private BWTextView mFooterOneButton;
    private BWTextView mFooterLeftButton;
    private BWTextView mFooterRightButton;
    private RelativeLayout mFooterTwoButtons;

    public static int ACTION_FINISH = 1;
    public static int ACTION_OPEN_URL = 2;

    public NUXDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static NUXDialogFragment newInstance(String title, String message, String footer,
                                                int imageSource) {
        return newInstance(title, message, footer, imageSource, false, "", 0, "");
    }

    public static NUXDialogFragment newInstance(String title, String message, String footer,
                                                int imageSource, boolean twoButtons,
                                                String secondButtonLabel, int secondButtonAction,
                                                String secondButtonParam) {
        NUXDialogFragment adf = new NUXDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_DESCRIPTION, message);
        bundle.putString(ARG_FOOTER, footer);
        bundle.putInt(ARG_IMAGE, imageSource);
        bundle.putBoolean(ARG_TWO_BUTTONS, twoButtons);
        bundle.putString(ARG_SECOND_BUTTON_LABEL, secondButtonLabel);
        bundle.putInt(ARG_SECOND_BUTTON_ACTION, secondButtonAction);
        bundle.putString(ARG_SECOND_BUTTON_PARAM, secondButtonParam);

        adf.setArguments(bundle);
        adf.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
        return adf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.nux_alert_bg));
        View v = inflater.inflate(R.layout.nux_dialog_fragment, container, false);

        mImageView = (ImageView)v.findViewById(R.id.nux_dialog_image);
        mTitleTextView = (BWTextView)v.findViewById(R.id.nux_dialog_title);
        mDescriptionTextView = (BWTextView)v.findViewById(R.id.nux_dialog_description);
        mFooterOneButton = (BWTextView)v.findViewById(R.id.nux_dialog_footer_1_button);
        mFooterTwoButtons = (RelativeLayout) v.findViewById(R.id.nux_dialog_footer_2_buttons);
        mFooterRightButton = (BWTextView)v.findViewById(R.id.nux_dialog_get_started_button);
        mFooterLeftButton = (BWTextView)v.findViewById(R.id.nux_dialog_learn_more_button);
        Bundle args = this.getArguments();

        mTitleTextView.setText(args.getString(ARG_TITLE));
        mDescriptionTextView.setText(args.getString(ARG_DESCRIPTION));
        mFooterOneButton.setText(args.getString(ARG_FOOTER));
        mImageView.setImageResource(args.getInt(ARG_IMAGE));

        if (args.getBoolean(ARG_TWO_BUTTONS)) {
            mFooterOneButton.setVisibility(View.GONE);
            mFooterTwoButtons.setVisibility(View.VISIBLE);
            mFooterRightButton.setText(args.getString(ARG_SECOND_BUTTON_LABEL));
        }

        View.OnClickListener clickListenerDismiss = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        };

        final int action = args.getInt(ARG_SECOND_BUTTON_ACTION, 0);
        final String param = args.getString(ARG_SECOND_BUTTON_PARAM);

        View.OnClickListener clickListenerFinish = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action == ACTION_FINISH) {
                    getActivity().finish();
                }
                if (action == ACTION_OPEN_URL) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(param));
                    startActivity(intent);
                    dismissAllowingStateLoss();
                }
            }
        };

        v.setClickable(true);
        v.setOnClickListener(clickListenerDismiss);
        mFooterOneButton.setOnClickListener(clickListenerDismiss);
        mFooterLeftButton.setOnClickListener(clickListenerDismiss);
        mFooterRightButton.setOnClickListener(clickListenerFinish);

        return v;
    }
}

