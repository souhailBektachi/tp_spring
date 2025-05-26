import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { CustomerService } from '../../core/services/customer.service';
import { Customer } from '../../core/models';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  displayedColumns = ['id', 'name', 'email', 'phone', 'actions'];
  search = '';
  pageIndex = 0;
  pageSize = 10;
  total = 0;
  sort: Sort = {active: 'name', direction: 'asc'};

  constructor(
    private customerService: CustomerService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getCustomers({
      page: this.pageIndex,
      size: this.pageSize,
      sort: this.sort.active + ',' + this.sort.direction,
      search: this.search
    }).subscribe(res => {
      this.customers = res.items;
      this.total = res.total;
    });
  }

  onSearchChange() {
    this.pageIndex = 0;
    this.loadCustomers();
  }

  onPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCustomers();
  }

  onSort(event: Sort) {
    this.sort = event;
    this.loadCustomers();
  }

  viewCustomer(customer: Customer) {
    this.router.navigate(['/customers', customer.id]);
  }

  editCustomer(customer: Customer) {
    this.router.navigate(['/customers', customer.id, 'edit']);
  }

  deleteCustomer(customer: Customer) {
    // Open confirmation dialog, then call service if confirmed
    // ...implementation...
  }

  addCustomer() {
    this.router.navigate(['/customers/new']);
  }
}
