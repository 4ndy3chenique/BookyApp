package com.andyechenique.booky.Modals;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SeleccionarImagenBottomSheet extends BottomSheetDialogFragment {

    public interface OnImagenSeleccionadaListener {
        void onImagenSeleccionada(Uri uri, String nombreArchivo);
        // Si luego necesitas color: void onColorSeleccionado(String hexColor);
    }

    private OnImagenSeleccionadaListener listener;
    private static final int PICK_IMAGE_REQUEST_CODE = 2001;

    public void setOnImagenSeleccionadaListener(OnImagenSeleccionadaListener listener) {
        this.listener = listener;
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

        LinearLayout btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);
        LinearLayout btnColorFondo = view.findViewById(R.id.btnColorFondo);

        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST_CODE);
        });

        btnColorFondo.setOnClickListener(v -> {
            // Aquí podrías usar un color fijo o abrir un selector personalizado
            // Ejemplo:
            // if (listener != null) listener.onColorSeleccionado("#FF4081");
            dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (listener != null && uri != null) {
                String nombre = obtenerNombreArchivo(uri);
                listener.onImagenSeleccionada(uri, nombre);
            }
            dismiss();
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombre = "imagen.jpg";
        Cursor cursor = null;
        try {
            cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index >= 0) {
                    nombre = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return nombre;
    }
}
