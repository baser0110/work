package bsshelper.service.logger;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
    private String timestamp;
    private String message;
}
