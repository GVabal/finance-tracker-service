package dev.vabalas.financetrackerservice.service;

import dev.vabalas.financetrackerservice.exception.EntryNotFoundException;
import dev.vabalas.financetrackerservice.model.Entry;
import dev.vabalas.financetrackerservice.repository.EntryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EntryService {
    private final EntryRepository entryRepository;

    public List<Entry> getAllEntries() {
        log.info("getAllEntries()");
        return entryRepository.findAll();
    }

    public Entry getEntry(String id) {
        log.info("getEntry({})", id);
        return entryRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("No entry with id " + id));
    }

    public Entry addEntry(Entry entry) {
        log.info("addEntry({})", entry);
        return entryRepository.save(entry);
    }

    public Entry editEntry(String id, Entry entry) {
        log.info("editEntry({}, {})", id, entry);
        if (entryRepository.existsById(id)) {
            entry.setId(id);
            return entryRepository.save(entry);
        }
        throw new EntryNotFoundException("No entry with id " + id);
    }

    public void removeEntry(String id) {
        log.info("removeEntry({})", id);
        Entry entry = getEntry(id);
        entryRepository.delete(entry);
    }
}
