package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.bank.Account;
import org.poo.bank.User;
import org.poo.bank.Card;
import org.poo.bank.transactions.CardTransaction;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class CreateCardCommand extends Command {

    public CreateCardCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasAccount(input.getAccount());
        if (user == null) {
            return;
        }
        Account account = user.getAccount(input.getAccount());

        if (!user.getEmail().equals(input.getEmail())) {
            return;
        }

        Card card = new Card();

        if (input.getCommand().equals("createOneTimeCard")) {
            card.setOneTime(true);
        }

        account.addCard(card);
        CardTransaction transaction = new CardTransaction("New card created", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
        transaction.setCardCreation(true);
        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }

}
