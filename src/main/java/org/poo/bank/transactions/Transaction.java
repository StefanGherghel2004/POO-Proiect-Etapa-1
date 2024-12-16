package org.poo.bank.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String description;
    private int timestamp;

    public Transaction(final String description, final int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("description", description);
        json.put("timestamp", timestamp);
        return json;
    }


}
