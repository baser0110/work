package bsshelper.service.paketlossstat.entity;

import lombok.Data;

@Data
public class ValueStat {
    private double monday = 0.0;
    private double tuesday = 0.0;
    private double wednesday = 0.0;
    private double thursday = 0.0;
    private double friday = 0.0;
    private double saturday = 0.0;
    private double sunday = 0.0;
    private double week = 0.0;
}
