package bsshelper.globalutil.entity;

import bsshelper.globalutil.Severity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MessageEntity {
    private final Severity severity;
    private final String message;
}
