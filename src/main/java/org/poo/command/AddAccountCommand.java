package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

public class AddAccountCommand extends Command {

    public AddAccountCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    @Override
    public void execute(CommandInput input) {
        bank.addAccount(input);
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }
}
