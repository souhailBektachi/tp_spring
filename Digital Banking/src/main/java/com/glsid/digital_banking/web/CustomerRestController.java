package com.glsid.digital_banking.web;

import com.glsid.digital_banking.dtos.CustomerDTO;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/customers")
@Tag(name = "Customer Management")
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Returns a customer based on ID")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping
    @Operation(summary = "Create new customer", description = "Creates a new customer and returns its data")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer details")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by ID")
    public void deleteCustomer(@PathVariable Long id) {
        bankAccountService.deleteCustomer(id);
    }
}
