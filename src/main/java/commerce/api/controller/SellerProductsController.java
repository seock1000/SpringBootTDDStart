package commerce.api.controller;

import java.net.URI;
import java.util.UUID;

import commerce.command.RegisterProductCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerProductsController() {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(
        @RequestBody RegisterProductCommand command
    ) {
        if (isValidUri(command.imageUri()) == false) {
            return ResponseEntity.badRequest().build();
        }

        URI location = URI.create("/seller/products/" + UUID.randomUUID());
        return ResponseEntity.created(location).build();
    }

    private boolean isValidUri(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getHost() != null;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    @GetMapping("/seller/products/{id}")
    void findProduct() {
    }
}
