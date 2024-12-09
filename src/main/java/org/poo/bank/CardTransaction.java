package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardTransaction extends Transaction {
    private boolean successFulPayment = false;
    private boolean cardCreation = false;
    private String account;
    private String card;
    private String cardholder;
    private double amount;
    private String commerciant;

    public CardTransaction(String description, int timestamp, String account, String card, String cardholder, double amount, String commerciant) {
        super(description, timestamp);
        this.account = account;
        this.card = card;
        this.cardholder = cardholder;
        this.amount = amount;
        this.commerciant = commerciant;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        if (successFulPayment) {
            json.put("amount", amount);
            json.put("commerciant", commerciant);
        } else if(cardCreation) {
            json.put("account", account);
            json.put("card", card);
            json.put("cardHolder", cardholder);
        }
        return json;
    }
}
