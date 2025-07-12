package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.R;
import com.andyechenique.booky.dialog.MenuFragment;
import com.andyechenique.booky.fragmentos.InicioFragment;
import com.andyechenique.booky.fragmentos.AmigosFragment;
import com.andyechenique.booky.fragmentos.FavoritosFragment;
import com.andyechenique.booky.fragmentos.PerfilFragment;
import com.andyechenique.booky.dialog.CrearPublicacionBottomSheet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private DrawerLayout drawerLayout;

    private ImageButton btnMenu, btnBuscar;

    private LinearLayout btnInicio, btnAmigos, btnCrear, btnFavoritos, btnPerfil;

    private ImageView iconInicio, iconAmigos, iconFavoritos, iconPerfil;
    private TextView textInicio, textAmigos, textFavoritos, textPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);

        // Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Header
        btnMenu = findViewById(R.id.btnMenu);
        btnBuscar = findViewById(R.id.btnBuscar);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btnBuscar.setOnClickListener(v -> {
            // lógica futura de búsqueda
        });

        // Navbar
        btnInicio = findViewById(R.id.btnInicio);
        btnAmigos = findViewById(R.id.btnAmigos);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnPerfil = findViewById(R.id.btnPerfil);

        iconInicio = findViewById(R.id.iconInicio);
        iconAmigos = findViewById(R.id.iconAmigos);
        iconFavoritos = findViewById(R.id.iconFavoritos);
        iconPerfil = findViewById(R.id.iconPerfil);

        textInicio = findViewById(R.id.textInicio);
        textAmigos = findViewById(R.id.textAmigos);
        textFavoritos = findViewById(R.id.textFavoritos);
        textPerfil = findViewById(R.id.textPerfil);

        // Listeners de navegación
        btnInicio.setOnClickListener(v -> {
            mostrarFragmento(new InicioFragment());
            activarBoton("inicio");
        });

        btnAmigos.setOnClickListener(v -> {
            mostrarFragmento(new AmigosFragment());
            activarBoton("amigos");
        });

        btnFavoritos.setOnClickListener(v -> {
            mostrarFragmento(new FavoritosFragment());
            activarBoton("favoritos");
        });

        btnPerfil.setOnClickListener(v -> {
            mostrarFragmento(new PerfilFragment());
            activarBoton("perfil");
        });

        ImageButton btnCrearPublicacion = findViewById(R.id.btnCrearPublicacion);
        btnCrearPublicacion.setOnClickListener(v -> {
            BottomSheetDialogFragment bottomSheet = new CrearPublicacionBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "CrearPublicacionBottomSheet");
        });


        // Menú lateral
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menuContainer, new MenuFragment())
                .commit();

        // Fragmento inicial
        mostrarFragmento(new InicioFragment());
        activarBoton("inicio");
    }

    private void mostrarFragmento(Fragment fragmento) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenido, fragmento)
                .commit();
    }

    private void activarBoton(String seccion) {
        iconInicio.setImageResource(R.drawable.ic_home_not_active);
        iconAmigos.setImageResource(R.drawable.ic_amigos_not_active);
        iconFavoritos.setImageResource(R.drawable.ic_favoritos_not_active);
        iconPerfil.setImageResource(R.drawable.ic_perfil_not_active);

        textInicio.setTextColor(getColor(R.color.gris_claro));
        textAmigos.setTextColor(getColor(R.color.gris_claro));
        textFavoritos.setTextColor(getColor(R.color.gris_claro));
        textPerfil.setTextColor(getColor(R.color.gris_claro));

        switch (seccion) {
            case "inicio":
                iconInicio.setImageResource(R.drawable.ic_home_active);
                textInicio.setTextColor(getColor(R.color.black));
                break;
            case "amigos":
                iconAmigos.setImageResource(R.drawable.ic_amigos_active);
                textAmigos.setTextColor(getColor(R.color.black));
                break;
            case "favoritos":
                iconFavoritos.setImageResource(R.drawable.ic_favoritos_active);
                textFavoritos.setTextColor(getColor(R.color.black));
                break;
            case "perfil":
                iconPerfil.setImageResource(R.drawable.ic_perfil_active);
                textPerfil.setTextColor(getColor(R.color.black));
                break;
        }
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
