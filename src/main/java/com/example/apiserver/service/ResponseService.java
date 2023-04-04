package com.example.apiserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Service
public class ResponseService {

    public Object getResult(JsonObject result) {
        //JsonObject result = new JsonObject();
        //result.addProperty(key, data);

        JsonObject response = new JsonObject();
        response.addProperty("status", HttpStatus.OK.value());
        response.addProperty("code", HttpStatus.OK.name());
        response.add("resultinfo",result);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        return gson.toJson(JsonParser.parseString(response.toString()));    
    }
}
