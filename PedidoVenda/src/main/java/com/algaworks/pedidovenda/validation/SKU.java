package com.algaworks.pedidovenda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * @author Yuri 
 * Anota��o respons�vel por validar o atributo SKU no sistema.
 * 
 * <code>@Target<code> define em quais locais a anota��o poder� ser utilizada 
 * <code>@Constraint<code> define alguma classe respons�vel por validar o atributo,
 * neste caso, nenhuma classe foi informada.
 * <code>@Pattern<code> Anota��o do bean validation para validar o campo atrav�s de uma 
 * express�o regular 
 * */
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "([a-zA-Z]{2}\\d{4,18})?")
public @interface SKU {
	
	/**
	 * Recupera uma mensagem definida no arquivo ValidationMessages.properties, 
	 * atrav�s da chave {com.algaworks.constraints.SKU.message}, e envia para 
	 * a anota��o Pattern que ir� exibi-la para ao usu�rio quando a restri��o n�o for satisfeita*/
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "{com.algaworks.constraints.SKU.message}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
