package br.com.fiap.soat1.t32.services.vendas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailValidatorServiceTest {

    @Autowired
    private EmailValidatorService emailValidatorService;

    @Test
    void testIsValido_ValidEmail_ReturnsTrue() {
        String email = "test@example.com";
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertTrue(isValid);
    }

    @Test
    void testIsValido_InvalidEmail_ReturnsFalse() {
        String email = "invalid-email";
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_EmailWithSpecialCharacters_ReturnsTrue() {
        String email = "test.email+tag@example.com";
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertTrue(isValid);
    }

    @Test
    void testIsValido_NullEmail_ReturnsFalse() {
        String email = null;
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_EmptyEmail_ReturnsFalse() {
        String email = "";
        boolean isValid = emailValidatorService.isValido(email);
        Assertions.assertFalse(isValid);
    }
}