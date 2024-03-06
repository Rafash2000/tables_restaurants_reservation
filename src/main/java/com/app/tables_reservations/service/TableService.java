package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    public List<Table> getTablesByRestaurantIdAndDateSortByDate(Long id, LocalDate date) {
        return tableRepository.findTablesByRestaurantIdAndDateOrderByTime(id, date);
    }

    public Table save(Table table) { return tableRepository.save(table); }

    public List<Table> findFreeTable(Long restaurantId, LocalDate date, int numberOfPeople) {
        return getTablesByRestaurantIdAndDateSortByDate(restaurantId, date)
                .stream()
                .filter(table -> table.getAvailability().equals(Availability.available))
                .filter(table -> (table.getCapacity() >= numberOfPeople) && (table.getCapacity() - numberOfPeople) <= 1)
                .toList();
    }
}
