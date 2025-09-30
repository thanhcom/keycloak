package thanhtrang.online.keycloak.exception;

import lombok.Getter;
import lombok.Setter;
import thanhtrang.online.keycloak.enums.ErrCode;

//Customer Response  Lỗi Trong Hệ Thống
@Setter
@Getter
public class AppException extends RuntimeException{
    private ErrCode errCode;

    public AppException(ErrCode errcode) {
        super(errcode.getMessage());
        this.errCode = errcode;
    }
}