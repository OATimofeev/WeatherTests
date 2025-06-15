package steps;

import api.Headers;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class WeatherSteps extends WeatherTestBase {
    private Response response;
    private Map<String, Object> expectedValues = new HashMap<>();

    @Когда("Запрашиваем погоду для города {string}")
    public void requestWeather(String city) {
        response = given()
                .spec(requestSpecCurrent)
                .queryParam("key", Headers.VALID_KEY)
                .queryParam("q", city)
                .get();
    }

    @Когда("Запрашиваем погоду c параметрами q == {string}, key == {string}")
    public void requestWeather(String q, String key) {
        response = given()
                .spec(requestSpecCurrent)
                .queryParam("q", q)
                .queryParam("key", key)
                .get();
    }

    @Тогда("Статус код должен быть {int}")
    public void checkStatusCode(int statusCode) {
        assertThat(response.statusCode())
                .as("Статус код равен ").isEqualTo(statusCode);
    }

    @Тогда("Проверяем, что JSON ответа равен ожидаемому в {string}")
    public void checkResponseBody(String expectedJsonFilename) throws IOException {
        validateResponse("src/test/resources/__files/", expectedJsonFilename);
    }

    @Тогда("Проверяем, что JSON ответа с ошибкой равен ожидаемому в {string}")
    public void checkResponseBodyWithError(String expectedJsonFilename) throws IOException {
        validateResponse("src/test/resources/__files/error/", expectedJsonFilename);
    }

    private void validateResponse(String path, String expectedJsonFilename) throws IOException {
        String actualJson = response.getBody().asString();
        String expectedJson = Files.readString(Paths.get(path + expectedJsonFilename + ".json"));

        assertThat(actualJson).as("Тело ответа").isEqualTo(expectedJson);
    }
}
