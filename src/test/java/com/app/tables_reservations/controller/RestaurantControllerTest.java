package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.dto.GetRestaurantDto;
import com.app.tables_reservations.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Comparator;
import java.util.List;

import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {
    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @Test
    public void testGetAllRestaurantsSuccess() {
        when(restaurantService.getAllRestaurants()).thenReturn(RESTAURANT_LIST);

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetAllRestaurantsDataAccessException() {
        when(restaurantService.getAllRestaurants()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNewRestaurantsSuccess() {
        var expectedDtos = RESTAURANT_DTO_LIST
                .stream()
                .sorted(Comparator.comparing(GetRestaurantDto::id)
                        .reversed())
                .toList();
        when(restaurantService.getAllRestaurants()).thenReturn(RESTAURANT_LIST);

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getNewRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
    }

    @Test
    public void testGetNewRestaurantsDataAccessException() {
        when(restaurantService.getAllRestaurants()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getNewRestaurants();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetRecommendedRestaurantsSuccess() {
        var recommendedId = 1L;

        when(restaurantService.getRestaurantById(recommendedId)).thenReturn(RESTAURANT_1);

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getRecommendedRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }


    @Test
    public void testGetRecommendedRestaurantsDataAccessException() {
        var id = 1L;
        when(restaurantService.getRestaurantById(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetRestaurantDto>> response = restaurantController.getRecommendedRestaurants();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetRestaurantByIdSuccess() {
        var id = 1L;

        when(restaurantService.existRestaurant(id)).thenReturn(true);
        when(restaurantService.getRestaurantById(id)).thenReturn(RESTAURANT_1);

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESTAURANT_1, response.getBody());
    }

    @Test
    public void testGetRestaurantByIdNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetRestaurantByIdDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddRestaurantSuccess() {
        when(restaurantService.addRestaurant(any(Restaurant.class))).thenReturn(RESTAURANT_1);

        ResponseEntity<Restaurant> response = restaurantController.addRestaurant(CREATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddRestaurantDataAccessException() {
        when(restaurantService.addRestaurant(any(Restaurant.class))).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Restaurant> response = restaurantController.addRestaurant(CREATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteRestaurantSuccess() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(true);

        ResponseEntity<String> response = restaurantController.deleteRestaurant(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restaurant with ID: 1 deleted successfully", response.getBody());
        verify(restaurantService).deleteRestaurant(id);
    }

    @Test
    public void testDeleteRestaurantNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<String> response = restaurantController.deleteRestaurant(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(restaurantService, never()).deleteRestaurant(id);
    }

    @Test
    public void testDeleteRestaurantDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(true);
        doThrow(new DataAccessException("Database Error") {}).when(restaurantService).deleteRestaurant(id);

        ResponseEntity<String> response = restaurantController.deleteRestaurant(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}