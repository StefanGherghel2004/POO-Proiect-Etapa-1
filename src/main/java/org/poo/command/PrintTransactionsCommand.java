package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class PrintTransactionsCommand extends Command {

    public PrintTransactionsCommand(Bank bank, ObjectMapper mapper) {
        super(bank,mapper);
    }

    public void execute(CommandInput input) {

    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {
        commandOutput.put("command", "printTransactions");
        ArrayNode output = mapper.createArrayNode();
        commandOutput.set("output", output);
        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(input.getEmail())) {
                for (Transaction transaction : user.getTransactions()) {
                    output.add(transaction.toJSON(mapper));
                }
            }
        }
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
