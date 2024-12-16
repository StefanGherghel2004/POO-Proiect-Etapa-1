package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.transactions.SplitTransaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SplitPaymentCommand extends Command {

    public SplitPaymentCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        double realAmount = input.getAmount() / input.getAccounts().size();
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

        // sorting the list of account to be in the same order as given in input
        splitingAccounts.sort(Comparator.comparingInt(account -> input.getAccounts().indexOf(account.getIban())));

        if (splitingAccounts.size() == input.getAccounts().size()) {

            String lastInsufficientIBAN = null;
            for (int i = splitingAccounts.size() - 1; i >= 0; i--) {
                Account account = splitingAccounts.get(i);
                double rate = bank.getRate(account.getCurrency(), input.getCurrency());
                if (account.getBalance() < (1 / rate) * realAmount) {
                    // Store the IBAN of the current insufficient account and break
                    lastInsufficientIBAN = account.getIban();
                    break;
                }
            }

            for (int i = 0; i < splitingAccounts.size(); i++) {
                Account account = splitingAccounts.get(i);
                if (lastInsufficientIBAN == null) {
                    double rate = bank.getRate(account.getCurrency(), input.getCurrency());
                    account.setBalance(account.getBalance() - (1 / rate) * realAmount);
                }
                String value = String.format("%.2f", input.getAmount());
                SplitTransaction transaction = new SplitTransaction("Split payment of " + value + " " + input.getCurrency(), input.getTimestamp(), input.getCurrency(), input.getAccounts(), realAmount, "");
                if (lastInsufficientIBAN != null) {
                    transaction.setError(true);
                    transaction.setErrorDescription("Account " + lastInsufficientIBAN + " has insufficient funds for a split payment.");
                }
                splitingUsers.get(i).addTransaction(transaction);
                splitingAccounts.get(i).addTransaction(transaction);
            }
        }
    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
