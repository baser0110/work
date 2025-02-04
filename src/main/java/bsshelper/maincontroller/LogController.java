package bsshelper.maincontroller;

import bsshelper.service.logger.LogEntry;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class LogController {

    private final String LOG_DIR = "C:/Users/User/Desktop/logs/operation/"; // Adjust as needed

    @GetMapping("/appAccessMng/logs")
    public String getLogs(String date, Model model) {
        if (date == null || date.isEmpty()) {
            // Provide a default log file or show a message
            model.addAttribute("message", "Please select a log file date.");
            return "logs";
        }

        String logFilePath = LOG_DIR + "operation-" + date + ".log"; // Assuming log files are named `operation-yyyy-MM-dd.log`

        try {
            List<String> logEntries = Files.readAllLines(Paths.get(logFilePath));

            // Create a list to store LogEntry objects
            List<LogEntry> logs = new ArrayList<>();
            for (String log : logEntries) {
                // Split the log entry by '||' to get timestamp and message
                String[] parts = log.split("\\|\\|");
                if (parts.length >= 2) {
                    // Add LogEntry object with timestamp and message to the list
                    logs.add(new LogEntry(parts[0].trim(), parts[1].trim()));
                }
            }

            // Pass the list of logs to the view
            model.addAttribute("logs", logs);

        } catch (IOException e) {
            model.addAttribute("message", "Error reading the log file.");
        }

        model.addAttribute("selectedDate", date);  // Pass the selected date for UI purposes
        return "logs";
    }
}