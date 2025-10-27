package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
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

        // 1. Crear el libro de trabajo (Workbook)
        // Usamos un 'try-with-resources' para asegurar que el workbook se cierre
        // y libere la memoria, incluso si ocurre un error.
        // XSSFWorkbook es para el formato moderno .xlsx
        try (Workbook workbook = new XSSFWorkbook()) {

            // 2. Crear una nueva hoja en el libro
            Sheet sheet = workbook.createSheet("Eventos");

            // 3. Crear la fila de cabecera (fila 0)
            Row headerRow = sheet.createRow(0);

            // 4. Definir los títulos de las columnas
            String[] headers = {"ID", "Nombre", "Fecha Inicio", "Costo Entrada", "Sala"};

            // 5. Llenar la fila de cabecera con los títulos
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                // (Opcional: aquí se podría añadir estilo a la cabecera, como negrita)
            }

            // 6. Llenar las filas de datos
            int rowIdx = 1; // Empezamos a escribir los datos desde la fila 1
            for (Evento evento : eventos) {
                // Crear una nueva fila por cada evento en la lista
                Row row = sheet.createRow(rowIdx++);

                // Llenar las celdas de la fila con los datos del evento
                row.createCell(0).setCellValue(evento.getId());
                row.createCell(1).setCellValue(evento.getNombre());
                
                // Nota: La fecha se guarda como String. Para un mejor formato
                // de fecha en Excel, se requeriría aplicar un CellStyle.
                row.createCell(2).setCellValue(evento.getFechaInicio().toString()); 
                
                // Aseguramos que el costo (BigDecimal) se escriba como un número (double)
                row.createCell(3).setCellValue(evento.getCostoEntrada().doubleValue());
                
                // Obtenemos el nombre de la sala relacionada
                row.createCell(4).setCellValue(evento.getSala().getNombre());
            }
            
            // (Opcional: autoajustar el tamaño de las columnas)
            // for(int i=0; i<headers.length; i++) {
            //     sheet.autoSizeColumn(i);
            // }

            // 7. Escribir el libro de trabajo en un flujo de bytes en memoria
            // ByteArrayOutputStream es un stream que guarda los datos en un array de bytes.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out); // El workbook "escribe" su contenido en el 'out'

            // 8. Devolver los bytes como un InputStream
            // El controlador necesita un InputStream para enviar los datos al cliente.
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            // 9. Manejo de errores
            // Si algo falla (ej. error de I/O), lanzamos una RuntimeException.
            // Esto será capturado por el manejador de excepciones de Spring.
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

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
        }catch(Exception e){
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }
}