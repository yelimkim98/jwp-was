package controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import request.HttpRequest;
import response.HttpResponse;

class UserControllerTest {

    private UserController userController = new UserController();

    @Test
    @DisplayName("회원가입 테스트")
    void serviceForJoin() {
        HttpRequest joinRequest = new HttpRequest("POST /user/create HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Cache-Control: max-age=0\n"
            + "Upgrade-Insecure-Requests: 1\n"
            + "Content-Length: 10\n",
            "userId=tls123&password=passwordoftls&name=신&email=test@email.com");

        HttpResponse response = userController.service(joinRequest);
        String responseHeader = response.buildHeader();

        assertThat(responseHeader.startsWith("HTTP/1.1 302 Found")).isTrue();
        assertThat(responseHeader.contains("Location: /")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"userId=", "password=", "nickname=", "email=", "zzzzzzz", "", " "})
    @DisplayName("회원가입 테스트 - 요청 body가 잘못된 경우")
    void serviceForJoin_IfBodyIsWrong(String wrongBody) {
        HttpRequest joinRequest = new HttpRequest("POST /user/create HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Cache-Control: max-age=0\n"
            + "Upgrade-Insecure-Requests: 1\n"
            + "Content-Length: 10\n",
            wrongBody);

        HttpResponse response = userController.service(joinRequest);
        String responseHeader = response.buildHeader();

        assertThat(responseHeader.startsWith("HTTP/1.1 400 Bad Request")).isTrue();
    }

    @Test
    @DisplayName("이상한 uri 로 요청 테스트")
    void serviceForStrangeUri() {
        HttpRequest joinRequest = new HttpRequest("POST /join HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Cache-Control: max-age=0\n"
            + "Upgrade-Insecure-Requests: 1\n"
            + "Content-Length: 10\n",
            "userId=");

        HttpResponse response = userController.service(joinRequest);
        String responseHeader = response.buildHeader();

        assertThat(responseHeader.startsWith("HTTP/1.1 404 NotFound")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "PUT", "DELETE"})
    @DisplayName("POST 이외의 method 로 회원가입 요청시 404 응답")
    void joinServiceWithHttpMethodNotPost(String httpMethod) {
        HttpRequest joinRequest = new HttpRequest(httpMethod
            + " /join HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Cache-Control: max-age=0\n"
            + "Upgrade-Insecure-Requests: 1\n"
            + "Content-Length: 10\n",
            "userId=");

        HttpResponse response = userController.service(joinRequest);
        String responseHeader = response.buildHeader();

        assertThat(responseHeader.startsWith("HTTP/1.1 404 NotFound")).isTrue();
    }
}