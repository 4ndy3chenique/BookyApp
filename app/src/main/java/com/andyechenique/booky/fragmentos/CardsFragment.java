package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andyechenique.booky.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CardsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PublicacionAdapter adapter;
    private final AsyncHttpClient client = new AsyncHttpClient();

    // Cambia esta URL si usas otro host
    private static final String URL_PUBLICACIONES = "https://booky-web.azurewebsites.net/obtenerPublicaciones.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerPublicaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarPublicaciones();
    }

    private void cargarPublicaciones() {
        client.get(URL_PUBLICACIONES, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null && response.optBoolean("success")) {
                    JSONArray publicaciones = response.optJSONArray("data");
                    if (publicaciones != null && publicaciones.length() > 0) {
                        adapter = new PublicacionAdapter(getContext(), publicaciones, getParentFragmentManager());
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "No hay publicaciones aún", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener publicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "Error de red al cargar publicaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
