package bsshelper.externalapi.alarmmng.activealarm.entity;

import bsshelper.globalutil.ManagedElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AlarmEntity {
    private final ManagedElementType moc;
    private final String ranshareswitch;
    private final DescriptionFields ran_sdr_fm_native_param;
    private final DescriptionFields ran_fm_alarm_object_name;
    private final String alarmraisedtime;
    private final String ackuserid;
    private final String relatedme;
    private final String clearstate;
    private final String acktime;
    private final String relatedrules;
    private final String alarmchangedtime;
    private final String me;
    private final DescriptionFields ran_fm_alarm_site_name;
    private final String ackstatename;
    private final DescriptionFields ran_fm_ne_virtualization;
    private final DescriptionFields productRestype;
    private final String id;
    private final ManagedElementType mocname;
    private final String alarmkey;
    private final String visible;
    private final DescriptionFields ran_fm_alarm_dn;
    private final String positionname;
    private final String reasoncode;
    private final String servertime;
    private final List<Plms> plmns;
    private final String nbiid;
    private final String neip;
    private final String restypename;
    private final String restype;
    private final String sequence;
    private final String intermittenceduplicatedkey;
    private final String relationflag;
    private final String intermittencecount;
    private final DescriptionFields ran_fm_alarm_object_type;
    private final String codename;
    private final DescriptionFields ran_ems_fm_function_info;
    private final String position;
    private final DescriptionFields maintainstatus;
    private final List<Plms> neplmns;
    private final String linkname;
    private final String aid;
    private final String alarmsource;
    private final String commenttime;
    private final String offsetalarmraisedtime;
    private final String alarmtypename;
    private final String relatedmename;
    private final String link;
    private final String naffiltered;
    private final String perceivedseverityname;
    private final String nmcreasoncode;
    private final String reasonname;
    private final String commentuserid;
    private final String ackstate;
    private final String additionaltext;
    private final String admc;
    private final String plmndisplayinfo;
    private final String timezoneid;
    private final String timezoneoffset;
    private final DescriptionFields ran_ems_fm_naf_params;
    private final String clearstatename;
    private final String alarmcode;
    private final String mename;
    private final String acksystemid;
    private final String commenttext;
    private final String alarmtype;
    private final String admcname;
    private final String perceivedseverity;
    private final DescriptionFields ran_ems_fm_product;
    private final String commentsystemid;
    private final DescriptionFields ran_ems_fm_additional_params;
    private final String value;
    private final DescriptionFields ran_fm_alarm_object;
    private final String cleartypename;
    private final String relationflagname;
    private final String ackinfo;
    private final DescriptionFields ran_fm_alarm_object_id;
    private final String dstsaving;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionFields {
        String value;
        String displayname;
        String columnname;
        boolean extentionfield;
        int datatype;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Plms {
        String mcc;
        String mnc;
    }
}