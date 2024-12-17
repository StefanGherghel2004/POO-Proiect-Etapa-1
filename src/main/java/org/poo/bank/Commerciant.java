package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commerciant {
    private String name;
    private double receivedAmount;

    public Commerciant(final String name) {
        this.name = name;
        this.receivedAmount = 0;
    }

    /**
     * Converts the object into a JSON representation.
     * The JSON includes the commerciant's name and the total received amount,
     * respectively how much one user spent.
     *
     * @param mapper The ObjectMapper used to serialize the object to JSON.
     * @return An ObjectNode containing a JSON representation of the commerciant.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("commerciant", name);
        json.put("total", receivedAmount);
        return json;
    }
}
