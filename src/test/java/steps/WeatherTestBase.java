package steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

public class WeatherTestBase {
    protected static RequestSpecification requestSpecCurrent;

    static {
        requestSpecCurrent = new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080")
                .setBasePath("/v1/current.json")
                .build();
    }
}
