package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PerfilFragment extends Fragment {

    private ImageButton btnEditarPerfil, btnEditarFondo;
    private LinearLayout btnSeguidores;

    // Iconos
    private ImageView iconPublicaciones, iconListas, iconFavoritos, iconInfo;
    // Líneas
    private View lineaPublicaciones, lineaListas, lineaFavoritos, lineaInfo;

    // Pestañas
    private FrameLayout tabPublicaciones, tabListas, tabFavoritos, tabInfo;

    public PerfilFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar header global
        View header = getActivity().findViewById(R.id.header);
        if (header != null) {
            header.setVisibility(View.GONE);
        }

        // Botones de edición y seguidores
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnEditarFondo = view.findViewById(R.id.btnEditarFondo);
        btnSeguidores = view.findViewById(R.id.btnSeguidores);

        btnEditarPerfil.setOnClickListener(v -> {
            BottomSheetDialogFragment dialog = new EditarFotoPerfilBottomSheet();
            dialog.show(getParentFragmentManager(), "BottomSheetEditarFotoPerfil");
        });

        // Mostrar BottomSheet para editar fondo
        btnEditarFondo.setOnClickListener(v -> {
            BottomSheetDialogFragment dialog = new EditarFondoBottomSheet();
            dialog.show(getParentFragmentManager(), "BottomSheetEditarFondo");
        });


        btnSeguidores.setOnClickListener(v -> {
            // Ir al fragmento de amigos
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.contenido, new AmigosFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Referencias a iconos
        iconPublicaciones = view.findViewById(R.id.iconPublicaciones);
        iconListas = view.findViewById(R.id.iconListas);
        iconFavoritos = view.findViewById(R.id.iconFavoritos);
        iconInfo = view.findViewById(R.id.iconInfo);

        // Líneas activas
        lineaPublicaciones = view.findViewById(R.id.lineaPublicaciones);
        lineaListas = view.findViewById(R.id.lineaListas);
        lineaFavoritos = view.findViewById(R.id.lineaFavoritos);
        lineaInfo = view.findViewById(R.id.lineaInfo);

        // Contenedores de pestañas
        tabPublicaciones = view.findViewById(R.id.tabPublicaciones);
        tabListas = view.findViewById(R.id.tabListas);
        tabFavoritos = view.findViewById(R.id.tabFavoritos);
        tabInfo = view.findViewById(R.id.tabInfo);

        // Acciones
        tabPublicaciones.setOnClickListener(v -> seleccionarTab(0));
        tabListas.setOnClickListener(v -> seleccionarTab(1));
        tabFavoritos.setOnClickListener(v -> seleccionarTab(2));
        tabInfo.setOnClickListener(v -> seleccionarTab(3));

        // Pestaña por defecto
        seleccionarTab(0);
    }

    private void seleccionarTab(int index) {
        // Resetear todos
        iconPublicaciones.setColorFilter(getResources().getColor(R.color.gris_claro));
        iconListas.setColorFilter(getResources().getColor(R.color.gris_claro));
        iconFavoritos.setColorFilter(getResources().getColor(R.color.gris_claro));
        iconInfo.setColorFilter(getResources().getColor(R.color.gris_claro));

        lineaPublicaciones.setVisibility(View.GONE);
        lineaListas.setVisibility(View.GONE);
        lineaFavoritos.setVisibility(View.GONE);
        lineaInfo.setVisibility(View.GONE);

        // Activar pestaña correspondiente
        switch (index) {
            case 0:
                iconPublicaciones.setColorFilter(getResources().getColor(R.color.black));
                lineaPublicaciones.setVisibility(View.VISIBLE);
                break;
            case 1:
                iconListas.setColorFilter(getResources().getColor(R.color.black));
                lineaListas.setVisibility(View.VISIBLE);
                break;
            case 2:
                iconFavoritos.setColorFilter(getResources().getColor(R.color.black));
                lineaFavoritos.setVisibility(View.VISIBLE);
                break;
            case 3:
                iconInfo.setColorFilter(getResources().getColor(R.color.black));
                lineaInfo.setVisibility(View.VISIBLE);
                break;
        }
    }
}
