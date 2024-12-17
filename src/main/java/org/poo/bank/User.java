package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

import static org.poo.utils.Utils.generateIBAN;

@Getter
@Setter
public class User {
    private List<Transaction> transactions = new ArrayList<>();
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts = new ArrayList<>();

    public User(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(final UserInput user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
    }

    /**
     * Adds a new account to the accounts list
     * A transaction for the creation of the new account is added to the transactions list.
     *
     * @param input The input data used to create the new account (currency, type etc.).
     */
    public void addAccount(final CommandInput input) {
        Account account = new Account(generateIBAN(), 0, input.getCurrency(),
                                      input.getAccountType(), input.getInterestRate());
        accounts.add(account);
        addTransaction(new Transaction("New account created", input.getTimestamp()));
        accounts.getLast().addTransaction(new Transaction("New account created",
                                          input.getTimestamp()));
    }

    /**
     * Converts the User instance into a JSON representation.
     * Each account is represented using its `toJSON` method.
     *
     * @param mapper The ObjectMapper used to serialize the object to JSON.
     * @return An ObjectNode containing the JSON representation of the User.
     */
    public ObjectNode toJSON(final ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("firstName", firstName);
        json.put("lastName", lastName);
        json.put("email", email);
        ArrayNode accountsArray = mapper.createArrayNode();
        for (Account account : accounts) {
            accountsArray.add(account.toJSON(mapper));
        }
        json.set("accounts", accountsArray);
        return json;
    }

    /**
     * Adds a transaction to the user's transaction list.
     * Transactions are used to track activities or actions associated with the user.
     *
     * @param transaction The transaction to be added to the user's transaction history.
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Finds an account associated with the user that has a card matching the provided card number.
     *
     * @param cardNumber The card number to search for.
     * @return The account associated with the card, or null if no such account is found.
     */
    public Account findAccountHasCard(final String cardNumber) {
        for (Account account : accounts) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves an account by its IBAN.
     *
     * @param iban The IBAN of the account to search for.
     * @return The account with the matching IBAN, or null if no such account is found.
     */
    public Account getAccount(final String iban) {
        for (Account account : accounts) {
            if (account.getIban().equals(iban)) {
                return account;
            }
        }
        return null;
    }

}
