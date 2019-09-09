package br.org.catolicasc.surca.handler;
import br.org.catolicasc.surca.error.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException cvException){
        String constraintName;
        if(cvException.getConstraintName() == null){
            constraintName = "Desculpa mas nehuma informação pode ser nula";
        }else{
            constraintName = "A propriedade " + cvException.getConstraintName() + " já foi cadastrado(a)" ;
        }

        ErrorDetailsWithFieldMessage cvExceptionDetail =  ErrorDetailsWithFieldMessage.Builder.newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("ConstraintViolationException")
                .detail(cvException.getMessage())
                .developmentMessage(cvException.getClass().getName())
                .field("Erro ao se conectar ao Banco de Dados")
                .fieldMessage(constraintName)
                .build();
        return new ResponseEntity<>(cvExceptionDetail, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException mnvException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = mnvException.getBindingResult().getFieldErrors();
        String fieldError = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldErrorDefaultMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        ValidationErrorDetails validationErrorDetails =  ValidationErrorDetails.Builder.newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field Validation Error")
                .detail("Field Validation Error")
                .developmentMessage(mnvException.getClass().getName())
                .field(fieldError)
                .fieldMessage(fieldErrorDefaultMessage)
                .build();
        return new ResponseEntity<>(validationErrorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<?> handlePropertyReferenceDetails(PropertyReferenceException prException){

        String fieldMessage = "A propriedade '" + prException.getPropertyName() + "' não existe ";
        ErrorDetailsWithFieldMessage prExceptionDetail =  ErrorDetailsWithFieldMessage.Builder.newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("PropertyReferenceDetails")
                .detail(prException.getMessage())
                .developmentMessage(prException.getClass().getName())
                .field("Erro ao se conectar no Banco de Dados")
                .fieldMessage(fieldMessage)
                .build();
        return new ResponseEntity<>(prExceptionDetail, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        ErrorDetails errorDetails =  ErrorDetails.Builder.newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Internal Exception")
                .detail(ex.getMessage())
                .developmentMessage(ex.getClass().getName())
                .build();
        return new ResponseEntity<>(errorDetails, headers, status);
    }

}
