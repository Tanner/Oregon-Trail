export class InputManager {
    constructor(canvas) {
        this.delegate = null;
        this.keysHeld = new Set();
        this.boundHandlers = new Map();
        this.canvas = canvas;
        this.setupListeners();
    }
    setDelegate(delegate) {
        this.delegate = delegate;
    }
    isKeyHeld(code) {
        return this.keysHeld.has(code);
    }
    setupListeners() {
        const mousedownHandler = (e) => {
            if (this.delegate) {
                const rect = this.canvas.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                this.delegate.mousePressed(e.button, x, y);
            }
        };
        const mouseupHandler = (e) => {
            if (this.delegate) {
                const rect = this.canvas.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                this.delegate.mouseReleased(e.button, x, y);
            }
        };
        const mousemoveHandler = (e) => {
            if (this.delegate) {
                const rect = this.canvas.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                this.delegate.mouseMoved(x, y);
            }
        };
        const keydownHandler = (e) => {
            if (!this.keysHeld.has(e.code)) {
                this.keysHeld.add(e.code);
                if (this.delegate) {
                    this.delegate.keyPressed(e.key, e.code);
                }
            }
        };
        const keyupHandler = (e) => {
            this.keysHeld.delete(e.code);
            if (this.delegate) {
                this.delegate.keyReleased(e.key, e.code);
            }
        };
        this.canvas.addEventListener('mousedown', mousedownHandler);
        this.canvas.addEventListener('mouseup', mouseupHandler);
        this.canvas.addEventListener('mousemove', mousemoveHandler);
        window.addEventListener('keydown', keydownHandler);
        window.addEventListener('keyup', keyupHandler);
        this.boundHandlers.set('mousedown', mousedownHandler);
        this.boundHandlers.set('mouseup', mouseupHandler);
        this.boundHandlers.set('mousemove', mousemoveHandler);
        this.boundHandlers.set('keydown', keydownHandler);
        this.boundHandlers.set('keyup', keyupHandler);
    }
    destroy() {
        this.canvas.removeEventListener('mousedown', this.boundHandlers.get('mousedown'));
        this.canvas.removeEventListener('mouseup', this.boundHandlers.get('mouseup'));
        this.canvas.removeEventListener('mousemove', this.boundHandlers.get('mousemove'));
        window.removeEventListener('keydown', this.boundHandlers.get('keydown'));
        window.removeEventListener('keyup', this.boundHandlers.get('keyup'));
        this.boundHandlers.clear();
    }
}
//# sourceMappingURL=InputManager.js.map