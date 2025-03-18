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

//                "moc":"MRNC",
//                "ranshareswitch":0,
//                "ran_sdr_fm_native_param":{"extentionfield":true,"datatype":1,"displayname":"{\"debugState\":0}","value":"{\"debugState\":0}","columnname":""},
//                "ran_fm_alarm_object_name":{"extentionfield":true,"datatype":1,"displayname":"MIN787C","value":"MIN787C","columnname":"Alarm Object Name"},
//                "alarmraisedtime":1733351151000,
//                "ackuserid":"viacheslav_ne",
//                "relatedme":"997a9264-468a-4f49-ac19-afaacccbf00c",
//                "clearstate":0,
//                "acktime":1733351832464,
//                "relatedrules":"",
//                "alarmchangedtime":0,
//                "me":"ed426e41-f5d1-4170-98f9-6ec7ac026ede",
//                "ran_fm_alarm_site_name":{"extentionfield":true,"datatype":1,"displayname":"MIN787","value":"MIN787","columnname":"Site Name(Office)"},
//                "ackstatename":"Ack",
//                "ran_fm_ne_virtualization":{"extentionfield":true,"datatype":0,"displayname":"PNF","value":"0","columnname":"Virtualization Identification"},
//                "productRestype":{"extentionfield":true,"datatype":0,"displayname":"GSM","value":"gv3","columnname":"Product"},
//                "id":1650229808096,
//                "mocname":"MRNC",
//                "alarmkey":"ed426e41-f5d1-4170-98f9-6ec7ac026ede---3@1702@100001@114@114",
//                "visible":1,
//                "ran_fm_alarm_dn":{"extentionfield":true,"datatype":1,"displayname":"SubNetwork=114,ManagedElement=114,GBssFunction=114,GBtsSiteManager=787,GGsmCell=3","value":"SubNetwork=114,ManagedElement=114,GBssFunction=114,GBtsSiteManager=787,GGsmCell=3","columnname":"DN"},
//                "positionname":"BCMN4(114),MIN787(787),GBssFunction=114,GBtsSiteManager=787,GGsmCell=3",
//                "reasoncode":199087342,
//                "servertime":1733351315518,
//                "plmns":[{"mcc":"257","mnc":"01"}],
//                "nbiid":"114",
//                "neip":"129.0.1.1",
//                "restypename":"GSM Controller Alarm(V4)",
//                "restype":"20425",
//                "sequence":99726161,
//                "intermittenceduplicatedkey":"",
//                "relationflag":0,
//                "intermittencecount":0,
//                "ran_fm_alarm_object_type":{"extentionfield":true,"datatype":1,"displayname":"CELL","value":"CELL","columnname":"Alarm Object Type"},
//                "codename":"Cell interruption alarm",
//                "ran_ems_fm_function_info":{"extentionfield":true,"datatype":0,"value":"{\"moc\":\"GGSMCELL\",\"origin_alarm_name\":\"Cell interruption alarm\",\"origin_alarm_reason\":\"1. The link between the BSC and the site that the cell belongs to is broken.\\n2. The BCCH TRX of the cell is faulty.\\n3. The cell is manually blocked.\\n4. The configuration of related cell parameters ...\",\"function_info1\":\"CRC=4279097439/0/0/0#MO=401#OTYPE=GBtsSiteManager#NENAME=BCMN4#RAISETIME=2024-12-05 01:25:51#CID=3#CNAME=MIN787C\"}"},
//                "position":"5c9ce31b-3239-4580-b5a4-735ee3f49d17,e88dd512-5081-4084-b9ad-a15adfa36d4f,GBssFunction=114,GBtsSiteManager=787,GGsmCell=3",
//                "maintainstatus":{"extentionfield":true,"datatype":0,"displayname":"Normal","value":"0","columnname":"Maintain Status"},
//                "neplmns":[{"mcc":"257","mnc":"01"}],
//                "linkname":"",
//                "aid":"3",
//                "alarmsource":"ed426e41-f5d1-4170-98f9-6ec7ac026ede",
//                "commenttime":1736950204245,
//                "offsetalarmraisedtime":1733361951000,
//                "alarmtypename":"Communication Alarm",
//                "relatedmename":"MIN787(787)",
//                "link":"",
//                "naffiltered":false,
//                "perceivedseverityname":"Minor",
//                "nmcreasoncode":315,
//                "reasonname":"1. The link between the BSC and the site that the cell belongs to is broken.\n2. The BCCH TRX of the cell is faulty.\n3. The cell is manually blocked.\n4. The configuration of related cell parameters is incorrect.",
//                "commentuserid":"sergey_b",
//                "ackstate":1,
//                "additionaltext":"Alarm Object:CELL; \nSite ID:787; \nBts ID:3; \nTrx ID:255; \nRack ID:255; \nShelf ID:255; \nPanel ID:255;\nLogic Position:System No.:787,Module No.:10,Index:15;",
//                "admc":false,"plmndisplayinfo":"257-01",
//                "timezoneid":"GMT+03:00",
//                "timezoneoffset":10800000,
//                "ran_ems_fm_naf_params":{"extentionfield":true,"datatype":0,"value":"{\"position1\":\"SubNetwork=114,ManagedElement=114,GBssFunction=114,GBtsSiteManager=787,GGsmCell=3\",\"nafPositionName\":\"MIN787(787)\"}"},
//                "clearstatename":"Uncleared",
//                "alarmcode":199087342,
//                "mename":"BCMN4(114)",
//                "acksystemid":"EM",
//                "commenttext":"off / restoration planned",
//                "alarmtype":0,
//                "admcname":"No",
//                "perceivedseverity":3,
//                "ran_ems_fm_product":{"extentionfield":true,"datatype":0,"value":"{\"product\":[\"gv3\"],\"product_internal\":\"product:gv3,gv3\"}"},
//                "commentsystemid":"EM",
//                "ran_ems_fm_additional_params":{"extentionfield":true,"datatype":1,"displayname":"siteId_office: 787\nsitename_office: MIN787\nalarmobject_type: CELL\nalarmobject_id: 3\nalarmobject_name: MIN787C",
//                "value":"{\"alarmobject_type\":\"CELL\",\"alarmobject_name\":\"MIN787C\",\"alarmobject_id\":\"3\",\"siteId_office\":\"787\",\"sitename_office\":\"MIN787\"}","columnname":"Additional Parameters"},
//                "ran_fm_alarm_object":{"extentionfield":true,"datatype":0,"displayname":"GGsmCell","value":"GGsmCell","columnname":"Alarm Object"},
//                "cleartypename":"-",
//                "relationflagname":"",
//                "ackinfo":"MIN787 locked after MS171 up",
//                "ran_fm_alarm_object_id":{"extentionfield":true,"datatype":1,"displayname":"3","value":"3","columnname":"Alarm Object ID"},
//                "dstsaving":0}