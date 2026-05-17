package com.duy.do_an_be.config;

import com.duy.do_an_be.entity.*;
import com.duy.do_an_be.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Tạo Admin mặc định
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(User.Role.ADMIN)
                    .status(true)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Created default Admin: admin@gmail.com / 123456");
        }

        // 2. Tạo dữ liệu mẫu nếu chưa có
        if (categoryRepository.count() == 0) {
            Category phone = Category.builder()
                    .categoryName("Điện thoại")
                    .slug("dien-thoai")
                    .categoryImage("https://cdn2.fptshop.com.vn/unsafe/360x0/filters:format(webp):quality(75)/dien_thoai_icon_cate_240938806d.png")
                    .isActive(true)
                    .build();
            Category laptop = Category.builder()
                    .categoryName("Laptop")
                    .slug("laptop")
                    .categoryImage("https://cdn2.fptshop.com.vn/unsafe/360x0/filters:format(webp):quality(75)/may_tinh_bang_ic_cate_dccb57ff5c.png")
                    .isActive(true)
                    .build();
            categoryRepository.save(phone);
            categoryRepository.save(laptop);

            Brand apple = Brand.builder().brandName("Apple").slug("apple").isActive(true).build();
            Brand samsung = Brand.builder().brandName("Samsung").slug("samsung").isActive(true).build();
            brandRepository.save(apple);
            brandRepository.save(samsung);

            Product iphone = Product.builder()
                    .proName("iPhone 15 Pro Max")
                    .slug("iphone-15-pro-max")
                    .description("Siêu phẩm mới nhất từ Apple")
                    .category(phone)
                    .brand(apple)
                    .isActive(true)
                    .build();
            productRepository.save(iphone);
            
            System.out.println(">>> Seeded sample categories, brands, and products.");
        }

    }
}
