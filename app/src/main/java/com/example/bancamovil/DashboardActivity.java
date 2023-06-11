package com.example.bancamovil;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bancamovil.databinding.ActivityDashboardBinding;
import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Movimiento;
import com.example.bancamovil.model.Usuario;

import java.util.ArrayList;
import java.util.List;

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

        // Recuperar las cuentas bancarias enviadas en forma de lista de putExtra
        ArrayList<Movimiento> movimientosExtras = (ArrayList<Movimiento>) intent.getSerializableExtra("movimientosExtras");
        Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");
        int i = 0;

        if (movimientosExtras == null) {
            // Obtener los valores de usuario
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
        } else {
            //CuentasBancaria cuentasBancaria = (CuentasBancaria) intent.getSerializableExtra("Cuenta");
            List<CuentasBancaria> cuentasList = new ArrayList<>();
            if (intent.hasExtra("Cuenta")) {
                CuentasBancaria cuentasBancaria = (CuentasBancaria) intent.getSerializableExtra("Cuenta");
                cuentasList.add(cuentasBancaria);
                usuario.setCuentasBancaria(cuentasList);
            } else {
                usuario.setCuentasBancaria(new ArrayList<>());
            }

            i++;
        }

        // Asignar el objeto Usuario a los fragmentos que lo necesiten
        HomeFragment homeFragment = HomeFragment.newInstance(usuario);
        FragmentTransferencias transferenciasFragment = FragmentTransferencias.newInstance(usuario);
        FragmentDeposito depositoFragment = FragmentDeposito.newInstance(usuario);
        FragmentRetiro retiroFragment = FragmentRetiro.newInstance(usuario);
        FragmentStatement estadocuentaFragment = FragmentStatement.newInstance(usuario, movimientosExtras);

        if (i > 0) {
            replaceFragment(estadocuentaFragment);
            binding.bottomNavigationView.getMenu().findItem(R.id.estadocuenta).setChecked(true);
            i = 0;
        } else {
            replaceFragment(homeFragment);
            binding.bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        }

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
