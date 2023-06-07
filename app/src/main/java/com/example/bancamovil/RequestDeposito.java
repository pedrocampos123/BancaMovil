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

public class RequestDeposito extends AsyncTask<String, Void, String> {
    private WeakReference<Context> contextRef;
    private int idUsuario;
    //private String cuentaOrigen;
    private String cuentaDestino;
    private double monto;
    private String descripcion;

    public RequestDeposito(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        idUsuario = Integer.parseInt(params[0]);
        //cuentaOrigen = params[1];
        cuentaDestino = params[1];
        monto = Double.parseDouble(params[2]);
        descripcion = params[3];

        String url = "https://bancaapiapp.azurewebsites.net/ApiBanca/Deposito?idUsuario=" + idUsuario +
                "&CuentaDestino=" + cuentaDestino +
                "&monto=" + monto +
                "&descripcion=" + descripcion;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
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

                AlertDialogHelper.showAlertDialog((Activity) context, message, statusCode == 200 ? "Success" : "Error");

                if (statusCode == 200) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Intent intent = new Intent(context, DashboardActivity.class);

                            RequestUsuarioId task = new RequestUsuarioId(context);
                            task.execute(String.valueOf(idUsuario));

                            //context.startActivity(intent);
                        }
                    }, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
