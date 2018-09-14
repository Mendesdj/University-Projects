package com.smartDoor.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smartDoor.R;

import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;

public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
    }
    private final int REQUEST_CODE_LOGIN = 1;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View toReturn = inflater.inflate(R.layout.my_account, container, false);
        Bundle args = getArguments();
        LoginInfo user = (LoginInfo) args.getSerializable("user");

        ImageView userImg = toReturn.findViewById(R.id.myAccount_image);
        TextView userName = toReturn.findViewById(R.id.myAccount_username);
        TextView rfidCode = toReturn.findViewById(R.id.myAccount_rfidCode);
        Button logout = toReturn.findViewById(R.id.button_logout);

        if (user.getisAdmin()) {
            Glide.with(getActivity().getApplicationContext()).load(constants.adminImgString).into(userImg);
        }
        else {
            Glide.with(getActivity().getApplicationContext()).load(constants.userImgString).into(userImg);
        }

        userName.setText(user.getusername());
        rfidCode.setText(user.getrfidCode());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(login, REQUEST_CODE_LOGIN );
            }
        });



        return toReturn;
    }
}