package app.card.controller;

import app.card.model.CardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CardRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String DEFAULT_ROUTE = "/cartoes";

    @Test
    void get_balance_with_invalid_card_number_returns_404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DEFAULT_ROUTE + "/1"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void create_card_with_valid_data_returns_201() throws Exception {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao("1234123412341234");
        cardDTO.setSenha("9999");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void create_card_with_duplicity_returns_422() throws Exception {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao("6464646464646464");
        cardDTO.setSenha("1111");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    void get_balance_with_valid_card_number_returns_200() throws Exception {

        String cardNumber = "6789678967896789";

        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao(cardNumber);
        cardDTO.setSenha("9999");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        mockMvc.perform(MockMvcRequestBuilders.get(DEFAULT_ROUTE + "/" + cardNumber))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string("500.0"));
    }

}