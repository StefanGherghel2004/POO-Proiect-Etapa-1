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

    public Commerciant(String name) {
        this.name = name;
        this.stolenMoney = 0;
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("commerciant", name);
        json.put("total", stolenMoney);
        return json;
    }
}
