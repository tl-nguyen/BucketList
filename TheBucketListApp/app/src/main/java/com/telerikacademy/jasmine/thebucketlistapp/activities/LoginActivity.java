package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.tasks.LoginRequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.tasks.RegisterRequestResultCallBackAction;

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private ProgressDialog progressDialog;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        RemoteDbManager.getInstance().setEverlive(getString(R.string.backendServicesApiKey));

        this.progressDialog = new ProgressDialog(this);

        this.username = (EditText) findViewById(R.id.etUsername);
        this.password = (EditText) findViewById(R.id.etPassword);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);

        this.btnLogin.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);

        this.username.setText(getResources().getString(R.string.defaultUsername));
        this.password.setText(getResources().getString(R.string.defaultPassword));
    }

    @Override
    protected void onResume() {
        super.onResume();

        final User loggedUser = LoggedUser.getInstance().getLoggedUser();

        if (loggedUser != null) {
            LoginActivity.startMainActivity(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin : {
                this.login();
                break;
            }
            case R.id.btnRegister :  {
                this.register();
                break;
            }
        }
    }

    public static void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showAlert(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_LONG).show();
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private void register() {
        String userName = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (userName.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, R.string.emptyUserNameAndPassword, Toast.LENGTH_SHORT).show();

            return;
        }

        this.progressDialog.setMessage(this.getResources().getString(R.string.dialogRegister));
        this.progressDialog.show();

        final User user = new User();
        user.setUsername(userName);
        user.setDisplayName(userName);
        UserSecretInfo secretInfo = new UserSecretInfo();
        secretInfo.setPassword(password);

        RemoteDbManager.getInstance().register(user,
                secretInfo,
                new RegisterRequestResultCallBackAction(this, this.progressDialog));
    }

    private void login() {
        this.progressDialog.setMessage(this.getResources().getString(R.string.dialogLogin));
        this.progressDialog.show();

        String userName = this.username.getText().toString();
        String password = this.password.getText().toString();

        RemoteDbManager.getInstance().login(userName,
                password,
                new LoginRequestResultCallbackAction(this, this.progressDialog));
    }
}
