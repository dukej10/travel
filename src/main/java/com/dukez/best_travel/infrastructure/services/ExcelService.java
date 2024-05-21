package com.dukez.best_travel.infrastructure.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.dukez.best_travel.domain.entities.jpa.CustomerEntity;
import com.dukez.best_travel.domain.repositories.jpa.CustomerRepository;
import com.dukez.best_travel.infrastructure.abstract_service.IReportService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelService implements IReportService {

    public final CustomerRepository customerRepository;

    /**
     * Método que lee el archivo de excel
     */
    @Override
    public byte[] readFile() {
        try {
            this.createReport();
            var path = Paths.get(REPORTS_PATH, String.format(FILE_NAME, LocalDate.now().getMonth())).toAbsolutePath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void createReport() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        // crear hoja de excel
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

        // Definir el ancho de las columnas
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 5000);

        // Configuración fila de celdas del header
        // Estilo celda del header
        XSSFRow header = sheet.createRow(0);

        XSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
        // Crear celdas del header
        this.createHeaderCells(header, headerStyle);

        // Crear el estilo de las otras celdas
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        // Insertar los datos en el archivo de excel
        this.insertData(sheet, style);
        // Guardar el reporte en local
        this.saveReportInLocal(workbook);

    }

    /**
     * Método que guarda el reporte en local
     * 
     * @param workbook Libro de excel
     */
    private void saveReportInLocal(XSSFWorkbook workbook) {
        var report = new File(String.format(REPORTS_PATH_WITH_NAME, LocalDate.now().getMonth()));
        var path = report.getAbsolutePath();
        var fileLocation = path + FILE_TYPE;
        try (var outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            log.error("Error creating the report", e);
            throw new RuntimeException();
        }
    }

    /**
     * Método que crea las celdas del header
     * 
     * @param header      Fila en la que se insertarán las celdas
     * @param headerStyle Estilo de las celdas
     */
    private void createHeaderCells(XSSFRow header, XSSFCellStyle headerStyle) {
        XSSFCell headerCell = header.createCell(0);
        headerCell.setCellValue(COLUMN_CUSTOMER_ID);
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue(COLUMN_CUSTOMER_NAME);
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue(COLUMN_CUSTOMER_PURCHASES);
        headerCell.setCellStyle(headerStyle);
    }

    /**
     * Método que crea el estilo del header
     * 
     * @param workbook Libro de excel
     * @return Estilo del header
     */
    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = workbook.createFont();
        font.setFontName(FONT_TYPE);
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * Método que inserta los datos en el archivo de excel
     * 
     * @param sheet Hoja de excel en la que se insertarán los datos
     * @param style Estilo de las celdas
     */
    private void insertData(XSSFSheet sheet, XSSFCellStyle style) {
        var customers = this.customerRepository.findAll();
        var rowPos = 1;
        for (CustomerEntity customer : customers) {
            var row = sheet.createRow(rowPos);
            // Celda COLUMN_CUSTOMER_ID de la fila correspondiente
            var cell = row.createCell(0);
            cell.setCellValue(customer.getDni());
            cell.setCellStyle(style);

            // Celda COLUMN_CUSTOMER_NAME de la fila correspondiente
            cell = row.createCell(1);
            cell.setCellValue(customer.getFullName());
            cell.setCellStyle(style);

            // Celda COLUMN_CUSTOMER_PURCHASES de la fila correspondiente
            cell = row.createCell(2);
            cell.setCellValue(getTotalPurchases(customer));
            cell.setCellStyle(style);

            rowPos++;
        }
    }

    /**
     * Método que obtiene el total de compras de un cliente
     * 
     * @param customer Cliente al que se le calculará el total de compras
     * @return Total de compras del cliente
     */
    private static int getTotalPurchases(CustomerEntity customer) {
        return customer.getTotalLodgings() + customer.getTotalFlights() + customer.getTotalTours();
    }

    private static final String SHEET_NAME = "Customer total sales";
    private static final String FONT_TYPE = "Arial";
    private static final String COLUMN_CUSTOMER_ID = "id";
    private static final String COLUMN_CUSTOMER_NAME = "name";
    private static final String COLUMN_CUSTOMER_PURCHASES = "purchases";
    private static final String REPORTS_PATH_WITH_NAME = "reports/Sales-%s";
    private static final String REPORTS_PATH = "reports";
    private static final String FILE_TYPE = ".xlsx";
    private static final String FILE_NAME = "Sales-%s.xlsx";
}
