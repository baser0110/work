package bsshelper.localservice.externalcustomdata.service;


import bsshelper.externalapi.perfmng.util.ExternalKPI;
import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import bsshelper.localservice.externalcustomdata.entity.AlarmLogEntity;
import bsshelper.localservice.externalcustomdata.entity.MECustomLink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface CustomDataService {
    ConcurrentHashMap<String, MECustomLink> MECustomLinkMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, AlarmUserLabel> alarmUserLabelToAlarmUserLabelMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, AlarmUserLabel> alarmCodeToAlarmUserLabelMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> CommentsMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> VLANMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ExternalKPI> externalKPIMap = new ConcurrentHashMap<>();
    void populateFromExcelKPI();
}
