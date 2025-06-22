package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.andyechenique.booky.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Configurar GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Ajustes visuales
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Acción del botón "Cerrar sesión"
        findViewById(R.id.cardSalir).setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {

        // Cierra sesión de Firebase
        FirebaseAuth.getInstance().signOut();

        // Cierra sesión de Google
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Editar solo lo necesario en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("BookyPrefs", MODE_PRIVATE);
            prefs.edit()
                    .remove("sesion_activa")
                    .remove("correo_usuario")
                    .putBoolean("mostrar_fragmento_d", true)
                    .commit();

            // Redirige a OnboardingActivity
            Intent intent = new Intent(this, OnboardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
