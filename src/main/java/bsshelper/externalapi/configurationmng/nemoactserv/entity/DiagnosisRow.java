package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DiagnosisRow {
    private final String elementId;
    private final String result;
    private final String functionId;
    private final String functionName;
    private final String position;
    private final int recordNo;
    private final String elementName;
    private final String status;
}
