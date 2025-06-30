package test.commerce.api;

import commerce.command.CreateShopperCommand;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

public record TestFixture(
    TestRestTemplate client
) {
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
}
