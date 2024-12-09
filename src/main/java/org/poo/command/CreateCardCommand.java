package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;

public class CreateCardCommand extends Command {
    public CreateCardCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    Card card = new Card();
                    account.addCard(card);
                    CardTransaction transaction = new CardTransaction("New card created", input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(), input.getAmount(), input.getCommerciant());
                    transaction.setCardCreation(true);
                    user.addTransaction(transaction);
                    account.addTransaction(transaction);
                }
            }
        }
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }

}
