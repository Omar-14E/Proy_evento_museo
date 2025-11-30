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

/**
 * Servicio encargado de calcular métricas generales para el panel de control.
 * Obtiene información a partir de eventos, reservas y productos del inventario.
 */
@Service
public class DashboardService {

    @Autowired
    private EventoRepositorio eventoRepo;

    @Autowired
    private ReservaRepositorio reservaRepo;

    @Autowired
    private ProductoInventarioRepository productoRepo;

    /**
     * Obtiene un conjunto de métricas relacionadas con la actividad del museo.
     * <p>
     * Métricas incluidas:
     * <ul>
     *     <li><b>ingresosTotales</b>: suma total generada por reservas</li>
     *     <li><b>eventosFuturos</b>: cantidad de eventos cuya fecha de inicio es posterior a hoy</li>
     *     <li><b>bajoStock</b>: cantidad de productos con stock disponible menor a 5</li>
     *     <li><b>totalReservas</b>: número total de reservas registradas</li>
     * </ul>
     *
     * @return un mapa con las métricas calculadas
     */
    public Map<String, Object> obtenerMetricas() {
        Map<String, Object> metricas = new HashMap<>();

        BigDecimal ingresos = reservaRepo.sumarIngresosTotales();
        metricas.put("ingresosTotales", ingresos);

        long eventosFuturos = eventoRepo.countByFechaInicioAfter(LocalDate.now());
        metricas.put("eventosFuturos", eventosFuturos);

        long bajoStock = productoRepo.countByStockDisponibleLessThan(5);
        metricas.put("bajoStock", bajoStock);

        long totalReservas = reservaRepo.count();
        metricas.put("totalReservas", totalReservas);

        return metricas;
    }
}
