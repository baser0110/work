package bsshelper.localservice.paketlossstat.util;

import bsshelper.localservice.paketlossstat.to.DomainTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

                Iterator<Row> rows = sheet.iterator();
                if (rows.hasNext()) rows.next(); // skip header

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Cell firstCell = row.getCell(0);

                    if (firstCell != null) {
                        try {
                            if ("#".equals(firstCell.getStringCellValue().trim())) break;
                        } catch (Exception ignored) {}
                    }

                    String siteName = row.getCell(1).getStringCellValue().toUpperCase();
                    String cluster = row.getCell(27).getStringCellValue().toUpperCase();
                    String domain = row.getCell(28).getStringCellValue().toUpperCase();

                    domainTos.add(new DomainTo(region, siteName, cluster, domain));
                }
            }

        } catch (IOException e) {
            log.warn(" >> Error in Exel file reading {}", e.getMessage());
        }

        return domainTos;
    }
}

