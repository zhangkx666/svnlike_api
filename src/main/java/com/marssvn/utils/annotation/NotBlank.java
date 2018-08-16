//package com.marssvn.utils.annotation;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//import java.getMessage.annotation.ElementType;
//import java.getMessage.annotation.Retention;
//import java.getMessage.annotation.RetentionPolicy;
//import java.getMessage.annotation.Target;
//import java.util.Date;
//
//import static java.getMessage.annotation.ElementType.*;
//import static java.getMessage.annotation.ElementType.PARAMETER;
//import static java.getMessage.annotation.ElementType.TYPE_USE;
//
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
//@Constraint(validatedBy = NotBlank.DateValidator.class)
//public @interface NotBlank {
//
//    /**
//     * default message
//     * @return message
//     */
//    String message() default "Invalid date";
//
//    Class<?>[] groups() default { };
//
//    Class<? extends Payload>[] payload() default { };
//
//    class DateValidator implements ConstraintValidator<ValidDate, Date> {
//
//
//    }
//}
