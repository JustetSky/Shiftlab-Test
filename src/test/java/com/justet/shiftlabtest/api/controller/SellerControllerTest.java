package com.justet.shiftlabtest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justet.shiftlabtest.api.ApiPath;
import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.seller.SellerRequest;
import com.justet.shiftlabtest.api.model.seller.SellerResponse;
import com.justet.shiftlabtest.core.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SellerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SellerService sellerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SellerController controller = new SellerController(sellerService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper.findAndRegisterModules();
    }

    private SellerResponse response(Long id) {
        return SellerResponse.builder()
                .id(id)
                .name("Seller " + id)
                .contactInfo("seller" + id + "@mail.com")
                .build();
    }

    @Test
    void getAll_shouldReturnPageResponse() throws Exception {

        PageResponse<SellerResponse> page = PageResponse.<SellerResponse>builder()
                .items(List.of(response(1L), response(2L)))
                .page(0)
                .size(2)
                .total(2)
                .hasNext(false)
                .build();

        when(sellerService.getAllSellers(any())).thenReturn(page);

        mockMvc.perform(get(ApiPath.SELLERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2));

        verify(sellerService).getAllSellers(any());
    }

    @Test
    void getById_shouldReturnSeller() throws Exception {

        when(sellerService.getSellerById(1L))
                .thenReturn(response(1L));

        mockMvc.perform(get(ApiPath.SELLERS + "/{sellerId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Seller 1"));

        verify(sellerService).getSellerById(1L);
    }

    @Test
    void create_shouldReturnCreatedSeller() throws Exception {

        SellerRequest request = SellerRequest.builder()
                .name("New Seller")
                .contactInfo("new@mail.com")
                .build();

        when(sellerService.createSeller(any()))
                .thenReturn(response(1L));

        mockMvc.perform(post(ApiPath.SELLERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(sellerService).createSeller(any());
    }

    @Test
    void update_shouldReturnUpdatedSeller() throws Exception {

        SellerRequest request = SellerRequest.builder()
                .name("Updated")
                .contactInfo("updated@mail.com")
                .build();

        when(sellerService.updateSeller(eq(1L), any()))
                .thenReturn(response(1L));

        mockMvc.perform(put(ApiPath.SELLERS + "/{sellerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(sellerService).updateSeller(eq(1L), any());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {

        doNothing().when(sellerService).deleteSeller(1L);

        mockMvc.perform(delete(ApiPath.SELLERS + "/{sellerId}", 1))
                .andExpect(status().isNoContent());

        verify(sellerService).deleteSeller(1L);
    }
}