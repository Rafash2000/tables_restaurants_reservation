package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private TableRepository tableRepository;

    public List<Table> getAllTables() { return tableRepository.findAll(); }

    public Table getTableById(Long id) { return tableRepository.findById(id).orElse(null); }

    public Table addTable(Table table) { return tableRepository.save(table); }

    public boolean existTable(Long id) { return tableRepository.existsById(id); }

    public void deleteTable(Long id) { tableRepository.deleteById(id); }
}
