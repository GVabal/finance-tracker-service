package dev.vabalas.financetrackerservice.service;

import dev.vabalas.financetrackerservice.exception.EntryNotFoundException;
import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.model.EntryType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntryServiceTest {

    @Autowired
    private EntryService entryService;

    @Test
    @Order(1)
    void getAllEntries_shouldReturnAllEntries() {
        List<Entry> entryList = entryService.getAllEntries();
        assertThat(entryList.size()).isEqualTo(5);
    }

    @Test
    @Order(2)
    void getEntry_shouldReturnCorrectEntry() {
        Entry entry = entryService.getEntry("2");
        assertThat(entry.getId()).isEqualTo("2");
        assertThat(entry.getEntryType()).isEqualTo(EntryType.EXPENSE);
        assertThat(entry.getTime()).isEqualTo("2021-03-02T13:30:00");
        assertThat(entry.getCategory()).isEqualTo("Food");
        assertThat(entry.getAmount()).isEqualTo(BigDecimal.valueOf(600));
    }

    @Test
    @Order(3)
    void getEntry_doesNotExist_shouldThrowEntryNotFoundException() {
        assertThrows(EntryNotFoundException.class, () -> {
            entryService.getEntry("X");
        });
    }

    @Test
    @Order(4)
    void addEntry_shouldReturnCorrectAddedEntry() {
        Entry newEntry = new Entry(EntryType.EXPENSE, LocalDateTime.now(),
                "Shopping", BigDecimal.valueOf(65.95));

        Entry savedEntry = entryService.addEntry(newEntry);
        newEntry.setId(savedEntry.getId());

        assertThat(entryService.getAllEntries().size()).isEqualTo(6);
        assertThat(savedEntry).isEqualTo(newEntry);
    }

    @Test
    @Order(5)
    void editEntry_shouldReturnCorrectEditedEntry() {
        Entry editedEntry = new Entry(EntryType.EXPENSE, LocalDateTime.now(),
                "Shopping", BigDecimal.valueOf(65.95));

        Entry updatedEntry = entryService.editEntry("2", editedEntry);
        editedEntry.setId("2");

        assertThat(updatedEntry).isEqualTo(editedEntry);
    }

    @Test
    @Order(6)
    void editEntry_doesNotExist_shouldThrowEntryNotFoundException() {
        assertThrows(EntryNotFoundException.class, () -> {
            entryService.editEntry("X", null);
        });
    }

    @Test
    @Order(7)
    void removeEntry_shouldDeleteCorrectEntry() {
        entryService.removeEntry("2");

        assertThrows(EntryNotFoundException.class, () -> {
            entryService.getEntry("2");
        });
    }

    @Test
    @Order(8)
    void removeEntry_doesNotExist_shouldThrowEntryNotFoundException() {
        assertThrows(EntryNotFoundException.class, () -> {
            entryService.removeEntry("X");
        });
    }
}
