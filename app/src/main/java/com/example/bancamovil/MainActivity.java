package com.example.bancamovil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bancamovil.Commom.Utilities;

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

                Utilities utilities = new Utilities(MainActivity.this);
                if (utilities.ConnectionTest()) {
                    // Ejecutar la llamada a la API en un hilo separado utilizando AsyncTask
                    //new APICallTask().execute(email, password);
                    RequestLogin task = new RequestLogin(MainActivity.this);
                    task.execute(email, password);
                }

                etEmail.setText("");
                etPassword.setText("");
            }
        });
    }

}