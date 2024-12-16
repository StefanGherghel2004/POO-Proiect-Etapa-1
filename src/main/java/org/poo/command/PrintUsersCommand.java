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
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        // method does not make changes in bank class
    }

    /**
     *
     * @param input
     * @param mapper
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
