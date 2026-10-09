import { HttpErrorResponse } from '@angular/common/http';

export function getApiError(error: unknown, fallback: string): string {
  if (error instanceof HttpErrorResponse && typeof error.error === 'object' && error.error !== null) {
    const body = error.error as Record<string, unknown>;
    const message = body['message'] ?? Object.values(body).find((value) => typeof value === 'string');
    if (typeof message === 'string') return message;
  }
  return fallback;
}