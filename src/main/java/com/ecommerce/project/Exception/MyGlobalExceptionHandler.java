package com.ecommerce.project.Exception;

import com.ecommerce.project.PayLoad.ApiRsponse;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // it intercepts exceptions globally
public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String ,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = ((FieldError)err).getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<Map<String, String>>(response,
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConfigDataResourceNotFoundException.class)

    public ResponseEntity<ApiRsponse> myResourceNotfoundException(ResourceNotFoundException e){

        String message = e.getMessage();
        ApiRsponse apiRsponse = new ApiRsponse(message , false);
        return new ResponseEntity<>(apiRsponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiRsponse> myApiException(ApiException e){
        String message = e.getMessage();
        ApiRsponse apiRsponse = new ApiRsponse(message, false);
        return new ResponseEntity<>(apiRsponse,HttpStatus.BAD_REQUEST);

    }
    


}


//       @RestControllerAdvice → Handles exceptions globally for all controllers.
//        @ExceptionHandler(...) → Tells Spring which exception this method should catch.
//         MethodArgumentNotValidException → Thrown when validation (@Valid) fails.
//         BindingResult → Contains all validation errors.
//         getAllErrors() → Returns every validation error.
//         FieldError → Lets you get the field name and its error message.
//         Map → Used to return a clean JSON response
