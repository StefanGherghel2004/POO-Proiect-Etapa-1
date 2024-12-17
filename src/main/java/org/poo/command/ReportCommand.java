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

import org.poo.bank.transactions.Transaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public final  class ReportCommand extends Command {
    // flag to indicate is the report is a "spendingsReport"
    private boolean spendingsReport;

    private List<Commerciant> commerciantsList = new ArrayList<>();

    /**
     * Adds a new commerciant to the list.
     *
     * @param name The name of the commerciant to add.
     */
    public void addCommerciant(final String name) {
        commerciantsList.add(new Commerciant(name));
    }

    /**
     * Collects transactions from the given account within the specified timestamp range.
     *
     * @param transactions The list to add transactions to.
     * @param target The account from which transactions are collected.
     * @param start The start timestamp of the range.
     * @param end The end timestamp of the range.
     */
    private void collectTransactions(final ArrayNode transactions, final Account target,
                                     final int start, final int end) {
        ObjectMapper mapper = new ObjectMapper();
        transactions.addAll(
                target.getTransactions().stream()
                        .filter(transaction -> transaction.getTimestamp() >= start
                                              && transaction.getTimestamp() <= end)
                        .map(transaction -> transaction.toJSON(mapper)) // Convert to JSON
                        .toList() // Collect as a List
        );

        // If generating a spending report, filter out non-card payments
        if (spendingsReport) {
            for (int i = transactions.size() - 1; i >= 0; i--) {
                JsonNode transaction = transactions.get(i);
                if (!"Card payment".equals(transaction.get("description").asText())) {
                    transactions.remove(i); // Remove if the description is not "Card payment"
                }
            }
        }
    }

    /**
     * Increases the received amount for the given commerciant.
     *
     * @param name The name of the commerciant to increase the received amount for.
     * @param amount The amount to increase.
     */
    public void increaseCommerciantMoney(final String name, final double amount) {
        commerciantsList.stream()
                .filter(commerciant -> commerciant.getName().equals(name))
                .findFirst() // Get the first match
                .ifPresent(commerciant ->
                        commerciant.setReceivedAmount(commerciant.getReceivedAmount() + amount));
    }

    public ReportCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * The report command does not modify the bank instance so this method
     * is empty.
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     * Updates the output after executing the report command.
     *
     * This method generates the final report, including the account's transactions and details
     * about commercants if a spending report is requested.
     *
     * @param input The input for the command (timestamps, account information).
     * @param mapper The ObjectMapper used to convert data to JSON format.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

        ObjectNode output = mapper.createObjectNode();
        Account target = bank.findAccount(input.getAccount());
        // If the account is not found, return an error message
        if (target == null) {
            commandOutput.put("command", input.getCommand());
            Transaction transaction =
                    new Transaction("Account not found", input.getTimestamp());
            commandOutput.set("output", transaction.toJSON(mapper));
            commandOutput.put("timestamp", input.getTimestamp());
            return;
        }

        output = target.toJSON(mapper);
        output.remove("type");
        output.remove("cards");

        if (!output.isEmpty()) {
            commandOutput.put("command", input.getCommand());

            ArrayNode transactions = mapper.createArrayNode();
            collectTransactions(transactions, target, input.getStartTimestamp(),
                                input.getEndTimestamp());

            // If spendings report is requested, process commerciants and received amounts
            if (spendingsReport) {
            ArrayNode commerciants = mapper.createArrayNode();
                for (JsonNode transaction : transactions) {

                        ObjectNode transactionObject = (ObjectNode) transaction;
                        // Extract the commerciant name
                        String commerciantName = transactionObject.has("commerciant")
                                ? transactionObject.get("commerciant").asText() : null;

                        // Add new commerciant if not already in the list
                        if (commerciantName != null
                                && !getCommerciantsList().stream()
                                        .anyMatch(commerciant -> commerciant.getName().
                                                                equals(commerciantName))) {
                            addCommerciant(commerciantName);
                        }

                        // Update the commerciant's received amount
                        if (commerciantName != null) {
                            increaseCommerciantMoney(commerciantName,
                                                     transactionObject.get("amount").asDouble());
                        }
                }

                // Sort the commerciants by name
                commerciantsList.sort(Comparator.comparing(Commerciant::getName));

                // Add the commerciants to the report
                for (Commerciant commerciant : commerciantsList) {
                    commerciants.add(commerciant.toJSON(mapper));
                }

                output.put("commerciants", commerciants);
            }
            output.put("transactions", transactions);
            commandOutput.set("output", output);
        } else {
            commandOutput.put("command", input.getCommand());
            Transaction transaction =
                    new Transaction("Account not found", input.getTimestamp());
            commandOutput.set("output", transaction.toJSON(mapper));
        }
        commandOutput.put("timestamp", input.getTimestamp());
    }

}
