package test.commerce;

import commerce.command.RegisterProductCommand;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterProductCommandGenerator {

    public static RegisterProductCommand generateRegisterProductCommand() {
        return new RegisterProductCommand(
            generateProductName(),
            generateProductImgUri(),
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }

    public static RegisterProductCommand generateRegisterProductCommandWithImageUrl(String imageUrl) {
        return new RegisterProductCommand(
            generateProductName(),
            imageUrl,
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }

    private static String generateProductName() {
        return "name" + UUID.randomUUID();
    }

    private static String generateProductImgUri() {
        return "https://test.com/images/" + UUID.randomUUID();
    }

    private static String generateProductDescription() {
        return "description-" + UUID.randomUUID();
    }

    private static BigDecimal generateProductPriceAmount() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return BigDecimal.valueOf(random.nextInt(10000, 100000));
    }

    private static int generateProductStockQuantity() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(10, 100);
    }
}
