package com.example.lab4_iot_20196044.services;

import com.example.lab4_iot_20196044.dto.Perfil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PerfilRandom {
    @GET("api/")
    Call<RandomUserResponse> getRandomUser();

    public static class RandomUserResponse {
        public List<Perfil> results;
    }
}