package com.smartDoor.client;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.recycler.AdapterEntries;
import com.smartDoor.shared.Entry;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RecordsFragment extends Fragment {

    public RecordsFragment() {
    }

    private AdapterEntries entriesAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toReturn = inflater.inflate(R.layout.records, container, false);

        RecyclerView rv = toReturn.findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(llm);

        Bundle args = getArguments();
        LoginInfo user = (LoginInfo) args.getSerializable("user");
        byte requestType;

        List <Entry> list;

        if (user.getisAdmin()) {
            requestType = constants.ADMIN_GET_ENTRIES;
        }
        else {
            requestType = constants.USER_GET_ENTRIES;
        }
        Client myClient = new Client( constants.ip_address,
                constants.port,
                requestType,
                user.getusername(),
                user.getpassword(),
                user.getisAdmin(),
                user.getrfidCode());
        myClient.execute();

        try {
            Result res = myClient.get();
            if (res.getSuccess()) {
                list = res.getEntriesList();
            }
            else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Erro a receber lista de entradas", Toast.LENGTH_LONG);
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

        entriesAdapter = new AdapterEntries(list);
        rv.setAdapter(entriesAdapter);
        return toReturn;
    }
}


