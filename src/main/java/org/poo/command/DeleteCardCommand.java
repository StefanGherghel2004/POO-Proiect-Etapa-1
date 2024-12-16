package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.transactions.CardTransaction;
import org.poo.fileio.CommandInput;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.Account;
import org.poo.bank.Card;

public final class DeleteCardCommand extends Command {

    public DeleteCardCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
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
        CardTransaction transaction = new CardTransaction("The card has been destroyed", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
        transaction.setCardCreation(true);
        user.addTransaction(transaction);
        account.getCards().remove(card);

    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
