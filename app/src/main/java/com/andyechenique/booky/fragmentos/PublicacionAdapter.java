package com.andyechenique.booky.fragmentos;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andyechenique.booky.R;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.andyechenique.booky.fragmentos.DetallesCardsFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder> {

    private final Context context;
    private final JSONArray publicaciones;
    private final FragmentManager fragmentManager;

    public PublicacionAdapter(Context context, JSONArray publicaciones, FragmentManager fragmentManager) {
        this.context = context;
        this.publicaciones = publicaciones;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_publicacion, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject publicacion = publicaciones.optJSONObject(position);
        if (publicacion == null) return;

        // Título
        holder.txtTitulo.setText(publicacion.optString("Titulo", "Sin título"));

        // Imagen de portada
        String urlPortada = publicacion.optString("FotoPortada");
        Glide.with(context).load(urlPortada).placeholder(R.drawable.bg_card).into(holder.imgPortada);

        // Etiquetas y curso combinados (puedes personalizarlo más)
        holder.layoutChips.removeAllViews();
        try {
            String[] etiquetas = publicacion.optString("Etiquetas", "").split(",");
            String curso = publicacion.optString("Curso", "");
            for (String chipText : etiquetas) {
                if (!chipText.trim().isEmpty()) holder.layoutChips.addView(crearChip(chipText.trim()));
            }
            if (!curso.isEmpty()) holder.layoutChips.addView(crearChip(curso));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Universidad y Nivel
        holder.txtUniversidad.setText(publicacion.optString("NombreUniversidad", ""));
        holder.txtNivel.setText(publicacion.optString("NombreNivel", ""));

        // Usuario y Fecha
        holder.txtNombrePerfil.setText(publicacion.optString("NombreUsuario", ""));
        holder.txtCalendario.setText(publicacion.optString("FechaPublicacion", "").split("T")[0]);

        // Calificación
        float promedio = (float) publicacion.optDouble("CalificacionPromedio", 0.0);
        holder.txtPuntaje.setText(String.valueOf(promedio));
        holder.ratingBar.setRating(promedio);

        // Evento click
        holder.itemView.setOnClickListener(v -> {
            DetallesCardsFragment detalles = new DetallesCardsFragment();
            detalles.setArguments(bundleDesdePublicacion(publicacion));
            fragmentManager.beginTransaction()
                    .replace(R.id.main, detalles)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return publicaciones.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPortada, imgUniversidad, imgNivel, iconPerfil, imgCalendario;
        TextView txtTitulo, txtUniversidad, txtNivel, txtNombrePerfil, txtCalendario, txtPuntaje, txtCalificacion;
        RatingBar ratingBar;
        FlexboxLayout layoutChips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.imgPortada);
            imgUniversidad = itemView.findViewById(R.id.imgUniversidad);
            imgNivel = itemView.findViewById(R.id.imgNivel);
            iconPerfil = itemView.findViewById(R.id.icon);
            imgCalendario = itemView.findViewById(R.id.imgCalendario);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtUniversidad = itemView.findViewById(R.id.txtUniversidad);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtNombrePerfil = itemView.findViewById(R.id.txtNombrePerfil);
            txtCalendario = itemView.findViewById(R.id.txtCalendario);
            txtPuntaje = itemView.findViewById(R.id.txtPuntaje);
            txtCalificacion = itemView.findViewById(R.id.txtCalificacion);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }

    private View crearChip(String texto) {
        TextView chip = new TextView(context);
        chip.setText(texto);
        chip.setTextColor(context.getColor(R.color.black));
        chip.setTextSize(12f);
        chip.setPadding(24, 8, 24, 8);

        GradientDrawable fondo = new GradientDrawable();
        fondo.setColor(context.getColor(R.color.white));
        fondo.setCornerRadius(48f);
        fondo.setStroke(2, context.getColor(R.color.black));

        chip.setBackground(fondo);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        chip.setLayoutParams(params);

        return chip;
    }

    private Bundle bundleDesdePublicacion(JSONObject obj) {
        Bundle bundle = new Bundle();
        bundle.putString("Titulo", obj.optString("Titulo"));
        bundle.putString("Descripcion", obj.optString("Descripcion"));
        bundle.putString("ArchivoAdjunto", obj.optString("ArchivoAdjunto"));
        bundle.putString("FotoPortada", obj.optString("FotoPortada"));
        bundle.putString("Universidad", obj.optString("NombreUniversidad"));
        bundle.putString("Nivel", obj.optString("NombreNivel"));
        bundle.putString("Idioma", obj.optString("NombreIdioma"));
        bundle.putString("Usuario", obj.optString("NombreUsuario"));
        bundle.putString("Fecha", obj.optString("FechaPublicacion"));
        bundle.putDouble("Calificacion", obj.optDouble("CalificacionPromedio", 0.0));
        return bundle;
    }
}
