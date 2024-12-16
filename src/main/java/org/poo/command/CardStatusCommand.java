package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Account;
import org.poo.bank.User;
import org.poo.bank.Card;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

public final class CardStatusCommand extends Command {

    private boolean notFound;
    public CardStatusCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasCard(input.getCardNumber());
        if (user == null) {
            notFound = true;
            return;
        }
        Account account = user.findAccountHasCard(input.getCardNumber());
        Card card = account.findCard(input.getCardNumber());

        if ((account.getBalance() <= account.getMinBalance() - 30 || account.getBalance() == 0) && !card.isFrozen()) {
            Transaction transaction = new Transaction("You have reached the minimum amount of funds, the card will be frozen", input.getTimestamp());
            user.addTransaction(transaction);
        }

    }

    /**
     *
     * @param input
     * @param mapper
     */
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
