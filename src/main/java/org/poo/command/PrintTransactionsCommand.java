package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class PrintTransactionsCommand extends Command {

    public PrintTransactionsCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * The printings of transaction does not modify the bank instance so
     * this method is empty
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     * Updates the output with the transactions associated with the provided email.
     *
     * @param input The input containing the details (email, timestamp) for filtering and output.
     * @param mapper The ObjectMapper instance used to format the transaction data as JSON.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        commandOutput.put("command", "printTransactions");
        ArrayNode output = mapper.createArrayNode();
        commandOutput.set("output", output);
        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(input.getEmail())) {
                for (Transaction transaction : user.getTransactions()) {
                    output.add(transaction.toJSON(mapper));
                }
                break;
            }
        }
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
