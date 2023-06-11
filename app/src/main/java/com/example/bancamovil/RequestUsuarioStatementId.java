package com.example.bancamovil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.bancamovil.model.CuentasBancaria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestUsuarioStatementId extends AsyncTask<String, Void, String> {
    private WeakReference<Context> contextRef;
    private int idUsuario;
    private String valorConsulta;

    public RequestUsuarioStatementId(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        idUsuario = Integer.parseInt(params[0]);
        valorConsulta = params[1];

        String url = "https://bancaapi.azurewebsites.net/ApiBanca/usuarios/" + idUsuario;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            // Ejecutar la solicitud y obtener la respuesta
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return responseBody;
            }
        } catch (IOException e) {
            e.printStackTrace();
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

                // Crear un intent para la actividad DashboardActivity
                Intent intent = new Intent(context, DashboardActivity.class);

                if (statusCode == 200) {
                    // Retrasar la ejecución del Intent durante 3 segundos (3000 milisegundos)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                                intent.putExtra("movimientos", valorConsulta);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Iniciar la actividad DashboardActivity en el contexto de MainActivity
                            context.startActivity(intent);
                        }
                    }, 0); // Retrasar la ejecución durante 3 segundos (3000 milisegundos)
                } else {
                    // Iniciar la actividad DashboardActivity en el contexto de MainActivity
                    context.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
