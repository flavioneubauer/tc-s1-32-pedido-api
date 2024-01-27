package br.com.fiap.soat1.t32.services.vendas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CpfValidatorServiceTest {

    @Autowired
    private CpfValidatorService cpfValidatorService;

    @Test
    void testIsValido_ValidCpf_ReturnsTrue() {
        String cpf = "41765533082";
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertTrue(isValid);
    }

    @Test
    void testIsValido_InvalidCpf_ReturnsFalse() {
        String cpf = "12345678900";
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_CpfWithSpecialCharacters_ReturnsTrue() {
        String cpf = "41765533082";
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertTrue(isValid);
    }

    @Test
    void testIsValido_CpfWithInvalidLength_ReturnsFalse() {
        String cpf = "1234567890";
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_NullCpf_ReturnsFalse() {
        String cpf = null;
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testIsValido_EmptyCpf_ReturnsFalse() {
        String cpf = "";
        boolean isValid = cpfValidatorService.isValido(cpf);
        Assertions.assertFalse(isValid);
    }
}
