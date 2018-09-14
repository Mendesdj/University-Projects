package com.smartDoor.recycler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smartDoor.R;
import com.smartDoor.client.Client;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder> {

    private List<LoginInfo> usersList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView usernameText;
        protected ImageView userImg, deleteImg;
        public ViewHolder (View itemView) {

            super(itemView);
            usernameText = itemView.findViewById(R.id.management_username);
            userImg = itemView.findViewById(R.id.management_img);
            deleteImg = itemView.findViewById(R.id.management_remove);
        }
    }

    public AdapterUsers(List<LoginInfo> list) {
        usersList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_card_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LoginInfo login = usersList.get(position);

        if (login.getisAdmin()) {
            Glide.with(holder.userImg.getContext()).load(constants.adminImgString).into(holder.userImg);
        }
        else {
            Glide.with(holder.userImg.getContext()).load(constants.userImgString).into(holder.userImg);
        }

        holder.usernameText.setText(login.getusername());

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // Set a title for alert dialog
                builder.setTitle("Tem a certeza de que quer remover este utilizador");

                // Ask the final question
//                builder.setMessage("Want to apply big font size?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                Client myClient = new Client( constants.ip_address,
                                        constants.port,
                                        constants.REMOVE_ATTEMPT,
                                        login.getusername(),
                                        login.getpassword(),
                                        login.getisAdmin(),
                                        login.getrfidCode());
                                myClient.execute();

                                Toast toast;

                                try {
                                    Result res = myClient.get();

                                    if (res.getSuccess()) {
                                        toast = Toast.makeText(view.getContext(), "Utilizador removido", Toast.LENGTH_LONG);
                                    }
                                    else {
                                        toast = Toast.makeText(view.getContext(), "Erro na remoção do utilizador", Toast.LENGTH_LONG);
                                    }
                                    toast.show();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    toast = Toast.makeText(view.getContext(), "Erro na remoção do utilizador", Toast.LENGTH_LONG);
                                    toast.show();

                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                    toast = Toast.makeText(view.getContext(), "Erro na remoção do utilizador", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Sim", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Não",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}