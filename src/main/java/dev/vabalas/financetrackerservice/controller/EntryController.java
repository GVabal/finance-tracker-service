package dev.vabalas.financetrackerservice.controller;

import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.model.EntryDto;
import dev.vabalas.financetrackerservice.service.EntryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/entries")
public class EntryController {
    private final EntryService entryService;

    @GetMapping
    public List<Entry> getALlEntries() {
        log.info("GET all");
        return entryService.getAllEntries();
    }

    @GetMapping("{id}")
    public Entry getEntryById(@PathVariable String id) {
        log.info("GET one with id {}", id);
        return entryService.getEntry(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry addEntry(@RequestBody @Valid EntryDto entryDto) {
        log.info("POST new {}", entryDto);
        return entryService.addEntry(entryDto.toEntry());
    }

    @PutMapping("{id}")
    public Entry editEntry(@PathVariable String id, @RequestBody @Valid EntryDto entryDto) {
        log.info("PUT one with id {} - {}", id, entryDto);
        return entryService.editEntry(id, entryDto.toEntry());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable String id) {
        log.info("DELETE one with id {}", id);
        entryService.removeEntry(id);
    }
}
