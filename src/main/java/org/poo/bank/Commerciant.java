package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commerciant {
    private String name;
    private double stolenMoney;

    public Commerciant(final String name) {
        this.name = name;
        this.stolenMoney = 0;
    }

    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        double value = stolenMoney;
        double roundedValue = value * 100000.0 / 100000.0;
        json.put("commerciant", name);
        json.put("total", roundedValue);
        return json;
    }
}
