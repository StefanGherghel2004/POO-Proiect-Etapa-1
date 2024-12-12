package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;

public final class DeleteCardCommand extends Command {

    public DeleteCardCommand(final Bank bank, final ObjectMapper mapper) {
        super (bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user: bank.getUsers()) {
            if (user.getEmail().equals(input.getEmail())) {
                for (Account account: user.getAccounts()) {
                    for (int i = 0; i < account.getCards().size(); i++) {
                        Card card = account.getCards().get(i);
                        if (card.getCardNumber().equals(input.getCardNumber())) {

                            CardTransaction transaction = new CardTransaction("The card has been destroyed", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
                            transaction.setCardCreation(true);
                            user.addTransaction(transaction);
                            account.getCards().remove(card);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void updateOutput(final CommandInput Input, final ObjectMapper mapper) {

    }
}
