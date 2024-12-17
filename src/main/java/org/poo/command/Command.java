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
     * Executes the specific command.
     * Each subclass should implement the logic for the command's execution.
     * This method should define the action that the command performs using the provided input.
     * It must modify the state of the bank(including its users, accounts etc.).
     * If a given command doesn't change the state of the bank then this method is empty.
     *
     * @param input The input containing the necessary parameters to execute the command.
     */
    public abstract void execute(CommandInput input);

    /**
     * Updates the command output based on the execution of the command.
     * This method is called after executing the command to update the `commandOutput` node with
     * result of the command execution. It should create a formatted JSON object for the output.
     * If a command does not update the output this method should be empty.
     *
     * @param input The input containing any relevant data for the output.
     * @param mapper The ObjectMapper used to format and update the output in JSON.
     */
    public abstract void updateOutput(CommandInput input, ObjectMapper mapper);
}
