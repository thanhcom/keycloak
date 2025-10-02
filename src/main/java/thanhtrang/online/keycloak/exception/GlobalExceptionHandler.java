package thanhtrang.online.keycloak.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thanhtrang.online.keycloak.enums.ErrCode;
import thanhtrang.online.keycloak.responseApi.ResponseApi;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ResponseApi<?>> HandlingAppException(AppException ex) {
        ResponseApi<String> responseApi = new ResponseApi<>();
        responseApi.setResponseCode(ex.getErrCode().getCode());
        responseApi.setMessenger(ex.getErrCode().getMessage());
        return ResponseEntity.status(ex.getErrCode().getStatusCode()).body(responseApi);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ResponseApi<?>> handleAccessDenied(AccessDeniedException ex) {
        ResponseApi<String> responseApi = new ResponseApi<>();
        responseApi.setResponseCode(ErrCode.ACCESS_DENIED.getCode());
        responseApi.setMessenger(ErrCode.ACCESS_DENIED.getMessage());
        return ResponseEntity.status(ErrCode.ACCESS_DENIED.getStatusCode()).body(responseApi);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseApi<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        // Gom lỗi cùng field thành 1 chuỗi
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                Collectors.joining(", ")
                        )
                ));

        // Lấy thông báo lỗi đầu tiên để gán messenger
        String firstErrorMessage = errors.values().stream().findFirst().orElse("Validation failed");

        ResponseApi<Map<String, String>> response = ResponseApi.<Map<String, String>>builder()
                .ResponseCode(400)
                .Messenger(firstErrorMessage)
                .data(errors) // giữ toàn bộ map lỗi
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ResponseApi<?>> HandlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        ResponseApi<String> responseApi = new ResponseApi<>();
//        ErrCode errCode = ErrCode.INVALID_KEY;
//        Map<String, Object> attributes = null;
//        try {
//            String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
//            errCode = ErrCode.valueOf(enumKey);
//
//
//            var constraintViolation =
//                    ex.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
//
//            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
//
//        } catch (IllegalArgumentException exc) {
//            responseApi.setResponseCode(ErrCode.INVALID_KEY.getCode());
//            responseApi.setMessenger(ErrCode.INVALID_KEY.getMessage() + "-- err =" + exc.getMessage());
//        }
//        responseApi.setResponseCode(errCode.getCode());
//        responseApi.setMessenger(Objects.nonNull(attributes)
//                ? mapAttribute(errCode.getMessage(), attributes)
//                : errCode.getMessage());
//        return ResponseEntity.badRequest().body(responseApi);
//    }
//
//    private String mapAttribute(String message, Map<String, Object> attributes) {
//        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
//        System.out.println("ATTR:" + minValue);
//        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
//    }
}
