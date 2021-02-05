package fr.sg.bankaccount.model;

import fr.sg.bankaccount.Exception.BalanceException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

import static fr.sg.bankaccount.model.OperationType.WITHDRAWAL;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
@Table(name = "T_Account")
public class Account {

    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long Id;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "balance")
    private Long balance;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Operation> operationList;

    public void updateBalanceWithOperation(Long amount, OperationType type) throws BalanceException {
        if (!isAuthorised(amount, type)) {
            throw new BalanceException("Insufficient balance for this operation");
        }
        switch (type) {
            case DEPOSIT:
                LOGGER.info("DEPOSIT");
                setBalance(balance + amount);
                break;
            case WITHDRAWAL:
                LOGGER.info("WITHDRAWAL");
                setBalance(balance - amount);
                break;
        }
    }

    private boolean isAuthorised(Long amount, OperationType operationType) {
        if (amount > balance && operationType == WITHDRAWAL) {
            return false;
        }
        return true;
    }


}
