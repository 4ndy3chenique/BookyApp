package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SeleccionarImagenBottomSheet extends BottomSheetDialogFragment {

    public SeleccionarImagenBottomSheet() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_seleccionar_imagen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnSeleccionarFoto).setOnClickListener(v -> {
            // lógica para seleccionar foto de galería
            dismiss();
        });

        view.findViewById(R.id.btnColorFondo).setOnClickListener(v -> {
            // lógica para abrir selector de color
            dismiss();
        });
    }
}
