package pl.microservices.rentalservice.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureAfterDateValidator.class)
public @interface FutureAfterDate {
    String message() default "Earlier date can't happen after the latter";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String earlierDate();
    String laterDate();
}
