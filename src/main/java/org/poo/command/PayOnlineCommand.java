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

import static org.poo.utils.Utils.generateCardNumber;

public final class PayOnlineCommand extends Command {

    private boolean cardNotFound;
    public PayOnlineCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasCard(input.getCardNumber());
        if (user == null) {
            cardNotFound = true;
            return;
        }

        Account account = user.findAccountHasCard(input.getCardNumber());
        Card card = account.findCard(input.getCardNumber());

        if (card.isFrozen()) {
            user.addTransaction(new Transaction("The card is frozen",
                    input.getTimestamp()));
            return;
        }

        double amount = bank.convert(account.getCurrency(), input.getCurrency(), input.getAmount());

        if (account.getBalance() < amount) {
            user.addTransaction(new Transaction("Insufficient funds",
                    input.getTimestamp()));
            return;
        }

        if (account.getBalance() - amount <= account.getMinBalance()) {
            user.addTransaction(new Transaction("The card is frozen",
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

        transaction.setSuccessFulPayment(true);
        user.addTransaction(transaction);
        account.addTransaction(transaction);

        if (card.isOneTime()) {
            CardTransaction transactionDestroyed = transaction.changeDescription("The card has been destroyed");

            card.setCardNumber(generateCardNumber());

            CardTransaction transactionCreate = transaction.changeDescription("New card created")
                                                            .changeNumber(card.getCardNumber());

            transactionDestroyed.setCardCreation(true);
            transactionCreate.setCardCreation(true);

            user.addTransaction(transactionDestroyed);
            user.addTransaction(transactionCreate);
        }

    }

    /**
     *
     * @param input
     * @param mapper
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
