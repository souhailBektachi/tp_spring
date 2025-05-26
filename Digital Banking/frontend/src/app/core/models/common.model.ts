/**
 * API response wrapper interface
 */
export interface ApiResponse<T = any> {
  /** Response data */
  data: T;
  /** Success status */
  success: boolean;
  /** Response message */
  message?: string;
  /** Error details if any */
  errors?: string[];
  /** Response timestamp */
  timestamp: Date;
}

/**
 * Paginated response interface
 */
export interface PagedResponse<T = any> {
  /** Array of data items */
  content: T[];
  /** Current page number (0-based) */
  page: number;
  /** Number of items per page */
  size: number;
  /** Total number of elements */
  totalElements: number;
  /** Total number of pages */
  totalPages: number;
  /** Whether this is the first page */
  first: boolean;
  /** Whether this is the last page */
  last: boolean;
  /** Number of elements in current page */
  numberOfElements: number;
}

/**
 * Error response interface
 */
export interface ErrorResponse {
  /** HTTP status code */
  status: number;
  /** Error code */
  error: string;
  /** Error message */
  message: string;
  /** Request path */
  path: string;
  /** Error timestamp */
  timestamp: Date;
  /** Validation errors */
  validationErrors?: ValidationError[];
}

/**
 * Validation error interface
 */
export interface ValidationError {
  /** Field name that has error */
  field: string;
  /** Error message */
  message: string;
  /** Rejected value */
  rejectedValue?: any;
}

/**
 * Dashboard statistics interface
 */
export interface DashboardStats {
  /** Total number of customers */
  totalCustomers: number;
  /** Total number of accounts */
  totalAccounts: number;
  /** Total balance across all accounts */
  totalBalance: number;
  /** Number of operations today */
  todayOperations: number;
  /** Recent operations */
  recentOperations: AccountOperation[];
  /** Account type distribution */
  accountTypeDistribution: AccountTypeStats[];
}

/**
 * Account type statistics
 */
export interface AccountTypeStats {
  /** Account type name */
  type: string;
  /** Number of accounts of this type */
  count: number;
  /** Total balance for this account type */
  totalBalance: number;
}

/**
 * Chart data interface for dashboard charts
 */
export interface ChartData {
  /** Chart labels */
  labels: string[];
  /** Chart datasets */
  datasets: ChartDataset[];
}

/**
 * Chart dataset interface
 */
export interface ChartDataset {
  /** Dataset label */
  label: string;
  /** Data points */
  data: number[];
  /** Background colors */
  backgroundColor?: string[];
  /** Border colors */
  borderColor?: string[];
  /** Border width */
  borderWidth?: number;
}

/**
 * Loading state interface
 */
export interface LoadingState {
  /** Whether data is currently loading */
  loading: boolean;
  /** Loading message */
  message?: string;
}

import { AccountOperation } from './operation.model';
