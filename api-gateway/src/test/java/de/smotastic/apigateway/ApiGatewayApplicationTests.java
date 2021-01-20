package de.smotastic.apigateway;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Setup a wiremockserver to be able to bootstrap the spring application context
 * without a running keycloak instance. We use wiremock, because MvcMock is not
 * supported in a gateway environment
 * (https://stackoverflow.com/questions/64098719/test-spring-cloud-gateway-by-mockmvc-cant-initialize-a-bean-of-type-webapplica)
 *
 */
@SpringBootTest(properties = {
		"spring.security.oauth2.client.provider.keycloak.issuer-uri=http://localhost:8091/auth/realms/testrealm" })
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8091)
class ApiGatewayApplicationTests {

	@Autowired
	private WebTestClient client;

	@Test
	void shouldRedirectToOauth2Provider_whenNoAuthGiven() throws Exception {
		client.get().uri("/").exchange().expectStatus().is3xxRedirection().expectHeader().value(HttpHeaders.LOCATION,
				is("/oauth2/authorization/keycloak"));
	}

	@Test
	void shouldGetUserInfos_whenUserIsAuthenticated() throws Exception {
		client //
				.mutateWith(mockOidcLogin().idToken(builder -> builder.subject("cooleruser"))) //
				.get().uri("/whoami").exchange() //
				.expectStatus().is2xxSuccessful().expectBody(String.class)
				.value(containsString("\"userName\":\"cooleruser\""));
	}

}