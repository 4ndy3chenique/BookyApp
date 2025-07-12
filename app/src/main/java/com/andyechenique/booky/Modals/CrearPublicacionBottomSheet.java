package com.andyechenique.booky.Modals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.andyechenique.booky.fragmentos.CrearPublicacionFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CrearPublicacionBottomSheet extends BottomSheetDialogFragment {

    public CrearPublicacionBottomSheet() {
        // Constructor vacío requerido
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
            // Aquí luego lanzarás la lógica real
            dismiss();
        });

        btnCrearPublicacion.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenido, new CrearPublicacionFragment())
                        .addToBackStack(null)
                        .commit();
                dismiss();
            }
        });

    }
}
