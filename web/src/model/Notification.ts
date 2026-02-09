export class Notification {
  private message: string;
  private isModal: boolean;

  constructor(message: string, isModal: boolean = false) {
    this.message = message;
    this.isModal = isModal;
  }

  getMessage(): string {
    return this.message;
  }

  getIsModal(): boolean {
    return this.isModal;
  }
}
