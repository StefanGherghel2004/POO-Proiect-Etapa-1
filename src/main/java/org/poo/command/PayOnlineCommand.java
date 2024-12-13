package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.bank.transactions.CardTransaction;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

import static org.poo.utils.Utils.generateCardNumber;

public final class PayOnlineCommand extends Command {

    public boolean cardNotFound;
    public PayOnlineCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                String currency = account.getCurrency();
                for (int i = 0; i < account.getCards().size(); i++) {
                    Card card = account.getCards().get(i);
                    if (card.getCardNumber().equals(input.getCardNumber())) {

                        if (card.isFrozen()) {
                            user.addTransaction(new Transaction("The card is frozen", input.getTimestamp()));
                            return;
                        }
                        double rate = bank.getRate(currency, input.getCurrency());
                        double convertedAmount = (1 / rate) * input.getAmount();

                        if (account.getBalance() < convertedAmount) {
                            user.addTransaction(new Transaction("Insufficient funds", input.getTimestamp()));
                            return;
                        }

                        if (account.getBalance() - convertedAmount <= account.getMinBalance()) {
                            user.addTransaction(new Transaction("The card is frozen", input.getTimestamp()));
                            card.setStatus("frozen");
                            return;
                        }

                        account.setBalance(account.getBalance() - convertedAmount);
                        CardTransaction transaction = new CardTransaction(
                                "Card payment",
                                input.getTimestamp(),
                                account.getIban(),
                                card.getCardNumber(),
                                user.getEmail(),
                                convertedAmount, // convertedAmount could be reused here
                                input.getCommerciant()
                        );

                        transaction.setSuccessFulPayment(true);
                        user.addTransaction(transaction);
                        account.addTransaction(transaction);

                        if (card.isOneTime()) {
                            CardTransaction transactionDestroyed = new CardTransaction(
                                    "The card has been destroyed",
                                    input.getTimestamp(),
                                    account.getIban(),
                                    card.getCardNumber(),
                                    user.getEmail(),
                                    convertedAmount, // convertedAmount could be reused here
                                    input.getCommerciant()
                            );

                            card.setCardNumber(generateCardNumber());

                            CardTransaction transactionCreate = new CardTransaction(
                                    "New card created",
                                    input.getTimestamp(),
                                    account.getIban(),
                                    card.getCardNumber(),
                                    user.getEmail(),
                                    convertedAmount, // convertedAmount could be reused here
                                    input.getCommerciant()
                            );

                            transactionDestroyed.setCardCreation(true);
                            transactionCreate.setCardCreation(true);

                            user.addTransaction(transactionDestroyed);
                            user.addTransaction(transactionCreate);
                        }
                        return;
                    }
                }
            }
        }
        cardNotFound = true;
    }

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
