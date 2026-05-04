package bsshelper.localservice.paketlossstat.util;

import bsshelper.localservice.paketlossstat.to.DomainTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class ExcelReaderService {

    public List<DomainTo> readFromSheets(String filePath, List<String> sheetNames) {
        List<DomainTo> domainTos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (String sheetName : sheetNames) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) continue;

                String region = sheetName.toUpperCase();
                int noDataFieldNum = 0;

                Iterator<Row> rows = sheet.iterator();
                if (rows.hasNext()) rows.next(); // skip header

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Cell firstCell = row.getCell(0);
                    noDataFieldNum++;

                    if (firstCell != null) {
                        try {
                            if ("#".equals(firstCell.getStringCellValue().trim())) continue;
                        } catch (Exception ignored) {}
                    } else break;

                    String siteName = row.getCell(1) != null ?
                            getCellAlwaysUpperCaseString(row.getCell(1)) :
                            "EMPTY_" + noDataFieldNum;
                    String cluster = row.getCell(27) != null ?
                            getCellAlwaysUpperCaseString(row.getCell(27)) :
                            "EMPTY_" + noDataFieldNum;
                    String domain = row.getCell(28) != null ?
                            getCellAlwaysUpperCaseString(row.getCell(28)) :
                            "EMPTY_" + noDataFieldNum;

                    domainTos.add(new DomainTo(region, siteName, cluster, domain));
                }
            }

        } catch (IOException e) {
            log.warn(" >> Error in Exel file reading {}", e.getMessage());
        }

        return domainTos;
    }

    private String getCellAlwaysUpperCaseString(Cell cell) {
        String value;
        if (cell.getCellType() == CellType.NUMERIC) {
            value = String.valueOf((long) cell.getNumericCellValue());
        } else {
            value = cell.getStringCellValue();
        }
        return value.toUpperCase();
    }
}

