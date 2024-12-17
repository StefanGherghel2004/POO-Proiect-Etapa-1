package org.poo.bank.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SplitTransaction extends Transaction {
    private double amount;
    private String currency;
    private List<String> accounts = new ArrayList<String>();
    private boolean error;
    private String errorDescription;


    public SplitTransaction(final String description, final int timestamp, final String currency,
                            final List<String> accounts, final double amount,
                            final String errorDescription) {
        super(description, timestamp);
        this.currency = currency;
        this.amount = amount;
        for (String account : accounts) {
            this.accounts.add(account);
        }
        this.errorDescription = errorDescription;
    }

    /**
     * Converts the current SplitTransaction instance into a JSON representation.
     * If there is an error in the transaction, the error description is included.
     *
     * @param mapper The ObjectMapper used to create JSON.
     * @return An ObjectNode containing the serialized JSON representation of current transaction.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        json.put("amount", amount);
        json.put("currency", currency);
        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : accounts) {
            accountsArray.add(account);
        }
        json.put("involvedAccounts", accountsArray);
        if (error) {
            json.put("error", errorDescription);
        }
        return json;
    }
}
