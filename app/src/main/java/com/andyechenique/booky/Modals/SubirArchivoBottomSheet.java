package com.andyechenique.booky.Modals;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubirArchivoBottomSheet extends BottomSheetDialogFragment {

    public interface OnArchivoSeleccionadoListener {
        void onArchivoSeleccionado(Uri uri, String nombreArchivo);
    }

    private OnArchivoSeleccionadoListener listener;
    private static final int PICK_FILE_REQUEST_CODE = 1001;

    public void setOnArchivoSeleccionadoListener(OnArchivoSeleccionadoListener listener) {
        this.listener = listener;
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

        LinearLayout btnSubirArchivo = view.findViewById(R.id.btnSubirArchivo);
        LinearLayout btnTomarFoto = view.findViewById(R.id.btnTomarFoto);
        LinearLayout btnDocumentos = view.findViewById(R.id.btnDocumentos);
        LinearLayout btnHojasCalculo = view.findViewById(R.id.btnHojasCalculo);
        LinearLayout btnPresentaciones = view.findViewById(R.id.btnPresentaciones);

        View.OnClickListener seleccionarArchivo = v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar archivo"), PICK_FILE_REQUEST_CODE);
        };

        btnSubirArchivo.setOnClickListener(seleccionarArchivo);
        btnDocumentos.setOnClickListener(seleccionarArchivo);
        btnHojasCalculo.setOnClickListener(seleccionarArchivo);
        btnPresentaciones.setOnClickListener(seleccionarArchivo);

        btnTomarFoto.setOnClickListener(v -> {
            // Implementar si necesitas tomar una foto
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (listener != null && uri != null) {
                String nombre = obtenerNombreArchivo(uri);
                listener.onArchivoSeleccionado(uri, nombre);
            }
            dismiss();
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombre = "archivo_desconocido";
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
