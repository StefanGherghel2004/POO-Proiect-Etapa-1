package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class AddFundsCommand extends Command {
    public AddFundsCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    account.addFunds(input.getAmount());
                }
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
