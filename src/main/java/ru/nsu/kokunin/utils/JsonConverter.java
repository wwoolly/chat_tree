package ru.nsu.kokunin.utils;

import com.google.gson.Gson;

public class JsonConverter {
    private final Gson gson = new Gson();

    public <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }
}
