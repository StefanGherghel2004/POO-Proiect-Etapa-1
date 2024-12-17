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
     * Executes the add funds command by finding the account and adding the specified amount.
     *
     * @param input The command input containing the account iban and the amount to be added.
     */
    public void execute(final CommandInput input) {
        Account account = bank.findAccount(input.getAccount());
        if (account != null) {
            account.addFunds(input.getAmount());
        }
    }

    /**
     * Updates the output of the command.
     * This method is intended to populate the output of the command, but it is currently empty.
     *
     * @param input The command input object containing the data needed for output.
     * @param mapper The ObjectMapper instance for JSON processing, if needed for output formatting.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        // maybe deal with the case where the account is not found
    }
}
