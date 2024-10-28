package com.example.nest_java.controller;

import com.example.nest_java.dto.ApiResponse;
import com.example.nest_java.dto.BaseDTO;
import com.example.nest_java.dto.FilterQueryDTO;
import com.example.nest_java.model.BaseEntity;
import com.example.nest_java.service.BaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MappedSuperclass
@Validated
@Data
@RequiredArgsConstructor
public abstract class BaseController<E extends BaseEntity,
        DTO extends BaseDTO<ID>,
        ID extends UUID> {

    private final BaseService<E, DTO, ID> baseService;

    @GetMapping("/filter")
    @Operation(summary = "filter entity", description =
            "method returns list of entities. with input including " +
                    "'fullTextSearch' is search by name or description." +
                    " Example : fullTextSearch=text search" +
                    "'where' is to search by column and value pairs. You can join other tables and set conditions." +
                    " Example: where=[\"name\":\"example\"] , where=[\"image_url\":\"image.img\"]" +
                    "'filters' is to search for a column value that is within a range of time or numbers or a list of values.\n" +
                    "example : filters={\"price\":[\"10000\",\"20000\"]} , filters={\"name\":[\"abc\",\"dcf\",\"xyz\"]}" +
                    "'skip' is to search for a column value that is not in a range or number or list of values." +
                    "example same as 'filters'"
    )
    public ResponseEntity<ApiResponse> filter(
            @RequestParam(value = "filters", required = false) String filtersJson,
            @RequestParam(value = "skip", required = false) String skipJson,
            @RequestParam(value = "where", required = false) String whereJson,
            @RequestParam(value = "fullTextSearch", required = false) String fullTextSearch,
            Pageable pageable
    ) {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ArrayList<String>> filters = null;
        Map<String, ArrayList<String>> skip = null;
        Map<String, String> where = null;
        try {
            if (filtersJson != null) {
                filters = objectMapper.readValue(filtersJson, new TypeReference<Map<String, ArrayList<String>>>() {

                });
            }
            if (skipJson != null) {
                skip = objectMapper.readValue(skipJson, new TypeReference<Map<String, ArrayList<String>>>() {

                });
            }
            if (whereJson != null) {
                where = objectMapper.readValue(whereJson, new TypeReference<Map<String, String>>() {

                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.badRequest("bad request"));
        }
        Page<DTO> result = baseService.filter(new FilterQueryDTO(filters, skip, where, fullTextSearch), pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

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
