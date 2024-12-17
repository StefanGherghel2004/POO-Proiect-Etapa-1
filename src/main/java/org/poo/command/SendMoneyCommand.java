package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.bank.Account;
import org.poo.bank.User;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.transactions.TransferTransaction;
import org.poo.fileio.CommandInput;

import java.util.List;
import java.util.Map;

import static org.poo.outputConstants.OutputConstants.FUNDS_ERROR;

public final class SendMoneyCommand extends Command {

    public SendMoneyCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    private static String findRealAccount(final Map<String, List<String>> aliases,
                                          final String alias) {
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            String realAccount = entry.getKey();
            List<String> aliasList = entry.getValue();

            if (aliasList.contains(alias)) {
                return realAccount;
            }
        }
        return null;
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        String senderIban = findRealAccount(bank.getAliases(), input.getAccount());
        if (senderIban != null) {
            return; // daca este un alias atunci nu e in regula comanda
        }

        Account sender = bank.findAccount(input.getAccount());
        Account receiver = bank.findAccount(input.getReceiver());

        User senderUser = sender != null ? bank.findUserHasAccount(sender.getIban()) : null;
        User receiverUser = receiver != null ? bank.findUserHasAccount(receiver.getIban()) : null;

        if (senderUser != null && !input.getEmail().equals(senderUser.getEmail())) {
            return;
        }

        if (sender != null && receiver != null) {
            double rate = bank.getRate(sender.getCurrency(), receiver.getCurrency());
            double amount = input.getAmount();

            if (sender.getBalance() >= input.getAmount()) {
                Transaction send = new TransferTransaction(input.getDescription(),
                        input.getTimestamp(), receiver.getIban(), sender.getIban(),
                        "sent", amountFormat(amount, 1, sender.getCurrency()));

                senderUser.addTransaction(send);
                sender.addTransaction(send);

                Transaction receive = new TransferTransaction(input.getDescription(),
                        input.getTimestamp(), receiver.getIban(), sender.getIban(),
                        "received", amountFormat(amount, rate, receiver.getCurrency()));

                receiverUser.addTransaction(receive);
                receiver.addTransaction(receive);

                sender.setBalance(sender.getBalance() - input.getAmount());
                receiver.setBalance(receiver.getBalance() + rate * input.getAmount());
            } else {
                senderUser.addTransaction(new Transaction(FUNDS_ERROR, input.getTimestamp()));
                sender.addTransaction(new Transaction(FUNDS_ERROR, input.getTimestamp()));
            }
        }
    }

    private String amountFormat(final double amount, final double rate, final String currency) {
        return amount * rate + " " + currency;
    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
