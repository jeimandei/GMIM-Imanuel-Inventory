package com.example.gmimimanuelinventory.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.gmimimanuelinventory.Configuration.LoginConfig;
import com.example.gmimimanuelinventory.HTTPHandler;
import com.example.gmimimanuelinventory.MainActivity;
import com.example.gmimimanuelinventory.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText username, password;
    boolean isAllFieldsChecked = false;
    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;
    Switch showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btnLogin);
        showPass = findViewById(R.id.showPass);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPass.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
            }
        });
    }

    private boolean CheckAllFields() {
        if (username.length() == 0) {
            username.setError("This field is required");
            return false;
        }else if (password.length() == 0){
            password.setError("This field is required");
            return false;
        } else{
            validasi();
        }
        return true;
    }

    private void validasi() {
        getJsonData();
    }

    private void getJsonData() {

        final String id = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        class GetJson extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,
                        "Getting Data", "Please Wait",
                        false, false);
            }
            @Override
            protected String doInBackground(Void... voids) {

                HTTPHandler handler = new HTTPHandler();
                String result = handler.sendGetResp2(LoginConfig.URL_GET_LOGIN, id, pass );
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(LoginConfig.TAG_JSON_ARRAY_LOGIN);
                    JSONObject object = result.getJSONObject(0);
                    String code = object.getString("code");
                    if (code.equals("login_false")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this,R.style.AlertDialogTheme);
                        alertDialogBuilder.setMessage("Wrong ID or password"
                        );
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        displayDetailLogin(s);
                        clearText();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }

        }
        GetJson getJson= new GetJson();
        getJson.execute();

    }

    private void clearText() {
        username.setText("");
        password.setText("");
    }

    private void displayDetailLogin(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(LoginConfig.TAG_JSON_ARRAY_LOGIN);
            JSONObject object = result.getJSONObject(0);

            String username = object.getString(LoginConfig.TAG_LOGIN_USER_NAME);
            String komisi = object.getString(LoginConfig.TAG_LOGIN_USER_KOMISI);


            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", username);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class );
            intent.putExtra(LoginConfig.TAG_LOGIN_USER_NAME, username);
            intent.putExtra(LoginConfig.TAG_LOGIN_USER_KOMISI, komisi);
            startActivity(intent);

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}