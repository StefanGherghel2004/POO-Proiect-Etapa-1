package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public final class AddFundsCommand extends Command {
    public AddFundsCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        Account account = bank.findAccount(input.getAccount());
        if (account != null) {
            account.addFunds(input.getAmount());
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
