package steps;

import io.cucumber.java.ru.Дано;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StubSteps {
    private static final String PATH = "/v1/current.json";

    @Дано("Создана заглушка для города {string}")
    public void prepareStubForCity(String city) {
        stubFor(get(urlPathEqualTo(PATH))
                .withQueryParam("q", equalTo(city))
                .withQueryParam("key", equalTo("valid_key"))
                .willReturn(ok().withBodyFile("/" + city + ".json")));
    }

    @Дано("Создана заглушка с ожиданием ошибки q == {string}, key == {string}, statusCode == {int}, body == {string}")
    public void prepareStubWithError(String q, String key, Integer statusCode, String jsonName) {
        stubFor(get(urlPathEqualTo(PATH))
                .withQueryParam("q", equalTo(q))
                .withQueryParam("key", equalTo(key))
                .willReturn(status(statusCode)
                        .withBodyFile("/error/" + jsonName + ".json")));
    }
}
