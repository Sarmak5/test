package ru.bakhaev.test;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.bakhaev.test.controllers.AuthController;
import ru.bakhaev.test.dto.User;
import ru.bakhaev.test.handlers.AuthExceptionHandler;
import ru.bakhaev.test.response.TokenResponse;
import ru.bakhaev.test.response.UserResponse;
import ru.bakhaev.test.services.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

	@MockBean
	private AuthService authService;

	private WebTestClient webClient;

	@Before
	public void setUp() throws Exception {
		webClient = WebTestClient
				.bindToController(new AuthController(authService))
				.controllerAdvice(AuthExceptionHandler.class)
				.build();
	}

	@Test
	public void shouldRegisterANewUser() {

		final User newUser = new User(new ObjectId(), "1234@ya.ru", "321", "token", 111111111L);

		given(authService.findByEmail(any(String.class))).willReturn(Mono.just(new User()));
		given(authService.save(any(User.class))).willReturn(Mono.just(newUser));

		webClient.post().uri("/register")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"email\":\"123@ya.ru\", \"password\": \"321\"}"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(TokenResponse.class)
				.consumeWith(response -> {
					assertThat(response.getResponseBody().getSuccess().equals(true));
					assertThat(response.getResponseBody().getToken().equals("token"));
				});
	}

	@Test
	public void shouldNotRegisterUserIfUserAlreadyExists() {

		final User newUser = new User(new ObjectId(), "1234@ya.ru", "321", "token", 111111111L);

		given(authService.findByEmail(any(String.class))).willReturn(Mono.just(newUser));

		webClient.post().uri("/register")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"email\":\"123@ya.ru\", \"password\": \"321\"}"))
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void shouldLoginUser() throws Exception {

		final User mockUser = new User(new ObjectId(), "123@ya.ru", "321", "token", 111111111L);

		given(authService.findByEmailAndPassword(any(String.class),any(String.class))).willReturn(Mono.just(mockUser));

		webClient.post().uri("/login")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"email\":\"123@ya.ru\", \"password\": \"321\"}"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponse.class)
				.consumeWith(response -> {
					assertThat(response.getResponseBody().getSuccess().equals(true));
					assertThat(response.getResponseBody().getUser().getCreated().equals(111111111L));
				});
	}

	@Test
	public void shouldNotLoginUserIfUserDoesntExist() {

		given(authService.findByEmailAndPassword(any(String.class),any(String.class))).willReturn(null);

		webClient.post().uri("/login")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"email\":\"123@ya.ru\", \"password\": \"321\"}"))
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void shouldConfirmToken() throws Exception {

		final User mockUser = new User(new ObjectId(), "1234@ya.ru", "321", "token", 111111111L);

		given(authService.findByToken(any(String.class))).willReturn(Mono.just(mockUser));

		webClient.post().uri("/confirm")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"token\":\"token\"}"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponse.class)
				.consumeWith(response -> {
					assertThat(response.getResponseBody().getSuccess().equals(true));
					assertThat(response.getResponseBody().getUser().getCreated().equals(111111111L));
				});
	}


	@Test
	public void shouldNotConfirmTokenIfTokenDoesntExist() throws Exception {

		given(authService.findByToken(any(String.class))).willReturn(null);

		webClient.post().uri("/confirm")
				.contentType(APPLICATION_JSON_UTF8)
				.body(fromObject("{\"token\":\"token\"}"))
				.exchange()
				.expectStatus().is5xxServerError();
	}
}
