package thanhtrang.online.keycloak.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhtrang.online.keycloak.dto.request.LoginRequest;
import thanhtrang.online.keycloak.dto.request.TokenRequest;
import thanhtrang.online.keycloak.dto.response.LoginResponse;
import thanhtrang.online.keycloak.dto.response.TokenResponse;
import thanhtrang.online.keycloak.responseApi.ResponseApi;
import thanhtrang.online.keycloak.service.UserClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUserController {
    private final UserClientService userClientService;
    /**
     * Tạo người dùng mới
     * @param username Tên đăng nhập
     * @param email Email của người dùng
     * @param password Mật khẩu của người dùng
     */

    @PostMapping("/user")
    public String createUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password) {
        userClientService.createUser(username, email);
        userClientService.setPassword(userClientService.getUserIdByUsername(username), password);
        return "User created successfully";
    }
    /**
     * Cập nhật email của người dùng
     * @param username Tên đăng nhập của người dùng
     * @param newEmail Email mới
     */
    @PostMapping("/user/update-email")
    public String updateUserEmail(@RequestParam String username,
                                  @RequestParam String newEmail) {
        String userId = userClientService.getUserIdByUsername(username);
        if (userId == null) {
            return "User not found";
        }
        userClientService.updateUser(userId, newEmail);
        return "Email updated successfully";
    }
    /**
     * Xoá người dùng
     * @param username Tên đăng nhập của người dùng
     */
    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam String username) {
        String userId = userClientService.getUserIdByUsername(username);
        if (userId == null) {
            return "User not found";
        }
        userClientService.deleteUser(userId);
        return "User deleted successfully";
    }
    /**
     * Cập nhật mật khẩu cho người dùng
     * @param username Tên đăng nhập của người dùng
     * @param newPassword Mật khẩu mới
     */
    @PostMapping("/user/update-password")
    public String updateUserPassword(@RequestParam String username,
                                     @RequestParam String newPassword) {
        String userId = userClientService.getUserIdByUsername(username);
        if (userId == null) {
            return "User not found";
        }
        userClientService.setPassword(userId, newPassword);
        return "Password updated successfully";
    }
    /**
     * Lấy ID người dùng theo tên đăng nhập
     * @param username Tên đăng nhập của người dùng
     */
    @PostMapping("/user/get-id")
    public String getUserIdByUsername(@RequestParam String username) {
        String userId = userClientService.getUserIdByUsername(username);
        if (userId == null) {
            return "User not found";
        }
        return "User ID: " + userId;
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest body) {
        ResponseApi<LoginResponse> responseApi = new ResponseApi<>();
        responseApi.setResponseCode(1005);
        responseApi.setData( userClientService.login(body.getUsername(), body.getPassword()));
        responseApi.setMessenger("Login successfully");
        return ResponseEntity.ok(responseApi);
    }

    @PostMapping("/user/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRequest body) {
        ResponseApi<LoginResponse> responseApi = new ResponseApi<>();
        responseApi.setResponseCode(1005);
        responseApi.setData( userClientService.refreshToken(body.getRefresh_token()));
        responseApi.setMessenger("Login successfully");
        return ResponseEntity.ok(responseApi);
    }




}
