package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class AmigosFragment extends Fragment {

    private TextView tabSeguidores, tabSeguidos;
    private View lineaSeguidores, lineaSeguidos;

    public AmigosFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        // Referencias a las pestañas
        tabSeguidores = view.findViewById(R.id.tabSeguidores);
        tabSeguidos = view.findViewById(R.id.tabSeguidos);

        // Referencias a las líneas de selección
        lineaSeguidores = view.findViewById(R.id.lineaSeguidores);
        lineaSeguidos = view.findViewById(R.id.lineaSeguidos);

        // Acciones de pestañas
        tabSeguidores.setOnClickListener(v -> seleccionarPestania(true));
        tabSeguidos.setOnClickListener(v -> seleccionarPestania(false));

        // Mostrar por defecto la pestaña de Seguidores
        seleccionarPestania(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header global
        View header = getActivity().findViewById(R.id.header);
        if (header != null) {
            header.setVisibility(View.GONE);
        }
    }

    private void seleccionarPestania(boolean mostrarSeguidores) {
        if (mostrarSeguidores) {
            tabSeguidores.setTextColor(getResources().getColor(R.color.black));
            tabSeguidos.setTextColor(getResources().getColor(R.color.gris_claro));
            lineaSeguidores.setVisibility(View.VISIBLE);
            lineaSeguidos.setVisibility(View.INVISIBLE);
            // Aquí puedes cargar SeguidoresFragment
        } else {
            tabSeguidores.setTextColor(getResources().getColor(R.color.gris_claro));
            tabSeguidos.setTextColor(getResources().getColor(R.color.black));
            lineaSeguidores.setVisibility(View.INVISIBLE);
            lineaSeguidos.setVisibility(View.VISIBLE);
            // Aquí puedes cargar SeguidosFragment
        }
    }
}
