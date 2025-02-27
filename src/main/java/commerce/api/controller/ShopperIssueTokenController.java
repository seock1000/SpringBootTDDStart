package commerce.api.controller;

import commerce.result.AccessTokenCarrier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperIssueTokenController() {

    @PostMapping("/shopper/issueToken")
    AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier("token");
    }
}
