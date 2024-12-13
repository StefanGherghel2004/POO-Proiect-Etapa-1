package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

public final class CardStatusCommand extends Command {

    private boolean notFound;
    public CardStatusCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        if ((account.getBalance() <= account.getMinBalance() - 30 || account.getBalance() == 0 ) && !card.isFrozen()) {
                            Transaction transaction = new Transaction("You have reached the minimum amount of funds, the card will be frozen", input.getTimestamp());
                            user.addTransaction(transaction);
                        }
                        return;
                    }
                }
            }
        }
        notFound = true;
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        if (notFound) {
            ObjectNode output = mapper.createObjectNode();
            output.put("description", "Card not found");
            output.put("timestamp", input.getTimestamp());
            commandOutput.put("command", "checkCardStatus");
            commandOutput.set("output", output);
            commandOutput.put("timestamp", input.getTimestamp());
        }
    }
}
