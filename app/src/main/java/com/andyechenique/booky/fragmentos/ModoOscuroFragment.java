package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class ModoOscuroFragment extends Fragment {

    private ImageView btnAtras, iconClaro, iconOscuro, iconSistema;

    public ModoOscuroFragment() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modo_oscuro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header y navbar
        View header = getActivity().findViewById(R.id.header);
        View navbar = getActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        btnAtras = view.findViewById(R.id.btnAtrasModoOscuro);
        iconClaro = view.findViewById(R.id.checkClaro);
        iconOscuro = view.findViewById(R.id.checkOscuro);
        iconSistema = view.findViewById(R.id.checkSistema);

        btnAtras.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        iconClaro.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            actualizarSeleccion("claro");
        });

        iconOscuro.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            actualizarSeleccion("oscuro");
        });

        iconSistema.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            actualizarSeleccion("sistema");
        });

        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            actualizarSeleccion("claro");
        } else if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            actualizarSeleccion("oscuro");
        } else {
            actualizarSeleccion("sistema");
        }
    }

    private void actualizarSeleccion(String seleccion) {
        iconClaro.setImageResource(seleccion.equals("claro") ? R.drawable.ic_radio_checked : R.drawable.ic_radio_unchecked);
        iconOscuro.setImageResource(seleccion.equals("oscuro") ? R.drawable.ic_radio_checked : R.drawable.ic_radio_unchecked);
        iconSistema.setImageResource(seleccion.equals("sistema") ? R.drawable.ic_radio_checked : R.drawable.ic_radio_unchecked);
    }

}
