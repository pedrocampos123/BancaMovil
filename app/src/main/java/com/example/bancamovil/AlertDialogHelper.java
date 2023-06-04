package com.example.bancamovil;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertDialogHelper {
    public static void showAlertDialog(Activity activity, String message, String alertType) {
        if (activity == null || activity.isFinishing()) {
            // La actividad es nula o ha sido finalizada, no se puede mostrar el diálogo
            return;
        }

        // Crear un Dialog personalizado
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_alert_dialog);

        // Obtener referencias a los elementos de la vista del Dialog
        TextView titleTextView = dialog.findViewById(R.id.dialog_title);
        TextView messageTextView = dialog.findViewById(R.id.dialog_message);
        //Button button = dialog.findViewById(R.id.dialog_button);
        ImageView alertImageView = dialog.findViewById(R.id.dialog_icon);

        int imageResId = 0;
        switch (alertType) {
            case "Error":
                imageResId = R.drawable.error;
                break;
            case "Success":
                imageResId = R.drawable.success;
                break;
            case "Information":
                imageResId = R.drawable.information;
                break;
        }

        alertImageView.setImageResource(imageResId);

        // Establecer el título y mensaje del Dialog
        titleTextView.setText(alertType);
        messageTextView.setText(message);

        // Mostrar el Dialog
        dialog.show();

        // Crear un Handler y un Runnable para cerrar el diálogo después de 2 segundos
        HandlerThread handlerThread = new HandlerThread("MiHilo");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
