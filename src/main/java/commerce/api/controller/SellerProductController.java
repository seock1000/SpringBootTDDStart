package commerce.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerProductController() {

    @PostMapping("/seller/products")
    public ResponseEntity<?> registerProduct() {
        return ResponseEntity.status(201).build();
    }
}
