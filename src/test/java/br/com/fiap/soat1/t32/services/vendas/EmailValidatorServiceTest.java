package br.com.fiap.soat1.t32.services.vendas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailValidatorServiceTest {

    @Autowired
    private EmailValidatorService emailValidatorService;

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "test.email+tag@example.com"})
    void testIsValido_ValidEmail_ReturnsTrue(String email) {
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", ""})
    void testIsValido_InvalidEmail_ReturnsFalse(String email) {
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_NullEmail_ReturnsFalse() {
        String email = null;
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertFalse(isValid);
    }
}