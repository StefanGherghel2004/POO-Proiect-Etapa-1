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
     *
     * @param mapper
     * @return
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("commerciant", name);
        json.put("total", receivedAmount);
        return json;
    }
}
