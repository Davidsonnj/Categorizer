package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class PerguntarTest {

    @Test
    public void testDadosIniciaisComAssuntoDefesa() {
        Email email = new Email();
        email.setSubject("Defesa");
        email.setBody("Nome: João Silva\nTítulo: Sistemas Inteligentes\nEmail: joao@ifes.br");

        try (MockedStatic<GeminiAPI> mockedGemini = mockStatic(GeminiAPI.class)) {
            mockedGemini.when(() -> GeminiAPI.perguntar(org.mockito.ArgumentMatchers.anyString()))
                    .thenReturn("DADOS_INICIAIS");

            String status = Perguntar.getStatusEmail(email);
            assertEquals("DADOS_INICIAIS", status);
        }
    }

    @Test
    public void testConfirmacaoDefesa() {
        Email email = new Email();
        email.setSubject("Confirmação de Defesa");
        email.setBody("Confirmo a defesa para a data marcada.");

        try (MockedStatic<GeminiAPI> mockedGemini = mockStatic(GeminiAPI.class)) {
            mockedGemini.when(() -> GeminiAPI.perguntar(org.mockito.ArgumentMatchers.anyString()))
                    .thenReturn("CONFIRMACAO_DEFESA");

            String status = Perguntar.getStatusEmail(email);
            assertEquals("CONFIRMACAO_DEFESA", status);
        }
    }

    @Test
    public void testRespostaIndefinidaEmCasoDeErro() {
        Email email = new Email();
        email.setSubject("Erro");
        email.setBody("Texto qualquer");

        try (MockedStatic<GeminiAPI> mockedGemini = mockStatic(GeminiAPI.class)) {
            mockedGemini.when(() -> GeminiAPI.perguntar(org.mockito.ArgumentMatchers.anyString()))
                    .thenThrow(new RuntimeException("Erro na API"));

            String status = Perguntar.getStatusEmail(email);
            assertEquals("INDEFINIDO", status);
        }
    }
}
