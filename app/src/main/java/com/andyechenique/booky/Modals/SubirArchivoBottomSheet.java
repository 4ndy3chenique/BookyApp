package com.andyechenique.booky.Modals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubirArchivoBottomSheet extends BottomSheetDialogFragment {

    public SubirArchivoBottomSheet() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_subir_archivo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnSubirArchivo).setOnClickListener(v -> {
            // lógica para subir archivo genérico
            dismiss();
        });

        view.findViewById(R.id.btnTomarFoto).setOnClickListener(v -> {
            // lógica para tomar foto
            dismiss();
        });

        view.findViewById(R.id.btnDocumentos).setOnClickListener(v -> {
            // lógica para seleccionar documentos
            dismiss();
        });

        view.findViewById(R.id.btnHojasCalculo).setOnClickListener(v -> {
            // lógica para seleccionar hojas de cálculo
            dismiss();
        });

        view.findViewById(R.id.btnPresentaciones).setOnClickListener(v -> {
            // lógica para seleccionar presentaciones
            dismiss();
        });
    }
}
