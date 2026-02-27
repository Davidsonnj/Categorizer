package edu.br.ifes.categorizer.GenAI;

import com.google.genai.Client;
import com.google.genai.errors.ServerException;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class GeminiAPI {
    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    private static final String GEMINI_API_KEY =
            dotenv.get("GEMINI_API_KEY");

    static {

        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isBlank()) {

            throw new RuntimeException(
                    "GEMINI_API_KEY não encontrada no .env"
            );
        }
    }

    private static final Client client =
            Client.builder()
                    .apiKey(GEMINI_API_KEY)
                    .build();

    public static String perguntar(String pergunta) throws Exception {
        int retries = 0;
        int maxRetries = 5;
        long waitTime = 1000;

        while (true) {
            try {
                GenerateContentResponse response = client.models.generateContent(
                        "gemini-flash-latest",
                        pergunta,
                        null
                );
                return response.text();

            } catch (ServerException e) {
                // Erro típico de sobrecarga (503)
                if (retries >= maxRetries) {
                    throw new RuntimeException("Falha após várias tentativas: " + e.getMessage(), e);
                }
                retries++;
                System.err.println("[GeminiAPI] Erro 503, tentativa " + retries + " de " + maxRetries +
                        ". Retentando em " + waitTime + "ms...");
                Thread.sleep(waitTime);
                waitTime *= 2;

            } catch (Exception e) {

                throw e;
            }
        }
    }
}

