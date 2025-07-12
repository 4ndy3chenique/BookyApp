package com.andyechenique.booky.dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyechenique.booky.R;
import com.andyechenique.booky.actividades.HomeActivity;
import com.andyechenique.booky.actividades.InicioSesionUserActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.List;


public class InicioSesionBottomSheet extends BottomSheetDialogFragment {

    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN_GOOGLE = 1001;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private LoadingDialog loadingDialog;

    public static InicioSesionBottomSheet newInstance() {
        return new InicioSesionBottomSheet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(requireActivity());

        MaterialButton btnUsuario = view.findViewById(R.id.btnUsuario);
        btnUsuario.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), InicioSesionUserActivity.class));
            dismiss();
        });

        FacebookSdk.sdkInitialize(requireContext().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        MaterialButton btnFacebook = view.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(v -> iniciarSesionFacebook());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        MaterialButton btnGoogle = view.findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(v -> iniciarSesionGoogle());

        return view;
    }

    private void iniciarSesionGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                loadingDialog.dismiss();
                Toast.makeText(getContext(), "Error al iniciar sesión con Google: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        loadingDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    loadingDialog.dismiss();

                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        guardarSesion(user);
                        Toast.makeText(getContext(), "Bienvenido, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dismiss();
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Falló la autenticación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void guardarSesion(FirebaseUser user) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("BookyPrefs", requireActivity().MODE_PRIVATE);
        prefs.edit()
                .putBoolean("sesion_activa", true)
                .putString("correo_usuario", user.getEmail())
                .apply();
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    //Facebook

    private void iniciarSesionFacebook() {
        loadingDialog.show();

        LoginManager.getInstance().logInWithReadPermissions(this, List.of("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                loadingDialog.dismiss();
                Toast.makeText(getContext(), "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                loadingDialog.dismiss();
                Toast.makeText(getContext(), "Error con Facebook: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = com.google.firebase.auth.FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        guardarSesion(user);
                        Toast.makeText(getContext(), "Bienvenido, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dismiss();
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Autenticación fallida: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}