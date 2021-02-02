package fr.sg.bankaccount.service;


import fr.sg.bankaccount.Exception.BalanceException;
import fr.sg.bankaccount.Exception.NotExistAccountException;
import fr.sg.bankaccount.model.Account;
import fr.sg.bankaccount.model.Operation;
import fr.sg.bankaccount.model.OperationType;
import fr.sg.bankaccount.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    private static Long DEPOSIT_AMOUNT = 300L;
    private static Long WITHDRAWAL_AMOUNT = 200L;
    private static Long WITHDRAWAL_AMOUNT_NOT_PERMITED = 400L;

    private Account account, depositAccount, withdrawalAccount;
    private Operation depositOperation, withdrawableOperation;
    private List<Operation> operationList = new ArrayList<Operation>();

    @Before
    public void setUp() {
        depositOperation = new Operation(100L, OperationType.DEPOSIT, DEPOSIT_AMOUNT, LocalDateTime.parse("2021-02-01T00:00:00"));
        withdrawableOperation = new Operation(101L, OperationType.WITHDRAWAL, WITHDRAWAL_AMOUNT, LocalDateTime.parse("2021-02-01T00:00:00"));
        operationList.add(depositOperation);
        operationList.add(withdrawableOperation);
        account = new Account(1L, 80009L, 500l, operationList);
        depositAccount = new Account(1L, 80009L, 800l, null);
        withdrawalAccount = new Account(1L, 80009L, 300l, null);
    }

    @Test(expected = NotExistAccountException.class)
    public void should_throw_exception_when_when_account_is_not_found() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        accountService.updateBalance(account.getAccountId(), DEPOSIT_AMOUNT, OperationType.DEPOSIT);
    }

    @Test(expected = BalanceException.class)
    public void should_throw_exception_when_when_balance_is_insufficient() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(withdrawalAccount));
        accountService.updateBalance(account.getAccountId(), WITHDRAWAL_AMOUNT_NOT_PERMITED, OperationType.WITHDRAWAL);
    }

    @Test
    public void should_update_account_when_operation_is_deposit() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(account));
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(depositAccount);
        Optional<Account> newAccount = accountService.updateBalance(account.getAccountId(), DEPOSIT_AMOUNT, OperationType.DEPOSIT);
        assertNotNull(newAccount);
        Assert.assertEquals(800L, newAccount.get().getBalance().longValue());
    }

    @Test
    public void should_update_account_when_operation_is_withdrawal() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(account));
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(withdrawalAccount);
        Optional<Account> newAccount = accountService.updateBalance(account.getAccountId(), WITHDRAWAL_AMOUNT, OperationType.WITHDRAWAL);
        assertNotNull(newAccount);
        Assert.assertEquals(300L, newAccount.get().getBalance().longValue());
    }

    @Test
    public void should_return_operations_by_account() throws NotExistAccountException {
        Mockito.when(accountRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(account));
        Optional<List<Operation>> operationList = accountService.getAllOperationsByAccountId(account.getAccountId());
        assertNotNull(operationList);
        Assert.assertEquals(2, operationList.get().size());
        Assert.assertEquals(Optional.of(100L), Optional.ofNullable(operationList.get().get(0).getOperationID()));
        Assert.assertEquals(Optional.of(101L), Optional.ofNullable(operationList.get().get(1).getOperationID()));
        Assert.assertEquals(LocalDateTime.parse("2021-02-01T00:00:00"), operationList.get().get(0).getDate());
        Assert.assertEquals(300L, operationList.get().get(0).getAmount().longValue());
        Assert.assertEquals(OperationType.DEPOSIT, operationList.get().get(0).getType());
    }

}
