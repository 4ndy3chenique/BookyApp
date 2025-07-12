package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class NivelCuentaFragment extends Fragment {

    private ImageView btnAtras;
    private TextView estadoVerificacion;
    private ImageView iconoVerificacion;

    // Simulación de estado verificado (esto se debe obtener desde la base de datos)
    private boolean estaVerificado = false;

    public NivelCuentaFragment() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nivel_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header y navbar global
        View header = getActivity().findViewById(R.id.header);
        View navbar = getActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        btnAtras = view.findViewById(R.id.btnAtrasNivel);
        estadoVerificacion = view.findViewById(R.id.Verificado);
        iconoVerificacion = view.findViewById(R.id.iconoVerificado);

        btnAtras.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        actualizarEstadoVerificacion();
    }

    private void actualizarEstadoVerificacion() {
        if (estaVerificado) {
            estadoVerificacion.setText("Cuenta Verificada");
            estadoVerificacion.setTextColor(getResources().getColor(R.color.black));
            iconoVerificacion.setVisibility(View.VISIBLE);
        } else {
            estadoVerificacion.setText("No Verificado");
            estadoVerificacion.setTextColor(getResources().getColor(R.color.black));
            iconoVerificacion.setVisibility(View.GONE);
        }
    }


}
