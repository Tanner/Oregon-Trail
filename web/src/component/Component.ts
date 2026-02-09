import { Color } from '../core/Color';

export enum DebugMode {
  NONE = 'NONE',
  VISIBLE = 'VISIBLE',
  ALL = 'ALL'
}

export enum BevelType {
  NONE = 'NONE',
  IN = 'IN',
  OUT = 'OUT'
}

export enum ReferencePoint {
  TOPLEFT = 'TOPLEFT',
  TOPCENTER = 'TOPCENTER',
  TOPRIGHT = 'TOPRIGHT',
  CENTERLEFT = 'CENTERLEFT',
  CENTERCENTER = 'CENTERCENTER',
  CENTERRIGHT = 'CENTERRIGHT',
  BOTTOMLEFT = 'BOTTOMLEFT',
  BOTTOMCENTER = 'BOTTOMCENTER',
  BOTTOMRIGHT = 'BOTTOMRIGHT'
}

export interface Visible {
  isVisible(): boolean;
  setVisible(visible: boolean): void;
}

export interface Vector2f {
  x: number;
  y: number;
}

export abstract class Component implements Visible {
  private static debugMode: DebugMode = DebugMode.NONE;

  private visibleParent: Visible | null = null;
  private origin: Vector2f;
  private translation: Vector2f;
  private width: number;
  private height: number;

  private backgroundColor: Color | null = null;
  private beveled: BevelType = BevelType.NONE;
  private bevelWidth: number = 0;

  private borderColor: Color | null = null;
  private topBorderWidth: number = 0;
  private rightBorderWidth: number = 0;
  private bottomBorderWidth: number = 0;
  private leftBorderWidth: number = 0;

  protected components: Component[] = [];
  private mouseOver: boolean = false;
  private visible: boolean = true;

  private tooltipEnabled: boolean = false;
  private tooltipMessage: string = '';
  private shouldUpdateComponents: boolean = false;
  private acceptingInput: boolean = true;
  private hasFocusFlag: boolean = false;
  protected componentListeners: Array<() => void> = [];

  constructor(width: number, height: number) {
    this.origin = { x: 0, y: 0 };
    this.translation = { x: 0, y: 0 };
    this.width = width;
    this.height = height;
  }

  render(ctx: CanvasRenderingContext2D): void {
    ctx.save();
    ctx.beginPath();
    ctx.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    ctx.clip();

    if (this.backgroundColor !== null) {
      if (this.beveled === BevelType.NONE) {
        ctx.fillStyle = this.backgroundColor.toCSS();
        ctx.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
      } else {
        const brightColor = this.backgroundColor.brighter(0.2);
        const darkColor = this.backgroundColor.darker(0.2);

        ctx.fillStyle = this.backgroundColor.toCSS();
        ctx.fillRect(
          this.getX() + this.bevelWidth,
          this.getY() + this.bevelWidth,
          this.getWidth() - this.bevelWidth * 2,
          this.getHeight() - this.bevelWidth * 2
        );

        if (this.beveled === BevelType.OUT) {
          ctx.fillStyle = brightColor.toCSS();
        } else if (this.beveled === BevelType.IN) {
          ctx.fillStyle = darkColor.toCSS();
        }
        ctx.fillRect(
          this.getX() + this.leftBorderWidth,
          this.getY() + this.topBorderWidth,
          this.getWidth() - this.leftBorderWidth - this.rightBorderWidth,
          this.bevelWidth
        );

        if (this.beveled === BevelType.OUT) {
          ctx.fillStyle = darkColor.toCSS();
        } else if (this.beveled === BevelType.IN) {
          ctx.fillStyle = brightColor.toCSS();
        }
        ctx.fillRect(
          this.getX() + this.leftBorderWidth,
          this.getY() + this.getHeight() - this.bevelWidth - this.bottomBorderWidth,
          this.getWidth() - this.leftBorderWidth - this.rightBorderWidth,
          this.bevelWidth
        );

        if (this.beveled === BevelType.OUT) {
          ctx.fillStyle = brightColor.toCSS();
        } else if (this.beveled === BevelType.IN) {
          ctx.fillStyle = darkColor.toCSS();
        }
        ctx.fillRect(
          this.getX() + this.leftBorderWidth,
          this.getY() + this.topBorderWidth,
          this.bevelWidth,
          this.getHeight() - this.topBorderWidth - this.bottomBorderWidth
        );

        if (this.beveled === BevelType.OUT) {
          ctx.fillStyle = darkColor.toCSS();
        } else if (this.beveled === BevelType.IN) {
          ctx.fillStyle = brightColor.toCSS();
        }
        ctx.fillRect(
          this.getX() + this.getWidth() - this.bevelWidth - this.rightBorderWidth,
          this.getY() + this.topBorderWidth,
          this.bevelWidth,
          this.getHeight() - this.topBorderWidth - this.bottomBorderWidth
        );
      }
    }

    for (const component of this.components) {
      component.render(ctx);

      if (
        Component.debugMode === DebugMode.ALL ||
        (Component.debugMode === DebugMode.VISIBLE && component.isVisible())
      ) {
        if (component.isVisible()) {
          ctx.strokeStyle = Color.red.toCSS();
        } else {
          ctx.strokeStyle = Color.yellow.toCSS();
        }
        ctx.strokeRect(
          component.getX(),
          component.getY(),
          component.getWidth() - 1,
          component.getHeight() - 1
        );
      }
    }

    if (this.borderColor !== null) {
      ctx.fillStyle = this.borderColor.toCSS();

      ctx.fillRect(this.getX(), this.getY(), this.getWidth(), this.topBorderWidth);

      ctx.fillRect(
        this.getX() + this.getWidth() - this.rightBorderWidth,
        this.getY(),
        this.rightBorderWidth,
        this.getHeight()
      );

      ctx.fillRect(
        this.getX(),
        this.getY() + this.getHeight() - this.bottomBorderWidth,
        this.getWidth(),
        this.bottomBorderWidth
      );

      ctx.fillRect(this.getX(), this.getY(), this.leftBorderWidth, this.getHeight());
    }

    ctx.restore();
  }

  update(delta: number): void {
    if (this.getShouldUpdateComponents()) {
      for (const c of this.components) {
        c.update(delta);
      }
    }
  }

  setShouldUpdateComponents(shouldUpdate: boolean): void {
    this.shouldUpdateComponents = shouldUpdate;
  }

  private getShouldUpdateComponents(): boolean {
    return this.shouldUpdateComponents;
  }

  setVisibleParent(visible: Visible | null): void {
    this.visibleParent = visible;
  }

  isVisible(): boolean {
    if (this.visibleParent !== null) {
      return this.visible && this.visibleParent.isVisible();
    }
    return this.visible;
  }

  setVisible(visible: boolean): void {
    this.visible = visible;
  }

  add(
    component: Component,
    location: Vector2f,
    referencePoint: ReferencePoint,
    xOffset: number = 0,
    yOffset: number = 0
  ): void {
    component.setPosition(location, referencePoint, xOffset, yOffset);
    this.components.push(component);
    component.setVisibleParent(this);
  }

  addAsColumn(
    components: Component[],
    location: Vector2f,
    xOffset: number,
    yOffset: number,
    spacing: number
  ): void {
    let currentY = location.y;
    for (const c of components) {
      this.add(c, { x: location.x, y: currentY }, ReferencePoint.TOPLEFT, xOffset, yOffset);
      currentY += c.getHeight() + spacing;
    }
  }

  addAsRow(
    components: Component[],
    location: Vector2f,
    xOffset: number,
    yOffset: number,
    spacing: number
  ): void {
    let currentX = location.x;
    for (const c of components) {
      if (c !== null) {
        this.add(c, { x: currentX, y: location.y }, ReferencePoint.TOPLEFT, xOffset, yOffset);
        currentX += c.getWidth() + spacing;
      }
    }
  }

  addAsGrid(
    components: Component[],
    location: Vector2f,
    _rows: number,
    cols: number,
    xOffset: number,
    yOffset: number,
    xSpacing: number,
    ySpacing: number
  ): void {
    let startIndex = 0;
    let currentY = location.y;

    while (startIndex < components.length) {
      const row: Component[] = [];
      for (let x = 0; x < cols && startIndex + x < components.length; x++) {
        row.push(components[startIndex + x]);
      }

      this.addAsRow(row, { x: location.x, y: currentY }, xOffset, yOffset, xSpacing);

      currentY += components[startIndex].getHeight() + ySpacing;
      startIndex += cols;
    }
  }

  addAsGrid2D(components: Component[][], xSpacing: number, ySpacing: number, location: Vector2f): void {
    let currentY = location.y;
    for (const row of components) {
      this.addAsRow(row, { x: location.x, y: currentY }, 0, 0, xSpacing);
      if (row.length > 0) {
        currentY += row[0].getHeight() + ySpacing;
      }
    }
  }

  remove(component: Component): void {
    component.setVisibleParent(null);
    const index = this.components.indexOf(component);
    if (index > -1) {
      this.components.splice(index, 1);
    }
  }

  setPosition(location: Vector2f, referencePoint: ReferencePoint, xOffset: number = 0, yOffset: number = 0): void {
    const offsetLocation: Vector2f = { x: location.x + xOffset, y: location.y + yOffset };
    const x = offsetLocation.x;
    const y = offsetLocation.y;

    switch (referencePoint) {
      case ReferencePoint.TOPLEFT:
        this.setLocation(x, y);
        break;
      case ReferencePoint.TOPCENTER:
        this.setLocation(x - this.getWidth() / 2, y);
        break;
      case ReferencePoint.TOPRIGHT:
        this.setLocation(x - this.getWidth(), y);
        break;
      case ReferencePoint.CENTERLEFT:
        this.setLocation(x, y - this.getHeight() / 2);
        break;
      case ReferencePoint.CENTERCENTER:
        this.setLocation(x - this.getWidth() / 2, y - this.getHeight() / 2);
        break;
      case ReferencePoint.CENTERRIGHT:
        this.setLocation(x - this.getWidth(), y - this.getHeight() / 2);
        break;
      case ReferencePoint.BOTTOMLEFT:
        this.setLocation(x, y - this.getHeight());
        break;
      case ReferencePoint.BOTTOMCENTER:
        this.setLocation(x - this.getWidth() / 2, y - this.getHeight());
        break;
      case ReferencePoint.BOTTOMRIGHT:
        this.setLocation(x - this.getWidth(), y - this.getHeight());
        break;
    }
  }

  getPosition(referencePoint: ReferencePoint): Vector2f {
    const x = this.getX();
    const y = this.getY();

    switch (referencePoint) {
      case ReferencePoint.TOPLEFT:
        return { x, y };
      case ReferencePoint.TOPCENTER:
        return { x: x + this.getWidth() / 2, y };
      case ReferencePoint.TOPRIGHT:
        return { x: x + this.getWidth(), y };
      case ReferencePoint.CENTERLEFT:
        return { x, y: y + this.getHeight() / 2 };
      case ReferencePoint.CENTERCENTER:
        return { x: x + this.getWidth() / 2, y: y + this.getHeight() / 2 };
      case ReferencePoint.CENTERRIGHT:
        return { x: x + this.getWidth(), y: y + this.getHeight() / 2 };
      case ReferencePoint.BOTTOMLEFT:
        return { x, y: y + this.getHeight() };
      case ReferencePoint.BOTTOMCENTER:
        return { x: x + this.getWidth() / 2, y: y + this.getHeight() };
      case ReferencePoint.BOTTOMRIGHT:
        return { x: x + this.getWidth(), y: y + this.getHeight() };
    }
  }

  getBackgroundColor(): Color | null {
    return this.backgroundColor;
  }

  setBackgroundColor(color: Color | null): void {
    this.backgroundColor = color;
  }

  setBevel(bevelType: BevelType): void {
    this.beveled = bevelType;
  }

  setBevelWidth(width: number): void {
    this.bevelWidth = width;
  }

  setBorderWidth(width: number): void {
    this.setTopBorderWidth(width);
    this.setRightBorderWidth(width);
    this.setBottomBorderWidth(width);
    this.setLeftBorderWidth(width);
  }

  setTopBorderWidth(width: number): void {
    this.topBorderWidth = width;
  }

  setRightBorderWidth(width: number): void {
    this.rightBorderWidth = width;
  }

  setBottomBorderWidth(width: number): void {
    this.bottomBorderWidth = width;
  }

  setLeftBorderWidth(width: number): void {
    this.leftBorderWidth = width;
  }

  setBorderColor(color: Color | null): void {
    this.borderColor = color;
  }

  containsPoint(x: number, y: number): boolean {
    return (
      x >= this.getX() &&
      x <= this.getX() + this.getWidth() &&
      y >= this.getY() &&
      y <= this.getY() + this.getHeight()
    );
  }

  getWidth(): number {
    return this.width;
  }

  getHeight(): number {
    return this.height;
  }

  getX(): number {
    return this.origin.x + this.translation.x;
  }

  getY(): number {
    return this.origin.y + this.translation.y;
  }

  setLocation(x: number, y: number): void {
    if (this.components.length > 0) {
      const deltaX = x - this.getX();
      const deltaY = y - this.getY();
      for (const c of this.components) {
        c.setLocation(c.getX() + deltaX, c.getY() + deltaY);
      }
    }
    this.origin.x = x;
    this.origin.y = y;
  }

  setTranslation(x: number, y: number): void {
    if (this.components.length > 0) {
      for (const c of this.components) {
        c.setTranslation(x, y);
      }
    }
    this.translation.x = x;
    this.translation.y = y;
  }

  setAcceptingInput(acceptingInput: boolean): void {
    this.acceptingInput = acceptingInput;

    if (!acceptingInput) {
      this.mouseOver = false;
    }

    for (const c of this.components) {
      c.setAcceptingInput(acceptingInput);
    }
  }

  isAcceptingInput(): boolean {
    return this.acceptingInput;
  }

  getVisibleParent(): Visible | null {
    return this.visibleParent;
  }

  mouseMoved(_oldx: number, _oldy: number, newx: number, newy: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    this.mouseOver = this.containsPoint(newx, newy);

    if (this.isMouseOver() && this.tooltipEnabled) {
      // TODO: Scene.showTooltip(newx, newy, this, tooltipMessage);
    }
  }

  mousePressed(_button: number, mx: number, my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    this.mouseOver = this.containsPoint(mx, my);
  }

  mouseReleased(_button: number, mx: number, my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    this.mouseOver = this.containsPoint(mx, my);
  }

  isMouseOver(): boolean {
    return this.mouseOver;
  }

  static changeDebugMode(): void {
    if (Component.debugMode === DebugMode.NONE) {
      Component.debugMode = DebugMode.VISIBLE;
    } else if (Component.debugMode === DebugMode.VISIBLE) {
      Component.debugMode = DebugMode.ALL;
    } else {
      Component.debugMode = DebugMode.NONE;
    }
  }

  isTooltipEnabled(): boolean {
    return this.tooltipEnabled;
  }

  setTooltipEnabled(tooltipEnabled: boolean): void {
    this.tooltipEnabled = tooltipEnabled;

    if (this.tooltipMessage === '') {
      this.setTooltipMessage('');
    }
  }

  getTooltipMessage(): string {
    return this.tooltipMessage;
  }

  setTooltipMessage(tooltipMessage: string): void {
    this.tooltipMessage = tooltipMessage;
  }

  hasFocus(): boolean {
    return this.hasFocusFlag;
  }

  setFocus(focus: boolean): void {
    this.hasFocusFlag = focus;
  }

  addListener(listener: () => void): void {
    this.componentListeners.push(listener);
  }

  removeListener(listener: () => void): void {
    const index = this.componentListeners.indexOf(listener);
    if (index > -1) {
      this.componentListeners.splice(index, 1);
    }
  }

  protected notifyListeners(): void {
    for (const listener of this.componentListeners) {
      listener();
    }
  }

  keyReleased(_key: string, _char: string): void {
    // Override in subclasses
  }
}
