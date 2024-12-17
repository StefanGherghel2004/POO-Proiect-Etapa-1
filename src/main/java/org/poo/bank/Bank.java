package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
public class Bank {

    private Map<String, List<String>> aliases = new HashMap<>();

    private List<User> users = new ArrayList<User>();

    private Map<String, Double> exchangeRates = new HashMap<>();

    // limit used to warn an user if an account's balance is low
    public static final double LIM = 30;

    /**
     * Adds a new exchange rate between two currencies.
     * This method stores both the forward and reverse exchange rate.
     *
     * @param from The currency to convert from.
     * @param to The currency to convert to.
     * @param rate The exchange rate from the "from" currency to the "to" currency.
     */
    public void addRate(final String from, final String to, final double rate) {
        exchangeRates.put(from + "->" + to, rate);
        exchangeRates.put(to + "->" + from, 1 / rate);
    }

    /**
     * Retrieves the exchange rate for a given pair of currencies.
     *
     * @param from The currency to convert from.
     * @param to The currency to convert to.
     * @return The exchange rate from "from" to "to".
     * @throws IllegalArgumentException If no exchange rate is available.
     */
    public double getRate(final String from, final String to) {

        if (from.equals(to)) {
            return 1.0;
        }

        if (exchangeRates.containsKey(from + "->" + to)) {
            return exchangeRates.get(from + "->" + to);
        }
        throw new IllegalArgumentException("No exchange rate available for " + from + " to " + to);
    }

    public Bank() {

    }

    public Bank(final Bank bank) {
        users = bank.getUsers();
        exchangeRates = bank.getExchangeRates();
        aliases = bank.getAliases();
    }

    /**
     * Adds a new user to the bank using the provided user input.
     *
     * @param user The user input that contains the information for creating a new user.
     */
    public void addUser(final UserInput user) {
        User u = new User(user);
        users.add(u);
    }

    /**
     * Adds multiple users to the bank by iterating over the provided array of user inputs.
     *
     * @param inputUsers The array of user input data to add to the bank.
     * @return The current Bank instance.
     */
    public Bank addUsers(final UserInput[] inputUsers) {
        for (UserInput user : inputUsers) {
            addUser(user);
        }
        return this;
    }

    /**
     * Adds multiple exchange rates to the bank from the provided array of exchange input data.
     *
     * @param exchanges The array of exchange input data
     * @return The current Bank instance.
     */
    public Bank addExchangeRates(final ExchangeInput[] exchanges) {
        for (ExchangeInput exchange : exchanges) {
            addRate(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }
        return this;
    }

    /**
     * Creates a set of all unique currencies present in the exchange rates stored by the bank.
     * Used in addSecondaryExchangeRates method to add exchange rates between all currencies
     *
     * @return A set of strings representing all unique currencies.
     */
    public Set<String> createCurrencyList() {
        Set<String> currencies = new HashSet<>();
        for (String key : exchangeRates.keySet()) {
            String[] parts = key.split("->");
            currencies.add(parts[0]);
            currencies.add(parts[1]);
        }
        return currencies;
    }

    /**
     * Adds secondary exchange rates to the bank by calculating them using existing exchange rates.
     * This method iterates over the set of all currencies and computes the exchange rates
     * between every pair of currencies using intermediate currencies.
     *
     * @return The current Bank instance with updated exchange rates.
     */
    public Bank addSecondaryExchangeRates() {

        Set<String> currencies = createCurrencyList();

        Map<String, Double> rates = exchangeRates;
        for (String intermediate : currencies) {
            for (String from : currencies) {
                for (String to : currencies) {
                    if (rates.containsKey(from + "->" + intermediate)
                            && rates.containsKey(intermediate + "->" + to)) {
                        double newRate = rates.get(from + "->" + intermediate)
                                * rates.get(intermediate + "->" + to);
                        rates.put(from + "->" + to, newRate);
                        rates.put(to + "->" + from, 1 / newRate);
                    }
                }
            }
        }
        return this;
    }

    /**
     * Adds an alias for a given key in the bank's aliases map.
     * If the key does not already exist, it will be created with the given value.
     *
     * @param key The key for which the alias is being added.
     * @param value The alias value associated with the key.
     */
    public void addAlias(final String key, final String value) {
        aliases.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    /**
     * Finds and returns a user by their email address.
     *
     * @param email The email address of the user to be found.
     * @return The User associated with the email, or null if no such user exists.
     */
    public User findUser(final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds and returns an account by its IBAN.
     *
     * @param iban The IBAN of the account to be found.
     * @return The Account associated with the IBAN, or null if no such account exists.
     */
    public Account findAccount(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns the user associated with a given account IBAN.
     *
     * @param iban The IBAN of the account.
     * @return The User object associated with the account, or null if no such user exists.
     */
    public User findUserHasAccount(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns the user associated with a given card number.
     *
     * @param number The card number to search for.
     * @return The User object associated with the card, or null if no such user exists.
     */
    public User findUserHasCard(final String number) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(number)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Converts an amount of money from one currency to another using the exchange
     * rates stored in the bank.
     *
     * @param from The currency to convert from.
     * @param to The currency to convert to.
     * @param amount The amount of money to be converted.
     * @return The equivalent amount in the "to" currency.
     */
    public double convert(final String from, final String to, final double amount) {
        double rate = getRate(from, to);
        return (1 / rate) * amount;
    }

}

