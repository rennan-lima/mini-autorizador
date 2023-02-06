package app.transaction.service;

import app.card.model.CardEntity;
import app.card.service.CardService;
import app.transaction.model.TransactionDTO;
import app.transaction.model.TransactionEntity;
import app.transaction.model.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final String NOT_ENOUGH_BALANCE = "SALDO_INSUFICIENTE";
    private final String INVALID_PASSWORD = "SENHA_INVALIDA";
    private final String CARD_NOT_EXIST = "CARTAO_INEXISTENTE";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardService cardService;

    public void create(TransactionDTO transactionDTO) throws Exception {

        CardEntity cardEntity = cardService.getCardByNumber(transactionDTO.getNumeroCartao());

        if (cardEntity == null) {
            throw new Exception(CARD_NOT_EXIST);
        }

        if (!cardEntity.getPassword().equals(transactionDTO.getSenhaCartao())) {
            throw new Exception(INVALID_PASSWORD);
        }

        Double cardCurrentBalance = cardEntity.getBalance();
        if (cardCurrentBalance < transactionDTO.getValor()) {
            throw new Exception(NOT_ENOUGH_BALANCE);
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setCard(cardEntity);
        transactionEntity.setAmount(transactionDTO.getValor());
        transactionRepository.save(transactionEntity);

        cardEntity.setBalance(cardCurrentBalance - transactionDTO.getValor());
        cardService.update(cardEntity);
    }
}