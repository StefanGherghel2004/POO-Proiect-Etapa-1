package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class SplitTransaction extends Transaction {
    private double amount;
    private String currency;
    private List<String> accounts = new ArrayList<String>();


    public SplitTransaction(final String description, final int timestamp, final String currency, final List<String> accounts, final double amount) {
        super(description, timestamp);
        this.currency = currency;
        this.amount = amount;
        for (String account : accounts) {
            this.accounts.add(account);
        }
    }

    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        json.put("amount", amount);
        json.put("currency", currency);
        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : accounts) {
            accountsArray.add(account);
        }
        json.put("involvedAccounts", accountsArray);
        return json;
    }
}
