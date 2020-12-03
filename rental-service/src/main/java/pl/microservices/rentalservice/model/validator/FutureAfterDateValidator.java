package pl.microservices.rentalservice.model.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureAfterDateValidator implements ConstraintValidator<FutureAfterDate, Object> {
    private String earlierDateField;
    private String laterDateField;

    @Override
    public void initialize(FutureAfterDate constraintAnnotation) {
        earlierDateField = constraintAnnotation.earlierDate();
        laterDateField = constraintAnnotation.laterDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);

        LocalDate earlierDate = (LocalDate) beanWrapper.getPropertyValue(earlierDateField);
        LocalDate laterDate = (LocalDate) beanWrapper.getPropertyValue(laterDateField);

        boolean isValid = true;

        if (laterDate != null && earlierDate != null) {
            isValid = earlierDate.isBefore(laterDate);
        }

        return isValid;
    }

}
