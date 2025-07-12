package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;
import com.andyechenique.booky.Modals.SeleccionarImagenBottomSheet;
import com.andyechenique.booky.Modals.SubirArchivoBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CrearPublicacionFragment extends Fragment {

    private EditText inputTitulo, inputDescripcion;
    private TextView textAdjunto, textPortada;
    private Button btnSubir;

    public CrearPublicacionFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_publicacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header y navbar global
        View header = getActivity().findViewById(R.id.header);
        View navbar = getActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        // Referencias de vistas
        inputTitulo = view.findViewById(R.id.inputTitulo);
        inputDescripcion = view.findViewById(R.id.inputDescripcion);
        textAdjunto = view.findViewById(R.id.textAdjunto);
        textPortada = view.findViewById(R.id.textPortada);
        btnSubir = view.findViewById(R.id.btnSubir);

        // Botón flecha para retroceder
        ImageView btnFlecha = view.findViewById(R.id.btnFlecha);
        btnFlecha.setOnClickListener(v -> {
            // Restaurar visibilidad
            if (header != null) header.setVisibility(View.VISIBLE);
            if (navbar != null) navbar.setVisibility(View.VISIBLE);

            // Volver atrás
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Validación de campos
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarCampos();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        inputTitulo.addTextChangedListener(watcher);
        inputDescripcion.addTextChangedListener(watcher);

        btnSubir.setOnClickListener(v -> {
            // Lógica para subir publicación
        });

        // Mostrar BottomSheet para subir archivo
        view.findViewById(R.id.btnAdjuntarArchivo).setOnClickListener(v -> {
            BottomSheetDialogFragment bottomSheet = new SubirArchivoBottomSheet();
            bottomSheet.show(getParentFragmentManager(), "SubirArchivoBottomSheet");
        });

        // Mostrar BottomSheet para seleccionar imagen
        view.findViewById(R.id.btnSeleccionarImagen).setOnClickListener(v -> {
            BottomSheetDialogFragment bottomSheet = new SeleccionarImagenBottomSheet();
            bottomSheet.show(getParentFragmentManager(), "SeleccionarImagenBottomSheet");
        });
    }


    private void validarCampos() {
        boolean valido = !inputTitulo.getText().toString().trim().isEmpty()
                && !inputDescripcion.getText().toString().trim().isEmpty();
        btnSubir.setEnabled(valido);
        btnSubir.setBackgroundResource(valido ? R.drawable.boton_principal : R.drawable.boton_subir_desactivado);
    }
}
