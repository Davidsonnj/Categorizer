package edu.br.ifes.categorizer.GenAI;

import com.google.genai.Client;
import com.google.genai.errors.ServerException;
import com.google.genai.types.GenerateContentResponse;

public class GeminiAPI {
    private static final Client client = Client.builder()
            .apiKey("AIzaSyBu_ZZ3dO_qPL-cBr83dnBGB3YJafHHYXw")
            .build();

    public static String perguntar(String pergunta) throws Exception {
        int retries = 0;
        int maxRetries = 5;
        long waitTime = 1000;

        while (true) {
            try {
                GenerateContentResponse response = client.models.generateContent(
                        "gemini-1.5-flash",
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

