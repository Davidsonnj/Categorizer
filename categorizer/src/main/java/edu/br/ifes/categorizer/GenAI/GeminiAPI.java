package edu.br.ifes.categorizer.GenAI;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiAPI {
    private static final Client client = Client.builder()
            .apiKey("AIzaSyDIElCTrrbKSzvsXKsctmls8B33vTXUTpM")
            .build();

    public static String perguntar(String pergunta) throws Exception{
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-1.5-flash",
                    pergunta,
                    null
            );
            return response.text();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
