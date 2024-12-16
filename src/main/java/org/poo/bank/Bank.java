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

    /**
     *
     * @param from
     * @param to
     * @param rate
     */
    public void addRate(final String from, final String to, final double rate) {
        exchangeRates.put(from + "->" + to, rate);
        exchangeRates.put(to + "->" + from, 1 / rate);
    }

    /**
     *
     * @param from
     * @param to
     * @return
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
     *
     * @param user
     */
    public void addUser(final UserInput user) {
        User u = new User(user);
        users.add(u);
    }

    /**
     *
     * @param inputUsers
     * @return
     */
    public Bank addUsers(final UserInput[] inputUsers) {
        for (UserInput user : inputUsers) {
            addUser(user);
        }
        return this;
    }

    /**
     *
     * @param exchanges
     * @return
     */
    public Bank addExchangeRates(final ExchangeInput[] exchanges) {
        for (ExchangeInput exchange : exchanges) {
            addRate(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }
        return this;
    }

    /**
     *
     * @return
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
     *
     * @return
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
     *
     * @param key
     * @param value
     */
    public void addAlias(final String key, final String value) {
        aliases.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    /**
     *
     * @param email
     * @return
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
     *
     * @param iban
     * @return
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
     *
     * @param iban
     * @return
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
     *
     * @param number
     * @return
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
     *
     * @param from
     * @param to
     * @param amount
     * @return
     */
    public double convert(final String from, final String to, final double amount) {
        double rate = getRate(from, to);
        return (1 / rate) * amount;
    }






}

