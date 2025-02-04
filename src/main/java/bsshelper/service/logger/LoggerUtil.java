package bsshelper.service.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    public static Logger getApplicationLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getOperationLogger() {
        return LoggerFactory.getLogger("Operation");
    }
}