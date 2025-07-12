package com.andyechenique.booky.fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class ConfiguracionFragment extends Fragment {

    private Switch switchSeguidores, switchComentarios;
    private TextView configurarNotificaciones, opcionCuenta, opcionNivel, opcionModoOscuro;
    private TextView opcionReportar, opcionCentroAyuda, opcionCondiciones, opcionPrivacidad;
    private View btnAtras;

    public ConfiguracionFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header y navbar global
        View header = getActivity().findViewById(R.id.header);
        View navbar = getActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        // Referencias
        switchSeguidores = view.findViewById(R.id.switchSeguidores);
        switchComentarios = view.findViewById(R.id.switchComentarios);

        configurarNotificaciones = view.findViewById(R.id.configurarNotificaciones);
        opcionCuenta = view.findViewById(R.id.opcionCuenta);
        opcionNivel = view.findViewById(R.id.opcionNivel);
        opcionModoOscuro = view.findViewById(R.id.opcionModoOscuro);

        opcionReportar = view.findViewById(R.id.opcionReportar);
        opcionCentroAyuda = view.findViewById(R.id.opcionCentroAyuda);
        opcionCondiciones = view.findViewById(R.id.opcionCondiciones);
        opcionPrivacidad = view.findViewById(R.id.opcionPrivacidad);

        btnAtras = view.findViewById(R.id.btnAtras);

        // Acciones
        configurarNotificaciones.setOnClickListener(v -> abrirConfiguracionNotificaciones());

        opcionReportar.setOnClickListener(v -> abrirEnlace("https://booky.com/reportar"));
        opcionCentroAyuda.setOnClickListener(v -> abrirEnlace("https://booky.com/ayuda"));
        opcionCondiciones.setOnClickListener(v -> abrirEnlace("https://booky.com/condiciones"));
        opcionPrivacidad.setOnClickListener(v -> abrirEnlace("https://booky.com/privacidad"));

        // Navegación a subfragments
        opcionCuenta.setOnClickListener(v -> navegarA(new CuentaFragment()));
        opcionNivel.setOnClickListener(v -> navegarA(new NivelCuentaFragment()));
        opcionModoOscuro.setOnClickListener(v -> navegarA(new ModoOscuroFragment()));

        btnAtras.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void abrirConfiguracionNotificaciones() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
        startActivity(intent);
    }

    private void abrirEnlace(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void navegarA(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment)
                .addToBackStack(null)
                .commit();
    }
}
