package bsshelper.globalutil;

public class GlobalUtil {
    public static final String GLOBAL_PATH = "https://10.172.74.11:2443";

    public static final String API_OAUTH = "/api/oauth2/v1";
    public static final String OAUTH_TOKEN = "/oauth/token";
    public static final String OAUTH_HANDSHAKE = "/oauth/handshake";

    public static final String API_PLANNEDMNG = "/api/plannedmng/v1";
    public static final String PLANNEDMNG_NEW = "/new";
    public static final String PLANNEDMNG_OPEN = "/open";
    public static final String PLANNEDMNG_CLOSE = "/close";
    public static final String PLANNEDMNG_DELETE = "/destroy";
    public static final String PLANNEDMNG_INFO = "/plannedarea/list";

    public static final String API_CURRENTAREA = "/api/currentarea/v1";
    public static final String CURRENTAREA_QUERY = "/modataquery";

    public static final String API_PLANNEDSERV = "/api/plannedarea/v1";
    public static final String PLANNEDSERV_ACTIVATE = "/activate";
    public static final String PLANNEDSERV_MO_CONFIG_UNS = "/moconfig";

    public static final String API_OSE = "/api/ofd-access/v1/ose/executions";
    public static final String OSE_UCLI_BATCH_SCRIPT = "/packet/303";
    public static final String OSE_UPLOAD_FILE = "/parameter-files";
    public static final String OSE_QUERY_EXEC_STATUS = "/state";

    public static final String API_PERFMNG = "/api/rest/performanceManagement/V3/pm-analysis/kpiQuery";

    public static final String API_EXEC_NE_SERV = "/api/moaction/v1";
    public static final String EXEC_NE_SERV_BOARD_DIAGNOSIS = "/moaction/diagnoseBoard";
    public static final String EXEC_NE_SERV_QUERY_OPT_ITBBU = "/moaction/queryOptAndElecInfo";
    public static final String EXEC_NE_SERV_QUERY_VSWR_ITBBU ="/moaction/queryFddRruVswr";

    public static final String API_EXPRT_ACTIVE_ALARM = "/api/fm-active/v2/activealarms/openapi/export";
    public static final String API_QUER_ACTIVE_ALARM = "/api/fm-active/v1/north/openapi/v1/activealarms";

    public static final String DRY_CONTACT_LDN_SDR_SLOT13 = "Equipment=1,Rack=1,SubRack=1,Slot=13,PlugInUnit=1,SdrDeviceGroup=1,DryContactDeviceSet=1,DryContactDevice=";
}
