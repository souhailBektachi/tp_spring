package com.glsid.digital_banking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    
    @ManyToOne
    private Customer customer;
    
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;
}
