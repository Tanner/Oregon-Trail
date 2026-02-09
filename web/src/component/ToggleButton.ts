import { Button } from './Button';
import { Label } from './Label';

export class ToggleButton extends Button {
  private disableAutoToggle: boolean = false;

  constructor(width: number, height: number, label: Label) {
    super(width, height, label);
  }

  override mousePressed(button: number, _mx: number, _my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    if (button === 0 && this.isMouseOver() && !this.isDisabled()) {
      if (!this.disableAutoToggle) {
        this.setActive(!this.isActive());
      } else {
        this.setActive(true);
      }
    }
  }

  override mouseReleased(button: number, _mx: number, _my: number): void {
    if (!this.isVisible()) {
      return;
    }

    if (button === 0 && this.isMouseOver() && !this.isDisabled() && this.isActive()) {
      if (this.disableAutoToggle) {
        this.setActive(false);
      }

      this.notifyListeners();
    }
  }

  setDisableAutoToggle(disableAutoToggle: boolean): void {
    this.disableAutoToggle = disableAutoToggle;
  }

  getDisableAutoToggle(): boolean {
    return this.disableAutoToggle;
  }
}
