package thanhtrang.online.keycloak.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrCode {
    ACCESS_DENIED(110,"Bạn không có quyền ADMIN để truy cập tài nguyên này!", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(111,"Đăng nhập thất bại: 401 – Truy cập không hợp lệ",HttpStatus.UNAUTHORIZED),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrCode(int code, String message , HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
