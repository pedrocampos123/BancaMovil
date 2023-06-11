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

    public RequestEstadoDeCuenta(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        cuenta = params[0];

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

                // Crear un intent para la actividad DashboardActivity
                Intent intent = new Intent(context, DashboardActivity.class);

                if (statusCode == 200) {
                    // Retrasar la ejecución del Intent durante 3 segundos (3000 milisegundos)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Serializar la respuesta JSON y agregarla como extra al intent
                            try {
                                // Crear un objeto JSON a partir de la respuesta
                                JSONArray cuentaJsonArray = new JSONArray(data);
                                CuentasBancaria cuentasExtras = new CuentasBancaria();

                                if (cuentaJsonArray.length() > 0) {
                                    JSONObject cuentaJsonObject = cuentaJsonArray.getJSONObject(0);

                                    // Obtener los valores para CuentasBancaria
                                    int idCuenta = cuentaJsonObject.getInt("IdCuenta");
                                    int idUsuarioCuenta = cuentaJsonObject.getInt("IdUsuario");
                                    double saldo = cuentaJsonObject.getDouble("Saldo");
                                    String noCuenta = cuentaJsonObject.getString("No_Cuenta");
                                    String descripcion = cuentaJsonObject.getString("Descripcion");

                                    // Crear una instancia de CuentasBancaria
                                    CuentasBancaria cuentasBancaria = new CuentasBancaria();
                                    cuentasBancaria.setIdCuenta(idCuenta);
                                    cuentasBancaria.setIdUsuario(idUsuarioCuenta);
                                    cuentasBancaria.setSaldo(saldo);
                                    cuentasBancaria.setNo_Cuenta(noCuenta);
                                    cuentasBancaria.setDescripcion(descripcion);

                                    // Obtener los valores para Usuario
                                    JSONObject usuarioObj = cuentaJsonObject.getJSONObject("IdUsuarioNavigation");
                                    int idUsuario = usuarioObj.getInt("IdUsuario");
                                    String nombre = usuarioObj.getString("Nombre");
                                    String apellido = usuarioObj.getString("Apellido");
                                    String correoElectronico = usuarioObj.getString("CorreoElectronico");
                                    String contrasena = usuarioObj.getString("Contrasena");

                                    Usuario usuario = new Usuario();
                                    usuario.setIdUsuario(idUsuario);
                                    usuario.setNombre(nombre);
                                    usuario.setApellido(apellido);
                                    usuario.setCorreoElectronico(correoElectronico);
                                    usuario.setContrasena(contrasena);
                                    //usuario.getCuentasBancaria().add(cuentasBancaria);

                                    // Obtener el primer objeto de la cuenta (asumimos que solo hay una cuenta)
                                    JSONObject cuentaJson = cuentaJsonArray.getJSONObject(0);

                                    // Obtener el saldo de la cuenta
                                    //double saldo = cuentaJsonArray.getDouble("Saldo");

                                    // Obtener el arreglo de movimientos
                                    JSONArray movimientosJsonArray = cuentaJson.getJSONArray("Movimientos");

                                    // Crear una lista para almacenar los movimientos
                                    ArrayList<Movimiento> movimientos = new ArrayList<>();

                                    // Iterar sobre los movimientos y crear los objetos Movimiento correspondientes
                                    for (int i = 0; i < movimientosJsonArray.length(); i++) {
                                        JSONObject movimientoJson = movimientosJsonArray.getJSONObject(i);

                                        int idMovimiento = movimientoJson.getInt("IdMovimiento");
                                        int idCuenta2 = movimientoJson.getInt("IdCuenta");
                                        int idTipoMovimiento = movimientoJson.getInt("IdTipoMovimiento");
                                        String fechaMovimientoStr = movimientoJson.getString("FechaMovimiento");
                                        double monto = movimientoJson.getDouble("Monto");
                                        String descripcion2 = movimientoJson.getString("Descripcion");
                                        String cuentaOrigen = movimientoJson.getString("CuentaOrigen");
                                        String cuentaDestino = movimientoJson.getString("CuentaDestino");

                                        // Convertir la fecha de movimiento a objeto Date
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
                                        Date fechaMovimiento = dateFormat.parse(fechaMovimientoStr);

                                        // Crear un objeto Movimiento
                                        Movimiento movimiento = new Movimiento(idMovimiento, idCuenta2, idTipoMovimiento, fechaMovimiento, monto, descripcion2, cuentaOrigen, cuentaDestino, null, null);

                                        // Agregar el movimiento a la lista
                                        movimientos.add(movimiento);
                                    }

                                    // Agregar las listas de movimientos y cuentas bancarias como extras al intent
                                    intent.putExtra("movimientosExtras", movimientos);
                                    intent.putExtra("Cuenta", cuentasBancaria);
                                    intent.putExtra("usuario", usuario);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            // Iniciar la actividad DashboardActivity en el contexto de MainActivity
                            context.startActivity(intent);
                        }
                    }, 0);
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
