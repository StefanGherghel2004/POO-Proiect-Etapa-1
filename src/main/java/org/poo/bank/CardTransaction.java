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

    public CardTransaction(final String description, final int timestamp, final String account, final String card, final String cardholder, final double amount, final String commerciant) {
        super(description, timestamp);
        this.account = account;
        this.card = card;
        this.cardholder = cardholder;
        this.amount = amount;
        this.commerciant = commerciant;
    }

    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        double rounded = amount;
        double roundedValue = Math.round(rounded * 10000.0) / 10000.0;
        if (successFulPayment) {
            json.put("amount", roundedValue);
            json.put("commerciant", commerciant);
        } else if(cardCreation) {
            json.put("account", account);
            json.put("card", card);
            json.put("cardHolder", cardholder);
        }
        return json;
    }
}
