package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tables")
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

    @PostMapping("/addTable/{restaurantId}")
    public ResponseEntity<Table> addTable(@RequestBody Table table, @PathVariable Long restaurantId) {
        try {
            if (!restaurantService.existRestaurant(restaurantId)) {
                return ResponseEntity.notFound().build();
            }
            table.setRestaurant(restaurantService.getRestaurantById(restaurantId));
            return ResponseEntity.ok(tableService.addTable(table));
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
}
