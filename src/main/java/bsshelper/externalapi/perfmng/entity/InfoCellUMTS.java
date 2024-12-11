package bsshelper.externalapi.perfmng.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InfoCellUMTS {
    private final String cellName;
    private final Integer ciRNC;
    private final Integer ciSDR;
    private final Double powerRNC;
    private final Double powerSDR;
    private final Integer radiusRNC;
    private final Integer radiusSDR;
}
