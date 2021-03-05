package dev.vabalas.financetrackerservice.controller;

import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.model.EntryDto;
import dev.vabalas.financetrackerservice.service.EntryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/entries")
public class EntryController {
    private final EntryService entryService;

    @GetMapping
    public List<Entry> getALlEntries() {
        return entryService.getAllEntries();
    }

    @GetMapping("{id}")
    public Entry getEntryById(@PathVariable String id) {
        return entryService.getEntry(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry addEntry(@RequestBody @Valid EntryDto entryDto) {
        return entryService.addEntry(entryDto.toEntry());
    }

    @PutMapping("{id}")
    public Entry editEntry(@PathVariable String id, @RequestBody @Valid EntryDto entryDto) {
        return entryService.editEntry(id, entryDto.toEntry());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable String id) {
        entryService.removeEntry(id);
    }
}
