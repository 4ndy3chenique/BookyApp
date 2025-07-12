package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class CuentaFragment extends Fragment {

    private ImageView btnAtras;
    private TextView txtGestionarCuenta, txtEliminarCuenta;
    private Switch switchCuentaPrivada;

    public CuentaFragment() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header y navbar global
        View header = requireActivity().findViewById(R.id.header);
        View navbar = requireActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        // Referencias
        btnAtras = view.findViewById(R.id.btnAtrasCuenta);
        txtGestionarCuenta = view.findViewById(R.id.opcionGestionarCuenta);
        txtEliminarCuenta = view.findViewById(R.id.opcionEliminarCuenta);
        switchCuentaPrivada = view.findViewById(R.id.switchPrivada);

        // Acciones
        btnAtras.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        txtGestionarCuenta.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Gestionar cuenta (pendiente)", Toast.LENGTH_SHORT).show()
        );

        txtEliminarCuenta.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Eliminar cuenta (pendiente)", Toast.LENGTH_SHORT).show()
        );

        switchCuentaPrivada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(requireContext(), "Cuenta " + (isChecked ? "Privada" : "Pública"), Toast.LENGTH_SHORT).show();
        });
    }

}
