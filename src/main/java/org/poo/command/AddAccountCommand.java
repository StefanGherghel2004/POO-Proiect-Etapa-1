package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class AddAccountCommand extends Command {

    public AddAccountCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the add account command by finding the user and adding a new account.
     *
     * @param input The command input containing the email of the user and account details.
     */
    public void execute(final CommandInput input) {
        User user = bank.findUser(input.getEmail());
        if (user != null) {
            user.addAccount(input);
        }
    }

    /**
     * Updates the output of the command.
     * This method is intended to populate the output of the command, but for this command
     * it is currently empty.
     *
     * @param input The command input object containing the data needed for output.
     * @param mapper The ObjectMapper instance for JSON representation
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        // maybe deal with the case where the mail given is not valid
    }
}
