package bsshelper.externalapi.openscriptexecengine.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.exception.CustomNetworkConnectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class ExecuteUCLIBatchScriptServiceImpl implements ExecuteUCLIBatchScriptService {

    public String executeBatch(String filePath, Token token) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ExecuteSuccessResponse executeSuccessResponse = null;
        ErrorEntity error = null;
        try {
            HttpRequest httpRequest = executeBatchRequest(filePath, token);
//            HttpRequest httpRequest = uploadParamFile(token);
//            System.out.println(httpRequest);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
//            System.out.println(response);
            executeSuccessResponse = new Gson().fromJson(response, ExecuteSuccessResponse.class);
        } catch (JsonSyntaxException e1) {
//            e1.printStackTrace();
            log.error(" >> error in response executeBatch parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(response, ErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
            }
//            System.out.println(uLocalCellMocTo);
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return executeSuccessResponse.getExecutionId();
    }

    public String uploadParamFile(StringFileEntity file, Token token) {
        RequestPath resultPath = null;
        HttpResponse<String> httpResponse = null;
        ErrorEntity error = null;
        String response = null;
//        System.out.println(file);
        try {
            HttpRequest httpRequest = uploadParamFileRequest(file, token);
//            System.out.println(httpRequest);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
//            System.out.println(response);
            resultPath = new Gson().fromJson(response, RequestPath.class);
        } catch (JsonSyntaxException e1) {
//            e1.printStackTrace();
            log.error(" >> error in response uploadParamFile parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(response, ErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
            }
//            System.out.println(uLocalCellMocTo);
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return resultPath.getPath();
    }

    public int queryExecStatus(String execId, Token token) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        QuerySuccessResponse querySuccessResponse = null;
        ErrorEntity error = null;
        try {
            HttpRequest httpRequest = queryExecutionStatusRequest(execId, token);
//            HttpRequest httpRequest = uploadParamFile(token);
//            System.out.println(httpRequest);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
//            System.out.println(response);
            querySuccessResponse = new Gson().fromJson(response, QuerySuccessResponse.class);
        } catch (JsonSyntaxException e1) {
//            e1.printStackTrace();
            log.error(" >> error in response queryExecStatus parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(response, ErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
            }
//            System.out.println(uLocalCellMocTo);
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return querySuccessResponse.getStatus();
    }

    private HttpRequest executeBatchRequest(String filePath, Token token) throws IOException {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        new ObjectMapper().writeValueAsString(new Body(filePath))))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OSE + GlobalUtil.OSE_UCLI_BATCH_SCRIPT))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest uploadParamFileRequest(StringFileEntity file, Token token) throws IOException {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(file.getFile()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OSE + GlobalUtil.OSE_UPLOAD_FILE))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "multipart/form-data; boundary=" + file.getBoundary())
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest queryExecutionStatusRequest(String execId, Token token) throws IOException {
        return HttpRequest.newBuilder()
                .method(Verb.GET.toString(), HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OSE + GlobalUtil.OSE_QUERY_EXEC_STATUS + "/" + execId))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    @Data
    @AllArgsConstructor
    static class Body {
        private String batch_file;
    }

    @Data
    @AllArgsConstructor
    static class RequestPath {
        private String path;
    }

    @Data
    @AllArgsConstructor
    static class ExecuteSuccessResponse{
        private int packetId;
        private String executionId;
        private Parameters parameters;
        private ParamNameAndTypes paramNameAndTypes;
        private String packetType;

        @Data
        @AllArgsConstructor
        static class Parameters{
            private String args;
            private String batch_file;
        }

        @Data
        @AllArgsConstructor
        static class ParamNameAndTypes{
            private String args;
            private String batch_file;
        }
    }

    @Data
    @AllArgsConstructor
    static class QuerySuccessResponse{
        private String id;
        private long createdDateTime;
        private long finishedDateTime;
        private int status;
        private String logLocation;
    }
}
