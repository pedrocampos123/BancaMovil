package com.example.bancamovil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShortsAdapter adapter;
    private Usuario usuario;

    private TextView nameTextView;
    private TextView lastNameTextView;
    private TextView welcomeTextView;

    private static final String ARG_USUARIO = "usuario";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Usuario usuario) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(ARG_USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (usuario != null) {
            // Acceder a los atributos del objeto Usuario
            int idUsuario = usuario.getIdUsuario();
            String nombre = usuario.getNombre();
            String apellido = usuario.getApellido();
            String correoElectronico = usuario.getCorreoElectronico();
            String contrasena = usuario.getContrasena();
            List<CuentasBancaria> cuentasBancarias = usuario.getCuentasBancaria();

            // Realizar las operaciones necesarias con los datos del usuario
            // ...

            // Actualizar la interfaz de usuario según los datos del usuario
            MaterialCardView cardView = view.findViewById(R.id.cardView);
            TextView nameTextView = view.findViewById(R.id.nameTextView);
            TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);

            nameTextView.setText(nombre);
            lastNameTextView.setText(apellido);
        }

        return view;
    }
}
