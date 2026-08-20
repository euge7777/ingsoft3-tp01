package com.agustin.server.controllers;

import com.agustin.server.dtos.requests.CategoryRequest;
import com.agustin.server.dtos.responses.CategoryDTO;
import com.agustin.server.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listTransactions() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.listCategories());
        } catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.createCategory(request));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(id, request));
    }

    @DeleteMapping(path = "/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategory(id);
    }
}
