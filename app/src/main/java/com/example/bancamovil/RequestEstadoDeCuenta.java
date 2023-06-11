package com.example.bancamovil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;

import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Movimiento;
import com.example.bancamovil.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestEstadoDeCuenta extends AsyncTask<String, Void, String> {
    private WeakReference<Context> contextRef;
    private String cuenta;
    private int idUsuario;

    public RequestEstadoDeCuenta(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        cuenta = params[0];
        idUsuario = Integer.parseInt(params[1]);

        String url = "https://bancaapi.azurewebsites.net/ApiBanca/MovimientosCuentaBancaria?cuenta=" + cuenta;

        Request request = new Request.Builder()
                .url(url)
                .get()
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
                ArrayList<Movimiento> movimientos = new ArrayList<>();
                // Procesar los movimientos
                ArrayList<Movimiento> movimientosExtras = new ArrayList<>();

                //AlertDialogHelper.showAlertDialog((Activity) context, message, statusCode == 200 ? "Success" : "Error");

                if (statusCode == 200) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray cuentaJsonArray = new JSONArray(data);
                                JSONObject cuentaJsonObject = cuentaJsonArray.getJSONObject(0);
                                JSONArray movimientosJsonArray = cuentaJsonObject.getJSONArray("Movimientos");


                                for (int i = 0; i < movimientosJsonArray.length(); i++) {
                                    JSONObject movimientoJson = movimientosJsonArray.getJSONObject(i);
                                    int idMovimiento = movimientoJson.getInt("IdMovimiento");
                                    int idCuenta = movimientoJson.getInt("IdCuenta");
                                    String descripcion = movimientoJson.getString("Descripcion");
                                    double monto = movimientoJson.getDouble("Monto");
                                    String fecha = movimientoJson.getString("FechaMovimiento");

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                    Date fechaMovimiento = dateFormat.parse(fecha);

                                    Movimiento movimiento = new Movimiento();
                                    movimiento.setIdMovimiento(idMovimiento);
                                    movimiento.setIdCuenta(idCuenta);
                                    movimiento.setDescripcion(descripcion);
                                    movimiento.setMonto(monto);
                                    movimiento.setFechaMovimiento(fechaMovimiento);

                                    movimientosExtras.add(movimiento);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }


                            String movimientosJsonString = null;
                            try {
                                movimientosJsonString = movimientosToJsonString(movimientosExtras);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            // Iniciar la tarea RequestUsuarioStatementId con la cadena JSON como parámetro
                            RequestUsuarioStatementId task = new RequestUsuarioStatementId(context);
                            task.execute(String.valueOf(idUsuario), movimientosJsonString);
                        }
                    }, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para convertir la lista de movimientos a una cadena JSON
    private String movimientosToJsonString(ArrayList<Movimiento> movimientosExtras) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Movimiento movimiento : movimientosExtras) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idMovimiento", movimiento.getIdMovimiento());
            jsonObject.put("idCuenta", movimiento.getIdCuenta());
            jsonObject.put("monto", movimiento.getMonto());
            jsonObject.put("descripcion", movimiento.getDescripcion());
            jsonObject.put("fechaMovimiento", movimiento.getFechaMovimiento());

            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }


}
