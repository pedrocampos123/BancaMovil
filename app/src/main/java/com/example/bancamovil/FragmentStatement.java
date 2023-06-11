package com.example.bancamovil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

        //LinearLayout cardContainer = view.findViewById(R.id.cardContainer);

        String leftTexto = "Salidas";
        String rightText = "Entradas";

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
        }


        if (movimientos != null && !movimientos.isEmpty()) {
            LinearLayout cardContainer = view.findViewById(R.id.cardContainer);

            // Crear el ScrollView
            ScrollView scrollView = new ScrollView(requireContext());
            scrollView.setNestedScrollingEnabled(true);

            // Crear un contenedor para el título y el MovimientosCardView
            LinearLayout containers = new LinearLayout(requireContext());
            containers.setOrientation(LinearLayout.VERTICAL);

            // Crear el contenedor para los controles en la misma línea
            LinearLayout infoLayout = new LinearLayout(requireContext());
            infoLayout.setOrientation(LinearLayout.HORIZONTAL);

            // TextView a la izquierda
            TextView infoTextViewLeft = new TextView(requireContext());
            infoTextViewLeft.setText(leftTexto);
            infoTextViewLeft.setTextColor(getResources().getColor(android.R.color.black));
            infoTextViewLeft.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            infoTextViewLeft.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_left_negative), 10, 0, 0);
            infoTextViewLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            infoTextViewLeft.setTypeface(null, Typeface.BOLD);
            infoTextViewLeft.setBackgroundResource(R.color.white);

            // TextView a la derecha
            TextView infoTextViewRight = new TextView(requireContext());
            infoTextViewRight.setText(rightText);
            infoTextViewRight.setTextColor(getResources().getColor(android.R.color.black));
            infoTextViewRight.setGravity(Gravity.END);
            infoTextViewRight.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            infoTextViewRight.setPadding(0, 10, getResources().getDimensionPixelSize(R.dimen.padding_right_positive), 0);
            infoTextViewRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            infoTextViewRight.setTypeface(null, Typeface.BOLD);
            infoTextViewRight.setBackgroundResource(R.color.white);

            // Agregar los TextViews al contenedor
            infoLayout.addView(infoTextViewLeft);
            infoLayout.addView(infoTextViewRight);

            // Agregar el contenedor al 'containers'
            containers.addView(infoLayout);


            for (int i = 0; i < movimientos.size(); i++) {
                Movimiento movimiento = movimientos.get(i);

                // Inflar el layout de la tarjeta
                View cardView = inflater.inflate(R.layout.item_statement, containers, false);

                // Obtener referencias a los TextViews dentro de la tarjeta
                TextView descriptionTextView = cardView.findViewById(R.id.descriptionTextView);
                TextView amountTextView = cardView.findViewById(R.id.amountTextView);

                // Configurar los valores de los TextViews
                descriptionTextView.setText(movimiento.getDescripcion());
                amountTextView.setText(String.format("$%.2f", movimiento.getMonto()));

                if (movimiento.getMonto() < 0) {
                    amountTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    amountTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    amountTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_left_negative), 0, 0, 0);

                    descriptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    descriptionTextView.setTextColor(getResources().getColor(android.R.color.black));
                    descriptionTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_left_negative), 0, 0, 0);
                } else {
                    amountTextView.setTextColor(getResources().getColor(android.R.color.black));
                    amountTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    amountTextView.setTypeface(null, Typeface.BOLD);
                    amountTextView.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.padding_right_positive), 0);

                    descriptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    descriptionTextView.setTextColor(getResources().getColor(android.R.color.black));
                    descriptionTextView.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.padding_right_positive), 0);
                }

                // Remover cualquier fondo previo
                cardView.setBackgroundResource(0);

                // Aplicar border radius solo al primer y último registro
                /*if (i == 0) {
                    // Primer registro
                    cardView.setBackgroundResource(R.drawable.card_radius_none);
                    //cardView.setPadding(0, 10, getResources().getDimensionPixelSize(R.dimen.padding_right_positive), 0);


                } else if (i == movimientos.size() - 1) {
                    // Último registro
                    cardView.setBackgroundResource(R.drawable.card_radius_bottom);
                }
                else{

                }*/
                cardView.setBackgroundResource(R.drawable.card_radius_none);
                containers.addView(cardView);
            }

            // Configurar el estilo de la barra de desplazamiento
            // Crear un Drawable a partir del archivo XML
            Drawable customBackground = ContextCompat.getDrawable(requireContext(), R.drawable.custom_scroll_background);

            // Establecer el Drawable como fondo del ScrollView
            scrollView.setBackground(customBackground);

            // Configurar las propiedades del ScrollView
            scrollView.setNestedScrollingEnabled(true);
            scrollView.setVerticalScrollBarEnabled(true);
            scrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            scrollView.setScrollbarFadingEnabled(false);


            // Agregar el contenedor al ScrollView
            scrollView.addView(containers);

            // Agregar el ScrollView al contenedor principal
            cardContainer.addView(scrollView);
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
                    task.execute(String.valueOf(CuentaOrigen), String.valueOf(usuario.getIdUsuario()));
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
