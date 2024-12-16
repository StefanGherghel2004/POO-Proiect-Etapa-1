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

public final class SendMoneyCommand extends Command {

    public SendMoneyCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    private static String findRealAccount(final Map<String, List<String>> aliases, final String alias) {

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
        String receiverIban = findRealAccount(bank.getAliases(), input.getReceiver());
        Account sender = null;
        Account receiver = null;
        User senderUser = null;
        User receiverUser = null;
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount()) || account.getIban().equals(senderIban)) {
                    sender = account;
                    senderUser = user;
                }
                if (account.getIban().equals(input.getReceiver()) || account.getIban().equals(receiverIban)) {
                    receiver = account;
                    receiverUser = user;
                }
            }
        }
        if (senderUser != null && !input.getEmail().equals(senderUser.getEmail())) {
            return;
        }

        if (sender != null && receiver != null) {
            double rate = bank.getRate(sender.getCurrency(), receiver.getCurrency());

            if (sender.getBalance() >= input.getAmount()) {
                senderUser.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "sent", input.getAmount() + " " +  sender.getCurrency()));
                sender.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "sent", input.getAmount() + " " +  sender.getCurrency()));
                receiverUser.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "received", input.getAmount() * rate + " " +  receiver.getCurrency()));
                receiver.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "received", input.getAmount() * rate + " " +  receiver.getCurrency()));
                sender.setBalance(sender.getBalance() - input.getAmount());
                receiver.setBalance(receiver.getBalance() + rate * input.getAmount());
            } else {
                senderUser.addTransaction(new Transaction("Insufficient funds", input.getTimestamp()));
                sender.addTransaction(new Transaction("Insufficient funds", input.getTimestamp()));
            }
        }
    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
