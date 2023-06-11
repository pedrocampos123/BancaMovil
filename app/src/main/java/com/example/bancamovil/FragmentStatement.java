package com.example.bancamovil;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bancamovil.Commom.Utilities;
import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Movimiento;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatement extends Fragment implements ShortsAdapter.OnTransferClickListener {

    private RecyclerView recyclerView;
    private ShortsAdapter adapter;
    private Usuario usuario;
    private ArrayList<Movimiento> movimientos;
    private TextView titleTextView;
    private Spinner spinner;

    private String CuentaOrigen = "";

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public FragmentStatement() {
        // Required empty public constructor
    }

    public static FragmentStatement newInstance(Usuario usuario, ArrayList<Movimiento> movimientosExtras) {
        FragmentStatement fragment = new FragmentStatement();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        fragment.movimientos = movimientosExtras;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement, container, false);

        LinearLayout cardContainer = view.findViewById(R.id.cardContainer);

        Bundle args = getArguments();
        if (args != null) {
            usuario = (Usuario) args.getSerializable("usuario");

            // Obtener la lista de cuentas bancarias del usuario
            List<CuentasBancaria> cuentasBancarias = usuario.getCuentasBancaria();

            // Crear la lista de elementos para el RecyclerView
            List<ShortItem> shortItemList = createSampleShortItemList(cuentasBancarias);

            // Configurar el RecyclerView y su adaptador
            MaterialCardView cardView = view.findViewById(R.id.cardView);
            //recyclerView = view.findViewById(R.id.recyclerView);
            titleTextView = view.findViewById(R.id.titleTextView);
            spinner = view.findViewById(R.id.spinner);
            TextView spinnerTitleTextView = view.findViewById(R.id.spinnerTitleTextView);

            // Establecer el título del Spinner
            spinnerTitleTextView.setText("Seleccione Cuenta");

            // Configurar el Spinner
            List<String> opciones = new ArrayList<>();
            for (ShortItem shortItem : shortItemList) {
                opciones.add(shortItem.getDescription());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_item, opciones);
            spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CuentaOrigen = parent.getItemAtPosition(position).toString();
                    // Realiza las acciones necesarias con el elemento seleccionado
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No se seleccionó ningún elemento
                }
            });
            Button submitButton = view.findViewById(R.id.submitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Realizar la transferencia
                    Utilities utilities = new Utilities(requireContext());
                    if (utilities.ConnectionTest()) {
                        // Ejecutar la llamada a la API en un hilo separado utilizando AsyncTask
                        //new APICallTask().execute(email, password);
                        RequestEstadoDeCuenta task = new RequestEstadoDeCuenta(requireContext());
                        task.execute(CuentaOrigen);
                    }

                    // Restablecer los controles a su estado original
                    //spinner.setSelection(0);
                }
            });
        }

        String texto = "  Retiro    Abono  ";
        int i = 0;
        if (movimientos != null && !movimientos.isEmpty()) {
            for (Movimiento movimiento : movimientos) {
                View cardView = inflater.inflate(R.layout.item_statement, cardContainer, false);
                TextView accountTextView = cardView.findViewById(R.id.accountTextView);
                TextView descriptionTextView = cardView.findViewById(R.id.descriptionTextView);
                TextView amountTextView = cardView.findViewById(R.id.amountTextView);

                if (i == 0) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(texto);

                    // Establecer estilo para "Retiro" (izquierda)
                    StyleSpan styleSpanLeft = new StyleSpan(Typeface.BOLD);
                    spannableStringBuilder.setSpan(styleSpanLeft, 2, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Establecer estilo para "Abono" (derecha)
                    StyleSpan styleSpanRight = new StyleSpan(Typeface.ITALIC);
                    spannableStringBuilder.setSpan(styleSpanRight, 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    accountTextView.setText(spannableStringBuilder);
                    accountTextView.setGravity(Gravity.CENTER);
                    accountTextView.setTextColor(getResources().getColor(android.R.color.black));
                    i++;
                }

                descriptionTextView.setText(movimiento.getDescripcion());
                amountTextView.setText(String.format("$%.2f", movimiento.getMonto()));

                if (movimiento.getMonto() < 0) {
                    amountTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    amountTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    descriptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    descriptionTextView.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    amountTextView.setTextColor(getResources().getColor(android.R.color.black));
                    amountTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    amountTextView.setTypeface(null, Typeface.BOLD);
                    descriptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    descriptionTextView.setTextColor(getResources().getColor(android.R.color.black));
                }

                cardContainer.addView(cardView);
            }
            i = 0;
        }




        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Realizar la transferencia
                Utilities utilities = new Utilities(requireContext());
                if (utilities.ConnectionTest()) {
                    // Ejecutar la llamada a la API en un hilo separado utilizando AsyncTask
                    //new APICallTask().execute(email, password);
                    RequestEstadoDeCuenta task = new RequestEstadoDeCuenta(requireContext());
                    task.execute(String.valueOf(CuentaOrigen));
                }

                // Restablecer los controles a su estado original
                //spinner.setSelection(0);
                //spinner2.setSelection(0);
            }
        });


        return view;
    }

    private List<ShortItem> createSampleShortItemList(List<CuentasBancaria> cuentasBancarias) {
        List<ShortItem> shortItemList = new ArrayList<>();
        for (CuentasBancaria cuenta : cuentasBancarias) {
            String title = cuenta.getDescripcion(); // Reemplaza 'getTitulo()' con el método adecuado para obtener el título de la cuenta
            String description = cuenta.getNo_Cuenta(); // Reemplaza 'getDescripcion()' con el método adecuado para obtener la descripción de la cuenta
            shortItemList.add(new ShortItem(title, description));
        }
        return shortItemList;
    }

    @Override
    public void onTransferClick(ShortItem shortItem) {
        // Implementa la lógica para manejar el evento de clic en el botón de transferencia aquí
        // Puedes acceder a los valores de CuentaOrigen, CuentaDestino y monto desde el objeto shortItem
        // y realizar las acciones necesarias, como mostrar un diálogo de confirmación o iniciar una nueva actividad.
        String s = "";
    }
}
