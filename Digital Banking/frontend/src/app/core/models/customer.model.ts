/**
 * Customer interface representing a bank customer
 * Maps to CustomerDTO from the backend
 */
export interface Customer {
  /** Unique identifier for the customer */
  id: number;
  /** Full name of the customer */
  name: string;
  /** Email address of the customer */
  email: string;
}

/**
 * Data Transfer Object for customer creation and updates
 */
export interface CustomerDTO {
  /** Optional ID for updates, null for creation */
  id?: number;
  /** Full name of the customer */
  name: string;
  /** Email address of the customer */
  email: string;
}

/**
 * Customer form interface for Angular reactive forms
 */
export interface CustomerForm {
  name: string;
  email: string;
}

/**
 * Customer filter interface for search functionality
 */
export interface CustomerFilter {
  /** Search term for name or email */
  searchTerm?: string;
  /** Filter by specific email domain */
  emailDomain?: string;
}
