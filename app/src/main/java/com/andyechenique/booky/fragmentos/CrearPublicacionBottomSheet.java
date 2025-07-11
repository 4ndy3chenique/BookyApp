package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CrearPublicacionBottomSheet extends BottomSheetDialogFragment {

    public CrearPublicacionBottomSheet() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_crear_publicacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnCrearCarpeta = view.findViewById(R.id.btnCrearCarpeta);
        View btnCrearPublicacion = view.findViewById(R.id.btnCrearPublicacion);

        btnCrearCarpeta.setOnClickListener(v -> {
            // lógica para crear carpeta
            dismiss();
        });

        btnCrearPublicacion.setOnClickListener(v -> {
            // lógica para crear publicación
            dismiss();
        });
    }
}
