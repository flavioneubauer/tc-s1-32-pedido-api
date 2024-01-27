package br.com.fiap.soat1.t32.services.vendas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidatorService {
    private static final String EMAIL_REGEX = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        
    public boolean isValido(String email) {
        boolean isValido = false;
        if (email != null && email.length() > 0) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isValido = true;
            }
        }
        return isValido;
    }
}
