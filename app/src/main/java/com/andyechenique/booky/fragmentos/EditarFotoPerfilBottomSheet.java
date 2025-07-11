package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditarFotoPerfilBottomSheet extends BottomSheetDialogFragment {

    public EditarFotoPerfilBottomSheet() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_editar_foto_perfil, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnVerFotoPerfil).setOnClickListener(v -> {
            // Lógica para ver la foto
            dismiss();
        });

        view.findViewById(R.id.btnTomarFotoPerfil).setOnClickListener(v -> {
            // Lógica para tomar una foto
            dismiss();
        });

        view.findViewById(R.id.btnSeleccionarFotoPerfil).setOnClickListener(v -> {
            // Lógica para seleccionar desde galería
            dismiss();
        });
    }
}
