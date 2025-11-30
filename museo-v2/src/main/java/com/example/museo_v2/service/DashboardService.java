package com.example.museo_v2.service;

import com.example.museo_v2.repository.EventoRepositorio;
import com.example.museo_v2.repository.ProductoInventarioRepository;
import com.example.museo_v2.repository.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private EventoRepositorio eventoRepo;

    @Autowired
    private ReservaRepositorio reservaRepo;

    @Autowired
    private ProductoInventarioRepository productoRepo;

    public Map<String, Object> obtenerMetricas() {
        Map<String, Object> metricas = new HashMap<>();

        // 1. Ingresos Totales
        BigDecimal ingresos = reservaRepo.sumarIngresosTotales();
        metricas.put("ingresosTotales", ingresos);

        // 2. Eventos Futuros (Próximos 30 días o total futuro)
        long eventosFuturos = eventoRepo.countByFechaInicioAfter(LocalDate.now());
        metricas.put("eventosFuturos", eventosFuturos);

        // 3. Productos con Bajo Stock (Menos de 5 unidades)
        long bajoStock = productoRepo.countByStockDisponibleLessThan(5);
        metricas.put("bajoStock", bajoStock);

        // 4. Total de Reservas
        long totalReservas = reservaRepo.count();
        metricas.put("totalReservas", totalReservas);

        return metricas;
    }
}