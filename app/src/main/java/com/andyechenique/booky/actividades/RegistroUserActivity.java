package com.andyechenique.booky.actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.andyechenique.booky.R;
import com.andyechenique.booky.clases.Hash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import cz.msebera.android.httpclient.Header;

public class RegistroUserActivity extends AppCompatActivity {
    private Spinner spinnerGenero;
    private ImageView btnFlecha;
    private EditText edtFecha, edtNombre, edtUsername, edtCorreo, edtTelefono;
    private TextInputEditText edtContrasena, edtConfirmarContrasena;
    private MaterialCheckBox chkTerminos;
    private MaterialButton btnContinuar;

    private boolean generoValido = false;
    private boolean fechaValida = false;
    private boolean nombreValido = false;
    private boolean usuarioValido = false;
    private boolean correoValido = false;
    private boolean telefonoValido = false;
    private boolean contrasenaValida = false;
    private boolean terminosMostrados = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnFlecha = findViewById(R.id.btnFlecha);
        edtFecha = findViewById(R.id.edtFecha);
        edtNombre = findViewById(R.id.edtNombre);
        edtUsername = findViewById(R.id.edtUsername);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtContrasena = findViewById(R.id.edtContrasena);
        edtConfirmarContrasena = findViewById(R.id.edtConfirmarContrasena);
        chkTerminos = findViewById(R.id.chkTerminos);
        btnContinuar = findViewById(R.id.btnRegistrarse);

        configurarSpinnerGenero();
        seleccionarFecha();
        configurarTextWatchers();

        chkTerminos.setOnClickListener(v -> {
            if (!terminosMostrados) {
                mostrarTerminos();
            } else {
                chkTerminos.setChecked(!chkTerminos.isChecked()); // Permite alternar el check
                validarCampos();
            }
        });


        btnFlecha.setOnClickListener(v -> {
            Intent intent = new Intent(this, OnboardingActivity.class);
            intent.putExtra("start_fragment", 3);
            startActivity(intent);
            finish();
        });

        btnContinuar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        if (!validarFormulario()) return;

        String url = "https://booky-web.azurewebsites.net/registro.php";
        String contrasena = edtContrasena.getText().toString().trim();
        String hash = new Hash().StringToHash(contrasena, "SHA-256");

        RequestParams params = new RequestParams();
        params.put("nombre_completo", edtNombre.getText().toString().trim());
        params.put("nombre_usuario", edtUsername.getText().toString().trim());
        params.put("telefono", edtTelefono.getText().toString().trim());
        params.put("fecha_nacimiento", convertirFecha(edtFecha.getText().toString().trim()));
        params.put("correo", edtCorreo.getText().toString().trim());
        params.put("contrasena", hash);
        params.put("genero", spinnerGenero.getSelectedItem().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.optBoolean("success")) {
                    Toast.makeText(RegistroUserActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroUserActivity.this, OnboardingActivity.class);
                    intent.putExtra("start_fragment", 3);
                    startActivity(intent);
                    finish();
                } else {
                    String mensaje = response.optString("message");
                    mostrarAlertaDuplicado(mensaje);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String mensaje = (errorResponse != null)
                        ? errorResponse.optString("message", "Error desconocido")
                        : "No se pudo conectar con el servidor";
                mostrarAlertaDuplicado(mensaje);
            }

        });
    }

    private void mostrarAlertaDuplicado(String mensaje) {
        String detalle;

        if (mensaje.contains("correo")) {
            detalle = "El correo electrónico ingresado ya está registrado. Por favor, usa uno diferente.";
        } else if (mensaje.contains("usuario")) {
            detalle = "El nombre de usuario ya está en uso. Intenta con otro nombre único.";
        } else if (mensaje.contains("teléfono") || mensaje.contains("telefono")) {
            detalle = "El número de teléfono ya está registrado en otra cuenta.";
        } else {
            detalle = mensaje;
        }
        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show();
    }


    private String convertirFecha(String fechaOriginal) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
            return formatoSalida.format(formatoEntrada.parse(fechaOriginal));
        } catch (Exception e) {
            return "";
        }
    }

    private void configurarSpinnerGenero() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.generos_array)) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };

        spinnerGenero.setAdapter(adapter);
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generoValido = position != 0;
                validarCampos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                generoValido = false;
                validarCampos();
            }
        });
    }

    private void seleccionarFecha() {
        edtFecha.setOnClickListener(v -> {
            final Calendar hoy = Calendar.getInstance();
            final Calendar fechaMinima = Calendar.getInstance();

            // Fecha mínima: hace 70 años desde hoy
            fechaMinima.set(hoy.get(Calendar.YEAR) - 70, hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

            // Fecha máxima: hace 10 años desde hoy
            Calendar fechaMaxima = Calendar.getInstance();
            fechaMaxima.set(hoy.get(Calendar.YEAR) - 10, hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

            // Si ya se ha seleccionado una fecha
            Calendar fechaSeleccionada = Calendar.getInstance();
            if (!edtFecha.getText().toString().isEmpty()) {
                try {
                    String[] partes = edtFecha.getText().toString().split("/");
                    fechaSeleccionada.set(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]) - 1, Integer.parseInt(partes[0]));
                } catch (Exception ignored) {
                    fechaSeleccionada.set(
                            fechaMaxima.get(Calendar.YEAR),
                            fechaMaxima.get(Calendar.MONTH),
                            fechaMaxima.get(Calendar.DAY_OF_MONTH)
                    );

                }
            } else {
                fechaSeleccionada.set(
                        fechaMaxima.get(Calendar.YEAR),
                        fechaMaxima.get(Calendar.MONTH),
                        fechaMaxima.get(Calendar.DAY_OF_MONTH)
                );

            }

            int dia = fechaSeleccionada.get(Calendar.DAY_OF_MONTH);
            int mes = fechaSeleccionada.get(Calendar.MONTH);
            int anio = fechaSeleccionada.get(Calendar.YEAR);

            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                edtFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                fechaValida = true;
                validarCampos();
            }, anio, mes, dia);

            // Establecer fechas mínima y máxima permitidas
            dpd.getDatePicker().setMaxDate(fechaMaxima.getTimeInMillis());
            dpd.getDatePicker().setMinDate(fechaMinima.getTimeInMillis());

            // Mostrar y personalizar botones
            dpd.setOnShowListener(dialog -> {
                int colorBoton = getResources().getColor(R.color.colorBotonDialogo, null);
                dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(colorBoton);
                dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(colorBoton);
            });

            dpd.show();
        });

        edtFecha.setKeyListener(null);
    }

    private boolean validarFormulario() {
        // Obtener los valores de los campos
        String nombre = edtNombre.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String fecha = edtFecha.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String contrasena = edtContrasena.getText().toString().trim();
        String confirmarContrasena = edtConfirmarContrasena.getText().toString().trim();

        // Validar campos vacíos
        if (nombre.isEmpty() || username.isEmpty() || telefono.isEmpty() ||
                fecha.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar selección de género
        if (spinnerGenero.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe elegir un género", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar formato y longitud de correo
        if (correo.length() > 50 || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo válido con máximo 50 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar número de teléfono
        if (!telefono.matches("^9\\d{8}$")) {
            Toast.makeText(this, "El teléfono debe tener 9 dígitos y comenzar con 9", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar nombre de usuario (mínimo 4 y máximo 20 caracteres)
        if (username.length() < 4 || username.length() > 20) {
            Toast.makeText(this, "El nombre de usuario debe tener entre 4 y 20 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar contraseña (mínimo 8, máximo 20, debe tener mayúscula, minúscula y número)
        if (contrasena.length() < 8 || contrasena.length() > 20 ||
                !contrasena.matches(".*[A-Z].*") ||
                !contrasena.matches(".*[a-z].*") ||
                !contrasena.matches(".*\\d.*")) {
            Toast.makeText(this, "La contraseña debe tener entre 8 y 20 caracteres, \n "+"incluir mayúsculas, minúsculas y números", Toast.LENGTH_LONG).show();
            return false;
        }

        // Validar coincidencia de contraseñas
        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar aceptación de términos
        if (!chkTerminos.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void mostrarTerminos() {
        AlertDialog.Builder terminos = new AlertDialog.Builder(this);
        terminos.setTitle("Términos y condiciones de Booky");
        terminos.setMessage(
                        "1. Aceptación de los Términos:\n" +
                        "El uso de la aplicación Booky implica la aceptación plena y sin reservas de los presentes Términos y Condiciones. Si el usuario no está de acuerdo con alguno de estos términos, se recomienda no utilizar la plataforma.\n\n" +

                        "2. Finalidad de la Aplicación:\n" +
                        "Booky es una aplicación educativa orientada a facilitar el intercambio de documentos académicos, materiales de estudio y recursos entre estudiantes, docentes y otros usuarios vinculados a la educación. La plataforma busca fomentar el aprendizaje colaborativo y el acceso igualitario a la información.\n\n" +

                        "3. Registro y Veracidad de la Información:\n" +
                        "Para utilizar ciertas funciones de la app, el usuario deberá registrarse proporcionando datos verídicos, actuales y completos. Booky se reserva el derecho de suspender o eliminar cuentas que contengan información falsa, fraudulenta o incompleta, o que presenten actividad sospechosa.\n\n" +

                        "4. Uso Responsable de la Plataforma:\n" +
                        "El usuario se compromete a utilizar Booky de manera ética, respetuosa y legal. Está estrictamente prohibido subir o compartir documentos que infrinjan derechos de autor, que contengan contenido ofensivo, discriminatorio, ilegal o que promuevan el plagio académico. El incumplimiento de estas normas puede conllevar la suspensión permanente de la cuenta.\n\n" +

                        "5. Propiedad Intelectual:\n" +
                        "El contenido generado por la app, como la interfaz, gráficos, funcionalidades y textos, pertenece a Booky o a sus respectivos licenciantes. Los documentos compartidos por los usuarios son responsabilidad exclusiva de sus autores. Cualquier uso indebido de materiales protegidos por derechos de autor será motivo de denuncia o retiro inmediato del contenido.\n\n" +

                        "6. Notificaciones y Geolocalización:\n" +
                        "Booky podrá enviar notificaciones sobre actualizaciones, promociones, recomendaciones educativas o seguridad de la cuenta. Asimismo, podrá hacer uso de datos de geolocalización para mejorar la experiencia del usuario, siempre respetando las disposiciones establecidas en nuestra Política de Privacidad.\n\n" +

                        "7. Protección de Datos Personales:\n" +
                        "La información personal del usuario será tratada con confidencialidad y conforme a las leyes de protección de datos vigentes. Los datos no serán compartidos con terceros sin el consentimiento expreso del usuario, salvo en los casos permitidos por la ley.\n\n" +

                        "8. Modificaciones a los Términos:\n" +
                        "Booky se reserva el derecho de modificar estos Términos y Condiciones en cualquier momento. Cualquier cambio sustancial será comunicado oportunamente a los usuarios. El uso continuado de la app después de la notificación de cambios implicará la aceptación de los nuevos términos.\n\n" +

                        "9. Contacto:\n" +
                        "Para cualquier consulta, queja o sugerencia relacionada con estos Términos y Condiciones, el usuario puede comunicarse con el equipo de soporte de Booky a través del correo electrónico oficial o el formulario de contacto disponible en la aplicación."
        );

        terminos.setCancelable(false);
        terminos.setPositiveButton("Aceptar", null); // Se asigna luego
        terminos.setNegativeButton("Cancelar", (dialog, which) -> {
            chkTerminos.setChecked(false);
            dialog.dismiss();
        });

        AlertDialog alerta = terminos.create();
        alerta.setCanceledOnTouchOutside(false);

        alerta.setOnShowListener(dialog -> {
            // Botones
            final int colorBoton = getResources().getColor(R.color.colorBotonDialogo, null);
            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            alerta.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(colorBoton);
            alerta.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorBoton);

            alerta.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                chkTerminos.setChecked(true);
                terminosMostrados = true;
                validarCampos();
                alerta.dismiss();
            });
        });

        alerta.show();
    }

    private void configurarTextWatchers() {
        edtNombre.addTextChangedListener(genericWatcher(() -> {
            nombreValido = !edtNombre.getText().toString().trim().isEmpty();
        }));

        edtUsername.addTextChangedListener(genericWatcher(() -> {
            usuarioValido = !edtUsername.getText().toString().trim().isEmpty();
        }));

        edtCorreo.addTextChangedListener(genericWatcher(() -> {
            String correo = edtCorreo.getText().toString().trim();
            correoValido = Patterns.EMAIL_ADDRESS.matcher(correo).matches();
        }));

        edtTelefono.addTextChangedListener(genericWatcher(() -> {
            String telefono = edtTelefono.getText().toString().trim();
            telefonoValido = telefono.length() >= 9 && telefono.matches("[0-9]+");
        }));

        edtContrasena.addTextChangedListener(genericWatcher(() -> {
            contrasenaValida = edtContrasena.getText().toString().trim().length() >= 6;
        }));

        edtConfirmarContrasena.addTextChangedListener(genericWatcher(this::validarCampos));
        chkTerminos.setOnCheckedChangeListener((buttonView, isChecked) -> validarCampos());
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
        boolean contrasenasCoinciden = edtContrasena.getText().toString().trim()
                .equals(edtConfirmarContrasena.getText().toString().trim());
        boolean terminosAceptados = chkTerminos.isChecked();

        boolean todoValido = generoValido && fechaValida && nombreValido &&
                usuarioValido && correoValido && telefonoValido && contrasenaValida &&
                contrasenasCoinciden && terminosAceptados;

        btnContinuar.setEnabled(todoValido);
        btnContinuar.setAlpha(todoValido ? 1f : 0.5f);

        int color = getResources().getColor(
                todoValido ? R.color.registrarse_enabled_bg : R.color.registrarse_disabled_bg
        );
        btnContinuar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
    }
}
