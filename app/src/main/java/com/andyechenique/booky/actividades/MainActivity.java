package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.andyechenique.booky.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnUniversidad, btnEstudiante, btnContinuar;
    private ImageView btnFlecha;
    private TextView btnOmitir;

    private String opcionSeleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnUniversidad = findViewById(R.id.btnUniversidad);
        btnEstudiante = findViewById(R.id.btnEstudiante);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnFlecha = findViewById(R.id.btnFlecha);
        btnOmitir = findViewById(R.id.btnOmitir);

        // Estado inicial
        btnContinuar.setEnabled(false);
        btnContinuar.setAlpha(0.5f);
        btnContinuar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.continuar_disabled_bg));
        btnContinuar.setTextColor(ContextCompat.getColor(this, R.color.txtcontinuar));

        btnUniversidad.setOnClickListener(v -> seleccionarOpcion("universidad"));
        btnEstudiante.setOnClickListener(v -> seleccionarOpcion("estudiante"));

        // Continuar → guarda flag y va a HomeActivity
        btnContinuar.setOnClickListener(v -> {
            if (opcionSeleccionada != null) {
                SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                prefs.edit().putBoolean("onboarding_completo", true).apply();

                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });

        // Flecha → regresa al fragmento D del onboarding
        btnFlecha.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.putExtra("start_fragment", 3);
            startActivity(intent);
            finish();
        });

        // Omitir → guarda flag y va a HomeActivity
        btnOmitir.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_completo", true).apply();

            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void seleccionarOpcion(String opcion) {
        opcionSeleccionada = opcion;

        btnContinuar.setEnabled(true);
        btnContinuar.setAlpha(1f);
        btnContinuar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.continuar_enabled_bg));

        btnUniversidad.setSelected("universidad".equals(opcion));
        btnEstudiante.setSelected("estudiante".equals(opcion));
    }
}
