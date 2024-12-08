package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class DeleteCardCommand extends Command {

    public DeleteCardCommand(Bank bank, ObjectMapper mapper) {
        super (bank, mapper);
    }

    public void execute(CommandInput Input) {
        for (User user: bank.getUsers()) {
            if (user.getEmail().equals(Input.getEmail())) {
                for (Account account: user.getAccounts()) {
                    for (int i = 0; i < account.getCards().size(); i++) {
                        Card card = account.getCards().get(i);
                        if (card.getCardNumber().equals(Input.getCardNumber())) {
                            account.getCards().remove(card);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void updateOutput(CommandInput Input, ObjectMapper mapper) {

    }
}
