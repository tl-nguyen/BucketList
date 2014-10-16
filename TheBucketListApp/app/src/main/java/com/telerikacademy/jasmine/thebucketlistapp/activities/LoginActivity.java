package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.models.SQLiteUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.LoginSettingsManager;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.SQLiteDBPref;
import com.telerikacademy.jasmine.thebucketlistapp.tasks.LoginRequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.tasks.RegisterRequestResultCallBackAction;
import com.telerikacademy.jasmine.thebucketlistapp.utils.DeviceStatusManager;

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private ProgressDialog progressDialog;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox rememberMe;

    private SQLiteDBPref sqliteDbPref;
    private DeviceStatusManager statusManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        RemoteDbManager.getInstance().setEverlive(getString(R.string.backendServicesApiKey));
        LoginSettingsManager.getInstance().setSharedPreferences(getSharedPreferences(getString(R.string.sharedPreferencesName), 0));

        sqliteDbPref = new SQLiteDBPref(this.getApplicationContext());
        statusManager = new DeviceStatusManager(this.getApplicationContext());

        this.progressDialog = new ProgressDialog(this);

        this.username = (EditText) findViewById(R.id.etUsername);
        this.password = (EditText) findViewById(R.id.etPassword);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.rememberMe = (CheckBox) findViewById(R.id.cbRememberMe);

        this.btnLogin.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
    }

    private void autoLoginHandle() {
        if (LoginSettingsManager.getInstance().getRememberMe()) {
            String username = LoginSettingsManager.getInstance().getCurrentUser();

            SQLiteUser user = sqliteDbPref.findUser(username);

            login(user.getUsername(), user.getPassword());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean isConnectedToInternet = statusManager.isInternetConnected();
        if (!isConnectedToInternet) {
            showAlert(this, "You're not connected to the internet.\n" +
                    "Bucket List needs an internet connection.\n\n" + "" +
                    "Please, connect to the internet first");
        }
        else {
            final User loggedUser = LoggedUser.getInstance().getLoggedUser();

            if (loggedUser != null) {
                LoginActivity.startMainActivity(this);
            } else {
                autoLoginHandle();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin : {
                String userName = this.username.getText().toString();
                String password = this.password.getText().toString();

                LoginSettingsManager.
                        getInstance().
                        putSettings(this.rememberMe.isChecked(), userName);

                if (!sqliteDbPref.userExist(userName)) {
                    sqliteDbPref.addRecord(userName, password);
                } else {
                    sqliteDbPref.updateRecord(userName, password);
                }

                this.login(userName, password);
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

    private void login(String userName, String password) {
        this.progressDialog.setMessage(this.getResources().getString(R.string.dialogLogin));
        this.progressDialog.show();

        RemoteDbManager.getInstance().login(userName,
                password,
                new LoginRequestResultCallbackAction(this, this.progressDialog));
    }
}
