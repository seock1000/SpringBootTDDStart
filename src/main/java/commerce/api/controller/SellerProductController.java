package commerce.api.controller;

import commerce.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductController(
    SellerRepository repository
) {

    @PostMapping("/seller/products")
    public ResponseEntity<?> registerProduct(
        Principal user
    ) {
        UUID id = UUID.fromString(user.getName());
        if(repository.findById(id).isEmpty()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.status(201).build();
    }
}
