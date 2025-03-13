package test.commerce.api;

import java.net.URI;
import java.util.UUID;

import commerce.command.CreateSellerCommand;
import commerce.command.CreateShopperCommand;
import commerce.command.RegisterProductCommand;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static test.commerce.UsernameGenerator.generateUsername;

public record TestFixture(TestRestTemplate client) {

    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate(new RestTemplateBuilder());
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        client.postForEntity("/shopper/signUp", command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        return issueShopperToken(email, password);
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private void setDefaultAuthorization(String authorization) {
        RestTemplate template = client.getRestTemplate();
        template.getInterceptors().addFirst((request, body, execution) -> {
            if (request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", authorization);
            }

            return execution.execute(request, body);
        });
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createSeller(email, generateUsername(), password);
        setSellerAsDefaultUser(email, password);
    }

    private void createSeller(String email, String username, String password) {
        var command = new CreateSellerCommand(email, username, password);
        client.postForEntity("/seller/signUp", command, Void.class);
    }

    private void setSellerAsDefaultUser(String email, String password) {
        String token = issueSellerToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
            "/seller/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperAsDefaultUser(email, password);
    }

    public UUID registerProduct() {
        return registerProduct(generateRegisterProductCommand());
    }

    public UUID registerProduct(RegisterProductCommand command) {
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/products",
            command,
            Void.class
        );
        URI location = response.getHeaders().getLocation();
        String path = requireNonNull(location).getPath();
        String id = path.substring("/seller/products/".length());
        return UUID.fromString(id);
    }
}
