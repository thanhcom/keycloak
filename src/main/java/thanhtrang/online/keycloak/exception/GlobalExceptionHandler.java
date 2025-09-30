package thanhtrang.online.keycloak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thanhtrang.online.keycloak.enums.ErrCode;
import thanhtrang.online.keycloak.responseApi.ResponseApi;

@ControllerAdvice
public class GlobalExceptionHandler {
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
}
