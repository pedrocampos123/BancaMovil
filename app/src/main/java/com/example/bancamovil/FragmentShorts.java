package com.example.bancamovil;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bancamovil.R;
import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class FragmentShorts extends Fragment implements ShortsAdapter.OnTransferClickListener {

    private RecyclerView recyclerView;
    private ShortsAdapter adapter;
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public FragmentShorts() {
        // Required empty public constructor
    }

    public static FragmentShorts newInstance(Usuario usuario) {
        FragmentShorts fragment = new FragmentShorts();
        Bundle args = new Bundle();
        args.putSerializable("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shorts, container, false);

        // Obtener el objeto Usuario del Bundle en el DashboardActivity
        Bundle args = getArguments();
        if (args != null) {
            usuario = (Usuario) args.getSerializable("usuario");

            // Obtener la lista de cuentas bancarias del usuario
            List<CuentasBancaria> cuentasBancarias = usuario.getCuentasBancaria();

            // Crear la lista de elementos para el RecyclerView
            List<ShortItem> shortItemList = createSampleShortItemList(cuentasBancarias);

            // Configurar el RecyclerView y su adaptador
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ShortsAdapter(shortItemList, this); // Pasar la instancia del Fragment como OnTransferClickListener
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    private List<ShortItem> createSampleShortItemList(List<CuentasBancaria> cuentasBancarias) {
        List<ShortItem> shortItemList = new ArrayList<>();
        for (CuentasBancaria cuenta : cuentasBancarias) {
            String title = cuenta.getNo_Cuenta(); // Reemplaza 'getTitulo()' con el método adecuado para obtener el título de la cuenta
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
    }
}
