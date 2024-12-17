package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Account;
import org.poo.bank.User;
import org.poo.bank.Card;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

import static org.poo.bank.Bank.LIM;
import static org.poo.outputConstants.OutputConstants.FUNDS_WARNING;

public final class CardStatusCommand extends Command {

    private boolean notFound;
    public CardStatusCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the card status check command by verifying if the card exists and its status.
     * It adds a transactions to the list of transactions of its user.
     *
     * @param input The command input containing the card number.
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasCard(input.getCardNumber());
        if (user == null) {
            notFound = true;
            return;
        }
        Account account = user.findAccountHasCard(input.getCardNumber());
        Card card = account.findCard(input.getCardNumber());

        if (checkBalance(account) && !card.isFrozen()) {
            // If balance is low or zero, and card is not frozen, add a warning transaction
            Transaction transaction = new Transaction(FUNDS_WARNING, input.getTimestamp());
            user.addTransaction(transaction);
        }

    }

    private boolean checkBalance(final Account account) {
        return account.getBalance() <= account.getMinBalance() - LIM || account.getBalance() == 0;
    }

    /**
     * Updates the output of the command based on the results of the execution.
     * This method creates a JSON output in case the card is not found.
     * The output will indicate that the card could not be found,
     * along with the timestamp of the action.
     *
     * @param input The command input containing the timestamp and other relevant data.
     * @param mapper The ObjectMapper instance used for JSON representation.
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
