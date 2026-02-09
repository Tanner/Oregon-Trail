export class Notification {
    constructor(message, isModal = false) {
        this.message = message;
        this.isModal = isModal;
    }
    getMessage() {
        return this.message;
    }
    getIsModal() {
        return this.isModal;
    }
}
//# sourceMappingURL=Notification.js.map