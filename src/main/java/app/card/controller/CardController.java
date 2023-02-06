package app.card.controller;

import app.card.model.CardDTO;
import app.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cartoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {

    @Autowired
    private CardService service;

    @PostMapping
    @ResponseBody
    public ResponseEntity<CardDTO> create(@RequestBody CardDTO cardDTO) {
        try {
            CardDTO card = service.create(cardDTO);
            return new ResponseEntity<>(card, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(cardDTO, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{cardNumber}")
    @ResponseBody
    public ResponseEntity<Double> getBalance(@PathVariable("cardNumber") String cardNumber) {
        try {
            Double balance = service.getBalance(cardNumber);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}