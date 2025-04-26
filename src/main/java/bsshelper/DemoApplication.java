package bsshelper;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.service.AuthServiceImpl;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnServiceImpl;
import bsshelper.externalapi.configurationmng.plannedmng.service.PlanMgnServiceImpl;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServService;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServServiceImpl;
import bsshelper.service.TokenServiceImpl;
import bsshelper.service.paketlossstat.service.PaketLossStatService;
import bsshelper.service.paketlossstat.service.PaketLossStatServiceImpl;
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
		SpringApplication.run(DemoApplication.class, args);

	}


}
