package com.example.nest_java.controller;

import com.example.nest_java.dto.ApiResponse;
import com.example.nest_java.dto.BaseDTO;
import com.example.nest_java.model.BaseEntity;
import com.example.nest_java.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Validated
@Data
@RequiredArgsConstructor
public abstract class BaseController<E extends BaseEntity,
        DTO extends BaseDTO<ID>,
        ID extends UUID> {

    private final BaseService<E, DTO, ID> baseService;

    @GetMapping
    @Operation(summary = "Lấy danh sách entity", description = "Phương thức này trả về một danh sách Entity chưa bị xóa và được phân trang.")
    public ResponseEntity<ApiResponse> findAll(Pageable pageable) {

        Page<DTO> dtos = baseService.findAll(pageable);

        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy entity với id", description = "Phương thức này trả về một Entity với id đã cung cấp.")
    public ResponseEntity<ApiResponse> findById(@PathVariable("id") ID id) {

        return ResponseEntity.ok(ApiResponse.success(baseService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "tạo entity", description = "Phương thức này tạo Entity từ Dto đã cung cấp .")
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid DTO dto) {

        return ResponseEntity.ok(ApiResponse.created(baseService.create(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "cập nhật lại entity", description = "Phương thức này cập nhật entity với Dto đã cung cấp.")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") ID id,
            @RequestBody @Valid DTO dto
    ) {

        return ResponseEntity.ok(ApiResponse.success(baseService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "xóa mềm entity", description = "Phương thức này thiết lập entity đã bị xóa mềm và giá trị deleted_at tại thời điểm gọi hàm.")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") ID id) {

        baseService.softDeleteById(id);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PutMapping("/disable/{id}")
    @Operation(summary = "vô hiệu hóa entity", description = "Phương thức này thiết lập entity đã bị vô hiệu hóa và giá trị deleted_at tại thời điểm gọi hàm..")
    public ResponseEntity<ApiResponse> disable(@PathVariable("id") ID id) {

        baseService.disableById(id);

        return ResponseEntity.ok(ApiResponse.disabled());
    }

}
