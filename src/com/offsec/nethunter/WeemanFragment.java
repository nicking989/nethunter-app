package com.offsec.nethunter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.offsec.nethunter.utils.NhPaths;

public class WeemanFragment extends Fragment {

    SharedPreferences sharedpreferences;
    private Context mContext;
    String port;
    String preset;
    EditText urlclone;
    EditText actionurl;

    NhPaths nh;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public WeemanFragment() {
    }

    public static WeemanFragment newInstance(int sectionNumber) {
        WeemanFragment fragment = new WeemanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.weeman, container, false);
        sharedpreferences = getActivity().getSharedPreferences("com.offsec.nethunter", Context.MODE_PRIVATE);
        mContext = getActivity().getApplicationContext();

        // URL Clone Text Field
        urlclone = (EditText) rootView.findViewById(R.id.weeman_urlclone);

        // Action URL Text Field
        actionurl = (EditText) rootView.findViewById(R.id.weeman_actionurl);

        // Port Text Field
        EditText port = (EditText) rootView.findViewById(R.id.weeman_port);
        port.setText("80");

        // Optional Presets Spinner
        Spinner typeSpinner = (Spinner) rootView.findViewById(R.id.weeman_presets);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weeman_presets_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedItemText = parent.getItemAtPosition(pos).toString();
                Log.d("Slected: ", selectedItemText);
                switch (pos) {
                    case 0:
                        break;
                    case 1:
                        // Facebook
                        urlclone.setText("www.facebook.com");
                        actionurl.setText("www.facebook.com/login.php?login_attempt=1&lwv=110");
                        break;
                    case 2:
                        // Gmail
                        urlclone.setText("accounts.google.com");
                        actionurl.setText("accounts.google.com/AccountLoginInfo");
                        break;
                    case 3:
                        // Linkedin
                        urlclone.setText("www.linkedin.com");
                        actionurl.setText("www.linkedin.com/uas/login-submit");
                        break;
                    case 4:
                        // Twitter
                        urlclone.setText("www.twitter.com");
                        actionurl.setText("www.twitter.com/sessions");
                        break;
                    case 5:
                        // Reddit
                        urlclone.setText("www.reddit.com");
                        actionurl.setText("www.reddit.com/post/login");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        return rootView;
    }

    private void addClickListener(int buttonId, View.OnClickListener onClickListener, View rootView) {
        rootView.findViewById(buttonId).setOnClickListener(onClickListener);
    }

    private void intentClickListener_NH(final String command) {
        try {
            Intent intent =
                    new Intent("com.offsec.nhterm.RUN_SCRIPT_NH");
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            intent.putExtra("com.offsec.nhterm.iInitialCommand", command);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toast_install_terminal), Toast.LENGTH_SHORT).show();
        }
    }
}