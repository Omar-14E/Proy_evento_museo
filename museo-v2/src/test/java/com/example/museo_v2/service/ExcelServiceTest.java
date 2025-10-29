package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Sala;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.math.*;

public class ExcelServiceTest {

    private ExcelService excelService;

    @BeforeEach
    void setUp(){
        excelService = new ExcelService();
    }

    @Test 
    void crearExcelDeEventos_DebeGenerarElContenidoCorrecto() throws Exception{
        Sala sala = new Sala();
        sala.setNombre("sala de Niños");
        
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setNombre("Evento 1");
        evento1.setFechaInicio(LocalDate.of(2025, 10, 27));
        evento1.setCostoEntrada(new BigDecimal("42.50"));
        evento1.setSala(sala);

        List<Evento> eventos = Arrays.asList(evento1);
        ByteArrayInputStream bais = excelService.crearExcelDeEventos(eventos);
        assertNotNull(bais);

        try(Workbook workbook = new XSSFWorkbook(bais)){
            Sheet sheet = workbook.getSheet("Eventos");
            assertNotNull(sheet);

            Row headeRow = sheet.getRow(0);
            assertEquals("ID", headeRow.getCell(0).getStringCellValue());
            assertEquals("Nombre", headeRow.getCell(1).getStringCellValue());
            assertEquals("Fecha Inicio", headeRow.getCell(2).getStringCellValue());
            assertEquals("Costo Entrada", headeRow.getCell(3).getStringCellValue());
            assertEquals("Sala", headeRow.getCell(4).getStringCellValue());

            Row datRow1 = sheet.getRow(1);
            assertEquals(1.0, datRow1.getCell(0).getNumericCellValue());
            assertEquals("Evento 1", datRow1.getCell(1).getStringCellValue());
            assertEquals("2025-10-27", datRow1.getCell(2).getStringCellValue());
            assertEquals(42.50, datRow1.getCell(3).getNumericCellValue());
            assertEquals("sala de Niños", datRow1.getCell(4).getStringCellValue());
        }
    }

    @Test
    void crearExcelDeSalas_DebeGenerarContenidoCorrecto() throws Exception {
        Sala sala1 = new Sala("Sala A", 100, "Piso 1", "Desc A", null);
        sala1.setId(1);
        List<Sala> salas = Arrays.asList(sala1);

        ByteArrayInputStream bais = excelService.crearExcelDeSalas(salas);
        assertNotNull(bais);

        try (Workbook workbook = new XSSFWorkbook(bais)) {
            Sheet sheet = workbook.getSheet("Salas");
            assertNotNull(sheet);

            Row headerRow = sheet.getRow(0);
            assertEquals("ID", headerRow.getCell(0).getStringCellValue());
            assertEquals("Nombre", headerRow.getCell(1).getStringCellValue());
            assertEquals("Capacidad", headerRow.getCell(2).getStringCellValue());
            assertEquals("Ubicación", headerRow.getCell(3).getStringCellValue());
            assertEquals("Descripción", headerRow.getCell(4).getStringCellValue());

            Row dataRow1 = sheet.getRow(1);
            assertEquals(1.0, dataRow1.getCell(0).getNumericCellValue()); 
            assertEquals("Sala A", dataRow1.getCell(1).getStringCellValue()); 
            assertEquals(100.0, dataRow1.getCell(2).getNumericCellValue()); 
            assertEquals("Piso 1", dataRow1.getCell(3).getStringCellValue()); 
            assertEquals("Desc A", dataRow1.getCell(4).getStringCellValue()); 
        }
    }
}

