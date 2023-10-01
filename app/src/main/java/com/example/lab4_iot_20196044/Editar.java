package com.example.lab4_iot_20196044;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab4_iot_20196044.dto.Perfil;


public class Editar extends Fragment {
    private EditText editTitle,editFirst,editLast, editGender, editCity, editCountry, editEmail, editPhone;
    private Perfil perfil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            perfil = (Perfil) bundle.getSerializable("perfil");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar, container, false);

        editTitle = view.findViewById(R.id.editTitle);
        editFirst = view.findViewById(R.id.editFirst);
        editLast = view.findViewById(R.id.editLast);
        editGender = view.findViewById(R.id.editGender);
        editCity = view.findViewById(R.id.editCity);
        editCountry = view.findViewById(R.id.editCountry);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);

        if (perfil != null) {
            editTitle.setText(perfil.getName().title);
            editFirst.setText(perfil.getName().first);
            editLast.setText(perfil.getName().last);
            editGender.setText(perfil.getGender());
            editCity.setText(perfil.getLocation().city);
            editCountry.setText(perfil.getLocation().country);
            editEmail.setText(perfil.getEmail());
            editPhone.setText(perfil.getPhone());
        }

        Button btnSave = view.findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                perfil.getName().title = editTitle.getText().toString();
                perfil.getName().first = editFirst.getText().toString();
                perfil.getName().last = editLast.getText().toString();

                perfil.setGender(editGender.getText().toString());
                perfil.getLocation().city = editCity.getText().toString();
                perfil.getLocation().country = editCountry.getText().toString();
                perfil.setEmail(editEmail.getText().toString());
                perfil.setPhone(editPhone.getText().toString());

                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}


