package dev.vabalas.financetrackerservice.service;

import dev.vabalas.financetrackerservice.exception.EntryNotFoundException;
import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.repository.EntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EntryService {
    private final EntryRepository entryRepository;

    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    public Entry getEntry(String id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("No entry with id " + id));
    }

    public Entry addEntry(Entry entry) {
        return entryRepository.save(entry);
    }

    public Entry editEntry(String id, Entry entry) {
        if (entryRepository.existsById(id)) {
            entry.setId(id);
            return entryRepository.save(entry);
        }
        throw new EntryNotFoundException("No entry with id " + id);
    }

    public void removeEntry(String id) {
        Entry entry = getEntry(id);
        entryRepository.delete(entry);
    }
}
