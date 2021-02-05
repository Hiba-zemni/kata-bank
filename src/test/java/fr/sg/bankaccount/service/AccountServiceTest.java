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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    private PrintStream printer = mock(PrintStream.class);

    private static Long DEPOSIT_AMOUNT = 300L;
    private static Long WITHDRAWAL_AMOUNT = 200L;
    private static Long WITHDRAWAL_AMOUNT_NOT_PERMITED = 400L;

    private Account account, depositAccount, withdrawalAccount;
    private Operation depositOperation, withdrawableOperation;
    private List<Operation> operationList = new ArrayList<Operation>();
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        depositOperation = new Operation(100L, OperationType.DEPOSIT, DEPOSIT_AMOUNT, LocalDateTime.parse("2021-02-01T00:00:00"));
        withdrawableOperation = new Operation(101L, OperationType.WITHDRAWAL, WITHDRAWAL_AMOUNT, LocalDateTime.parse("2021-02-01T00:00:00"));
        operationList.add(depositOperation);
        operationList.add(withdrawableOperation);
        account = new Account(1L, 80009L, 500l, operationList);
        depositAccount = new Account(1L, 80009L, 800l, null);
        withdrawalAccount = new Account(1L, 80009L, 300l, null);
        System.setOut(new PrintStream(out));

    }

    @Test(expected = NotExistAccountException.class)
    public void should_throw_exception_when_when_account_is_not_found() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.empty());
        accountService.updateBalance(account.getId(), DEPOSIT_AMOUNT, OperationType.DEPOSIT);
    }

    @Test(expected = BalanceException.class)
    public void should_throw_exception_when_when_balance_is_insufficient() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.ofNullable(withdrawalAccount));
        accountService.updateBalance(account.getId(), WITHDRAWAL_AMOUNT_NOT_PERMITED, OperationType.WITHDRAWAL);
    }

    @Test
    public void should_update_account_when_operation_is_deposit() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.ofNullable(account));
        Mockito.when(accountRepository.save(account)).thenReturn(depositAccount);
        Optional<Account> updatedAccount = accountService.updateBalance(account.getId(), DEPOSIT_AMOUNT, OperationType.DEPOSIT);
        assertNotNull(updatedAccount);
        Assert.assertEquals(800L, updatedAccount.get().getBalance().longValue());
    }

    @Test
    public void should_update_account_when_operation_is_withdrawal() throws BalanceException, NotExistAccountException {
        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.ofNullable(account));
        Mockito.when(accountRepository.save(account)).thenReturn(withdrawalAccount);
        Optional<Account> updatedAccount = accountService.updateBalance(account.getId(), WITHDRAWAL_AMOUNT, OperationType.WITHDRAWAL);
        assertNotNull(updatedAccount);
        Assert.assertEquals(300L, updatedAccount.get().getBalance().longValue());
    }

    @Test
    public void should_return_operations_by_account() throws NotExistAccountException {
        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.ofNullable(account));
        Optional<List<Operation>> listOperation = accountService.getAllOperationsByAccountId(account.getId());
        assertNotNull(listOperation);
        Assert.assertEquals(2, listOperation.get().size());
        Assert.assertArrayEquals(operationList.stream().toArray(), listOperation.get().stream().toArray());
    }

    @Test
    public void should_print_statment() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" date               | amount   | type").append("\r\n");
        stringBuilder.append("2021-02-01T00:00    | 300      | DEPOSIT").append("\r\n");
        stringBuilder.append("2021-02-01T00:00    | 200      | WITHDRAWAL").append("\r\n");
        accountService.printStatment(account);
        assertEquals(stringBuilder.toString(), out.toString());

    }
}
