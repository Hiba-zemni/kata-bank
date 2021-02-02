package fr.sg.bankaccount.controller;

import fr.sg.bankaccount.BankAccountApplication;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountControllerTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testgetAllOperation() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "api/accounts/100/operations",
                HttpMethod.GET, entity, String.class);

        String expected = "[\n" +
                "    {\n" +
                "        \"operationID\": 100,\n" +
                "        \"type\": \"DEPOSIT\",\n" +
                "        \"amount\": 1000,\n" +
                "        \"date\": \"2021-01-16T00:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"operationID\": 101,\n" +
                "        \"type\": \"WITHDRAWAL\",\n" +
                "        \"amount\": 400,\n" +
                "        \"date\": \"2021-02-01T00:00:00\"\n" +
                "    }\n" +
                "]";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void testDepositOperation() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/accounts/100/balance/update?amount=300&type=DEPOSIT",
                HttpMethod.POST, entity, String.class);

        String expected = "{\n" +
                "    \"accountId\": 100,\n" +
                "    \"accountNumber\": 1000,\n" +
                "    \"balance\": 5300,\n" +
                "    \"operationList\": [\n" +
                "        {\n" +
                "            \"operationID\": 100,\n" +
                "            \"type\": \"DEPOSIT\",\n" +
                "            \"amount\": 1000,\n" +
                "            \"date\": \"2021-01-16T00:00:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"operationID\": 101,\n" +
                "            \"type\": \"WITHDRAWAL\",\n" +
                "            \"amount\": 400,\n" +
                "            \"date\": \"2021-02-01T00:00:00\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

}
