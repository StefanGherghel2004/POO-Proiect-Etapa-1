package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.*;
import org.poo.bank.transactions.CardTransaction;
import org.poo.fileio.CommandInput;

public final class DeleteCardCommand extends Command {

    public DeleteCardCommand(final Bank bank, final ObjectMapper mapper) {
        super (bank, mapper);
    }

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
        CardTransaction transaction = new CardTransaction("The card has been destroyed", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
        transaction.setCardCreation(true);
        user.addTransaction(transaction);
        account.getCards().remove(card);

    }

    public void updateOutput(final CommandInput Input, final ObjectMapper mapper) {

    }
}
