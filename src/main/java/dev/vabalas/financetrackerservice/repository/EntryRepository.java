package dev.vabalas.financetrackerservice.repository;

import dev.vabalas.financetrackerservice.model.Entry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntryRepository extends CrudRepository<Entry, String> {
    List<Entry> findAll();
}
