package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.RegisterProductCommand;
import commerce.commandmodel.RegisterProductCommandExecutor;
import commerce.view.ArrayCarrier;
import commerce.view.SellerProductView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Comparator.comparing;

@RestController
public record SellerProductController(ProductRepository repository) {

    @PostMapping("/seller/products")
    public ResponseEntity<?> registerProduct(
        @RequestBody RegisterProductCommand command,
        Principal user
    ) {
        UUID id = UUID.randomUUID();
        var executor = new RegisterProductCommandExecutor(repository::save);
        executor.execute(id, UUID.fromString(user.getName()), command);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<SellerProductView> findProduct(
        @PathVariable("id") UUID id,
        Principal user
    ) {
        UUID sellerId = UUID.fromString(user.getName());

        return repository.findById(id)
            .filter(product -> product.getSellerId().equals(sellerId))
            .map(this::convertToView)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/products")
    ResponseEntity<ArrayCarrier<SellerProductView>> findAllProducts(
        Principal user
    ) {
        SellerProductView[] items = repository
            .findBySellerId(UUID.fromString(user.getName()))
            .stream()
            .sorted(comparing(Product::getRegisteredTimeUtc).reversed())
            .map(this::convertToView)
            .toArray(SellerProductView[]::new);
        return ResponseEntity.ok(new ArrayCarrier<>(items));
    }

    private SellerProductView convertToView(Product product) {
        return new SellerProductView(
            product.getId(),
            product.getName(),
            product.getImageUri(),
            product.getDescription(),
            product.getPriceAmount(),
            product.getStockQuantity(),
            product.getRegisteredTimeUtc()
        );
    }
}
