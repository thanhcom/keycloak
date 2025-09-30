package thanhtrang.online.keycloak.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    // Các phương thức xử lý yêu cầu liên quan đến sản phẩm sẽ được định nghĩa ở đây
    // Ví dụ: thêm sản phẩm, cập nhật sản phẩm, xoá sản phẩm, lấy danh sách sản phẩm, v.v.

     @GetMapping("/all")
     @PreAuthorize("hasRole('ADMIN')")
     public String getAllProducts() {
         // Logic để lấy danh sách sản phẩm
         return "Danh sách sản phẩm";
     }

    // @PostMapping("/add")
    // public Product addProduct(@RequestBody Product product) {
    //     // Logic để thêm sản phẩm mới
    // }

    // @PutMapping("/update/{id}")
    // public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
    //     // Logic để cập nhật thông tin sản phẩm
    // }

    // @DeleteMapping("/delete/{id}")
    // public void deleteProduct(@PathVariable Long id) {
    //     // Logic để xoá sản phẩm
    // }
}
