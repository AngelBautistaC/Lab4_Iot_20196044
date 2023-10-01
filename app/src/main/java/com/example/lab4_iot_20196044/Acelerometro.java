package com.example.lab4_iot_20196044;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab4_iot_20196044.adapter.ProfileAdapter;
import com.example.lab4_iot_20196044.dto.Perfil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Acelerometro extends Fragment implements ProfileAdapter.OnEditClickListener, SensorEventListener {

    @Override
    public void onEditClick(int position) {
        Perfil perfil = profileListAcelerometro.get(position);
        Editar editarFragment = new Editar();
        Bundle bundle = new Bundle();
        bundle.putSerializable("perfil",perfil);
        editarFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, editarFragment)
                .addToBackStack(null)
                .commit();
    }


    private static final float UMBRAL = 15.0f; // UMBRAL
    private RecyclerView recyclerView;
    private List<Perfil> profileListAcelerometro = new ArrayList<>();
    private ProfileAdapter profileAdapter;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acelerometro, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_acelerometro);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        profileAdapter = new ProfileAdapter(profileListAcelerometro, position -> {
            profileListAcelerometro.remove(position);
            profileAdapter.notifyDataSetChanged();
        },this);

        recyclerView.setAdapter(profileAdapter);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if (acceleration > UMBRAL) {
            Toast.makeText(getActivity(), "Su aceleración es de : " + acceleration + "m/s^2", Toast.LENGTH_SHORT).show();
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                int nextPosition = layoutManager.findLastVisibleItemPosition() + 1;
                if (nextPosition < profileListAcelerometro.size()) {
                    recyclerView.smoothScrollToPosition(nextPosition);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addProfile(Perfil perfil) {
        profileListAcelerometro.add(perfil);
        profileAdapter.notifyDataSetChanged();
    }
}
