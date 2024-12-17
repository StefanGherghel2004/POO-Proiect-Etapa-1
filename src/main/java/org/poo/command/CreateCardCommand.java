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
     * Executes the command to create a new card for the specified user and account.
     * This method handles the logic for creating a new card (standard or one-time)
     * The card is then associated with the specified account,
     * and a card transaction is created to reflect the card creation.
     *
     * @param input The input containing the necessary parameters to create the card.
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasAccount(input.getAccount());
        if (user == null) {
            return;
        }
        Account account = user.getAccount(input.getAccount());

        // Ensure the email matches the account holder's email
        if (!user.getEmail().equals(input.getEmail())) {
            return;
        }

        Card card = new Card();

        if (input.getCommand().equals("createOneTimeCard")) {
            card.setOneTime(true);
        }

        // Add the card to the account
        account.addCard(card);

        // Create a new card transaction for the card creation
        CardTransaction transaction = new CardTransaction("New card created",
                input.getTimestamp(), account.getIban(), card.getCardNumber(), user.getEmail(),
                input.getAmount(), input.getCommerciant());
        transaction.setCardCreation(true);

        // Add the transaction to both the user and account
        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    /**
     * Updates the output after executing the command.
     * This method could include any results or status related to the card creation.
     *
     * @param input The input containing relevant data for the output.
     * @param mapper The ObjectMapper used to format and update the output in JSON.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        // empty in current implementation
    }

}
