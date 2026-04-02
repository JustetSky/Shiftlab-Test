package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.seller.SellerRequest;
import com.justet.shiftlabtest.api.model.seller.SellerResponse;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.mapper.SellerMapper;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private Seller seller(Long id) {
        return Seller.builder()
                .id(id)
                .name("Seller " + id)
                .contactInfo("seller" + id + "@mail.com")
                .build();
    }

    private SellerRequest request() {
        return SellerRequest.builder()
                .name("Test Seller")
                .contactInfo("test@mail.com")
                .build();
    }

    private SellerResponse response(Long id) {
        return SellerResponse.builder()
                .id(id)
                .name("Test Seller")
                .contactInfo("test@mail.com")
                .build();
    }

    @Test
    void createSeller_shouldSaveAndReturnResponse() {

        SellerRequest request = request();
        Seller entity = seller(null);
        Seller saved = seller(1L);
        SellerResponse response = response(1L);

        when(sellerMapper.toEntity(request)).thenReturn(entity);
        when(sellerRepository.save(entity)).thenReturn(saved);
        when(sellerMapper.toResponse(saved)).thenReturn(response);

        SellerResponse result = sellerService.createSeller(request);

        assertThat(result.getId()).isEqualTo(1L);

        verify(sellerMapper).toEntity(request);
        verify(sellerRepository).save(entity);
        verify(sellerMapper).toResponse(saved);
    }

    @Test
    void getAllSellers_shouldReturnPageResponse() {

        Pageable pageable = PageRequest.of(0, 2);

        Seller s1 = seller(1L);
        Seller s2 = seller(2L);

        Page<Seller> page = new PageImpl<>(List.of(s1, s2), pageable, 2);

        when(sellerRepository.findAll(pageable)).thenReturn(page);
        when(sellerMapper.toResponse(any())).thenAnswer(inv ->
                response(((Seller) inv.getArgument(0)).getId())
        );

        PageResponse<SellerResponse> result = sellerService.getAllSellers(pageable);

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
    }

    @Test
    void getSellerById_whenExists_shouldReturn() {

        Seller seller = seller(1L);
        SellerResponse response = response(1L);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerMapper.toResponse(seller)).thenReturn(response);

        SellerResponse result = sellerService.getSellerById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getSellerById_whenNotExists_shouldThrow() {

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sellerService.getSellerById(1L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Seller with id 1 not found");
    }

    @Test
    void updateSeller_shouldUpdateFields() {

        Seller existing = seller(1L);
        SellerRequest request = SellerRequest.builder()
                .name("Updated")
                .contactInfo("updated@mail.com")
                .build();

        Seller updated = seller(1L);
        updated.setName("Updated");
        updated.setContactInfo("updated@mail.com");

        SellerResponse response = SellerResponse.builder()
                .id(1L)
                .name("Updated")
                .contactInfo("updated@mail.com")
                .build();

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sellerRepository.save(existing)).thenReturn(updated);
        when(sellerMapper.toResponse(updated)).thenReturn(response);

        SellerResponse result = sellerService.updateSeller(1L, request);

        assertThat(existing.getName()).isEqualTo("Updated");
        assertThat(result.getName()).isEqualTo("Updated");
    }

    @Test
    void updateSeller_whenNotFound_shouldThrow() {

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sellerService.updateSeller(1L, request()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteSeller_shouldCallRepository() {

        sellerService.deleteSeller(1L);

        verify(sellerRepository).deleteById(1L);
    }
}