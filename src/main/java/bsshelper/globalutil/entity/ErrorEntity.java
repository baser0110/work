package bsshelper.globalutil.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorEntity {
    private final int code;
    private final String message;
}
