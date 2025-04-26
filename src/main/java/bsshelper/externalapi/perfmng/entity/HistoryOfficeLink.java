package bsshelper.externalapi.perfmng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class HistoryOfficeLink {
    private final String mrnc;
    private final String id;
    private final String siteName;
    private final double value;
    private final LocalDateTime time;
}

