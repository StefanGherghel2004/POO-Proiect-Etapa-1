package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import static org.poo.utils.Utils.generateCardNumber;

@Getter
@Setter
public class Card {
    private String cardNumber;
    private String status;

    public Card() {
        cardNumber = generateCardNumber();
        status = "active";
    }

    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("cardNumber", cardNumber);
        json.put("status", status);
        return json;
    }

    public boolean isFrozen() {
        return status.equals("frozen");
    }

}
