package org.poo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Commerciant;

import org.poo.bank.User;
import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final  class ReportCommand extends Command {

    private List<Commerciant> commerciantsList = new ArrayList<>();

    /**
     *
     * @param name
     */
    public void addCommerciant(final String name) {
        commerciantsList.add(new Commerciant(name));
    }

    /**
     *
     * @param name
     * @param amount
     */
    public void increaseCommerciantMoney(final String name, final double amount) {
        commerciantsList.stream()
                .filter(commerciant -> commerciant.getName().equals(name)) // Find the commerciant with the given name
                .findFirst() // Get the first match
                .ifPresent(commerciant -> commerciant.setReceivedAmount(commerciant.getReceivedAmount() + amount)); // Increase receivedAmount
    }

    private boolean spendingsReport;

    public ReportCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

        ObjectNode output = mapper.createObjectNode();
        Account target = null;
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    target = account;
                    output = account.toJSON(mapper);
                    output.remove("type");
                    output.remove("cards");
                }
            }
        }

        if (!output.isEmpty()) {
            commandOutput.put("command", "report");

            ArrayNode transactions = mapper.createArrayNode();

            if (!spendingsReport) {
                transactions.addAll(
                        target.getTransactions().stream()
                                .filter(transaction -> transaction.getTimestamp() >= input.getStartTimestamp() && transaction.getTimestamp() <= input.getEndTimestamp()) // Filter transactions
                                .map(transaction -> transaction.toJSON(mapper)) // Convert to JSON
                                .toList() // Collect as a List
                );
            } else {
                transactions.addAll(
                        target.getTransactions().stream()
                                .filter(trans ->
                                        "Card payment".equals(trans.getDescription())
                                                && trans.getTimestamp() >= input.getStartTimestamp()
                                                && trans.getTimestamp() <= input.getEndTimestamp()
                                )
                                .map(transaction -> transaction.toJSON(mapper)) // Convert to JSON
                                .toList() // Collect as a List
                );
            }

            if (spendingsReport) {
            ArrayNode commerciants = mapper.createArrayNode();
                for (JsonNode transaction : transactions) {
                    // Check if the transaction is an ObjectNode
                        ObjectNode transactionObject = (ObjectNode) transaction;
                        // Extract the commerciant name
                        String commerciantName = transactionObject.has("commerciant")
                                ? transactionObject.get("commerciant").asText() : null;

                        if (commerciantName != null
                                && !getCommerciantsList().stream()
                                        .anyMatch(commerciant -> commerciant.getName().
                                                                equals(commerciantName))) {
                            addCommerciant(commerciantName);
                        }

                        if (commerciantName != null) {
                            increaseCommerciantMoney(commerciantName,
                                                     transactionObject.get("amount").asDouble());
                        }
                }

                commerciantsList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
            for (Commerciant commerciant : commerciantsList) {
                commerciants.add(commerciant.toJSON(mapper));
            }


                output.put("commerciants", commerciants);
            }
            output.put("transactions", transactions);
            commandOutput.set("output", output);
        } else {
            commandOutput.put("command", "report");
            Transaction transaction = new Transaction("Account not found", input.getTimestamp());
            commandOutput.set("output", transaction.toJSON(mapper));
        }
        commandOutput.put("timestamp", input.getTimestamp());
    }

}
