package com.example.apiserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; 

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringBootTest
@AutoConfigureMockMvc
class ApiserverApplicationTests {
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	@DisplayName("Device register")
	void deviceRegister() throws Exception {		
		JsonObject data = new JsonObject();		
        data.addProperty("deviceid", "64B7C7602C634139B9975E5958AAC");

        final ResultActions actions = this.mockMvc.perform(
            post("/Device/Register")
				.content(data.toString())                
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)           
        );

		
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("resultinfo.id").exists())			
			.andDo(print())			
		;		
	}

	@Test
	@DisplayName("Device auth(jwt)")
	String deviceAuth() throws Exception {		
		JsonObject data = new JsonObject();		
        data.addProperty("deviceid", "64B7C7602C634139B9975E5958AAC");
		data.addProperty("id", 1);

        final ResultActions actions = this.mockMvc.perform(
            post("/Device/Auth")
				.content(data.toString())                
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)           
        );

		
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("resultinfo.token").exists())
			//.andExpect(jsonPath("code").value("OK"))
			.andDo(print())			
		;
		JsonObject jsonObjectAlt = JsonParser.parseString(actions.andReturn().getResponse().getContentAsString()).getAsJsonObject();		
		return jsonObjectAlt.get("resultinfo").getAsJsonObject().get("token").getAsString();	
	}

	@Test
	@DisplayName("device Log")
	void deviceLog() throws Exception {		
		JsonObject data = new JsonObject();		
        data.addProperty("deviceid", "64B7C7602C634139B9975E5958AAC");

        final ResultActions actions = this.mockMvc.perform(
            post("/Device/Log")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + deviceAuth())
				.content(data.toString())                
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)           
        );

		
		actions
			.andExpect(status().isOk())			
			.andDo(print())			
		;			
	}
}
