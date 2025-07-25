package bsshelper.externalapi.inventorymng.service;

import bsshelper.exception.CustomNetworkConnectionException;
import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.RiCableMocTo;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.externalapi.inventorymng.entity.InventoryEntity;
import bsshelper.externalapi.inventorymng.to.InventoryEntityTo;
import bsshelper.externalapi.inventorymng.util.InventoryMngBodySettings;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class InventoryMngServiceImpl implements InventoryMngService {

    @Override
    public List<InventoryEntity> getHWInventory(Token token, ManagedElement managedElement) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        InventoryEntityTo inventoryEntityTo = null;
        List<InventoryEntity> inventoryEntityList = null;
        try {
            HttpRequest httpRequest = getSiteInventory(token, managedElement);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
            if (response.contains("\"code\":") && (response.contains("\"message\":"))) {
                try {
                    error = new Gson().fromJson(response, ErrorEntity.class);
                } catch (JsonSyntaxException e2) {
                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
            } else {
                try {
                    String json = "{inventoryEntityList : " + response + "}";
                    inventoryEntityTo = new Gson().fromJson(json, InventoryEntityTo.class);
                } catch (JsonSyntaxException e1) {
                    log.error(" >> error in inventoryEntityTo parsing: {}", e1.toString());
                }
                if (inventoryEntityTo != null) {
                    inventoryEntityList = inventoryEntityTo.getInventoryEntityList();
                }
                log.info(" >> inventoryEntityList: {}", inventoryEntityList);
            }
        } catch(IOException | InterruptedException e){
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return inventoryEntityList;
        }

    private HttpRequest getSiteInventory(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        InventoryMngBodySettings.builder()
                                .subnetid(managedElement.getSubNetworkNum().toString())
                                .neid(managedElement.getManagedElementNum())
                                .netype(managedElement.getManagedElementType().toString())
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_QUER_HW_INVENTORY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }
}
