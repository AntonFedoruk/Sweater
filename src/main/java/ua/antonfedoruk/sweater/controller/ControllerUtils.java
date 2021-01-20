package ua.antonfedoruk.sweater.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        //get list of validation errors
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                (fieldError) -> fieldError.getField() + "Error", FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
