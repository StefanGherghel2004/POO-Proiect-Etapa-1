package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class SetMinimumBalanceCommand extends Command {

    public SetMinimumBalanceCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        for (User user: bank.getUsers()) {
            for (Account account : user.getAccounts() ) {
                if (account.getIban().equals(input.getAccount())) {
                    account.setMinBalance(input.getAmount());
                    account.setSetMinBalance(true);
                    System.out.println(account.getMinBalance());
                    System.out.println(account.getBalance());
                    if (account.getBalance() <= account.getMinBalance()) {
                        for (Card card: account.getCards()) {
                            card.setStatus("frozen");
                        }
                    }
                }
            }
        }
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }
}
