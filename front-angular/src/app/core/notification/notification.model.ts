export interface Notification {
    id: string;
    from: string;
    to: string;
    payload: string;
    seen: boolean;
}

export interface SocketResponse {
  type: 'SUCCESS' | 'ERROR',
  message: any;
}
