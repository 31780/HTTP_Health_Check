package com.httphc.healthcheckservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class HealthCheckServiceApplication {

	private static final String TARGET_URL = "https://www.example.com";
    private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(HealthCheckServiceApplication.class, args);
	}

	@Scheduled(fixedRate = 10000)  // Check every 10 seconds
    public void performHealthCheck() {
        try {
            restTemplate.getForEntity(TARGET_URL, String.class);
            System.out.println("Server health: GOOD");
        } catch (HttpClientErrorException e) {
            alert("Failed: HTTP error code : " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            alert("Failed: Resource Access Exception - " + e.getMessage());
        } catch (Exception e) {
            alert("Failed: An unexpected error occurred - " + e.getMessage());
        }
    }

    private void alert(String message) {
        System.err.println("ALERT: " + message);
        // Implement additional alerting logic here (e.g., sending an email, SMS, etc.)
    }

}
