package thanhtrang.online.keycloak.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrCode {
    ACCESS_DENIED(110,"Bạn không có quyền ADMIN để truy cập tài nguyên này!", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(111,"Đăng nhập thất bại: 401 – Truy cập không hợp lệ",HttpStatus.UNAUTHORIZED),
    //---Check validate
    USER_USERNAME_SIZE(1001,"User must be at least {min}character !!!", HttpStatus.LENGTH_REQUIRED),
    USER_PASSWORD_SIZE(1001,"Password must be at least {min} character !!!",HttpStatus.LENGTH_REQUIRED),
    INVALID_KEY(9999,"Warning !!! Check and Changer field message valid in ENUM define ",HttpStatus.NOT_ACCEPTABLE),
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
