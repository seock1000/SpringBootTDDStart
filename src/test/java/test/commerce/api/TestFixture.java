package test.commerce.api;

import commerce.ProductRepository;
import commerce.command.CreateShopperCommand;
import commerce.command.RegisterProductCommand;
import commerce.query.IssueSellerToken;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import commerce.view.SellerMeView;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.RequestEntity.get;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static test.commerce.UsernameGenerator.generateUsername;

public record TestFixture(
    TestRestTemplate client,
    ProductRepository productRepository
) {
    public static TestFixture create(Environment environment, ProductRepository productRepository) {
        var client = new TestRestTemplate();
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client, productRepository);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        client().postForEntity("/shopper/signup", command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        return client().postForObject(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        ).accessToken();
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        createShopper(email, username, password);
        return issueShopperToken(email, password);
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        String authorization = "Bearer " + token;
        setDefaultAuthorization(authorization);
    }

    private void setDefaultAuthorization(String authorization) {
        RestTemplate template = client().getRestTemplate();
        template.getInterceptors().addFirst((request, body, execution) -> {
            if(!request.getHeaders().containsKey("Authorization")) {
                request.getHeaders().add("Authorization", authorization);
            }
            return execution.execute(request, body);
        });
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        createSeller(email, username, password);
        setSellerAsDefaultUser(email, password);
    }

    private void createSeller(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        client().postForEntity("/seller/signup", command, Void.class);
    }

    private void setSellerAsDefaultUser(String email, String password) {
        String token = issueSellerToken(email, password);
        String authorization = "Bearer " + token;
        setDefaultAuthorization(authorization);
    }

    private String issueSellerToken(String email, String password) {
        return client().postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        ).accessToken();
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        createShopper(email, username, password);
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
        URI location = requireNonNull(response.getHeaders().getLocation());
        String id = location.getPath().substring("/seller/products/".length());
        return UUID.fromString(id);
    }

    public List<UUID> registerProducts() {
        return List.of(registerProduct(), registerProduct(), registerProduct());
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public List<UUID> registerProducts(int count) {
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(registerProduct());
        }
        return ids;
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }

    public String consumeProductPage() {
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
            get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );
        return requireNonNull(response.getBody()).continuationToken();
    }
}
