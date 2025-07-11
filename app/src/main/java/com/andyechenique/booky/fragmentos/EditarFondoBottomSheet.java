package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditarFondoBottomSheet extends BottomSheetDialogFragment {

    public EditarFondoBottomSheet() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_editar_fondo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los botones
        view.findViewById(R.id.btnVerFondo).setOnClickListener(v -> {
            // Lógica para ver el fondo actual
            dismiss();
        });

        view.findViewById(R.id.btnTomarFotoFondo).setOnClickListener(v -> {
            // Lógica para abrir cámara
            dismiss();
        });

        view.findViewById(R.id.btnSeleccionarFotoFondo).setOnClickListener(v -> {
            // Lógica para elegir de galería
            dismiss();
        });

        view.findViewById(R.id.btnElegirColorFondo).setOnClickListener(v -> {
            // Lógica para abrir selector de color
            dismiss();
        });
    }
}
