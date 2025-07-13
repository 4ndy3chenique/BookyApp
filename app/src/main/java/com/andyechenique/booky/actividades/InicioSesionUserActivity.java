package com.andyechenique.booky.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andyechenique.booky.R;
import com.andyechenique.booky.clases.Hash;
import com.andyechenique.booky.Modals.LoadingDialog;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class InicioSesionUserActivity extends AppCompatActivity {

    private EditText edtCorreo, edtContrasena;
    private MaterialButton btnIniciarSesion;
    private ImageView btnFlecha;

    private boolean correoValido = false;
    private boolean contrasenaValida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_user);

        edtCorreo = findViewById(R.id.edtCorreo);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnFlecha = findViewById(R.id.btnFlecha);

        configurarTextWatchers();

        btnFlecha.setOnClickListener(v -> finish());

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correo = edtCorreo.getText().toString().trim();
        String contrasena = edtContrasena.getText().toString().trim();
        String hash = new Hash().StringToHash(contrasena, "SHA-256");

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "El formato del correo es inválido. Ejemplo: usuario@correo.com", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        String url = "https://booky-web.azurewebsites.net/login.php";

        RequestParams params = new RequestParams();
        params.put("correo", correo);
        params.put("contrasena", hash);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loadingDialog.dismiss();

                if (response.optBoolean("success")) {
                    JSONObject user = response.optJSONObject("user");

                    SharedPreferences prefs = getSharedPreferences("BookyPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("sesion_activa", true)
                            .putString("correo", user.optString("Correo"))
                            .putString("rol", user.optString("Rol"))
                            .putInt("id_usuario", user.optInt("IdUsuario", -1))
                            .apply();

                    Toast.makeText(InicioSesionUserActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InicioSesionUserActivity.this, HomeActivity.class));
                    finish();
                } else {
                    String error = response.optString("message");

                    switch (error) {
                        case "Usuario no registrado":
                            Toast.makeText(InicioSesionUserActivity.this,
                                    "El correo ingresado no está registrado."
                                    , Toast.LENGTH_SHORT).show();
                            break;
                        case "Contraseña incorrecta":
                            Toast.makeText(InicioSesionUserActivity.this,
                                    "La contraseña es incorrecta. Inténtalo nuevamente."
                                    , Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(InicioSesionUserActivity.this,
                                    "Error: " + error, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                loadingDialog.dismiss();
                Toast.makeText(InicioSesionUserActivity.this, "Error de conexión o servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarTextWatchers() {
        edtCorreo.addTextChangedListener(genericWatcher(() -> {
            String texto = edtCorreo.getText().toString().trim();
            correoValido = Patterns.EMAIL_ADDRESS.matcher(texto).matches();
        }));

        edtContrasena.addTextChangedListener(genericWatcher(() -> {
            contrasenaValida = edtContrasena.getText().toString().trim().length() >= 6;
        }));
    }

    private TextWatcher genericWatcher(Runnable onChange) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                onChange.run();
                validarCampos();
            }
            @Override public void afterTextChanged(Editable s) {}
        };
    }

    private void validarCampos() {
        boolean habilitar = correoValido && contrasenaValida;

        btnIniciarSesion.setEnabled(habilitar);
        btnIniciarSesion.setAlpha(habilitar ? 1f : 0.5f);

        int color = getResources().getColor(
                habilitar ? R.color.registrarse_enabled_bg : R.color.registrarse_disabled_bg
        );
        btnIniciarSesion.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
    }
}
