package com.Accesos.CoffeeAndStudy;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class WireMockSmokeTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startServer() {
        wireMockServer = new WireMockServer(
                options().port(8089).usingFilesUnderDirectory("src/test/resources/wiremock")
        );
        wireMockServer.start();
    }

    @AfterAll
    static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldStartWireMockServer() {
        Assertions.assertTrue(wireMockServer.isRunning());
    }

    @Test
    void shouldReturnMockedCategory() throws Exception {
        URL url = new URL("http://localhost:8089/categories/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        connection.disconnect();

        Assertions.assertEquals(200, status);
        Assertions.assertTrue(response.toString().contains("\"name\":\"Cafe\""));
    }
}