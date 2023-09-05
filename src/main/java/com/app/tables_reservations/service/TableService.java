package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public List<Table> getAllTables() { return tableRepository.findAll(); }

    public Table getTableById(Long id) { return tableRepository.findById(id).orElse(null); }

    public Table addTable(Table table) { return tableRepository.save(table); }

    public boolean existTable(Long id) { return tableRepository.existsById(id); }

    public void deleteTable(Long id) { tableRepository.deleteById(id); }

    public List<Table> getTablesByRestaurantId(Long id) { return tableRepository.findTablesByRestaurantId(id); }

    public List<Table> getTablesByRestaurantIdSortByCapacity(Long id) {
        return tableRepository.findTablesByRestaurantIdOrderByCapacity(id);
    }

    public List<Table> getTablesByRestaurantIdAndDateSortByCapacity(Long id, LocalDateTime dateTime) {
        return tableRepository.findTablesByRestaurantIdAndDateOrderByCapacity(id, dateTime);
    }

    public Table save(Table table) { return tableRepository.save(table); }

    public List<Table> getTablesByRestaurantIdAndDate(Long id, LocalDateTime dateTime) {
        return tableRepository.findTablesByRestaurantIdAndDate(id, dateTime);
    }

    public List<Table> findFreeTable(Long restaurantId, LocalDateTime dateTime, int numberOfPeople) {
        return getTablesByRestaurantIdAndDateSortByCapacity(restaurantId, dateTime)
                .stream()
                .filter(table -> table.getAvailability().equals(Availability.available))
                .filter(table -> table.getCapacity() >= numberOfPeople)
                .filter(table -> (table.getCapacity() - numberOfPeople) <= 1)
                .toList();
    }
}
