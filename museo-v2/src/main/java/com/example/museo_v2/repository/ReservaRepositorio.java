package com.example.museo_v2.repository;

import com.example.museo_v2.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
    
    // Sumar todo el dinero recaudado
    @Query("SELECT COALESCE(SUM(r.totalPagar), 0) FROM Reserva r")
    BigDecimal sumarIngresosTotales();
}