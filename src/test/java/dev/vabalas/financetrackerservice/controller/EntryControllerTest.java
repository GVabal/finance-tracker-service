package dev.vabalas.financetrackerservice.controller;

import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.model.EntryDto;
import dev.vabalas.financetrackerservice.model.EntryType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void getALlEntries_shouldReturnAllEntries() {
        ResponseEntity<List<Entry>> result = restTemplate.exchange(
                baseUrl(), HttpMethod.GET, null,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertThat(result.getBody().size()).isEqualTo(5);
    }

    @Test
    @Order(2)
    void getEntryById_shouldReturnCorrectEntry() {
        ResponseEntity<Entry> result = restTemplate.exchange(
                baseUrlWith("1"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertThat(result.getBody().getId()).isEqualTo("1");
        assertThat(result.getBody().getType()).isEqualTo(EntryType.INCOME);
        assertThat(result.getBody().getTime()).isEqualTo("2021-03-01T11:30:00");
        assertThat(result.getBody().getCategory()).isEqualTo("Work");
        assertThat(result.getBody().getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    @Order(3)
    void getEntryById_notFound_shouldReturnCorrectError() {
        ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                baseUrlWith("X"), HttpMethod.GET, null,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is4xxClientError());
        assertThat(result.getBody().get("message")).isNotBlank();
    }

    @Test
    @Order(4)
    void addEntry_shouldReturnCorrectAddedEntry() {
        EntryDto entryDto = new EntryDto("EXPENSE", "2021-03-01T11:30:00",
                "Food", BigDecimal.valueOf(64.95));
        HttpEntity<EntryDto> request = new HttpEntity<>(entryDto, null);

        ResponseEntity<Entry> result = restTemplate.exchange(
                baseUrl(), HttpMethod.POST, request,
                new ParameterizedTypeReference<>(){});
        Entry newEntry = entryDto.toEntry();
        newEntry.setId(result.getBody().getId());

        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertThat(result.getBody()).isEqualTo(newEntry);
    }

    @Test
    @Order(5)
    void addEntry_invalidEntry_shouldReturnCorrectError() {
        EntryDto entryDto = new EntryDto("wrong", "wrong",
                "", BigDecimal.valueOf(0));
        HttpEntity<EntryDto> request = new HttpEntity<>(entryDto, null);

        ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                baseUrl(), HttpMethod.POST, request,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is4xxClientError());
        assertThat(result.getBody().get("message")).isNotBlank();
    }

    @Test
    @Order(6)
    void editEntry_shouldReturnCorrectEditedEntry() {
        EntryDto entryDto = new EntryDto("EXPENSE", "2021-03-01T11:30:00",
                "Food", BigDecimal.valueOf(64.95));
        HttpEntity<EntryDto> request = new HttpEntity<>(entryDto, null);

        ResponseEntity<Entry> result = restTemplate.exchange(
                baseUrlWith("1"), HttpMethod.PUT, request,
                new ParameterizedTypeReference<>(){});
        Entry newEntry = entryDto.toEntry();
        newEntry.setId("1");

        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertThat(result.getBody()).isEqualTo(newEntry);
    }

    @Test
    @Order(7)
    void editEntry_invalidEntry_shouldReturnCorrectError() {
        EntryDto entryDto = new EntryDto("wrong", "wrong",
                "", BigDecimal.valueOf(0));
        HttpEntity<EntryDto> request = new HttpEntity<>(entryDto, null);

        ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                baseUrlWith("1"), HttpMethod.PUT, request,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is4xxClientError());
        assertThat(result.getBody().get("message")).isNotBlank();
    }

    @Test
    @Order(8)
    void editEntry_doesNotExist_shouldReturnCorrectError() {
        EntryDto entryDto = new EntryDto("EXPENSE", "2021-03-01T11:30:00",
                "Food", BigDecimal.valueOf(64.95));
        HttpEntity<EntryDto> request = new HttpEntity<>(entryDto, null);

        ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                baseUrlWith("X"), HttpMethod.PUT, request,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is4xxClientError());
        assertThat(result.getBody().get("message")).isNotBlank();
    }

    @Test
    @Order(9)
    void deleteEntry_shouldDeleteCorrectEntry() {
        ResponseEntity<Void> result = restTemplate.exchange(
                baseUrlWith("1"), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<>(){});
        ResponseEntity<Map<String, String>> resultAfterDeletion = restTemplate.exchange(
                baseUrlWith("1"), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(resultAfterDeletion.getStatusCode().is4xxClientError());
    }

    @Test
    @Order(10)
    void deleteEntry_doesNotExist_shouldReturnCorrectError() {
        ResponseEntity<Map<String, String>> result = restTemplate.exchange(
                baseUrlWith("X"), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<>(){});

        assertTrue(result.getStatusCode().is4xxClientError());
        assertThat(result.getBody().get("message")).isNotBlank();
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/entries/";
    }

    private String baseUrlWith(String id) {
        return baseUrl() + id;
    }
}