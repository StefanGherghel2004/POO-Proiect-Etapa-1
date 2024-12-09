package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class SplitPaymentCommand extends Command {

    public SplitPaymentCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        double realAmount = input.getAmount() / input.getAccounts().size();
        System.out.println(realAmount);
        List<Account> splitingAccounts = new ArrayList<Account>();
        List<User> splitingUsers = new ArrayList<User>();
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (input.getAccounts().contains(account.getIban())) {
                    splitingAccounts.add(account);
                    splitingUsers.add(user);
                }

            }
        }

        if (splitingAccounts.size() == input.getAccounts().size()) {
            for (int i = 0; i < splitingAccounts.size(); i++) {
                Account account = splitingAccounts.get(i);
                double rate = bank.getRate(account.getCurrency(), input.getCurrency());
                account.setBalance(account.getBalance() - (1 / rate) * realAmount);
                String value = String.format("%.2f", input.getAmount());
                Transaction transaction = new SplitTransaction("Split payment of " + value + " " + input.getCurrency(), input.getTimestamp(), input.getCurrency(), input.getAccounts(), realAmount);
                splitingUsers.get(i).addTransaction(transaction);
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {}
}
