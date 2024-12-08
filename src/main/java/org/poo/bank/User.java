package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

import static org.poo.utils.Utils.generateIBAN;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    public List<Account> accounts = new ArrayList<>();

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(UserInput user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
    }

    public void addAccount(CommandInput input) {
        Account account = new Account(generateIBAN(), 0, input.getCurrency(), input.getAccountType());
        accounts.add(account);
    }

    public ObjectNode toJSON(ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("firstName", firstName);
        json.put("lastName", lastName);
        json.put("email", email);
        ArrayNode accountsArray = mapper.createArrayNode();
        for (Account account : accounts) {
            accountsArray.add(account.toJSON(mapper));
        }
        json.set("accounts", accountsArray);
        return json;
    }
}
