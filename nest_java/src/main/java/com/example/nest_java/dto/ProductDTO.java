package com.example.nest_java.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends BaseDTO<UUID> {
    @NotBlank(message = "Name not blank")
    @Size(min = 1, max = 150, message = "size name error")
    String name;

    @NotBlank(message = "Description not blank")
    @Size(min = 1, max = 500, message = "size description error")
    String description;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 1")
    int quantity;

    @NotNull
    @DecimalMin(value = "1", message = "Price must be greater than 1")
    BigDecimal price;

    @DateTimeFormat
    @FutureOrPresent
    LocalDate discountStartDate;

    @DateTimeFormat
    @FutureOrPresent
    LocalDate discountEndDate;

    @DecimalMin(value = "1", message = "DiscountPercentage must be greater than 1")
    @DecimalMax(value = "100.0", message = "DiscountPercentage must be less than 100")
    BigDecimal discountPercentage;

    @NotNull
    private List<ImageDTO> images;

    @NotNull
    private List<SizeProductDTO> sizeProducts;

}
