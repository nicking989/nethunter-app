package com.offsec.nethunter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.offsec.nethunter.utils.ShellExecuter;

public class WeemanFragment extends Fragment {

    SharedPreferences sharedpreferences;
    private Context mContext;
    EditText urlclone;
    EditText actionurl;
    EditText port;
    EditText interface_weeman;

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
        port = (EditText) rootView.findViewById(R.id.weeman_port);
        port.setText("80");

        // Interface
        interface_weeman = (EditText) rootView.findViewById(R.id.weeman_interface);
        interface_weeman.setText("wlan1");

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
                        // Tested: Gmail, Yahoo, Reddit (not working)
                        break;
                    case 1:
                        // Facebook (works)
                        urlclone.setText("www.facebook.com");
                        actionurl.setText("www.facebook.com/login.php?login_attempt=1&lwv=110");
                        break;
                    case 2:
                        // Yahoo (not working)
                        urlclone.setText("login.yahoo.com/");
                        actionurl.setText("/");
                        break;
                    case 3:
                        // Linkedin (sorta working / ugly)
                        urlclone.setText("www.linkedin.com");
                        actionurl.setText("www.linkedin.com/uas/login-submit");
                        break;
                    case 4:
                        // Twitter (sorta working / ugly)
                        urlclone.setText("mobile.twitter.com/session/new");
                        actionurl.setText("mobile.twitter.com/sessions");
                        break;
                    case 5:
                        // Reddit (not working)
                        urlclone.setText("m.reddit.com/login");
                        actionurl.setText("m.reddit.com/login");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        // Buttons
        // Start Weeman with no Ettercap Button
        addClickListener(R.id.weemanStart, new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("thecmd", "cd /opt/weeman && python weeman.py " + getStart(rootView));
                intentClickListener_NH("cd /opt/weeman && python weeman.py " + getStart(rootView));
            }
        }, rootView);

        // Start Weeman/ Ettercap Button
        addClickListener(R.id.weemanStartARP, new View.OnClickListener() {
            public void onClick(View v) {
                EttercapStart(rootView);
            }
        }, rootView);

        // Stop Weeman/ Ettercap Button
        addClickListener(R.id.weemanStopARP, new View.OnClickListener() {
            public void onClick(View v) {
                EttercapStop();
            }
        }, rootView);
        return rootView;
    }


    private String getStart(View rootView){
        urlclone = (EditText)rootView.findViewById(R.id.weeman_urlclone);
        actionurl = (EditText)rootView.findViewById(R.id.weeman_actionurl);
        port = (EditText)rootView.findViewById(R.id.weeman_port);

        return " --port=" + port.getText() + " -s " + "--url=http://" + urlclone.getText() + " --action-url=http://" + actionurl.getText();
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
    public void EttercapStart(View rootView) {
        urlclone = (EditText)rootView.findViewById(R.id.weeman_urlclone);
        interface_weeman = (EditText)rootView.findViewById(R.id.weeman_interface);

        // Run Command
        ShellExecuter exe = new ShellExecuter();
        String[] command = new String[1];
        command[0] = nh.APP_SCRIPTS_PATH + "/weeman start " + interface_weeman + " " + urlclone ;
        exe.RunAsRoot(command);
        nh.showMessage("ARP Poisoning Stopped!");
    }

    public void EttercapStop() {
        // Run Command
        ShellExecuter exe = new ShellExecuter();
        String[] command = new String[1];
        command[0] = nh.APP_SCRIPTS_PATH + "/weeman stop";
        exe.RunAsRoot(command);
        nh.showMessage("ARP Poisoning Stopped!");
    }
}