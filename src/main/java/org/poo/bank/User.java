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
     *
     * @param input
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
     *
     * @param mapper
     * @return
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
     *
     * @param iban
     * @return
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
