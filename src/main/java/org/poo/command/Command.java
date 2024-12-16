package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public abstract class Command {
    protected Bank bank;
    protected ObjectNode commandOutput;
    public Command(final Bank bank, final ObjectMapper mapper) {
        this.bank = new Bank(bank);
        this.commandOutput = mapper.createObjectNode();
    }

    /**
     *
     * @param input
     */
    public abstract void execute(CommandInput input);

    /**
     *
     * @param input
     * @param mapper
     */
    public abstract void updateOutput(CommandInput input, ObjectMapper mapper);
}
