package commerce.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperMeController() {

    @GetMapping("/shopper/me")
    void me() {
    }
}
