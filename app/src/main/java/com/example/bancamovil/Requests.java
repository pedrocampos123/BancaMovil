package com.example.bancamovil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.example.bancamovil.model.CuentasBancaria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Requests extends AsyncTask<String, Void, String> {
    private WeakReference<Context> contextRef;
    private String email = "";
    private String password = "";

    public Requests(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        email = params[0];
        password = params[1];

        // Crear el cuerpo de la solicitud en formato JSON utilizando los valores ingresados por el usuario
        MediaType mediaType = MediaType.parse("application/json");
        String json = "{\n" +
                "    \"idUsuario\": 0,\n" +
                "    \"nombre\": \"\",\n" +
                "    \"apellido\": \"\",\n" +
                "    \"correoElectronico\": \"" + email + "\",\n" +
                "    \"contrasena\": \"" + password + "\",\n" +
                "    \"cuentasBancaria\": []\n" +
                "}";
        RequestBody requestBody = RequestBody.create(json, mediaType);

        // Crear la solicitud HTTP POST
        Request request = new Request.Builder()
                .url("https://bancaapiapp.azurewebsites.net/ApiBanca/login")
                .post(requestBody)
                .build();

        try {
            // Ejecutar la solicitud y obtener la respuesta
            Response response = client.newCall(request).execute();

            //if (response.isSuccessful()) {
            String responseBody = response.body().string();
            return responseBody;
            //}
        } catch (IOException e) {
            //Log.e(TAG, "Error in HTTP request: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Context context = contextRef.get();
        if (context != null && result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                String data = jsonObject.getString("data");
                String message = jsonObject.getString("message");

                // Mostrar la alerta utilizando la clase AlertDialogHelper
                AlertDialogHelper.showAlertDialog((Activity) context, message, statusCode == 200 ? "Success" : "Error");

                // Validar el código antes de ejecutar el Intent
                if (statusCode == 200) {
                    // Retrasar la ejecución del Intent durante 3 segundos (3000 milisegundos)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Crear un intent para la actividad DashboardActivity
                            Intent intent = new Intent(context, DashboardActivity.class);

                            // Serializar la respuesta JSON y agregarla como extra al intent
                            try {
                                JSONObject dataJson = new JSONObject(data);
                                int idUsuario = dataJson.getInt("IdUsuario");
                                String nombre = dataJson.getString("Nombre");
                                String apellido = dataJson.getString("Apellido");
                                String correoElectronico = dataJson.getString("CorreoElectronico");
                                String contrasena = dataJson.getString("Contrasena");

                                intent.putExtra("idUsuario", idUsuario);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("apellido", apellido);
                                intent.putExtra("correoElectronico", correoElectronico);
                                intent.putExtra("contrasena", contrasena);

                                JSONArray cuentasJson = dataJson.getJSONArray("CuentasBancaria");
                                ArrayList<CuentasBancaria> cuentasExtras = new ArrayList<>();

                                for (int i = 0; i < cuentasJson.length(); i++) {
                                    JSONObject cuentaJson = cuentasJson.getJSONObject(i);
                                    int idCuenta = cuentaJson.getInt("IdCuenta");
                                    int idUsuarioCuenta = cuentaJson.getInt("IdUsuario");
                                    double saldo = cuentaJson.getDouble("Saldo");
                                    String noCuenta = cuentaJson.getString("No_Cuenta");
                                    String descripcion = cuentaJson.getString("Descripcion");

                                    CuentasBancaria cuenta = new CuentasBancaria();
                                    cuenta.setIdCuenta(idCuenta);
                                    cuenta.setIdUsuario(idUsuarioCuenta);
                                    cuenta.setSaldo(saldo);
                                    cuenta.setNo_Cuenta(noCuenta);
                                    cuenta.setDescripcion(descripcion);

                                    cuentasExtras.add(cuenta);
                                }

                                intent.putExtra("cuentasExtras", cuentasExtras);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Iniciar la actividad DashboardActivity en el contexto de MainActivity
                            context.startActivity(intent);
                        }
                    }, 1000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
