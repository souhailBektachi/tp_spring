/**
 * Authentication request interface for login
 */
export interface LoginRequest {
  /** User email address */
  email: string;
  /** User password */
  password: string;
  /** Remember me flag for extended session */
  rememberMe?: boolean;
}

/**
 * Authentication response interface from login
 */
export interface LoginResponse {
  /** JWT access token */
  accessToken: string;
  /** Token type (usually 'Bearer') */
  tokenType: string;
  /** Token expiration time in seconds */
  expiresIn: number;
  /** User information */
  user: User;
}

/**
 * User interface representing authenticated user
 */
export interface User {
  /** Unique user identifier */
  id: number;
  /** User's email address */
  email: string;
  /** User's first name */
  firstName: string;
  /** User's last name */
  lastName: string;
  /** User roles */
  roles: Role[];
  /** Account status flags */
  enabled: boolean;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
}

/**
 * Role interface for user permissions
 */
export interface Role {
  /** Role identifier */
  id: number;
  /** Role name */
  name: string;
  /** Role description */
  description?: string;
}

/**
 * JWT token payload interface
 */
export interface JwtPayload {
  /** Subject (user ID) */
  sub: string;
  /** Email */
  email: string;
  /** Issued at timestamp */
  iat: number;
  /** Expiration timestamp */
  exp: number;
  /** Roles array */
  roles: string[];
}

/**
 * Password reset request interface
 */
export interface PasswordResetRequest {
  /** User email address */
  email: string;
}

/**
 * Change password interface
 */
export interface ChangePasswordRequest {
  /** Current password */
  currentPassword: string;
  /** New password */
  newPassword: string;
  /** Confirm new password */
  confirmPassword: string;
}
