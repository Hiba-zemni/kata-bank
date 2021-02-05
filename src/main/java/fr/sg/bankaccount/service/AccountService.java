package fr.sg.bankaccount.service;

import fr.sg.bankaccount.Exception.BalanceException;
import fr.sg.bankaccount.Exception.NotExistAccountException;
import fr.sg.bankaccount.model.Account;
import fr.sg.bankaccount.model.Operation;
import fr.sg.bankaccount.model.OperationType;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> updateBalance(Long accountId, Long amount, OperationType type) throws NotExistAccountException, BalanceException;

    Optional<List<Operation>> getAllOperationsByAccountId(Long accountId) throws NotExistAccountException;

    void printStatment(Account account);
}
