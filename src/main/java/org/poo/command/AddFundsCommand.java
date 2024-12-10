package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public final class AddFundsCommand extends Command {
    public AddFundsCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        bank.addFunds(input);
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
