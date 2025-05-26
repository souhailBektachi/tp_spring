/**
 * Operation type enumeration
 * Maps to OperationType enum from backend
 */
export enum OperationType {
  DEBIT = 'DEBIT',
  CREDIT = 'CREDIT'
}

/**
 * Account operation interface
 * Maps to AccountOperationDTO from backend
 */
export interface AccountOperation {
  /** Unique operation identifier */
  id: number;
  /** Date and time of the operation */
  operationDate: Date;
  /** Operation amount */
  amount: number;
  /** Type of operation (debit or credit) */
  type: OperationType;
  /** Description of the operation */
  description: string;
}

/**
 * Debit operation DTO for API calls
 * Maps to DebitDTO from backend controller
 */
export interface DebitDTO {
  /** Account ID to debit from */
  accountId: string;
  /** Amount to debit */
  amount: number;
  /** Description of the debit operation */
  description: string;
}

/**
 * Credit operation DTO for API calls
 * Maps to CreditDTO from backend controller
 */
export interface CreditDTO {
  /** Account ID to credit to */
  accountId: string;
  /** Amount to credit */
  amount: number;
  /** Description of the credit operation */
  description: string;
}

/**
 * Transfer operation DTO for API calls
 * Maps to TransferDTO from backend controller
 */
export interface TransferDTO {
  /** Source account ID */
  accountSource: string;
  /** Destination account ID */
  accountDestination: string;
  /** Amount to transfer */
  amount: number;
}

/**
 * Operation filter interface for transaction history
 */
export interface OperationFilter {
  /** Filter by operation type */
  type?: OperationType;
  /** Start date for date range filter */
  startDate?: Date;
  /** End date for date range filter */
  endDate?: Date;
  /** Minimum amount filter */
  minAmount?: number;
  /** Maximum amount filter */
  maxAmount?: number;
  /** Search term for description */
  description?: string;
}

/**
 * Pagination interface for operation history
 */
export interface OperationPagination {
  /** Current page number (0-based) */
  page: number;
  /** Number of items per page */
  size: number;
  /** Sort field */
  sortBy?: string;
  /** Sort direction */
  sortDirection?: 'asc' | 'desc';
}
