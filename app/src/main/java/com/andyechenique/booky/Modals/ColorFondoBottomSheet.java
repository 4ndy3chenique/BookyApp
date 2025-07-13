package com.andyechenique.booky.Modals;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ColorFondoBottomSheet extends BottomSheetDialogFragment {

    private static final String URL_COLORES = "https://booky-web.azurewebsites.net/mostrarColores.php";

    public interface OnColorSeleccionadoListener {
        void onColorSeleccionado(String codigoHex);
    }

    public ColorFondoBottomSheet() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_color_fondo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayout grid = view.findViewById(R.id.gridColores);
        cargarColoresDesdeBD(grid);
    }

    private void cargarColoresDesdeBD(GridLayout gridLayout) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_COLORES, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    gridLayout.removeAllViews();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject color = response.getJSONObject(i);
                        String hex = color.getString("nombre"); // usa "nombre" si el JSON lo devuelve así

                        ImageView colorBox = new ImageView(getContext());
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = 130;
                        params.height = 130;
                        params.setMargins(24, 24, 24, 24);
                        colorBox.setLayoutParams(params);
                        colorBox.setBackgroundColor(Color.parseColor(hex));
                        colorBox.setBackgroundResource(R.drawable.borde_color); // tu drawable de borde
                        colorBox.setClickable(true);
                        colorBox.setFocusable(true);

                        colorBox.setOnClickListener(v -> {
                            if (getParentFragment() instanceof OnColorSeleccionadoListener) {
                                ((OnColorSeleccionadoListener) getParentFragment()).onColorSeleccionado(hex);
                            }
                            dismiss();
                        });

                        gridLayout.addView(colorBox);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al cargar colores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
