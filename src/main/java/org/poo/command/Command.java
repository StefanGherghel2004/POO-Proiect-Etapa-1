package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import java.io.IOException;

@Getter
@Setter
public abstract class Command {
    protected Bank bank;
    protected ObjectNode commandOutput;
    public Command(Bank bank, ObjectMapper mapper) {
        this.bank = new Bank(bank);
        this.commandOutput = mapper.createObjectNode();
    }
    abstract public void execute(CommandInput input);

    abstract public void updateOutput(CommandInput input, ObjectMapper mapper);
}
