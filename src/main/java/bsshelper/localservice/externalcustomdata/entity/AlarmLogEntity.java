package bsshelper.localservice.externalcustomdata.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AlarmLogEntity {
    private final String ME;
    private final String AlarmCode;
    private final String Position;
    private final String CommentInformation;
    private final String DN;
}
