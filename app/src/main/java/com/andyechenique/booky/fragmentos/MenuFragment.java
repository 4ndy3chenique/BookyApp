package com.andyechenique.booky.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;
import com.andyechenique.booky.actividades.HomeActivity;


public class MenuFragment extends Fragment {

    private LinearLayout opcionInicio, opcionResenas, opcionVerificacion, opcionConfiguracion, opcionCerrarSesion;
    private TextView txtNombre, txtCorreo;

    public MenuFragment() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Vistas
        opcionInicio = view.findViewById(R.id.opcionInicio);
        opcionResenas = view.findViewById(R.id.opcionResenas);
        opcionVerificacion = view.findViewById(R.id.opcionVerificacion);
        opcionConfiguracion = view.findViewById(R.id.opcionConfiguracion);
        opcionCerrarSesion = view.findViewById(R.id.opcionCerrarSesion);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtCorreo = view.findViewById(R.id.txtCorreo);

        // Datos simulados (puedes obtenerlos desde Firebase o SharedPreferences)
        txtNombre.setText("Andy Echenique");
        txtCorreo.setText("andy974275@gmail.com");

        // Acciones
        opcionInicio.setOnClickListener(v -> {
            // Acción para volver al inicio
        });

        opcionResenas.setOnClickListener(v -> {
            // Acción para ir a reseñas
        });

        opcionVerificacion.setOnClickListener(v -> {
            // Acción para solicitar verificación
        });

        opcionConfiguracion.setOnClickListener(v -> {
        });

        opcionCerrarSesion.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).cerrarSesion();
            }
        });

        return view;
    }
}
