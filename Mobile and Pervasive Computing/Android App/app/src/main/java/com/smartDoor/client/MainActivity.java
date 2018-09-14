package com.smartDoor.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private final int REQUEST_CODE_LOGIN = 1;
    private LoginInfo userInfo = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragment = new OpenFragment();

                    break;
                case R.id.navigation_entrylist:

                    fragment = new RecordsFragment();
                    break;

                case R.id.navigation_management:

                    fragment = new ManagementFragment();
                    break;

                case R.id.navigation_account:
                    fragment = new MyAccountFragment();
                    break;
            }

            Bundle args = new Bundle();
            args.putSerializable("user", userInfo);
            fragment.setArguments(args);

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String sdCardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String folderPath = sdCardPath + "/SCMUServer";
        File Directory = new File(folderPath);
        Directory.mkdirs();
        String filePath = folderPath + "/ipConfig.json";
        File ipFile = new File(filePath);

        if (!ipFile.exists()){
            try {
                ipFile.createNewFile();

                try {

                    String ipAddress = constants.ip_address + ":" + constants.port ;

                    Log.d("IpAddress written File:", ipAddress);

                    FileOutputStream fis = new FileOutputStream (ipFile);

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fis);
                    outputStreamWriter.write(ipAddress);
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            int length = (int) ipFile.length();
            byte[] bytes = new byte[length];
            FileInputStream in ;

            try {
                in = new FileInputStream(ipFile);

                try {
                    in.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String contents = new String(bytes);
            String [] parts = contents.split(":");
            String ipAddress = parts[0];
            String port = parts[1];
            constants.ip_address = ipAddress;
            constants.port = Integer.parseInt(port);

        }

        if (userInfo == null) {

            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, REQUEST_CODE_LOGIN );

        }

    }

    private void reset () {

        if (!userInfo.getisAdmin()) {
            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.getMenu().removeItem(R.id.navigation_management);
        }

        fragmentManager = getSupportFragmentManager();
        fragment = new OpenFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", userInfo);
        fragment.setArguments(args);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commitAllowingStateLoss();

    }

    @Override
    protected void onActivityResult (int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK && reqCode == REQUEST_CODE_LOGIN) {
           userInfo = (LoginInfo) data.getSerializableExtra("UserInfo");

            reset();

        }
        else {
            Log.d("SOMETHING IS WRONG", "SOMETHING IS WRONG ");
        }
    }

}
