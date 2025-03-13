package commerce.api.controller;

import java.security.Principal;
import java.util.UUID;

import commerce.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerProductsController(SellerRepository repository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(Principal user) {
        UUID id = UUID.fromString(user.getName());
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.status(201).build();
    }
}
