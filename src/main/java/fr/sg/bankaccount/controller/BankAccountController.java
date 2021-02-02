package fr.sg.bankaccount.controller;


import fr.sg.bankaccount.Exception.BalanceException;
import fr.sg.bankaccount.Exception.NotExistAccountException;
import fr.sg.bankaccount.model.Account;
import fr.sg.bankaccount.model.Operation;
import fr.sg.bankaccount.model.OperationType;
import fr.sg.bankaccount.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/accounts")
public class BankAccountController {

    @Autowired
    AccountServiceImpl accountService;

    @PostMapping(path = "/{accountId}/balance/update")
    public Optional<Account> updateAccountWithOperation(@PathVariable Long accountId,
                                                        @RequestParam(name = "amount") Long amount,
                                                        @RequestParam(name = "type") OperationType operationType) throws BalanceException, NotExistAccountException {
        return accountService.updateBalance(accountId, amount, operationType);
    }

    @GetMapping(path = "/{accountId}/operations")
    public Optional<List<Operation>> getAllOperation(@PathVariable Long accountId) throws NotExistAccountException {
        return accountService.getAllOperationsByAccountId(accountId);
    }


}
