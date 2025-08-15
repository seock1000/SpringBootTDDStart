package test.commerce;

import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@MethodSource("test.commerce.TestDataSource#invalidEmails")
public @interface InvalidEmailSource {
}
