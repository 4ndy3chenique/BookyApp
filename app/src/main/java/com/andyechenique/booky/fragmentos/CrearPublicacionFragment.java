package com.andyechenique.booky.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andyechenique.booky.Modals.SeleccionarImagenBottomSheet;
import com.andyechenique.booky.Modals.SubirArchivoBottomSheet;
import com.andyechenique.booky.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CrearPublicacionFragment extends Fragment {

    private EditText inputTitulo, inputDescripcion;
    private TextView textAdjunto, textPortada;
    private Button btnSubir;
    private ImageView btnBack;
    private Spinner spinnerUbicacion, spinnerEtiquetas, spinnerTipo, spinnerCurso, spinnerNivel, spinnerIdioma;

    private final AsyncHttpClient client = new AsyncHttpClient();
    private final HashMap<String, String> urls = new HashMap<String, String>() {{
        put("Etiquetas", "https://booky-web.azurewebsites.net/mostrarEtiquetas.php");
        put("Tipo", "https://booky-web.azurewebsites.net/mostrarTiposRecurso.php");
        put("Curso", "https://booky-web.azurewebsites.net/mostrarEtiquetas.php");
        put("Nivel", "https://booky-web.azurewebsites.net/mostrarNivelesEducativos.php");
        put("Idioma", "https://booky-web.azurewebsites.net/mostrarIdiomas.php");
        put("Ubicacion", "https://booky-web.azurewebsites.net/mostrarUniversidades.php");
    }};

    private Uri archivoSeleccionadoUri = null;
    private Uri imagenPortadaUri = null;
    private String archivoSubidoUrl = null;
    private String portadaSubidaUrl = null;

    private int spinnersCargados = 0;
    private static final int TOTAL_SPINNERS = 6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_publicacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputTitulo = view.findViewById(R.id.inputTitulo);
        inputDescripcion = view.findViewById(R.id.inputDescripcion);
        textAdjunto = view.findViewById(R.id.textAdjunto);
        textPortada = view.findViewById(R.id.textPortada);
        btnSubir = view.findViewById(R.id.btnSubir);
        btnBack = view.findViewById(R.id.btnFlecha);

        spinnerUbicacion = view.findViewById(R.id.spinnerUbicacion);
        spinnerEtiquetas = view.findViewById(R.id.spinnerEtiquetas);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerCurso = view.findViewById(R.id.spinnerCurso);
        spinnerNivel = view.findViewById(R.id.spinnerNivel);
        spinnerIdioma = view.findViewById(R.id.spinnerIdioma);

        cargarSpinners();

        View header = getActivity().findViewById(R.id.header);
        View navbar = getActivity().findViewById(R.id.navbar);
        if (header != null) header.setVisibility(View.GONE);
        if (navbar != null) navbar.setVisibility(View.GONE);

        view.findViewById(R.id.btnAdjuntarArchivo).setOnClickListener(v -> {
            SubirArchivoBottomSheet modal = new SubirArchivoBottomSheet();
            modal.setOnArchivoSeleccionadoListener((uri, nombreArchivo) -> {
                archivoSeleccionadoUri = uri;
                textAdjunto.setText(nombreArchivo);
                subirArchivoConRuta(uri, "adjunto");
            });
            modal.show(getParentFragmentManager(), "SubirArchivoBottomSheet");
        });

        view.findViewById(R.id.btnSeleccionarImagen).setOnClickListener(v -> {
            SeleccionarImagenBottomSheet modal = new SeleccionarImagenBottomSheet();
            modal.setOnImagenSeleccionadaListener((uri, nombreArchivo) -> {
                imagenPortadaUri = uri;
                textPortada.setText(nombreArchivo);
                subirArchivoConRuta(uri, "portada");
            });
            modal.show(getParentFragmentManager(), "SeleccionarImagenBottomSheet");
        });

        inputTitulo.addTextChangedListener(genericWatcher());
        inputDescripcion.addTextChangedListener(genericWatcher());

        btnSubir.setOnClickListener(v -> {
            if (validarFormulario()) {
                subirPublicacion();
            } else {
                mostrarToast("Completa todos los campos para subir la publicación");
            }
        });

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private boolean validarFormulario() {
        if (inputTitulo.getText().toString().trim().isEmpty()) {
            mostrarToast("Falta el título"); return false;
        }
        if (inputDescripcion.getText().toString().trim().isEmpty()) {
            mostrarToast("Falta la descripción"); return false;
        }
        if (archivoSubidoUrl == null) {
            mostrarToast("Falta subir el archivo adjunto"); return false;
        }
        if (portadaSubidaUrl == null) {
            mostrarToast("Falta subir la imagen de portada"); return false;
        }
        if (spinnerTipo.getSelectedItemPosition() == 0) {
            mostrarToast("Selecciona el tipo de recurso"); return false;
        }
        if (spinnerEtiquetas.getSelectedItemPosition() == 0 || spinnerCurso.getSelectedItemPosition() == 0) {
            mostrarToast("Selecciona una etiqueta/curso"); return false;
        }
        if (spinnerNivel.getSelectedItemPosition() == 0) {
            mostrarToast("Selecciona el nivel educativo"); return false;
        }
        if (spinnerIdioma.getSelectedItemPosition() == 0) {
            mostrarToast("Selecciona el idioma"); return false;
        }
        if (spinnerUbicacion.getSelectedItemPosition() == 0) {
            mostrarToast("Selecciona la universidad"); return false;
        }
        return true;
    }

    private void cargarSpinners() {
        for (String clave : urls.keySet()) {
            Spinner spinner = obtenerSpinnerPorClave(clave);
            cargarDatosSpinner(clave, spinner);
        }
    }

    private Spinner obtenerSpinnerPorClave(String clave) {
        switch (clave) {
            case "Etiquetas": return spinnerEtiquetas;
            case "Tipo": return spinnerTipo;
            case "Curso": return spinnerCurso;
            case "Nivel": return spinnerNivel;
            case "Idioma": return spinnerIdioma;
            case "Ubicacion": return spinnerUbicacion;
            default: return null;
        }
    }

    private void cargarDatosSpinner(String clave, Spinner spinner) {
        String url = urls.get(clave);
        if (url == null || spinner == null) return;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<String> datos = new ArrayList<>();
                datos.add("Seleccionar");
                for (int i = 0; i < response.length(); i++) {
                    datos.add(response.optJSONObject(i).optString("nombre"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, datos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinnersCargados++;
                if (spinnersCargados == TOTAL_SPINNERS) {
                    btnSubir.setEnabled(true);
                    btnSubir.setBackgroundResource(R.drawable.boton_subir_estados);
                }
            }
        });
    }

    private void subirArchivoConRuta(Uri uri, String tipo) {
        SharedPreferences prefs = requireContext().getSharedPreferences("BookyPrefs", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);
        if (idUsuario == -1) return;

        String nombre = uri.getLastPathSegment();
        String extension = ".bin";
        int punto = nombre != null ? nombre.lastIndexOf('.') : -1;
        if (punto != -1) extension = nombre.substring(punto);

        String nombreArchivo = tipo + "_" + idUsuario + "_" + System.currentTimeMillis() + extension;
        String rutaArchivo = "usuarios/" + idUsuario + "/" + nombreArchivo;

        String encodedRuta = Uri.encode(rutaArchivo, "/");
        String endpoint = "https://booky-web.azurewebsites.net/generarSasToken.php?archivo=" + encodedRuta;

        client.get(endpoint, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.optBoolean("success")) {
                    String sasUrl = response.optString("sas_url");
                    String publicUrl = sasUrl.split("\\?")[0];

                    if (tipo.equals("adjunto")) archivoSubidoUrl = publicUrl;
                    else if (tipo.equals("portada")) portadaSubidaUrl = publicUrl;

                    subirArchivoAzure(uri, sasUrl, tipo);
                } else {
                    mostrarToast("No se pudo generar la URL segura para Azure.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mostrarToast("Error al generar SAS desde el servidor");
            }
        });
    }


    private void subirArchivoAzure(Uri uri, String finalUrl, String tipo) {
        new Thread(() -> {
            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri)) {
                if (inputStream == null) {
                    Log.e("AzureUpload", "InputStream nulo para tipo: " + tipo);
                    return;
                }

                Log.d("AzureUpload", "Intentando conectar a: " + finalUrl);
                HttpURLConnection connection = (HttpURLConnection) new URL(finalUrl).openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("x-ms-blob-type", "BlockBlob");

                OutputStream outputStream = connection.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                int responseCode = connection.getResponseCode();
                Log.d("AzureUpload", "Código de respuesta: " + responseCode);

                if (responseCode == 201) {
                    Log.d("AzureUpload", "Archivo subido correctamente a: " + finalUrl);
                    requireActivity().runOnUiThread(() -> {
                        mostrarToast((tipo.equals("adjunto") ? "Archivo adjunto" : "Portada") + " subida correctamente");
                        if (validarFormulario()) subirPublicacion();
                    });
                } else {
                    Log.e("AzureUpload", "Error al subir archivo. Código: " + responseCode);
                    requireActivity().runOnUiThread(() -> mostrarToast("No se pudo subir el archivo (código: " + responseCode + ")"));
                }

            } catch (Exception e) {
                Log.e("AzureUpload", "Excepción al subir archivo: " + e.getMessage());
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> mostrarToast("Error al subir archivo"));
            }
        }).start();
    }


    private void mostrarToast(String mensaje) {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    private void subirPublicacion() {
        SharedPreferences prefs = requireContext().getSharedPreferences("BookyPrefs", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);
        if (idUsuario == -1) {
            mostrarToast("Usuario no identificado");
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("IdUsuario", idUsuario);
            data.put("Titulo", inputTitulo.getText().toString().trim());
            data.put("Descripcion", inputDescripcion.getText().toString().trim());
            data.put("ArchivoAdjunto", archivoSubidoUrl);
            data.put("FotoPortada", portadaSubidaUrl);
            data.put("ColorPortadaId", 1);
            data.put("TipoRecursoId", spinnerTipo.getSelectedItemPosition());
            data.put("NivelEducativoId", spinnerNivel.getSelectedItemPosition());
            data.put("UniversidadId", spinnerUbicacion.getSelectedItemPosition());
            data.put("IdiomaId", spinnerIdioma.getSelectedItemPosition());

            // Si no usas carpeta, manda null
            data.put("IdCarpeta", JSONObject.NULL);

            StringEntity entity = new StringEntity(data.toString(), "UTF-8");
            client.post(getContext(), "https://booky-web.azurewebsites.net/crearPublicacion.php", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response.optBoolean("success")) {
                        mostrarToast("Publicación creada correctamente");
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        mostrarToast("Error: " + response.optString("message"));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    mostrarToast("Fallo de red al subir la publicación");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            mostrarToast("Error inesperado");
        }
    }


    private TextWatcher genericWatcher() {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (validarFormulario()) subirPublicacion();
            }
        };
    }
}