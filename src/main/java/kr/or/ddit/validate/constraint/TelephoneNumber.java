package kr.or.ddit.validate.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Constraint(validatedBy = { })
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}")
//@ReportAsSingleViolation
public @interface TelephoneNumber {
	String regexp() default "";
	
	String message() default "전화번호 형식 확인";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}










