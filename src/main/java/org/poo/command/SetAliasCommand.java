package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAliasCommand extends Command {

    public SetAliasCommand(Bank bank, ObjectMapper mapper) {
        super(bank,mapper);
    }

    public void execute(CommandInput input) {

    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }
}
