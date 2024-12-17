package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;


public final class PrintUsersCommand extends Command {
    public PrintUsersCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * The printing of users does not modify the bank instance so
     * this method is empty
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     * Updates the output with the list of users in the bank.
     * This method loops through all the users in the bank, converts each user's data
     * to JSON using the `toJSON` method in User class.
     *
     * @param input The input containing necessary details like the timestamp for the output.
     * @param mapper The ObjectMapper instance used to convert user data into JSON format.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        commandOutput.put("command", "printUsers");
        ArrayNode users = mapper.createArrayNode();
        for (User user : bank.getUsers()) {
            ObjectNode userNode = user.toJSON(mapper);
            users.add(userNode);
        }
        commandOutput.set("output", users);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
