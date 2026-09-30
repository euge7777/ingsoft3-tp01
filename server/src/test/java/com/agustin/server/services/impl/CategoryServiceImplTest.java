package com.agustin.server.services.impl;

import com.agustin.server.domain.entities.Category;
import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.requests.CategoryRequest;
import com.agustin.server.dtos.responses.CategoryDTO;
import com.agustin.server.mappers.CategoryMapper;
import com.agustin.server.repositories.CategoryRepository;
import com.agustin.server.util.AuthenticatedUserProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void modificaCategoriaPropia() {

        // ARRANGE
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        User usuario = mock(User.class);
        User propietario = mock(User.class);

        Category categoria = mock(Category.class);
        CategoryRequest request = mock(CategoryRequest.class);
        CategoryDTO dto = mock(CategoryDTO.class);

        when(usuario.getId()).thenReturn(userId);
        when(propietario.getId()).thenReturn(userId);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoria));

        when(categoria.getUser())
                .thenReturn(propietario);

        when(request.getName()).thenReturn("Comida");
        when(request.getColor()).thenReturn("#FF0000");
        when(request.getIsDefault()).thenReturn(false);

        when(categoryMapper.toDTO(categoria))
                .thenReturn(dto);


        // ACT
        CategoryDTO resultado =
                categoryService.updateCategory(categoryId, request);


        // ASSERT
        assertSame(dto, resultado);

        verify(categoria).setName("Comida");
        verify(categoria).setColor("#FF0000");
        verify(categoria).setIsDefault(false);

        verify(categoryRepository).save(categoria);
    }


    @Test
    void rechazaCategoriaAjena() {

        // ARRANGE
        UUID usuarioActualId = UUID.randomUUID();
        UUID propietarioId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        User usuario = mock(User.class);
        User propietario = mock(User.class);

        Category categoria = mock(Category.class);
        CategoryRequest request = mock(CategoryRequest.class);

        when(usuario.getId()).thenReturn(usuarioActualId);
        when(propietario.getId()).thenReturn(propietarioId);

        when(authenticatedUserProvider.getAuthenticatedUser())
                .thenReturn(usuario);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(categoria));

        when(categoria.getUser())
                .thenReturn(propietario);


        // ACT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(categoryId, request)
        );


        // ASSERT
        assertEquals(
                "Unauthorized to update this category",
                exception.getMessage()
        );

        verify(categoryRepository, never()).save(any());
    }
}