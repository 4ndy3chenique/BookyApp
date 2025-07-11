package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;

public class InicioFragment extends Fragment {

    public InicioFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Asegurarse de que el header y navbar estén visibles al volver a Inicio
        View header = requireActivity().findViewById(R.id.header);
        View navbar = requireActivity().findViewById(R.id.navbar);

        if (header != null) header.setVisibility(View.VISIBLE);
        if (navbar != null) navbar.setVisibility(View.VISIBLE);
    }
}
