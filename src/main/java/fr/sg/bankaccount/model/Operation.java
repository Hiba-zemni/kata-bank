package fr.sg.bankaccount.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
@Table(name = "T_Operation")
public class Operation {

    public Long getOperationID() {
        return operationID;
    }

    @Id
    @GeneratedValue
    @Column(name = "operation_id")
    private Long operationID;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();

}
