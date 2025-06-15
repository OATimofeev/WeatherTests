package steps;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public class WeatherSteps extends WeatherTestBase {
    private Response response;

    @Когда("Запрашиваем погоду для города {string}")
    public void requestWeather(String city) {
        response = given()
                .spec(requestSpecCurrent)
                .queryParam("key", "valid_key")
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

        try {
            assertThatJson(actualJson)
                    .withOptions(IGNORING_ARRAY_ORDER)
                    .isEqualTo(expectedJson);
        } catch (AssertionError e) {
            Allure.addAttachment("Различия в JSON", "text/plain", e.getMessage());
            throw e;
        }
    }
}
