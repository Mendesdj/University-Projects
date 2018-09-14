package com.smartDoor.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.recycler.AdapterUsers;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManagementFragment extends Fragment {


    public ManagementFragment() {
    }

    private AdapterUsers usersAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View toReturn = inflater.inflate(R.layout.management, container, false);

        RecyclerView rv = toReturn.findViewById(R.id.management_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(llm);

        Bundle args = getArguments();
        final LoginInfo user = (LoginInfo) args.getSerializable("user");
        byte requestType;

        List<LoginInfo> list;

        Client myClient = new Client( constants.ip_address,
                constants.port,
                constants.ADMIN_GET_USERS,
                user.getusername(),
                user.getpassword(),
                user.getisAdmin(),
                user.getrfidCode());
        myClient.execute();

        try {
            Result res = myClient.get();
            if (res.getSuccess()) {
                list = res.getEntriesUser();
            }
            else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Error a receber lista de Utilizadores", Toast.LENGTH_LONG);
                toast.show();
                list = new ArrayList<>();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        } catch (ExecutionException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }

        usersAdapter = new AdapterUsers(list);
        rv.setAdapter(usersAdapter);

        Button addUser = toReturn.findViewById(R.id.button_add_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new AddUserFragment();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                fragment.setArguments(args);
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();

            }
        });

        return toReturn;

    }
}
