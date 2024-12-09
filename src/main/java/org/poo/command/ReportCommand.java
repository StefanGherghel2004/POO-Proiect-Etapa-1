package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final  class ReportCommand extends Command{

    public ReportCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {

    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

        ObjectNode output = mapper.createObjectNode();
        Account target = null;
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    target = account;
                    output = account.toJSON(mapper);
                    output.remove("type");
                    output.remove("cards");
                }
            }
        }

        if (!output.isEmpty()) {
            commandOutput.put("command", "report");

            ArrayNode transactions = mapper.createArrayNode();
            transactions.addAll(
                    target.getTransactions().stream()
                            .filter(transaction -> transaction.getTimestamp() >= input.getStartTimestamp() && transaction.getTimestamp() <= input.getEndTimestamp()) // Filter transactions
                            .map(transaction -> transaction.toJSON(mapper)) // Convert to JSON
                            .toList() // Collect as a List
            );

            output.put("transactions", transactions);
            commandOutput.set("output", output);
            commandOutput.put("timestamp", input.getTimestamp());
        }
    }

}
