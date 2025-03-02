package test.commerce.api;

import org.springframework.boot.test.web.client.TestRestTemplate;

public record TestFixture(TestRestTemplate client) {
}
