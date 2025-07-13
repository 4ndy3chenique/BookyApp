package com.andyechenique.booky.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.andyechenique.booky.fragmentos.AbrirArchivoDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andyechenique.booky.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetallesCardsFragment extends Fragment {

    private ImageView imgPortada;
    private TextView txtTitulo, txtDescripcion, txtArchivoAdjunto,
            txtUniversidad, txtNivel, txtIdioma, txtUsuario,
            txtFecha, txtPuntaje, txtLabelCalificacion, txtDescargas;
    private RatingBar ratingBar;
    private RecyclerView recyclerPaginas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalles_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgPortada = view.findViewById(R.id.imgPortadaDetalle);
        txtTitulo = view.findViewById(R.id.txtTituloDetalle);
        txtDescripcion = view.findViewById(R.id.txtDescripcionDetalle);
        txtUniversidad = view.findViewById(R.id.txtUniversidadDetalle);
        txtNivel = view.findViewById(R.id.txtNivelDetalle);
        txtIdioma = view.findViewById(R.id.txtIdiomaDetalle);
        txtUsuario = view.findViewById(R.id.txtUsuarioDetalle);
        txtPuntaje = view.findViewById(R.id.txtPuntajeDetalle);
        txtLabelCalificacion = view.findViewById(R.id.txtLabelCalificacion);
        ratingBar = view.findViewById(R.id.ratingDetalle);
        recyclerPaginas = view.findViewById(R.id.recyclerPaginas);
        txtDescargas = view.findViewById(R.id.txtDescargas); // opcional

        // Obtener datos del bundle
        Bundle args = getArguments();
        if (args != null) {
            txtTitulo.setText(args.getString("Titulo", "Sin título"));
            txtDescripcion.setText(args.getString("Descripcion", ""));
            txtArchivoAdjunto.setText("Ver archivo adjunto");
            txtArchivoAdjunto.setOnClickListener(v -> {
                String url = args.getString("ArchivoAdjunto", "");
                if (!url.isEmpty()) {
                    AbrirArchivoDialog dialog = new AbrirArchivoDialog(url);
                    dialog.show(getParentFragmentManager(), "AbrirArchivoDialog");
                }
            });

            txtUniversidad.setText(args.getString("Universidad", ""));
            txtNivel.setText(args.getString("Nivel", ""));
            txtIdioma.setText(args.getString("Idioma", ""));
            txtUsuario.setText(args.getString("Usuario", ""));
            txtFecha.setText(args.getString("Fecha", ""));
            double calificacion = args.getDouble("Calificacion", 0.0);
            txtPuntaje.setText(String.valueOf(calificacion));
            ratingBar.setRating((float) calificacion);

            Glide.with(requireContext())
                    .load(args.getString("FotoPortada", ""))
                    .placeholder(R.drawable.bg_card)
                    .into(imgPortada);

            // Simulación de páginas del archivo (deberías cargar esto desde tu backend real)
            List<String> paginas = new ArrayList<>();
            paginas.add("https://via.placeholder.com/400x500?text=Página+1");
            paginas.add("https://via.placeholder.com/400x500?text=Página+2");
            paginas.add("https://via.placeholder.com/400x500?text=Página+3");

            recyclerPaginas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            PaginasAdapter adapter = new PaginasAdapter(getContext(), paginas);
            recyclerPaginas.setAdapter(adapter);
        }
    }
}
