package com.example.bancamovil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView nameTextView;
    private TextView lastNameTextView;
    private TextView welcomeTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Obtener el objeto Usuario del Bundle
        Bundle args = getArguments();
        if (args != null) {
            Usuario usuario = (Usuario) args.getSerializable("usuario");
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
        }

        return view;
    }


}
