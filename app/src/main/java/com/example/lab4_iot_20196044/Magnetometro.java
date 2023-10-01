package com.example.lab4_iot_20196044;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Surface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_iot_20196044.adapter.ProfileAdapter;
import com.example.lab4_iot_20196044.dto.Perfil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Magnetometro extends Fragment implements ProfileAdapter.OnEditClickListener, SensorEventListener{
    @Override
    public void onEditClick(int position) {
        Perfil perfil = profileList.get(position);
        Editar editarFragment = new Editar();
        Bundle bundle = new Bundle();
        bundle.putSerializable("perfil", perfil);
        editarFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, editarFragment)
                .addToBackStack(null)
                .commit();
    }

    private RecyclerView recyclerView;
    private List<Perfil> profileList = new ArrayList<>();
    private ProfileAdapter profileAdapter;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magnetometro, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profileAdapter = new ProfileAdapter(profileList, position -> {
            profileList.remove(position);
            profileAdapter.notifyDataSetChanged();
        }, this);

        recyclerView.setAdapter(profileAdapter);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            float[] rotationMatrixAdjusted = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);

            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_0) {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y, rotationMatrixAdjusted);
            } else if (rotation == Surface.ROTATION_90) {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrixAdjusted);
            } else if (rotation == Surface.ROTATION_180) {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, rotationMatrixAdjusted);
            } else {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, rotationMatrixAdjusted);
            }
            SensorManager.getOrientation(rotationMatrixAdjusted, orientation);
            float azimuthInRadians = orientation[0];
            float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
            float absoluteAzimuthInDegrees = Math.abs(azimuthInDegrees);
            float percentageVisibility;

            if (absoluteAzimuthInDegrees <= 0.05 * 180) {
                percentageVisibility = 1f;
            } else if (absoluteAzimuthInDegrees <= 0.25 * 180) {
                percentageVisibility = 0.8f;
            } else if (absoluteAzimuthInDegrees <= 0.5 * 180) {
                percentageVisibility = 0.6f;
            } else if (absoluteAzimuthInDegrees <= 0.75 * 180) {
                percentageVisibility = 0.4f;
            } else if (absoluteAzimuthInDegrees <= 0.9 * 180) {
                percentageVisibility = 0.2f;
            } else {
                percentageVisibility = 0f;
            }

            recyclerView.setAlpha(percentageVisibility);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @SuppressLint("NotifyDataSetChanged")
    public void addProfile(Perfil perfil) {
        profileList.add(perfil);
        profileAdapter.notifyDataSetChanged();
    }
}