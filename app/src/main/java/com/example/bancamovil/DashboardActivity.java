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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        //ArrayList<Movimiento> movimientosExtras = (ArrayList<Movimiento>) intent.getSerializableExtra("movimientosExtras");
        Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");
        ArrayList<CuentasBancaria> cuentasExtras = (ArrayList<CuentasBancaria>) intent.getSerializableExtra("cuentasExtras");
        String movimientosJsonString = getIntent().getStringExtra("movimientos");
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        int j = 0;

            // Obtener los valores de usuario
            int idUsuario = intent.getIntExtra("idUsuario", 0);
            String nombre = intent.getStringExtra("nombre");
            String apellido = intent.getStringExtra("apellido");
            String correoElectronico = intent.getStringExtra("correoElectronico");
            String contrasena = intent.getStringExtra("contrasena");

            // Recuperar las cuentas bancarias enviadas en forma de lista de putExtra
            //ArrayList<CuentasBancaria> cuentasExtras = (ArrayList<CuentasBancaria>) intent.getSerializableExtra("cuentasExtras");

            usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setContrasena(contrasena);
            usuario.setCuentasBancaria(cuentasExtras);

        if (movimientosJsonString != null) {
            try {
                // Realizar la deserialización del JSON
                JSONArray movimientosJsonArray = new JSONArray(movimientosJsonString);

                // Iterar sobre los objetos JSON de los movimientos
                for (int i = 0; i < movimientosJsonArray.length(); i++) {
                    JSONObject movimientoJson = movimientosJsonArray.getJSONObject(i);

                    // Obtener los valores del objeto JSON
                    int idMovimiento = movimientoJson.getInt("idMovimiento");
                    int idCuenta = movimientoJson.getInt("idCuenta");
                    double monto = movimientoJson.getDouble("monto");
                    String descripcion = movimientoJson.getString("descripcion");
                    String fecha = movimientoJson.getString("fechaMovimiento");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    Date fechaMovimiento = dateFormat.parse(fecha);

                    // Crear el objeto Movimiento y asignar los valores
                    Movimiento movimiento = new Movimiento();
                    movimiento.setIdMovimiento(idMovimiento);
                    movimiento.setIdCuenta(idCuenta);
                    movimiento.setMonto(monto);
                    movimiento.setDescripcion(descripcion);
                    movimiento.setFechaMovimiento(fechaMovimiento);

                    // Agregar el objeto Movimiento al ArrayList
                    movimientos.add(movimiento);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            j++;
        }

        // Asignar el objeto Usuario a los fragmentos que lo necesiten
        HomeFragment homeFragment = HomeFragment.newInstance(usuario);
        FragmentTransferencias transferenciasFragment = FragmentTransferencias.newInstance(usuario);
        FragmentDeposito depositoFragment = FragmentDeposito.newInstance(usuario);
        FragmentRetiro retiroFragment = FragmentRetiro.newInstance(usuario);
        FragmentStatement estadocuentaFragment = FragmentStatement.newInstance(usuario, movimientos);

        movimientos = null;

        if (j > 0) {
            replaceFragment(estadocuentaFragment);
            binding.bottomNavigationView.getMenu().findItem(R.id.estadocuenta).setChecked(true);
            j = 0;
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
