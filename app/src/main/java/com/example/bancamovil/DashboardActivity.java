package com.example.bancamovil;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bancamovil.databinding.ActivityDashboardBinding;
import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recuperar los valores enviados por el Intent
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", 0);
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        String correoElectronico = intent.getStringExtra("correoElectronico");
        String contrasena = intent.getStringExtra("contrasena");

        // Recuperar las cuentas bancarias enviadas en forma de lista de putExtra
        ArrayList<CuentasBancaria> cuentasExtras = (ArrayList<CuentasBancaria>) intent.getSerializableExtra("cuentasExtras");

        usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreoElectronico(correoElectronico);
        usuario.setContrasena(contrasena);
        usuario.setCuentasBancaria(cuentasExtras);

        // Asignar el objeto Usuario a los fragmentos que lo necesiten
        HomeFragment homeFragment = HomeFragment.newInstance(usuario);
        FragmentTransferencias transferenciasFragment = FragmentTransferencias.newInstance(usuario);
        FragmentStatement estadocuentaFragment = new FragmentStatement();
        FragmentDeposito depositoFragment = new FragmentDeposito();
        FragmentRetiro retiroFragment = new FragmentRetiro();

        replaceFragment(homeFragment);

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(homeFragment);
                    break;
                case R.id.transfer:
                    replaceFragment(transferenciasFragment);
                    break;
                case R.id.deposito:
                    replaceFragment(depositoFragment);
                    break;
                case R.id.retiro:
                    replaceFragment(retiroFragment);
                    break;
                case R.id.estadocuenta:
                    replaceFragment(estadocuentaFragment);
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
