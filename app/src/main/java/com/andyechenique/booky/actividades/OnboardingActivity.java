package com.andyechenique.booky.actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.andyechenique.booky.R;
import com.andyechenique.booky.clases.OnboardingAdapter;
import com.andyechenique.booky.fragmentos.OnboardingFragmentA;
import com.andyechenique.booky.fragmentos.OnboardingFragmentB;
import com.andyechenique.booky.fragmentos.OnboardingFragmentC;
import com.andyechenique.booky.fragmentos.OnboardingFragmentD;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);

        SharedPreferences prefs = getSharedPreferences("BookyPrefs", MODE_PRIVATE);
        boolean yaVioOnboarding = prefs.getBoolean("onboarding_completo", false);
        boolean mostrarSoloD = prefs.getBoolean("mostrar_fragmento_d", false);

        List<Fragment> fragmentList = new ArrayList<>();

        if (yaVioOnboarding || mostrarSoloD) {
            // Mostrar solo Fragmento D
            fragmentList.add(new OnboardingFragmentD());

            // Limpiar bandera temporal si existe
            prefs.edit().remove("mostrar_fragmento_d").apply();
        } else {
            // Mostrar flujo completo A → B → C → D
            fragmentList.add(new OnboardingFragmentA());
            fragmentList.add(new OnboardingFragmentB());
            fragmentList.add(new OnboardingFragmentC());
            fragmentList.add(new OnboardingFragmentD());
        }

        onboardingAdapter = new OnboardingAdapter(this, fragmentList);
        viewPager.setAdapter(onboardingAdapter);

        // Animación tipo fade
        viewPager.setPageTransformer((@NonNull View page, float position) -> {
            page.setTranslationX(-position * page.getWidth());
            page.setAlpha(1 - Math.abs(position));
        });

        // Desactivar swipe en último fragmento
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                boolean isLastPage = (position == onboardingAdapter.getItemCount() - 1);
                viewPager.setUserInputEnabled(!isLastPage);
            }
        });
    }
}
