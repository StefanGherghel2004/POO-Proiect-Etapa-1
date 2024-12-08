package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;

import java.util.*;

@Getter
@Setter
public class Bank {
    private List<User> users = new ArrayList<User>();

    private Map<String, Double> exchangeRates = new HashMap<>();

    public void addRate(String from, String to, double rate) {
        exchangeRates.put(from + "->" + to, rate);
        exchangeRates.put(to + "->" + from, 1 / rate);
    }

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
    }

    public void addUser(final UserInput user) {
        User u = new User(user);
        users.add(u);
    }

    public Bank addUsers(final UserInput[] users) {
        for (UserInput user : users) {
            addUser(user);
        }
        return this;
    }

    public void addAccount(final CommandInput input) {
        for (User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                user.addAccount(input);
            }
        }
    }

    public Bank addExchangeRates(final ExchangeInput[] exchanges) {
        for (ExchangeInput exchange : exchanges) {
            addRate(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }
        return this;
    }

    public Set<String> createCurrencyList() {
        Set<String> currencies = new HashSet<>();
        for (String key : exchangeRates.keySet()) {
            String[] parts = key.split("->");
            currencies.add(parts[0]);
            currencies.add(parts[1]);
        }
        return currencies;
    }

    public Bank addSecondaryExchangeRates() {

        Set<String> currencies = createCurrencyList();

        Map<String, Double> rates = exchangeRates;
        for (String intermediate : currencies) {
            for (String from : currencies) {
                for (String to : currencies) {
                    if (rates.containsKey(from + "->" + intermediate) &&
                            rates.containsKey(intermediate + "->" + to)) {
                        double newRate = rates.get(from + "->" + intermediate) *
                                rates.get(intermediate + "->" + to);
                        rates.put(from + "->" + to, newRate);
                        rates.put(to + "->" + from, 1 / newRate); // Add reverse
                    }
                }
            }
        }
        return this;
    }
}

