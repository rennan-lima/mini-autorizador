package app.transaction.controller;

import app.card.model.CardDTO;
import app.transaction.model.TransactionDTO;
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
class CardTransactionRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String DEFAULT_TRANSACTION_ROUTE = "/transacoes";
    private final String DEFAULT_CARD_ROUTE = "/cartoes";

    @Test
    void add_transaction_returns_card_not_exist() throws Exception {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setNumeroCartao("9632111122223333");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_TRANSACTION_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }

    @Test
    void add_transaction_returns_invalid_password() throws Exception {

        String cardNumber = "6655665566556655";

        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao(cardNumber);
        cardDTO.setSenha("9999");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_CARD_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setNumeroCartao(cardNumber);
        transactionDTO.setSenhaCartao("8888");

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_TRANSACTION_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    void add_transaction_returns_not_enough_balance() throws Exception {

        String cardNumber = "8877887788778877";
        String cardPassword = "9999";

        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao(cardNumber);
        cardDTO.setSenha(cardPassword);

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_CARD_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setNumeroCartao(cardNumber);
        transactionDTO.setSenhaCartao(cardPassword);
        transactionDTO.setValor(Double.valueOf("1000.00"));

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_TRANSACTION_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }

    @Test
    void add_transaction_returns_200() throws Exception {

        String cardNumber = "4566456645664566";
        String cardPassword = "9999";

        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumeroCartao(cardNumber);
        cardDTO.setSenha(cardPassword);

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_CARD_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setNumeroCartao(cardNumber);
        transactionDTO.setSenhaCartao(cardPassword);
        transactionDTO.setValor(Double.valueOf("50.00"));

        mockMvc.perform(MockMvcRequestBuilders.post(DEFAULT_TRANSACTION_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().string("OK"));
    }

}