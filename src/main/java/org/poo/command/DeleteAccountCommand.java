package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

import java.util.Iterator;

public class DeleteAccountCommand extends Command {
    public DeleteAccountCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        for (User user : bank.getUsers()) {
            for (int i = 0; i < user.getAccounts().size(); i++) {
                Account account = user.getAccounts().get(i);
                if (account.getIban().equals(input.getAccount()) && account.getBalance() == 0)
                    user.getAccounts().remove(i);
            }
        }
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {
        commandOutput.put("command", "deleteAccount");
        ObjectNode output = mapper.createObjectNode();
        output.put("success", "Account deleted");
        output.put("timestamp", input.getTimestamp());
        commandOutput.put("output", output);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
