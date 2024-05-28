package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Model.LocalDateDeSerializer;
import org.example.Model.LocalDateSerializer;
import org.example.Model.Student;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class StudentController {
    private static final String STUDENT_API_URL = "http://localhost:8080/api/items/5";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateDeSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .create();

    public static void callRegistrarGetStudentDetails() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(STUDENT_API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                //System.out.println("Response Body: " + responseBody);

                Student student = GSON.fromJson(responseBody, Student.class);
                if ("Emily".equals(student.getFirstName())) {
                    System.out.println("It's Emily");
                    System.out.println(student);
                    student.setCgpa(4.0);
                    makingEmilyStraightA(student.getStudentId(), student);
                }
            } else {
                System.out.println("GET request failed. Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makingEmilyStraightA(Long id, Student student) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String jsonRequestBody = GSON.toJson(student);
            System.out.println("jsonRequestBody");
            System.out.println(jsonRequestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(STUDENT_API_URL))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("POST request successful. Response Body: " + response.body());
            } else {
                System.out.println("POST request failed. Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
