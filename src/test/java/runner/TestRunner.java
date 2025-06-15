package runner;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps"
)
public class TestRunner {


    private static WireMockServer wireMockServer;

    @BeforeClass
    public static void startServer() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

//        WeatherStubs.setupNegativeStubs();
    }

    @AfterClass
    public static void stopServer() {
        wireMockServer.stop();
    }
}
