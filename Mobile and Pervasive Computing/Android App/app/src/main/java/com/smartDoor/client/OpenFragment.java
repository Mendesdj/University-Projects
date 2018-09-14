package com.smartDoor.client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.concurrent.ExecutionException;

public class OpenFragment extends Fragment {

    private LoginInfo user;

    public OpenFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.open_view, container, false);

        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.fab);

        Bundle args = getArguments();
        user = (LoginInfo) args.getSerializable("user");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client myClient = new Client( constants.ip_address,
                        constants.port,
                        constants.DOOR_OPEN,
                        user.getusername(), user.getpassword(),
                        user.getisAdmin(), user.getrfidCode());
                myClient.execute();


                Toast toast;
                try {
                    Result res = myClient.get();


                    if (res.getSuccess()) {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Porta Aberta", Toast.LENGTH_LONG);
                    }
                    else {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Falha na Abertura", Toast.LENGTH_LONG);

                    }

                    toast.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Falha na Abertura", Toast.LENGTH_LONG);
                    toast.show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Falha na Abertura", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        return myFragmentView;
    }

}
