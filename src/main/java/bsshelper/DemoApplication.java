package bsshelper;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.service.AuthServiceImpl;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnServiceImpl;
import bsshelper.externalapi.configurationmng.plannedmng.service.PlanMgnServiceImpl;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServService;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServServiceImpl;
import bsshelper.service.TokenServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication {

	static {
		Properties props = System.getProperties();
		props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

		System.out.println("disableHostnameVerification: " +
				System.getProperty("jdk.internal.httpclient.disableHostnameVerification"));
	}

	public static void main(String[] args) {

//		DryContactDevice

//		System.out.println(new PlanMgnServiceImpl().newArea(new AuthServiceImpl().getToken()));

//		new PlanMgnServiceImpl().deleteArea("20240530_173946_182", new AuthServiceImpl().getToken());

//		new CurrentMgnServiceImpl().dataQuery(new AuthServiceImpl().getToken());
//
//		ManagedElement managedElement= new CurrentMgnServiceImpl().getManagedElementByNeName(new AuthServiceImpl().getToken(), "zhda12");
//
//
//
//
//		Token token = new AuthServiceImpl().getToken();
//
//		PlanMgnServiceImpl planMgnService = new PlanMgnServiceImpl();
//
//		String dataAreaId = planMgnService.newArea(token);
//

//		MocData mocData = new DryContactDeviceMocData(Operation.create,8, AlmUserLabel.LOW_TEMPERATURE, AlmStatus.OPEN);
//
//		PlannedServBodySettings bodySettings = PlannedServBodySettings.builder()
//				.ManagedElementType(ManagedElementType.SDR.toString())
//				.ne("SubNetwork=201,ManagedElement=999")
//				.moData(List.of(mocData))
//				.build();
//
//		PlanServService planServService = new PlanServServiceImpl();
//
//		planServService.dataConfigUns(dataAreaId,token,bodySettings);
//
//		planServService.activateArea(dataAreaId,token);

//		planMgnService.deleteArea("20240806_032734_887",token);
//		planMgnService.deleteArea("20240806_033319_201",token);
//		planMgnService.deleteArea("20240806_033457_659",token);
//		planMgnService.deleteArea("20240806_040252_998",token);


//		CurrentMgnServiceImpl currentMgnService = new CurrentMgnServiceImpl();
//		currentMgnService.rawDataQuery(token, managedElement, "DryContactCable");
//
//		currentMgnService.getDryContactDeviceMoc(token, currentMgnService.getManagedElementByNeName(token, "min798"));

		SpringApplication.run(DemoApplication.class, args);


	}


}
