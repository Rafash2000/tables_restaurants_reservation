package com.app.tables_reservations.repository;

import com.app.tables_reservations.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findTablesByRestaurantIdAndDateOrderByTime(Long id, LocalDate date);
}
