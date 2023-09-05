package com.app.tables_reservations.repository;

import com.app.tables_reservations.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findTablesByRestaurantId(Long id);

    List<Table> findTablesByRestaurantIdOrderByCapacity(Long id);

    List<Table> findTablesByRestaurantIdAndDateOrderByCapacity(Long id, LocalDateTime dateTime);

    List<Table> findTablesByRestaurantIdAndDate(Long id, LocalDateTime dateTime);
}
