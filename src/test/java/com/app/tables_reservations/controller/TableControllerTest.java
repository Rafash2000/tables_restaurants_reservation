package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.dto.GetTableDto;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableControllerTest {
    @Mock
    private TableService tableServiceMock;

    @Mock
    private RestaurantService restaurantServiceMock;

    @InjectMocks
    private TableController tableController;

    @Test
    public void testGetAllTablesSuccess() {
        when(tableServiceMock.getAllTables()).thenReturn(TABLE_LIST);

        ResponseEntity<List<Table>> response = tableController.getAllTables();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetAllTablesDataAccessException() {
        when(tableServiceMock.getAllTables()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<Table>> response = tableController.getAllTables();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetTableByIdSuccess() {
        var id = 1L;
        when(tableServiceMock.existTable(id)).thenReturn(true);
        when(tableServiceMock.getTableById(id)).thenReturn(TABLE_1);

        ResponseEntity<Table> response = tableController.getTableById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TABLE_1, response.getBody());
    }

    @Test
    public void testGetTableByIdNotFound() {
        var id = 1L;
        when(tableServiceMock.existTable(id)).thenReturn(false);

        ResponseEntity<Table> response = tableController.getTableById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTableSuccess() {
        var restaurantId = 1L;

        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(true);
        when(restaurantServiceMock.getRestaurantById(restaurantId)).thenReturn(RESTAURANT_1);
        when(tableServiceMock.addTable(any(Table.class))).thenReturn(TABLE_1);

        ResponseEntity<Table> response = tableController.addTable(CREATE_TABLE_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TABLE_1, response.getBody());
    }

    @Test
    public void testAddTableRestaurantNotFound() {
        var restaurantId = CREATE_TABLE_DTO_1.restaurantId();

        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(false);

        ResponseEntity<Table> response = tableController.addTable(CREATE_TABLE_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTableDataAccessException() {
        var restaurantId = CREATE_TABLE_DTO_1.restaurantId();

        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(true);
        when(tableServiceMock.addTable(any(Table.class))).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Table> response = tableController.addTable(CREATE_TABLE_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteTableSuccess() {
        var id = 1L;
        when(tableServiceMock.existTable(id)).thenReturn(true);

        ResponseEntity<String> response = tableController.deleteTable(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Table with ID: 1 deleted successfully", response.getBody());
        verify(tableServiceMock).deleteTable(id);
    }

    @Test
    public void testDeleteTableNotFound() {
        var id = 1L;
        when(tableServiceMock.existTable(id)).thenReturn(false);

        ResponseEntity<String> response = tableController.deleteTable(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tableServiceMock, never()).deleteTable(id);
    }

    @Test
    public void testDeleteTableDataAccessException() {
        var id = 1L;
        when(tableServiceMock.existTable(id)).thenReturn(true);
        doThrow(new DataAccessException("Database Error") {}).when(tableServiceMock).deleteTable(id);

        ResponseEntity<String> response = tableController.deleteTable(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetFreeTablesByRestaurantIdAndDateSuccess() {
        var restaurantId = 1L;
        var date = LocalDate.now().plusDays(2);
        var numberOfPeople = 4;
        var freeTables = List.of(TABLE_5);
        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(true);
        when(tableServiceMock.findFreeTable(restaurantId, date, numberOfPeople)).thenReturn(freeTables);

        ResponseEntity<List<GetTableDto>> response = tableController.getFreeTablesByRestaurantIdAndDate(restaurantId, date, numberOfPeople);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetFreeTablesByRestaurantIdAndDateNotFound() {
        var restaurantId = 1L;
        var date = LocalDate.now();
        var numberOfPeople = 4;
        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(false);

        ResponseEntity<List<GetTableDto>> response = tableController.getFreeTablesByRestaurantIdAndDate(restaurantId, date, numberOfPeople);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFreeTablesByRestaurantIdAndDateDataAccessException() {
        var restaurantId = 1L;
        var date = LocalDate.now();
        var numberOfPeople = 4;
        when(restaurantServiceMock.existRestaurant(restaurantId)).thenReturn(true);
        when(tableServiceMock.findFreeTable(restaurantId, date, numberOfPeople)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetTableDto>> response = tableController.getFreeTablesByRestaurantIdAndDate(restaurantId, date, numberOfPeople);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}