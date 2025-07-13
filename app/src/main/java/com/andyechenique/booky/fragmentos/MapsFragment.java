package com.andyechenique.booky.fragmentos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyechenique.booky.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsFragment extends Fragment {

    // Cambia esto por la URL de tu endpoint real
    private static final String URL_UNIVERSIDADES = "https://booky-web.scm.azurewebsites.net/mostrarUniversidades.php";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL_UNIVERSIDADES, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        LatLng posicionInicial = null;

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject universidad = response.getJSONObject(i);
                            String nombre = universidad.getString("nombre_universidad");
                            double lat = universidad.getDouble("latitud");
                            double lon = universidad.getDouble("longitud");

                            LatLng posicion = new LatLng(lat, lon);
                            googleMap.addMarker(new MarkerOptions().position(posicion).title(nombre));

                            // Guardar primera ubicación para centrar la cámara
                            if (i == 0) {
                                posicionInicial = posicion;
                            }
                        }

                        if (posicionInicial != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionInicial, 12));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    // Manejo de error en caso falle la consulta
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
