package com.example.bancamovil;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bancamovil.Commom.Utilities;
import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTransferencias extends Fragment implements ShortsAdapter.OnTransferClickListener {

    private RecyclerView recyclerView;
    private ShortsAdapter adapter;
    private Usuario usuario;
    private TextView titleTextView;
    private EditText amountEditText;
    private Spinner spinner;
    private Spinner spinner2;

    private String CuentaOrigen = "";
    private String CuentaDestino = "";
    private String Amount = "";

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public FragmentTransferencias() {
        // Required empty public constructor
    }

    public static FragmentTransferencias newInstance(Usuario usuario) {
        FragmentTransferencias fragment = new FragmentTransferencias();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transferencias, container, false);

        // Obtener el objeto Usuario del Bundle en el DashboardActivity
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
            amountEditText = view.findViewById(R.id.amountEditText);
            spinner = view.findViewById(R.id.spinner);
            TextView spinnerTitleTextView = view.findViewById(R.id.spinnerTitleTextView);
            spinner2 = view.findViewById(R.id.spinner2);
            TextView spinnerTitleTextView2 = view.findViewById(R.id.spinnerTitleTextView2);

            // Establecer el título del Spinner
            spinnerTitleTextView.setText("Seleccione Cuenta Origen");

            /*recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ShortsAdapter(shortItemList, this); // Pasar la instancia del Fragment como OnTransferClickListener
            recyclerView.setAdapter(adapter);*/

            // Configurar el Spinner
            List<String> opciones = new ArrayList<>();
            for (ShortItem shortItem : shortItemList) {
                opciones.add(shortItem.getDescription());
            }
            List<String> opciones2 = new ArrayList<>();
            for (ShortItem shortItem : shortItemList) {
                opciones2.add(shortItem.getDescription());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_item, opciones);
            spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_item, opciones);
            spinnerAdapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            spinner2.setAdapter(spinnerAdapter2);

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

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CuentaDestino = parent.getItemAtPosition(position).toString();
                    // Realiza las acciones necesarias con el elemento seleccionado
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No se seleccionó ningún elemento
                }
            });
        }

        Button transferButton = view.findViewById(R.id.transferButton);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el monto ingresado
                Amount = amountEditText.getText().toString();

                // Validar el monto
                if (Amount.isEmpty() || Double.parseDouble(Amount) <= 0) {
                    // Mostrar advertencia: monto inválido
                    AlertDialogHelper.showAlertDialog(getActivity(), "Ingrese un monto válido", "Error");
                    return;
                }

                // Validar cuentas seleccionadas
                if (CuentaOrigen.equals(CuentaDestino)) {
                    // Mostrar advertencia: cuentas iguales
                    AlertDialogHelper.showAlertDialog(getActivity(), "Las cuentas seleccionadas son iguales", "Error");
                    return;
                }

                // Realizar la transferencia
                Utilities utilities = new Utilities(requireContext());
                if (utilities.ConnectionTest()) {
                    // Ejecutar la llamada a la API en un hilo separado utilizando AsyncTask
                    //new APICallTask().execute(email, password);
                    RequestTransfer task = new RequestTransfer(requireContext());
                    task.execute(String.valueOf(usuario.getIdUsuario()), CuentaOrigen, CuentaDestino, Amount);
                }

                // Restablecer los controles a su estado original
                amountEditText.setText("");
                spinner.setSelection(0);
                spinner2.setSelection(0);
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
        String s= "";
    }
}
