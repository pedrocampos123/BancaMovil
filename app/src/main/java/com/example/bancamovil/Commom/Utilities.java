package com.example.bancamovil.Commom;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bancamovil.AlertDialogHelper;
import com.example.bancamovil.MainActivity;

public class Utilities {
    private Context context;

    public Utilities(Context context) {
        this.context = context;
    }

    public boolean ConnectionTest() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (isConnected) {
            return true;
        } else {
            // No hay conexión a Internet, mostrar mensaje informativo
            AlertDialogHelper.showAlertDialog((Activity) context, "No hay conexión a Internet", "Information");
            return false;
        }
    }
}
