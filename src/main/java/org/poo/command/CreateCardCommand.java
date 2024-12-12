package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class CreateCardCommand extends Command {

    public boolean oneTime;

    public CreateCardCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {

                    if (!user.getEmail().equals(input.getEmail())) {
                        return;
                    }
                    Card card = new Card();
                    if (oneTime) {
                        System.out.println("ACOLOOOOOOOOO");
                        card.setOneTime(true);
                    }
                    account.addCard(card);
                    CardTransaction transaction = new CardTransaction("New card created", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
                    transaction.setCardCreation(true);
                    user.addTransaction(transaction);
                    account.addTransaction(transaction);
                }
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }

}
