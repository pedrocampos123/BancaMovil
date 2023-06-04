package com.example.bancamovil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancamovil.model.CuentasBancaria;
import com.example.bancamovil.model.Usuario;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

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

            // Obtener el contenedor para los cardViews
            LinearLayout cardContainer = view.findViewById(R.id.cardContainer);

            // Recorrer la lista de cuentas bancarias y crear los elementos
            for (int i = 0; i < cuentasBancarias.size(); i++) {
                CuentasBancaria cuenta = cuentasBancarias.get(i);
                boolean isLastItem = (i == cuentasBancarias.size() - 1);

                // Crear un nuevo MaterialCardView
                MaterialCardView accountCardView = new MaterialCardView(getActivity());
                // Establecer los atributos del cardView (e.g., color de fondo, elevación, etc.)
                // ...

                // Crear un nuevo LinearLayout para los elementos internos del cardView
                accountCardView.setCardBackgroundColor(Color.WHITE);
                LinearLayout innerLayout = new LinearLayout(getActivity());
                innerLayout.setOrientation(LinearLayout.VERTICAL);
                // Establecer los atributos del LinearLayout (e.g., padding, margen, etc.)
                innerLayout.setBackgroundColor(Color.WHITE);

                // Crear los TextViews para descripción, número de cuenta y saldo
                TextView descriptionTextView = new TextView(getActivity());
                TextView accountNumberTextView = new TextView(getActivity());
                TextView amountTextView = new TextView(getActivity());

                // Establecer los valores de los TextViews según la cuenta bancaria actual
                descriptionTextView.setText(cuenta.getDescripcion());
                descriptionTextView.setTextColor(Color.BLACK);
                descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size));

                accountNumberTextView.setText(cuenta.getNo_Cuenta());
                accountNumberTextView.setTextColor(Color.BLACK);
                accountNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size));

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                numberFormat.setCurrency(Currency.getInstance("USD"));
                String formattedAmount = numberFormat.format(cuenta.getSaldo());
                amountTextView.setText(formattedAmount);
                amountTextView.setTextColor(Color.BLACK);
                amountTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size));

                // Añadir los TextViews al LinearLayout interno
                innerLayout.addView(descriptionTextView);
                innerLayout.addView(accountNumberTextView);
                innerLayout.addView(amountTextView);

                // Establecer el margen inferior del LinearLayout (excepto para el último elemento)
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Obtener el color de @Color/colorCard
                int colorBlack = ContextCompat.getColor(getActivity(), R.color.colorCard);

                // Establecer el color de fondo de innerLayout a negro
                innerLayout.setBackgroundColor(colorBlack);

                // Aplicar el padding izquierdo al LinearLayout interno
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.padding_left);
                innerLayout.setPadding(paddingLeft, 0, 0, 0);

                // Crear un ShapeAppearanceModel con el border radius en las cuatro esquinas
                float cornerRadius = getResources().getDimension(R.dimen.card_corner_radius);
                ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                        .toBuilder()
                        .setAllCorners(CornerFamily.ROUNDED, cornerRadius)
                        .build();

                // Aplicar el ShapeAppearanceModel al MaterialCardView y al LinearLayout interno
                accountCardView.setShapeAppearanceModel(shapeAppearanceModel);

                // Aplicar el magen al final de cada elemento
                int marginBottom = getResources().getDimensionPixelSize(R.dimen.margin_bottom);
                if (!isLastItem) {
                    layoutParams.setMargins(0, 0, 0, marginBottom);
                }
                accountCardView.setLayoutParams(layoutParams);

                // Añadir el LinearLayout interno al MaterialCardView
                accountCardView.addView(innerLayout);

                // Añadir el MaterialCardView al contenedor
                cardContainer.addView(accountCardView);
            }


        }

        return view;
    }
}
