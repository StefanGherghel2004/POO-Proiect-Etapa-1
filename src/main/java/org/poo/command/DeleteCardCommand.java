package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.transactions.CardTransaction;
import org.poo.fileio.CommandInput;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.Account;
import org.poo.bank.Card;

import static org.poo.outputConstants.OutputConstants.CARD_DESTROYED;

public final class DeleteCardCommand extends Command {

    public DeleteCardCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the delete card operation.
     * This method checks if the user exists and has an account containing the specified card.
     * If the account and card are found, the card is deleted from the account, and a transaction
     * representing the card destruction is added to both the user’s and account’s
     * transaction history.
     *
     * @param input The input containing the necessary information..
     */
    public void execute(final CommandInput input) {
        User user = bank.findUser(input.getEmail());
        if (user == null) {
            return;
        }

        Account account = user.findAccountHasCard(input.getCardNumber());
        if (account == null) {
            return;
        }

        Card card = account.findCard(input.getCardNumber());
        // Create a new CardTransaction to record the destruction of the card
        CardTransaction transaction = new CardTransaction(CARD_DESTROYED, input.getTimestamp(),
                account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(),
                input.getCommerciant());

        transaction.setCardCreation(true);

        // Add the transaction to both the user's and account's transaction histories
        account.addTransaction(transaction);
        user.addTransaction(transaction);
        account.getCards().remove(card);

    }

    /**
     * Updates the output after executing the delete card operation.
     * Emptu method in this implementation
     *
     * @param input The input containing relevant data for the operation.
     * @param mapper The ObjectMapper instance used to generate the output if needed.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        // treat cases where the user or the card is not found
    }
}
