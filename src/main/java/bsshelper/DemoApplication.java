package bsshelper;

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
