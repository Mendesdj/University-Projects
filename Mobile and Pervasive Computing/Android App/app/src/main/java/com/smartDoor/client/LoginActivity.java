package com.smartDoor.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.link_signup)
    TextView linkSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    public void finish(LoginInfo user) {
        Intent data = new Intent();
        data.putExtra("UserInfo", user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @OnClick(R.id.btn_login)
    public void LoginClicked() {

        Client myClient = new Client( constants.ip_address,
                constants.port,
                constants.LOGIN_ATTEMPT,
                inputEmail.getText().toString(),
                inputPassword.getText().toString(),
                false, "");
        myClient.execute();

        try {
            Result res = myClient.get();
            if (res.getSuccess()) {
                finish(res.getLoginInfo());
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "O Login Falhou", Toast.LENGTH_LONG);
                toast.show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.link_signup)
    public void SignupClicked() {
        String uriText =
                "mailto:dj.mendes@campus.fct.unl.pt,lg.correia@campus.fct.unl.pt,rm.esteves@campus.fct.unl.pt" +
                        "?cc=" + "" +
                        "&subject=" + Uri.encode("Criação de conta para Acesso Remoto") +
                        "&body=" + Uri.encode("");

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, "Enviar email"));
    }

    @Override
    public void onBackPressed() {

    }
}
