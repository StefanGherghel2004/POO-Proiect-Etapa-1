package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Account {
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private List<Card> cards = new ArrayList<Card>();

    private boolean setMinBalance;
    private double minBalance;


    public Account(String iban, double balance, String currency, String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("IBAN", iban);
        json.put("balance", balance);
        json.put("currency", currency);
        json.put("type", type);
        ArrayNode cardsArray = mapper.createArrayNode();
        for (Card card : cards) {
            cardsArray.add(card.toJSON(mapper));
        }
        json.set("cards", cardsArray);
        return json;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addFunds(double amount) {
        balance += amount;
    }
}
