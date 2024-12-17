package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.transactions.SplitTransaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public final class SplitPaymentCommand extends Command {

    public SplitPaymentCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the split payment command. This method:
     * 1. Checks if each account has sufficient funds to cover its share.
     * 2. If funds are available, deducts the corresponding amount from each account.
     * 3. If any account has insufficient funds, marks the transaction as an error.
     *
     * @param input The input data containing useful information for execution.
     */
    public void execute(final CommandInput input) {
        String currency = input.getCurrency();
        double split = input.getAmount() / input.getAccounts().size();
        List<Account> splittingAccounts = new ArrayList<Account>();
        List<User> splittingUsers = new ArrayList<User>();
        for (String iban : input.getAccounts()) {
            Account account = bank.findAccount(iban);
            if (account == null) {
                return;
            }
            splittingAccounts.add(account);
            splittingUsers.add(bank.findUserHasAccount(iban));
        }

        // errorIBAN will be null if all accounts are eligible for payment and will
        // be the iban of the last account that has insufficient funds in the other case
        String errorIBAN = checkFunds(splittingAccounts, currency, split);

        // Iterate over each account and perform the payment
        for (int i = 0; i < splittingAccounts.size(); i++) {
            Account account = splittingAccounts.get(i);

            // If all accounts have sufficient funds, proceed with the payment
            if (errorIBAN == null) {
                double payment = bank.convert(account.getCurrency(), currency, split);
                account.setBalance(account.getBalance() - payment);
            }
            String value = String.format("%.2f", input.getAmount());
            SplitTransaction transaction = new SplitTransaction(splitMessage(value, currency),
                    input.getTimestamp(), input.getCurrency(), input.getAccounts(), split, "");

            // If there was an error due to insufficient funds, mark transaction as error
            if (errorIBAN != null) {
                transaction.setError(true);
                transaction.setErrorDescription(fundsError(errorIBAN));
            }

            splittingUsers.get(i).addTransaction(transaction);
            splittingAccounts.get(i).addTransaction(transaction);
        }

    }

    private String splitMessage(final String value, final String currency) {
        return "Split payment of " + value + " " + currency;
    }

    private String fundsError(final String iban) {
        return "Account " + iban + " has insufficient funds for a split payment.";
    }

    /**
     * Checks if all the accounts involved in the split payment have sufficient funds.
     *
     * @param accounts The list of accounts to check.
     * @param currency The currency of the payment.
     * @param amount   The amount to be paid by each account.
     * @return The IBAN of the first account with insufficient funds,
     * or null if all accounts have enough funds.
     */
    private String checkFunds(final List<Account> accounts, final String currency,
                              final double amount) {
        for (int i = accounts.size() - 1; i >= 0; i--) {
            Account account = accounts.get(i);
            double rate = bank.getRate(account.getCurrency(), currency);
            if (account.getBalance() < (1 / rate) * amount) {
                return account.getIban();
            }
        }
        return null;
    }


    /**
     * This functionality does not update the output at the moment so
     * this method is empty
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
