package com.offsec.nethunter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.offsec.nethunter.utils.NhPaths;
import com.offsec.nethunter.utils.ShellExecuter;

//import android.app.Fragment;


public class AndroidAttackFragment extends Fragment {

    private String attackFileName;;
    private static final String ARG_SECTION_NUMBER = "section_number";
    NhPaths nh;
    ShellExecuter exe = new ShellExecuter();
    public AndroidAttackFragment() {

    }

    public static AndroidAttackFragment newInstance(int sectionNumber) {
        AndroidAttackFragment fragment = new AndroidAttackFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.androidattack, container, false);
        final Button button = (Button) rootView.findViewById(R.id.startandroidattack);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.androidattackspinner);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startattack(spinner.getSelectedItemPosition());
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        nh = new NhPaths();
    }

    public void onResume() {
        super.onResume();
    }

    public void showHIDcode(String fileName){
        nh.showMessage(fileName);
    }

    public void startattack(int attackid){
        switch (attackid){
            case 1:
                //HID ATTACK
                attackFileName = "android-unlock";
                break;
            case 2:
                //HID ATTACK
                attackFileName = "android-debugging";
                break;
            case 3:
                //ADB P2P ATTACK
                attackFileName = "android-adbp2p-install-AntiGuard";
                break;
            case 4:
                //ADB P2P ATTACK
                attackFileName = "android-adbp2p-uninstall-AntiGuard";
                break;
        }
        if(attackFileName != null && !attackFileName.isEmpty())
        {
            String[] command = new String[1];
            command[0] = "su -c '" + nh.APP_SCRIPTS_PATH + "/bootkali duck-hunt-run"+nh.APP_SCRIPTS_PATH+attackFileName+"'";
            exe.RunAsRoot(command);
            nh.showMessage("Attack Started!");
        }
    }
}