package com.andyechenique.booky.fragmentos;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;
import com.andyechenique.booky.actividades.MainActivity;
import com.andyechenique.booky.actividades.RegistroUserActivity;
import com.andyechenique.booky.dialog.InicioSesionBottomSheet;

public class OnboardingFragmentD extends Fragment {

    public OnboardingFragmentD() {
        // Constructor público requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_d, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.btnAcceder).setOnClickListener(v -> {
            InicioSesionBottomSheet bottomSheet = InicioSesionBottomSheet.newInstance();
            bottomSheet.show(getParentFragmentManager(), "InicioSesionBottomSheet");
            SharedPreferences prefs = requireActivity().getSharedPreferences("BookyPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_completo", true).apply();

        });

        // Registrarse
        view.findViewById(R.id.btnRegistroClickable).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistroUserActivity.class);
            startActivity(intent);
            SharedPreferences prefs = requireActivity().getSharedPreferences("BookyPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_completo", true).apply();

        });


        // Omitir
        view.findViewById(R.id.btnOmitir).setOnClickListener(v -> {
            // Solo va a MainActivity, sin guardar estado de completado
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
            SharedPreferences prefs = requireActivity().getSharedPreferences("BookyPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_completo", true).apply();

        });

    }
}
