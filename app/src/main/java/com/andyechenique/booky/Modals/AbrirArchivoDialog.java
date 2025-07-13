package com.andyechenique.booky.fragmentos;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.andyechenique.booky.R;

public class AbrirArchivoDialog extends DialogFragment {

    private final String urlArchivo;

    public AbrirArchivoDialog(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialogo = super.onCreateDialog(savedInstanceState);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCanceledOnTouchOutside(true);
        return dialogo;
    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.dialog_archivo_webview, container, false);

        WebView webView = vista.findViewById(R.id.webViewArchivo);
        ImageButton btnCerrar = vista.findViewById(R.id.btnCerrarArchivo);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        // Usa visor de Google Docs para PDFs y Word
        String url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + urlArchivo;
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        btnCerrar.setOnClickListener(v -> dismiss());

        return vista;
    }
}
