package com.example.lab4_iot_20196044.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab4_iot_20196044.R;
import com.example.lab4_iot_20196044.dto.Perfil;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private  List<Perfil> profiles;
    private OnDeleteClickListener onDeleteClickListener;

    public ProfileAdapter(List<Perfil> profiles, OnDeleteClickListener onDeleteClickListener) {
        this.profiles = profiles;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private OnEditClickListener onEditClickListener;

    public ProfileAdapter(List<Perfil> profiles, OnDeleteClickListener onDeleteClickListener, OnEditClickListener onEditClickListener) {
        this.profiles = profiles;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    public interface OnEditClickListener {
        void onEditClick(int position);
    }




    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
        return new ProfileViewHolder(view, onDeleteClickListener,onEditClickListener);
    }
    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Perfil perfil = profiles.get(position);
        TextView nameText = holder.itemView.findViewById(R.id.name_text);
        TextView genderText = holder.itemView.findViewById(R.id.gender_text);
        TextView cityText = holder.itemView.findViewById(R.id.city_text);
        TextView countryText = holder.itemView.findViewById(R.id.country_text);
        TextView emailText = holder.itemView.findViewById(R.id.email_text);
        TextView phoneText = holder.itemView.findViewById(R.id.phone_text);
        ImageView profileImage = holder.itemView.findViewById(R.id.profile_image);


        String fullName = "Nombre: ";
        if (perfil.getName() != null) {
            fullName += perfil.getName().title + " " + perfil.getName().first + " " + perfil.getName().last;
        }
        nameText.setText(fullName);

        genderText.setText("Género: " + (perfil.getGender() != null ? perfil.getGender() : ""));

        String location = "Ciudad: ";
        if (perfil.getLocation() != null) {
            location += perfil.getLocation().city;
        }
        cityText.setText(location);

        String country = "País: ";
        if (perfil.getLocation() != null) {
            country += perfil.getLocation().country;
        }
        countryText.setText(country);

        emailText.setText("Email: " + (perfil.getEmail() != null ? perfil.getEmail() : ""));
        phoneText.setText("Teléfono: " + (perfil.getPhone() != null ? perfil.getPhone() : ""));

        if (perfil.getPicture() != null) {
            Glide.with(holder.itemView.getContext()).load(perfil.getPicture().large).into(profileImage);
        }
    }



    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteButton;
        public ImageView editButton;

        public ProfileViewHolder(View itemView, OnDeleteClickListener onDeleteListener, OnEditClickListener onEditListener) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onDeleteListener != null && position != RecyclerView.NO_POSITION) {
                    onDeleteListener.onDeleteClick(position);
                }
            });

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onEditListener != null && position != RecyclerView.NO_POSITION) {
                    onEditListener.onEditClick(position);
                }
            });


        }
    }
}
