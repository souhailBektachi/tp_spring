package com.glsid.digital_banking.dtos;

import com.glsid.digital_banking.entities.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
