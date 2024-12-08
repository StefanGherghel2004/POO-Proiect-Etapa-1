package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import javax.sound.midi.SysexMessage;

public class PrintUsersCommand extends Command {
    public PrintUsersCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        // method does not make changes in bank class
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {
        commandOutput.put("command","printUsers");
        ArrayNode users = mapper.createArrayNode();
        for (User user : bank.getUsers()) {
            ObjectNode userNode = user.toJSON(mapper);
            users.add(userNode);
        }
        commandOutput.set("output",users);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
