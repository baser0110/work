package bsshelper.maincontroller.handler;

import bsshelper.globalutil.exception.CustomNetworkConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomNetworkConnectionException.class)
    public String handleConnectException(CustomNetworkConnectionException e, Model model) {
//        System.out.println(e.getMessage());
        model.addAttribute("error", "Network Connection Error. Try Again Later.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
//        System.out.println(e.getMessage());
        model.addAttribute("error", "Unidentified Internal Error");
        log.error(">> handle error: {}, {} \n {}", e.getMessage(), e.getCause().toString(), Arrays.toString(e.getStackTrace()));
        return "error";
    }
}
