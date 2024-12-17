package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

import static org.poo.outputConstants.OutputConstants.INTEREST_CHANGE;

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

    public static final double UTIL = 100;


    public Account(final String iban, final double balance, final String currency,
                   final String type, final double interestRate) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.interestRate = interestRate;
    }

    /**
     * Converts the Account into a JSON representation
     * It uses the toJSON method in Card class to represent all the cards
     * the account has
     *
     * @param mapper The ObjectMapper used to serialize the object to JSON.
     * @return An ObjectNode containing the serialized JSON representation.
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
     * Adds a new card to the account.
     * The card is added to the list of cards associated with this account.
     *
     * @param card The card to be added to the account.
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * Adds funds to the account's balance.
     * The balance is increased by the specified amount.
     *
     * @param amount The amount to be added to the balance.
     */
    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Adds an alias to the list of aliases.
     * Aliases are used for transactions with accounts under multiple identifiers,
     * not just by iban.
     *
     * @param alias The alias to be added to the list of aliases.
     */
    public void addAlias(final String alias) {
        aliases.add(alias);
    }

    /**
     * Adds a transaction to the list of transactions.
     *
     * @param transaction The transaction to be added to the account.
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Finds a card associated with the account by its card number.
     *
     * @param cardNumber The card number to search for.
     * @return The card matching the provided card number, or null if no card matches.
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
     * Freezes all cards associated with the account.
     * The status of each card is set to "frozen", preventing transactions from being processed.
     */
    public void frozeCards() {
        for (Card card : cards) {
            card.setStatus("frozen");
        }
    }

    /**
     * Adds interest to the account's balance.
     * The balance is updated by applying the current interest rate divided by 100..
     */
    public void addInterest() {
        balance = balance + balance * (interestRate / UTIL);
    }

    /**
     * Changes the interest rate for the account and records a transaction for this change.
     * The new interest rate is applied and a corresponding transaction is logged.
     *
     * @param rate The new interest rate to be applied to the account.
     * @param timestamp The timestamp of when the interest rate change occurred.
     */
    public void changeInterestRate(final double rate, final int timestamp) {
        interestRate = rate;
        Transaction transaction = new Transaction(INTEREST_CHANGE + interestRate, timestamp);
        addTransaction(transaction);
    }


}
