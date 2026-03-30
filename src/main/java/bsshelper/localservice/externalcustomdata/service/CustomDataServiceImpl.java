package bsshelper.localservice.externalcustomdata.service;

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

    @Value("${custom.data.file.path}")
    private String path;

    @PostConstruct
    @Override
    public void populateMECustomLink() {
        populateFromExcelMECustomLink(path);
    }

    @PostConstruct
    @Override
    public void populateAlarmUserLabel() {
        populateFromExcelAlarmUserLabel(path);
    }

    @PostConstruct
    @Override
    public void populateComments() {
        populateFromExcelComments(path);
    }


    private void populateFromExcelMECustomLink(String filePath) {
        CustomDataService.MECustomLinkMap.clear();
        Map<String, MECustomLink> MECustomLinkMap = new ConcurrentHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheet("MECustomLink");
            if (sheet1 != null) {
                Iterator<Row> rows = sheet1.iterator();
                if (rows.hasNext()) rows.next(); // skip header

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Cell firstCell = row.getCell(0);

                    if (firstCell == null) break;

                    String userlabel = getCellAlwaysUpperCaseString(row.getCell(0));
                    String BSCID = getCellAlwaysUpperCaseString(row.getCell(1));
                    String RNCID = getCellAlwaysUpperCaseString(row.getCell(2));
                    String GSMID = getCellAlwaysUpperCaseString(row.getCell(3));
                    String UMTSID = getCellAlwaysUpperCaseString(row.getCell(4));

                    MECustomLinkMap.put(userlabel, new MECustomLink(userlabel, BSCID, RNCID, GSMID, UMTSID));
                }
                CustomDataService.MECustomLinkMap.putAll(MECustomLinkMap);
            }
        } catch (IOException e) {
            log.warn(" >> Error in Excel CustomData file reading {}", e.getMessage());
        }
    }

    private void populateFromExcelAlarmUserLabel(String filePath) {
        CustomDataService.alarmUserLabelToAlarmUserLabelMap.clear();
        CustomDataService.alarmCodeToAlarmUserLabelMap.clear();

        Map<String, AlarmUserLabel> alarmUserLabelToAlarmUserLabelMap = new ConcurrentHashMap<>();
        Map<String, AlarmUserLabel> alarmCodeToAlarmUserLabelMap = new ConcurrentHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet2 = workbook.getSheet("AlarmUserLabel");
            if (sheet2 != null) {
                Iterator<Row> rows = sheet2.iterator();
                if (rows.hasNext()) rows.next(); // skip header

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Cell firstCell = row.getCell(0);

                    if (firstCell == null) break;

                    String code = getCellAlwaysUpperCaseString(row.getCell(0));
                    String userlabel = getCellAlwaysUpperCaseString(row.getCell(1));

                    AlarmUserLabel alarmUserLabel = new AlarmUserLabel(Integer.parseInt(code), userlabel);

                    alarmUserLabelToAlarmUserLabelMap.put(userlabel, alarmUserLabel);
                    alarmCodeToAlarmUserLabelMap.put(code, alarmUserLabel);
                }
                CustomDataService.alarmUserLabelToAlarmUserLabelMap.putAll(alarmUserLabelToAlarmUserLabelMap);
                CustomDataService.alarmCodeToAlarmUserLabelMap.putAll(alarmCodeToAlarmUserLabelMap);
            }
        } catch (IOException e) {
            log.warn(" >> Error in Exel CustomData file reading {}", e.getMessage());
        }
    }

    private void populateFromExcelComments(String filePath) {
        CustomDataService.CommentsMap.clear();
        Map<String, String> CommentsMap = new ConcurrentHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheet("Comments");
            if (sheet1 != null) {
                Iterator<Row> rows = sheet1.iterator();
                if (rows.hasNext()) rows.next(); // skip header

                while (rows.hasNext()) {
                    Row row = rows.next();
                    Cell firstCell = row.getCell(0);

                    if (firstCell == null) break;

                    String comment = getCellAlwaysString(row.getCell(0));

                    CommentsMap.put(comment, comment);
                }
                CustomDataService.CommentsMap.putAll(CommentsMap);
            }
        } catch (IOException e) {
            log.warn(" >> Error in Excel CustomData file reading {}", e.getMessage());
        }
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

    private String getCellAlwaysString(Cell cell) {
        String value;
        if (cell.getCellType() == CellType.NUMERIC) {
            value = String.valueOf((long) cell.getNumericCellValue());
        } else {
            value = cell.getStringCellValue();
        }
        return value;
    }
}
