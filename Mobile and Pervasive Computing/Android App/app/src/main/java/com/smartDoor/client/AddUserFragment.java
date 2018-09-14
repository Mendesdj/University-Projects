package com.smartDoor.client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.concurrent.ExecutionException;

public class AddUserFragment extends Fragment {

    public AddUserFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.add_user_view, container, false);

        Bundle args = getArguments();
        final LoginInfo user = (LoginInfo) args.getSerializable("user");

        FloatingActionButton add_person = myFragmentView.findViewById(R.id.add_person);

        ImageView imageView = myFragmentView.findViewById(R.id.person_image);

        Glide.with(this).load(constants.addUserString).into(imageView);

        final EditText username = myFragmentView.findViewById(R.id.input_username);
        final EditText password = myFragmentView.findViewById(R.id.input_password);
        final CheckBox isAdmin = myFragmentView.findViewById(R.id.isAdmin);
        final MaskedEditText rfidCode = myFragmentView.findViewById(R.id.masked_edit_text);

        add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client myClient = new Client( constants.ip_address,
                        constants.port,
                        constants.REGISTER_ATTEMPT,
                        username.getText().toString(),
                        password.getText().toString(),
                        isAdmin.isChecked(),
                        rfidCode.getText().toString());
                myClient.execute();

                Toast toast;
                try {
                    Result res = myClient.get();

                    if (res.getSuccess()) {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Utilizador Registado", Toast.LENGTH_LONG);
                        toast.show();

                        Fragment fragment = new ManagementFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("user", user);
                        fragment.setArguments(args);
                        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();


                    }
                    else {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Falha ao registar utilizador", Toast.LENGTH_LONG);
                        toast.show();

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Falha ao registar utilizador", Toast.LENGTH_LONG);
                    toast.show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Falha ao registar utilizador", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return myFragmentView;

    }

}

