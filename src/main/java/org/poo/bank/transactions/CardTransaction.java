package org.poo.bank.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import static org.poo.inputHandler.InputHandler.ROUNDING_UTIL;

@Getter
@Setter
public class CardTransaction extends Transaction {
    private boolean successFulPayment = false;
    private boolean cardCreation = false;
    private String account;
    private String card;
    private String cardholder;
    private double amount;
    private String commerciant;

    public CardTransaction(final String description, final int timestamp,
                           final String account, final String card, final String cardholder,
                           final double amount, final String commerciant) {
        super(description, timestamp);
        this.account = account;
        this.card = card;
        this.cardholder = cardholder;
        this.amount = amount;
        this.commerciant = commerciant;
    }


    /**
     * Converts the current transaction to a JSON representation.
     * The resulting JSON will contain different fields depending on the object's state.
     * If the payment was successful, it includes the amount and commerciant details.
     * If it is a card creation, it includes account, card, and cardholder details.
     *
     * @param mapper The ObjectMapper used to create the JSON.
     * @return An ObjectNode containing the serialized JSON representation of current transaction.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = super.toJSON(mapper);
        double rounded = amount;
        double roundedValue = Math.round(rounded * ROUNDING_UTIL) / ROUNDING_UTIL;
        if (successFulPayment) {
            json.put("amount", roundedValue);
            json.put("commerciant", commerciant);
        } else if (cardCreation) {
            json.put("account", account);
            json.put("card", card);
            json.put("cardHolder", cardholder);
        }
        return json;
    }

    /**
     * Creates a new instance of CardTransaction with the updated description.
     * Used for non-repetitive code if we want the same base transaction.
     *
     * @param newDescription The new description to set.
     * @return A new CardTransaction instance with updated description.
     */
    public CardTransaction changeDescription(final String newDescription) {
        return new CardTransaction(
                newDescription,
                getTimestamp(),
                account,
                card,
                cardholder,
                amount,
                commerciant
        );
    }

    /**
     * Creates a new instance of CardTransaction with the updated card number.
     * Used for non-repetitive code if we want the same base transaction.
     *
     * @param number The new card number to set for the transaction.
     * @return A new CardTransaction object with the updated card number.
     */
    public CardTransaction changeNumber(final String number) {
        return new CardTransaction(
                getDescription(),
                getTimestamp(),
                account,
                number,
                cardholder,
                amount,
                commerciant
        );
    }


}
