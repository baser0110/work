package bsshelper.localservice.externalcustomdata.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AlarmUserLabel {
    private final int code;
    private final String userLabel;
}
