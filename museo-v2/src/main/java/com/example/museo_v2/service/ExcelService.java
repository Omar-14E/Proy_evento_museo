package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.model.Sala;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    /**
     * Crea un archivo Excel en memoria a partir de una lista de eventos.
     * @param eventos La lista de eventos a exportar.
     * @return Un ByteArrayInputStream que contiene el archivo Excel.
     */
    public ByteArrayInputStream crearExcelDeEventos(List<Evento> eventos) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Eventos");

            Row headerRow = sheet.createRow(0);

            String[] headers = {"ID", "Nombre", "Fecha Inicio", "Costo Entrada", "Sala"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Evento evento : eventos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(evento.getId());
                row.createCell(1).setCellValue(evento.getNombre());
                
                row.createCell(2).setCellValue(evento.getFechaInicio().toString()); 
                
                row.createCell(3).setCellValue(evento.getCostoEntrada().doubleValue());
                
                row.createCell(4).setCellValue(evento.getSala().getNombre());
            }
            
            // Autoajustar columnas
            for(int i=0; i<headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel de Eventos", e);
        }
    }

    /**
     * Crea un archivo Excel en memoria a partir de una lista de salas.
     * @param salas La lista de salas a exportar.
     * @return Un ByteArrayInputStream que contiene el archivo Excel.
     */
    public ByteArrayInputStream crearExcelDeSalas(List<Sala> salas) {
        try (Workbook workbook = new XSSFWorkbook()) {
            
            Sheet sheet = workbook.createSheet("Salas");
            
            Row headerRow = sheet.createRow(0);

            String[] headers = {"ID","Nombre","Capacidad","Ubicación","Descripción"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for(Sala sala: salas){
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(sala.getId());
                row.createCell(1).setCellValue(sala.getNombre());
                row.createCell(2).setCellValue(sala.getCapacidad());
                row.createCell(3).setCellValue(sala.getUbicacion());
                row.createCell(4).setCellValue(sala.getDescripcion());
            }

            for(int i=0; i<headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch(IOException e){
            throw new RuntimeException("Error al generar el archivo Excel de Salas", e);
        }
    }

    /**
     * Crea un archivo Excel en memoria a partir de una lista de productos de inventario.
     * @param productos La lista de productos a exportar.
     * @return Un ByteArrayInputStream que contiene el archivo Excel.
     */
    public ByteArrayInputStream crearExcelDeInventario(List<ProductoInventario> productos) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");
            Row headerRow = sheet.createRow(0);

            // Definir cabeceras
            String[] headers = {"ID", "Nombre", "Tipo", "Stock Total", "Stock Disponible"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Llenar datos
            int rowIdx = 1;
            for (ProductoInventario p : productos) {
                Row row = sheet.createRow(rowIdx++);
                // Usamos validaciones simples por si algún dato viene nulo
                row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(1).setCellValue(p.getNombre() != null ? p.getNombre() : "");
                row.createCell(2).setCellValue(p.getTipo() != null ? p.getTipo() : "");
                row.createCell(3).setCellValue(p.getStockTotal() != null ? p.getStockTotal() : 0);
                row.createCell(4).setCellValue(p.getStockDisponible() != null ? p.getStockDisponible() : 0);
            }
            
            // Autoajustar columnas
            for(int i=0; i<headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error al generar Excel de Inventario", e);
        }
    }
}