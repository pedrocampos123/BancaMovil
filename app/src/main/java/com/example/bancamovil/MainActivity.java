package com.example.bancamovil;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener referencias a los campos de nombre de usuario y contraseña
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (ConnectionTest()) {
                    // Ejecutar la llamada a la API en un hilo separado utilizando AsyncTask
                    //new APICallTask().execute(email, password);
                    Requests task = new Requests(MainActivity.this);
                    task.execute(email, password);
                }
                etEmail.setText("");
                etPassword.setText("");
            }
        });
    }

    private Boolean ConnectionTest(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (isConnected) {
            return true;
        } else {
            // No hay conexión a Internet, mostrar mensaje informativo
            AlertDialogHelper.showAlertDialog(MainActivity.this, "No hay conexión a Internet", "Information");
            return false;
        }
    }

}