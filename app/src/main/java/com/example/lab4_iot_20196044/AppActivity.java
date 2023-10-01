package com.example.lab4_iot_20196044;// AppActivity.java

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lab4_iot_20196044.adapter.ProfileAdapter;
import com.example.lab4_iot_20196044.dto.Perfil;
import com.example.lab4_iot_20196044.services.PerfilRandom;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private Magnetometro magnetometroFragment;
        private Acelerometro acelerometroFragment;
    private TextView titulo;
    private Button btnIrHacia;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        progressBar = findViewById(R.id.progressBar);

        magnetometroFragment = new Magnetometro();
        acelerometroFragment = new Acelerometro();

        titulo = findViewById(R.id.titulo);
        btnIrHacia = findViewById(R.id.irhacia);


        titulo.setText("Magnetometro");
        btnIrHacia.setText("Ir a Acelerometro");


        switchFragment(magnetometroFragment);

        Button btnAnhadir = findViewById(R.id.anhadir);
        btnAnhadir.setOnClickListener(view -> {
            btnAnhadir.setEnabled(false);

            btnIrHacia.setEnabled(false);
            btnAnhadir.setAlpha(0.5F);
            btnIrHacia.setAlpha(0.5F);
            progressBar.setVisibility(View.VISIBLE);


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://randomuser.me/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PerfilRandom perfilRandom = retrofit.create(PerfilRandom.class);
            Call<PerfilRandom.RandomUserResponse> call = perfilRandom.getRandomUser();
            call.enqueue(new Callback<PerfilRandom.RandomUserResponse>() {
                @Override
                public void onResponse(Call<PerfilRandom.RandomUserResponse> call, Response<PerfilRandom.RandomUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Perfil perfil = response.body().results.get(0);
                        if (titulo.getText().equals("Magnetometro")) {
                            magnetometroFragment.addProfile(perfil);
                        } else {
                            acelerometroFragment.addProfile(perfil);
                        }
                    }
                    btnAnhadir.setEnabled(true);
                    btnIrHacia.setEnabled(true);
                    btnIrHacia.setAlpha(1F);
                    btnAnhadir.setAlpha(1F);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<PerfilRandom.RandomUserResponse> call, Throwable t) {
                    btnAnhadir.setEnabled(true);
                    btnIrHacia.setEnabled(true);
                    btnIrHacia.setAlpha(1F);
                    btnAnhadir.setAlpha(1F);
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        btnIrHacia.setOnClickListener(view -> {
            // Cambia entre fragmentos
            if (titulo.getText().equals("Magnetometro")) {
                switchFragment(acelerometroFragment);
                titulo.setText("Acelerometro");
                btnIrHacia.setText("Ir a Magnetometro");
            } else {
                switchFragment(magnetometroFragment);
                titulo.setText("Magnetometro");
                btnIrHacia.setText("Ir a Acelerometro");
            }
        });



        ImageView imageViewCellTower = findViewById(R.id.imageViewCellTower);
        imageViewCellTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
                if (titulo.getText().equals("Magnetometro")) {
                    builder.setTitle("Detalles - Magnetómetro")
                            .setMessage("Haga click en 'Añadir' para agregar contactos a su lista. Esta aplicación está utilizando el MAGNETÓMETRO de su dispositivo.\nDe esta forma, la lista se mostrará al 100% cuando se apunte al NORTE. Caso contrario, se desvanecerá...");
                } else {
                    builder.setTitle("Detalles - Acelerómetro")
                            .setMessage("Haga click en 'Añadir' para agregar contactos a su lista. Esta aplicación está utilizando el ACELERÓMETRO de su dispositivo.\nDe esta forma, la lista hara scroll hacia abajo cuando agite su dispositivo.");
                }
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack();  // Limpia el BackStack
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}