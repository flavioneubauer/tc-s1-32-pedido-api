package br.com.fiap.soat1.t32;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

public class TestHelper {
    
    public static String asJsonString(@NonNull final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
