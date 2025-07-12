package com.andyechenique.booky.Modals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;
import com.andyechenique.booky.actividades.HomeActivity;
import com.andyechenique.booky.fragmentos.ConfiguracionFragment;
import com.andyechenique.booky.fragmentos.InicioFragment;
import com.andyechenique.booky.fragmentos.ResenasFragment;
import com.andyechenique.booky.fragmentos.VerificacionFragment;

public class MenuFragment extends Fragment {

    private LinearLayout opcionInicio, opcionResenas, opcionVerificacion, opcionConfiguracion, opcionCerrarSesion;
    private TextView txtNombre, txtCorreo;

    public MenuFragment() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Vistas
        opcionInicio = view.findViewById(R.id.opcionInicio);
        opcionResenas = view.findViewById(R.id.opcionResenas);
        opcionVerificacion = view.findViewById(R.id.opcionVerificacion);
        opcionConfiguracion = view.findViewById(R.id.opcionConfiguracion);
        opcionCerrarSesion = view.findViewById(R.id.opcionCerrarSesion);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtCorreo = view.findViewById(R.id.txtCorreo);

        // Datos simulados (puedes reemplazar por valores reales)
        txtNombre.setText("Andy Echenique");
        txtCorreo.setText("andy974275@gmail.com");

        // Acciones
        opcionInicio.setOnClickListener(v -> {
            cerrarDrawer();
            mostrarFragmento(new InicioFragment());
        });

        opcionResenas.setOnClickListener(v -> {
            cerrarDrawer();
            mostrarFragmento(new ResenasFragment());
        });

        opcionVerificacion.setOnClickListener(v -> {
            cerrarDrawer();
            mostrarFragmento(new VerificacionFragment());
        });

        opcionConfiguracion.setOnClickListener(v -> {
            cerrarDrawer();
            mostrarFragmento(new ConfiguracionFragment());
        });

        opcionCerrarSesion.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                LogoutDialog logoutDialog = new LogoutDialog(requireContext());
                logoutDialog.show();


                v.postDelayed(() -> {
                    ((HomeActivity) getActivity()).cerrarSesion();
                    logoutDialog.dismiss();
                }, 1500);
            }
        });



        return view;
    }

    private void cerrarDrawer() {
        if (getActivity() instanceof HomeActivity) {
            DrawerLayout drawerLayout = ((HomeActivity) getActivity()).findViewById(R.id.drawerLayout);
            if (drawerLayout != null) {
                drawerLayout.closeDrawers();
            }
        }
    }

    private void mostrarFragmento(Fragment fragmento) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenido, fragmento)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
