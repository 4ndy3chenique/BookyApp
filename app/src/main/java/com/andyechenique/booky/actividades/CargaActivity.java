package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import com.andyechenique.booky.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carga);

        HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed(() -> {

            FirebaseUser usuarioFirebase = FirebaseAuth.getInstance().getCurrentUser();
            SharedPreferences prefs = getSharedPreferences("BookyPrefs", MODE_PRIVATE);

            boolean sesionAzure = prefs.getBoolean("sesion_activa", false);
            boolean onboardingCompleto = prefs.getBoolean("onboarding_completo", false);

            Intent intent;

            if (sesionAzure || usuarioFirebase != null) {
                // Si la Sesión activa: con Firebase o Azure
                intent = new Intent(CargaActivity.this, HomeActivity.class);

            } else if (onboardingCompleto) {
                //  Si ya vio el onboarding → mostrar solo fragmento D
                prefs.edit().putBoolean("mostrar_fragmento_d", true).apply();
                intent = new Intent(CargaActivity.this, OnboardingActivity.class);

            } else {
                // La Primera vez → mostrar onboarding completo
                intent = new Intent(CargaActivity.this, OnboardingActivity.class);
                intent.putExtra("start_fragment", 0);
            }

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }, 2000);
    }
}



