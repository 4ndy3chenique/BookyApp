package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.andyechenique.booky.R;
import com.andyechenique.booky.fragmentos.MenuFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;

    // Drawer
    private DrawerLayout drawerLayout;

    // Header
    private ImageButton btnMenu, btnBuscar;

    // Navbar
    private LinearLayout btnInicio, btnAmigos, btnCrear, btnFavoritos, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);

        // Google Sign-In config
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

        // Inicializar botones del Header
        btnMenu = findViewById(R.id.btnMenu);
        btnBuscar = findViewById(R.id.btnBuscar);

        btnMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        btnBuscar.setOnClickListener(v -> {
            // Acción para búsqueda
        });

        // Inicializar botones del Navbar
        btnInicio = findViewById(R.id.btnInicio);
        btnAmigos = findViewById(R.id.btnAmigos);
        btnCrear = findViewById(R.id.btnCrear);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(v -> {
            // Mostrar fragmento de inicio
        });

        btnAmigos.setOnClickListener(v -> {
            // Mostrar sección de amigos
        });

        btnCrear.setOnClickListener(v -> {
            //startActivity(new Intent(this, CrearPublicacionActivity.class));
        });

        btnFavoritos.setOnClickListener(v -> {
            // Mostrar favoritos
        });

        btnPerfil.setOnClickListener(v -> {
            // Mostrar perfil o ir a PerfilActivity
        });

        // Cargar el fragmento del menú una vez
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuContainer, new MenuFragment())
                .commit();
    }

    public void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            SharedPreferences prefs = getSharedPreferences("BookyPrefs", MODE_PRIVATE);
            prefs.edit()
                    .remove("sesion_activa")
                    .remove("correo_usuario")
                    .putBoolean("mostrar_fragmento_d", true)
                    .apply();

            Intent intent = new Intent(this, OnboardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
