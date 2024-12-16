package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Account {
    private List<Commerciant> commerciants = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private List<Card> cards = new ArrayList<Card>();
    private double interestRate;

    private boolean setMinBalance;
    private double minBalance = 0;


    public Account(final String iban, final double balance, final String currency, final String type, final double interestRate) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.interestRate = interestRate;
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("IBAN", iban);
        json.put("balance", balance);
        json.put("currency", currency);
        json.put("type", type);
        ArrayNode cardsArray = mapper.createArrayNode();
        for (Card card : cards) {
            cardsArray.add(card.toJSON(mapper));
        }
        json.set("cards", cardsArray);
        return json;
    }

    /**
     *
     * @param card
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     *
     * @param amount
     */
    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     *
     * @param alias
     */
    public void addAlias(final String alias) {
        aliases.add(alias);
    }

    /**
     *
     * @param transaction
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     *
     * @param cardNumber
     * @return
     */
    public Card findCard(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     *
     */
    public void frozeCards() {
        for (Card card : cards) {
            card.setStatus("frozen");
        }
    }

    /**
     *
     */
    public void addInterest() {
        balance = balance + balance * (interestRate / 100);
    }

    /**
     *
     * @param rate
     * @param timestamp
     */
    public void changeInterestRate(final double rate, final int timestamp) {
        interestRate = rate;
        Transaction transaction = new Transaction("Interest rate of the account changed to " + interestRate, timestamp);
        addTransaction(transaction);
    }


}
