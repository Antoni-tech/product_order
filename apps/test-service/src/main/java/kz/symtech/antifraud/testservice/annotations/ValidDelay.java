package kz.symtech.antifraud.testservice.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kz.symtech.antifraud.testservice.annotations.impl.DelayValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DelayValidator.class)
@Documented
public @interface ValidDelay {
    String message() default "Invalid delay for chrono unit!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
