import { Customer } from './customer.model';

/**
 * Account status enumeration
 * Maps to AccountStatus enum from backend
 */
export enum AccountStatus {
  CREATED = 'CREATED',
  ACTIVATED = 'ACTIVATED',
  SUSPENDED = 'SUSPENDED',
  BLOCKED = 'BLOCKED'
}

/**
 * Base bank account interface
 * Maps to BankAccountDTO from backend
 */
export interface BankAccount {
  /** Unique account identifier */
  id: string;
  /** Current account balance */
  balance: number;
  /** Account creation date */
  createdAt: Date;
  /** Current account status */
  status: AccountStatus;
  /** Account owner information */
  customer: Customer;
  /** Account type discriminator */
  type: string;
}

/**
 * Current account interface extending base account
 * Maps to CurrentAccountDTO from backend
 */
export interface CurrentAccount extends BankAccount {
  type: 'CurrentAccount';
  /** Overdraft limit allowed for this account */
  overDraft: number;
}

/**
 * Savings account interface extending base account
 * Maps to SavingAccountDTO from backend
 */
export interface SavingsAccount extends BankAccount {
  type: 'SavingAccount';
  /** Interest rate applied to this savings account */
  interestRate: number;
}

/**
 * Union type for all account types
 */
export type Account = CurrentAccount | SavingsAccount;

/**
 * Data Transfer Object for account creation
 */
export interface CreateAccountDTO {
  /** Initial balance for the account */
  initialBalance: number;
  /** Customer ID who will own the account */
  customerId: number;
  /** Overdraft limit (for current accounts) */
  overDraft?: number;
  /** Interest rate (for savings accounts) */
  interestRate?: number;
}

/**
 * Account filter interface for search and filtering
 */
export interface AccountFilter {
  /** Filter by account type */
  accountType?: 'CurrentAccount' | 'SavingAccount';
  /** Filter by account status */
  status?: AccountStatus;
  /** Filter by customer ID */
  customerId?: number;
  /** Minimum balance filter */
  minBalance?: number;
  /** Maximum balance filter */
  maxBalance?: number;
}
