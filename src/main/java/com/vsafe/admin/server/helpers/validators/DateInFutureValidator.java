package com.vsafe.admin.server.helpers.validators;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateInFutureValidator implements ConstraintValidator<DateInFuture, Date> {
    @Override
    public void initialize(DateInFuture constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) return true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();

        // Get tomorrow
        calendar.setTime(new Date());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        Date tomorrow = calendar.getTime();

        return date.compareTo(tomorrow) >= 0;
    }
}
