##### Student - Gherghel Stefan-Ciprian 323CA

# Phase 1 OOP Project - J. POO Morgan Chase & Co.

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/2024/proiect-e1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)

Throughout this project, I implemented several classes that interact with each other to create a simplified bank system.
Here’s a breakdown of how these classes work together:

## Interaction of Classes

#### InputHandler Class
The **InputHandler** class is responsible for processing and handling various commands in the bank system.
It provides methods to map command inputs to their corresponding **command** objects (**handler** method) and to
sequentially execute and accumulate the results of these commands (**bankHandler** method).
The class supports different banking operations such as account management, transactions, and reporting.
This class and its methods serve as the first step of transforming and transmitting the input for the classes below to
work with, trying to respect the **principle of decoupling** as much as possible.

#### Command Classes
Classes that **extend** *Command abstract class* represent the second step of the process presented above, using the input 
given to call **specific methods** in the **bank class** and its components. These classes are part of the
**"Command" Design Pattern** and are implemented to prevent **other classes**(such as Bank, User, etc.) from directly
interacting with **input classes**, with two exceptions in the Bank class: the **addUsers()** and **addExchangeRates()** methods.

A **Command** is characterised by its two main methods:

###### execute() - involving functionalities that modify the state of the bank instance
###### updateOutput() - updates the output of the command (ObjectNode)

#### Bank Class
The **Bank** class represents a central entity that manages **users**, **accounts**, **exchange rates**, and **aliases**.
The class provides methods to find users or accounts by identifiers such as email or IBAN and facilitates
currency conversion between different currencies using stored exchange rates. This class has 3 **builder** methods 
(addUsers(), addExchangeRates(), addSecondaryExchangeRates) that are used in its initialisation.

#### User Class

The **User** class represents a bank user with personal information (first name, last name, email) and a **list**
of **accounts**. It provides methods for adding new accounts, tracking transactions etc. Transactions are logged for
each action, such as account creation or other account-related activities.

#### Account Class

The **Account** class represents a bank account with various attributes such as IBAN, balance, currency, type, interest rate,
and associated cards. It provides methods for managing account-related actions like adding funds, creating transactions, 
and adding cards or aliases. The class also supports functionality to change the interest rate, freeze cards,
and accumulate interest on the account balance.

#### Card Class

The **Card** class represents a bank card with attributes such as the card number, status (e.g., **"active"** or **"frozen"**),
and a flag indicating if it is a **one-time** use card. It automatically generates a unique **card number** upon creation.

#### Transaction Class

The **Transaction** class and its subclasses are crucial for representing and storing different transactions that include:
card creation/delete, account creation/delete etc.

#### Commerciant Class

The **Commerciant** class is used for statistics belonging to one user/account, used in spendingsReportCommand.
A commerciant is characterised by a **name** and by a **value** representing how much money one user spent at the commerciant.

#### JSON Method
Almost all classes described above have one **toJSON()** method that helps with representing the data in a readable
format to the output. In many cases the method uses other toJSON() methods  from the classes inside a bigger class to
represent the object (example User class and its toJSON() method). 

#### Design patterns

###### Command Design Pattern
The **Command Design Pattern** is employed to decouple the requests for banking operations from the object that executes
the operations. The **Command classes** are responsible for invoking specific methods on the Bank class and its components.

###### Builder Design Pattern
The **Builder Design Pattern** is used in the **Bank** class, where methods like addUsers(), addExchangeRates(),
and addSecondaryExchangeRates() provide a interface to add multiple users or exchange rates to the bank in a structured
and readable manner.

#### OutputConstants Class
The **OutputConstants** class is designed to improve the overall coding style and enhance the **readability** of the application.
Its primary purpose is to centralize output messages, making it easier to **manage** and **modify** them without having to change
them throughout the implementation.

## Potential Improvements
While implementing this project, I realized that the structure could have benefited from a more careful application of
object-oriented design principles. One key improvement would have been the creation of a **skeleton structure** before
diving into the actual implementation, defined by a more strategic use of inheritance and polymorphism to
handle different types of transactions and others. This would help reduce redundancy and
increase flexibility for future changes and expansions. Additionally, I came to the conclusion that making the
**Bank** class a **singleton** would have been a logical decision early on.
