package app.card.service;

import app.card.model.CardDTO;
import app.card.model.CardEntity;
import app.card.model.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final Double DEFAULT_BALANCE = 500.00;

    @Autowired
    private CardRepository repository;

    public CardDTO create(CardDTO cardDTO) throws Exception {
        CardEntity card = new CardEntity();
        card.setNumber(cardDTO.getNumeroCartao());

        Example<CardEntity> example = Example.of(card);
        List<CardEntity> results = repository.findAll(example);

        if (!results.isEmpty()) {
            throw new Exception("Card number " + cardDTO.getNumeroCartao() + " already exists.");
        }

        card.setPassword(cardDTO.getSenha());
        card.setBalance(DEFAULT_BALANCE);
        repository.save(card);
        return cardDTO;
    }

    public void update(CardEntity cardEntity) {
        repository.save(cardEntity);
    }

    public Double getBalance(String cardNumber) throws Exception {
        CardEntity card = new CardEntity();
        card.setNumber(cardNumber);

        Example<CardEntity> example = Example.of(card);
        List<CardEntity> results = repository.findAll(example);

        if (results.isEmpty()) {
            throw new Exception("Card number " + cardNumber + " not found.");
        }

        return results.get(0).getBalance();
    }

    public CardEntity getCardByNumber(String cardNumber) {
        CardEntity card = new CardEntity();
        card.setNumber(cardNumber);

        Example<CardEntity> example = Example.of(card);
        List<CardEntity> results = repository.findAll(example);

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

}