package com.example.demo.service;

import lombok.extern.slf4j.Slf4j; // Import the Lombok logger
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j  // Lombok annotation to add the logger
@Service
public class PdfParsingService {

    private static final String DEEPSEEK_API_KEY = "e7aad2e20a0247f29668760188285ea7";  // Replace with your API key
    private static final String DEEPSEEK_ENDPOINT = "https://api.deepseek.ai/v1/chat/completions";  // Replace with the correct endpoint

    // Method to parse PDF using LLM API
    public Map<String, String> parsePdfWithLlm(MultipartFile file, String firstname, String dob) throws IOException {
        // Step 1: Extract text from PDF file (You'll need a PDF parser here)
        String pdfText = extractTextFromPdf(file);  // Replace with actual PDF parsing logic

        // Step 2: Create the prompt using extracted text and user inputs
        String prompt = createPrompt(pdfText, firstname, dob);

        // Step 3: Call the LLM API with the generated prompt
        String apiResponse = callDeepSeekAPI(prompt);

        // Step 4: Parse the response and extract required details (e.g., name, email, balance)
        Map<String, String> response = parseApiResponse(apiResponse);

        return response;
    }

    // Extract text from the PDF file (Implement your PDF extraction logic here)
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        // Placeholder - implement PDF parsing logic here
        return "Sample PDF text extracted here";  // Replace with actual PDF extraction
    }

    // Create prompt for LLM API call
    private String createPrompt(String pdfText, String firstname, String dob) {
        return "Extract the following details from the CASA PDF statement: " +
                "Name, Email, Opening Balance, Closing Balance, for user: " + firstname +
                " with Date of Birth: " + dob + ". Text: " + pdfText;
    }

    // Call the DeepSeek (LLM) API
    private String callDeepSeekAPI(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String json = "{\n" +
                "  \"model\": \"deepseek-chat\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}\n" +
                "  ],\n" +
                "  \"temperature\": 0.5,\n" +
                "  \"max_tokens\": 1000\n" +
                "}";

        // For OkHttp versions before 4.9.0, we manually create the request body
        RequestBody body = RequestBody.create(
                json, MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DEEPSEEK_ENDPOINT)
                .post(body)
                .addHeader("Authorization", "Bearer " + DEEPSEEK_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("DeepSeek API call failed: " + response);
                throw new IOException("API call failed: " + response);
            }

            String result = response.body().string();
            log.info("DeepSeek API response: " + result);
            return result;
        }
    }

    // Parse the API response to extract necessary data (such as Name, Email, etc.)
    private Map<String, String> parseApiResponse(String apiResponse) {
        Map<String, String> result = new HashMap<>();

        // Sample parsing (you can customize it based on the response structure)
        result.put("name", "John Doe");  // Placeholder - replace with actual parsing
        result.put("email", "john.doe@example.com");
        result.put("opening_balance", "1000.00");
        result.put("closing_balance", "1200.00");

        return result;
    }
}
