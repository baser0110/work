package bsshelper.localservice.externalcustomdata.service;

import bsshelper.externalapi.perfmng.util.ExternalKPI;
import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import bsshelper.localservice.externalcustomdata.entity.AlarmLogEntity;
import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDataServiceImpl implements CustomDataService{

    @Value("${kpi.data.file.path}")
    private String path;

    @PostConstruct
    @Override
    public void populateFromExcelKPI() {
        populateFromExcelKPI(path);
//        CustomDataService.externalKPIMap.putAll(ExternalKPI.getTestMap(ExternalKPI.getTest()));
    }

    private void populateFromExcelKPI(String filePath) {
    CustomDataService.externalKPIMap.clear();

    Map<String, ExternalKPI> externalKPIMap = new ConcurrentHashMap<>();

    try (FileInputStream fis = new FileInputStream(filePath);
         Workbook workbook = new XSSFWorkbook(fis)) {

        Sheet sheet = workbook.getSheet("Sheet0");
        if (sheet != null) {
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();
                if (row == null) continue;

                Cell firstCell = row.getCell(0);

                if (firstCell == null) continue;

                String code = getCellAlwaysString(firstCell);
                if (code == null || code.trim().isEmpty()) continue; // Пропускаем, если код пустой

                String raw = getCellAlwaysString(row.getCell(1));
                String info = "No Name";

                if (raw != null) {
                    info = raw.matches(".*\"en\"\\s*:\\s*\"[^\"]+\".*")
                            ? raw.replaceAll(".*\"en\"\\s*:\\s*\"([^\"]+)\".*", "$1")
                            : "No Name";
                }

                String moType = getCellAlwaysString(row.getCell(7));
                String unit = getCellAlwaysString(row.getCell(17));
                String format = getCellAlwaysString(row.getCell(18));

                ExternalKPI kpi = new ExternalKPI(code, info, moType, unit, format);
                externalKPIMap.put(code, kpi);
            }
            CustomDataService.externalKPIMap.putAll(externalKPIMap);
        }
    } catch (IOException e) {
        log.warn(" >> Error in Excel CustomData file reading {}", e.getMessage(), e); // Передаем e для сохранения stacktrace
    }
}

//    private String getCellAlwaysUpperCaseString(Cell cell) {
//        String value;
//        if (cell.getCellType() == CellType.NUMERIC) {
//            value = String.valueOf((long) cell.getNumericCellValue());
//        } else {
//            value = cell.getStringCellValue();
//        }
//        return value.toUpperCase();
//    }

    private String getCellAlwaysString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case BLANK -> "";
            default -> cell.toString();
        };
    }
}
