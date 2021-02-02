package fr.sg.bankaccount.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorInfo {
    private String errorMessage;
}
