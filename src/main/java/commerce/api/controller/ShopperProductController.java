package commerce.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperProductController() {

    @GetMapping("/shopper/products")
    public void getProducts() {
    }
}
