package fr.sg.bankaccount.service;

import fr.sg.bankaccount.Exception.BalanceException;
import fr.sg.bankaccount.Exception.NotExistAccountException;
import fr.sg.bankaccount.model.Account;
import fr.sg.bankaccount.model.Operation;
import fr.sg.bankaccount.model.OperationType;
import fr.sg.bankaccount.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> updateBalance(Long accountId, Long amount, OperationType type) throws NotExistAccountException, BalanceException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotExistAccountException("Please provide a valid accountId"));
        account.updateBalanceWithOperation(amount, type);
        return Optional.of(accountRepository.save(account));
    }

    public Optional<List<Operation>> getAllOperationsByAccountId(Long accountId) throws NotExistAccountException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotExistAccountException("Please provide a valid accountId"));
        return Optional.of(account.getOperationList());
    }

}
