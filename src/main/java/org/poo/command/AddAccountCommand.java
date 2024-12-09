package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

public final class AddAccountCommand extends Command {

    public AddAccountCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    @Override
    public void execute(final CommandInput input) {
        bank.addAccount(input);
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
