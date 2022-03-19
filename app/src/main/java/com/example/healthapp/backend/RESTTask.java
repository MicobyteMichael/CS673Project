package com.example.healthapp.backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RESTTask<ReturnType> {

    public abstract String getMethod();
    public abstract String getEndpoint();
    public abstract String getMessage();
    public abstract JSONObject getParameters() throws JSONException;
    public abstract ReturnType process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException;
}
