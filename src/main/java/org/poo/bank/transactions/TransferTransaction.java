package org.poo.bank.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TransferTransaction extends Transaction {
    private String amount;
    private String receiverIBAN;
    private String senderIBAN;
    private String transferType;

    public TransferTransaction(final String description, final int timestamp,
                               final String receiverIBAN, final String senderIBAN,
                               final String transferType, final String amount) {
        super(description, timestamp);
        this.receiverIBAN = receiverIBAN;
        this.senderIBAN = senderIBAN;
        this.transferType = transferType;
        this.amount = amount;
    }

    /**
     * Converts the TransferTransaction into a JSON representation
     *
     * @param mapper The ObjectMapper used to serialize the object to JSON.
     * @return An ObjectNode containing the serialized JSON representation.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        json.put("receiverIBAN", receiverIBAN);
        json.put("senderIBAN", senderIBAN);
        json.put("transferType", transferType);
        json.put("amount", amount);
        return json;
    }

}
