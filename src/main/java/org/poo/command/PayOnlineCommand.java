package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.transactions.CardTransaction;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

import static org.poo.outputConstants.OutputConstants.CARD_FROZEN;
import static org.poo.outputConstants.OutputConstants.FUNDS_ERROR;
import static org.poo.outputConstants.OutputConstants.CARD_DESTROYED;
import static org.poo.outputConstants.OutputConstants.CARD_CREATED;
import static org.poo.utils.Utils.generateCardNumber;

public final class PayOnlineCommand extends Command {
    // Flag to indicate if the card was not found
    private boolean cardNotFound;
    public PayOnlineCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the pay online transaction, which involves:
     * 1. Verifying that the card exists.
     * 2. Ensuring the card is not frozen.
     * 3. Checking if the account has sufficient funds for the transaction.
     * 4. Proceeding with the payment, adjusting balances, and handling one-time cards.
     * 5. Generating appropriate transactions for the action.
     *
     * @param input The CommandInput containing the necessary information.
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasCard(input.getCardNumber());
        if (user == null) {
            cardNotFound = true;
            return;
        }

        Account account = user.findAccountHasCard(input.getCardNumber());
        Card card = account.findCard(input.getCardNumber());

        // If the card is frozen, record the transaction and exit
        if (card.isFrozen()) {
            user.addTransaction(new Transaction(CARD_FROZEN,
                    input.getTimestamp()));
            return;
        }

        double amount = bank.convert(account.getCurrency(), input.getCurrency(), input.getAmount());

        // If the account balance is insufficient, record the transaction and exit
        if (account.getBalance() < amount) {
            user.addTransaction(new Transaction(FUNDS_ERROR,
                    input.getTimestamp()));
            return;
        }

        // If the payment would cause the account balance to go below the minimum, freeze the card
        if (account.getBalance() - amount <= account.getMinBalance()) {
            user.addTransaction(new Transaction(CARD_FROZEN,
                    input.getTimestamp()));
            card.setStatus("frozen");
            return;
        }

        account.setBalance(account.getBalance() - amount);
        CardTransaction transaction = new CardTransaction(
                "Card payment",
                input.getTimestamp(),
                account.getIban(),
                card.getCardNumber(),
                user.getEmail(),
                amount, // amount could be reused here
                input.getCommerciant()
        );

        // Mark the payment as successful and add the transactions
        transaction.setSuccessFulPayment(true);
        user.addTransaction(transaction);
        account.addTransaction(transaction);

        // If the card is a one-time card, destroy the card and issue a new one
        if (card.isOneTime()) {
            CardTransaction transactionDestroyed = transaction.changeDescription(CARD_DESTROYED);

            card.setCardNumber(generateCardNumber());

            CardTransaction transactionCreate = transaction.changeDescription(CARD_CREATED)
                                                            .changeNumber(card.getCardNumber());

            transactionDestroyed.setCardCreation(true);
            transactionCreate.setCardCreation(true);

            user.addTransaction(transactionDestroyed);
            user.addTransaction(transactionCreate);
        }

    }

    /**
     * Updates the output in case the card was not found.
     *
     * @param input The CommandInput containing the timestamp for the error message.
     * @param mapper The ObjectMapper used to construct the JSON output.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        if (cardNotFound) {
            commandOutput.put("command", "payOnline");
            ObjectNode output =  mapper.createObjectNode();
            output.put("timestamp", input.getTimestamp());
            output.put("description", "Card not found");
            commandOutput.put("output", output);
            commandOutput.put("timestamp", input.getTimestamp());
        }
    }
}
