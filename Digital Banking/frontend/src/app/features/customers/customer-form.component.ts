import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { CustomerService } from '../../core/services/customer.service';
import { Customer } from '../../core/models';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatButtonModule, MatInputModule],
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.css']
})
export class CustomerFormComponent implements OnInit {
  form: FormGroup;
  isEdit = false;
  customerId?: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['']
    });
  }

  ngOnInit() {
    this.customerId = this.route.snapshot.paramMap.get('id') || undefined;
    if (this.customerId) {
      this.isEdit = true;
      this.customerService.getCustomerById(this.customerId).subscribe(c => {
        this.form.patchValue(c);
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    const customer: Customer = this.form.value;
    if (this.isEdit && this.customerId) {
      this.customerService.updateCustomer(this.customerId, customer).subscribe(() => {
        this.router.navigate(['/customers', this.customerId]);
      });
    } else {
      this.customerService.createCustomer(customer).subscribe((created) => {
        this.router.navigate(['/customers', created.id]);
      });
    }
  }
}
