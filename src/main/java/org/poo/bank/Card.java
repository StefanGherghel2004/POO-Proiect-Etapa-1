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
    private boolean oneTime;

    public Card() {
        cardNumber = generateCardNumber();
        status = "active";
    }

    /**
     * Converts the Card object into a JSON representation.
     * The JSON includes the card's card number and status.
     *
     * @param mapper The ObjectMapper used to create ObjectNode.
     * @return An ObjectNode containing the JSON representation of the Card.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("cardNumber", cardNumber);
        json.put("status", status);
        return json;
    }

    /**
     * Checks whether the card's status is "frozen" (cannot perform action with it)
     *
     * @return A boolean indicating whether the card is frozen.
     */
    public boolean isFrozen() {
        return status.equals("frozen");
    }

}
