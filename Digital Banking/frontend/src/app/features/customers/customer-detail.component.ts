import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CustomerService } from '../../core/services/customer.service';
import { Customer, Account } from '../../core/models';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.css']
})
export class CustomerDetailComponent implements OnInit {
  customer?: Customer;
  accounts: Account[] = [];

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.customerService.getCustomerById(id).subscribe(c => {
        this.customer = c;
        this.accounts = c.accounts || [];
      });
    }
  }

  editCustomer() {
    if (this.customer) {
      this.router.navigate(['/customers', this.customer.id, 'edit']);
    }
  }

  deleteCustomer() {
    // Open confirmation dialog, then call service if confirmed
    // ...implementation...
  }
}
