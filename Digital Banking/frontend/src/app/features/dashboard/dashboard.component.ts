import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardStats, AccountOperation } from '../../core/models';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatDividerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats?: DashboardStats;
  isLoading = true;
  displayedColumns = ['date', 'type', 'amount', 'account', 'description'];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.isLoading = false;
        setTimeout(() => this.renderCharts(), 0);
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  renderCharts() {
    if (!this.stats) return;
    // Account type distribution pie chart
    new Chart('accountTypeChart', {
      type: 'pie',
      data: {
        labels: this.stats.accountTypeDistribution.map(t => t.type),
        datasets: [{
          data: this.stats.accountTypeDistribution.map(t => t.count),
          backgroundColor: ['#1976d2', '#4caf50', '#ff9800']
        }]
      },
      options: { plugins: { legend: { position: 'bottom' } } }
    });
    // Transaction history line chart
    new Chart('transactionHistoryChart', {
      type: 'line',
      data: {
        labels: this.stats.recentOperations.map(op => op.date),
        datasets: [{
          label: 'Amount',
          data: this.stats.recentOperations.map(op => op.amount),
          borderColor: '#1976d2',
          backgroundColor: 'rgba(25,118,210,0.1)',
          fill: true,
          tension: 0.4
        }]
      },
      options: { plugins: { legend: { display: false } } }
    });
  }

  quickAction(action: string) {
    // Implement navigation or dialog for quick actions
  }
}
