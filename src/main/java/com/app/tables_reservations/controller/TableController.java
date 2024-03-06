package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.dto.CreateTableDto;
import com.app.tables_reservations.model.dto.GetTableDto;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tables")
@CrossOrigin
public class TableController {
    private final TableService tableService;

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Table>> getAllTables() {
        try {
            return ResponseEntity.ok(tableService.getAllTables());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table> getTableById(@PathVariable Long id) {
        try {
            if (!tableService.existTable(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(tableService.getTableById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Table> addTable(@RequestBody CreateTableDto createTableDto) {
        try {
            if (!restaurantService.existRestaurant(createTableDto.restaurantId())) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(tableService.addTable(createTableDto.toTable(restaurantService.getRestaurantById(createTableDto.restaurantId()))));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable Long id) {
        try {
            if (!tableService.existTable(id)) {
                return ResponseEntity.notFound().build();
            }

            tableService.deleteTable(id);

            return ResponseEntity.ok("Table with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{restaurantId}/{date}/{numberOfPeople}")
    public ResponseEntity<List<GetTableDto>> getFreeTablesByRestaurantIdAndDate(
            @PathVariable Long restaurantId, @PathVariable LocalDate date, @PathVariable int numberOfPeople) {
        try {
            if (!restaurantService.existRestaurant(restaurantId)) {
                return ResponseEntity.notFound().build();
            }

            var tables = tableService.findFreeTable(restaurantId, date, numberOfPeople)
                    .stream()
                    .map(Table::toGetTableDto)
                    .collect(Collectors.toMap(
                            GetTableDto::time,
                            getTableDto -> getTableDto,
                            (existing, replacement) -> existing))
                    .values()
                    .stream()
                    .sorted(Comparator.comparing(GetTableDto::time))
                    .toList();

            return ResponseEntity.ok(tables);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
