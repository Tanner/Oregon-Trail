import { Color } from '../core/Color';
export var DebugMode;
(function (DebugMode) {
    DebugMode["NONE"] = "NONE";
    DebugMode["VISIBLE"] = "VISIBLE";
    DebugMode["ALL"] = "ALL";
})(DebugMode || (DebugMode = {}));
export var BevelType;
(function (BevelType) {
    BevelType["NONE"] = "NONE";
    BevelType["IN"] = "IN";
    BevelType["OUT"] = "OUT";
})(BevelType || (BevelType = {}));
export var ReferencePoint;
(function (ReferencePoint) {
    ReferencePoint["TOPLEFT"] = "TOPLEFT";
    ReferencePoint["TOPCENTER"] = "TOPCENTER";
    ReferencePoint["TOPRIGHT"] = "TOPRIGHT";
    ReferencePoint["CENTERLEFT"] = "CENTERLEFT";
    ReferencePoint["CENTERCENTER"] = "CENTERCENTER";
    ReferencePoint["CENTERRIGHT"] = "CENTERRIGHT";
    ReferencePoint["BOTTOMLEFT"] = "BOTTOMLEFT";
    ReferencePoint["BOTTOMCENTER"] = "BOTTOMCENTER";
    ReferencePoint["BOTTOMRIGHT"] = "BOTTOMRIGHT";
})(ReferencePoint || (ReferencePoint = {}));
export class Component {
    constructor(width, height) {
        this.visibleParent = null;
        this.backgroundColor = null;
        this.beveled = BevelType.NONE;
        this.bevelWidth = 0;
        this.borderColor = null;
        this.topBorderWidth = 0;
        this.rightBorderWidth = 0;
        this.bottomBorderWidth = 0;
        this.leftBorderWidth = 0;
        this.components = [];
        this.mouseOver = false;
        this.visible = true;
        this.tooltipEnabled = false;
        this.tooltipMessage = '';
        this.shouldUpdateComponents = false;
        this.acceptingInput = true;
        this.hasFocusFlag = false;
        this.componentListeners = [];
        this.origin = { x: 0, y: 0 };
        this.translation = { x: 0, y: 0 };
        this.width = width;
        this.height = height;
    }
    render(ctx) {
        ctx.save();
        ctx.beginPath();
        ctx.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        ctx.clip();
        if (this.backgroundColor !== null) {
            if (this.beveled === BevelType.NONE) {
                ctx.fillStyle = this.backgroundColor.toCSS();
                ctx.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            }
            else {
                const brightColor = this.backgroundColor.brighter(0.2);
                const darkColor = this.backgroundColor.darker(0.2);
                ctx.fillStyle = this.backgroundColor.toCSS();
                ctx.fillRect(this.getX() + this.bevelWidth, this.getY() + this.bevelWidth, this.getWidth() - this.bevelWidth * 2, this.getHeight() - this.bevelWidth * 2);
                if (this.beveled === BevelType.OUT) {
                    ctx.fillStyle = brightColor.toCSS();
                }
                else if (this.beveled === BevelType.IN) {
                    ctx.fillStyle = darkColor.toCSS();
                }
                ctx.fillRect(this.getX() + this.leftBorderWidth, this.getY() + this.topBorderWidth, this.getWidth() - this.leftBorderWidth - this.rightBorderWidth, this.bevelWidth);
                if (this.beveled === BevelType.OUT) {
                    ctx.fillStyle = darkColor.toCSS();
                }
                else if (this.beveled === BevelType.IN) {
                    ctx.fillStyle = brightColor.toCSS();
                }
                ctx.fillRect(this.getX() + this.leftBorderWidth, this.getY() + this.getHeight() - this.bevelWidth - this.bottomBorderWidth, this.getWidth() - this.leftBorderWidth - this.rightBorderWidth, this.bevelWidth);
                if (this.beveled === BevelType.OUT) {
                    ctx.fillStyle = brightColor.toCSS();
                }
                else if (this.beveled === BevelType.IN) {
                    ctx.fillStyle = darkColor.toCSS();
                }
                ctx.fillRect(this.getX() + this.leftBorderWidth, this.getY() + this.topBorderWidth, this.bevelWidth, this.getHeight() - this.topBorderWidth - this.bottomBorderWidth);
                if (this.beveled === BevelType.OUT) {
                    ctx.fillStyle = darkColor.toCSS();
                }
                else if (this.beveled === BevelType.IN) {
                    ctx.fillStyle = brightColor.toCSS();
                }
                ctx.fillRect(this.getX() + this.getWidth() - this.bevelWidth - this.rightBorderWidth, this.getY() + this.topBorderWidth, this.bevelWidth, this.getHeight() - this.topBorderWidth - this.bottomBorderWidth);
            }
        }
        for (const component of this.components) {
            component.render(ctx);
            if (Component.debugMode === DebugMode.ALL ||
                (Component.debugMode === DebugMode.VISIBLE && component.isVisible())) {
                if (component.isVisible()) {
                    ctx.strokeStyle = Color.red.toCSS();
                }
                else {
                    ctx.strokeStyle = Color.yellow.toCSS();
                }
                ctx.strokeRect(component.getX(), component.getY(), component.getWidth() - 1, component.getHeight() - 1);
            }
        }
        if (this.borderColor !== null) {
            ctx.fillStyle = this.borderColor.toCSS();
            ctx.fillRect(this.getX(), this.getY(), this.getWidth(), this.topBorderWidth);
            ctx.fillRect(this.getX() + this.getWidth() - this.rightBorderWidth, this.getY(), this.rightBorderWidth, this.getHeight());
            ctx.fillRect(this.getX(), this.getY() + this.getHeight() - this.bottomBorderWidth, this.getWidth(), this.bottomBorderWidth);
            ctx.fillRect(this.getX(), this.getY(), this.leftBorderWidth, this.getHeight());
        }
        ctx.restore();
    }
    update(delta) {
        if (this.getShouldUpdateComponents()) {
            for (const c of this.components) {
                c.update(delta);
            }
        }
    }
    setShouldUpdateComponents(shouldUpdate) {
        this.shouldUpdateComponents = shouldUpdate;
    }
    getShouldUpdateComponents() {
        return this.shouldUpdateComponents;
    }
    setVisibleParent(visible) {
        this.visibleParent = visible;
    }
    isVisible() {
        if (this.visibleParent !== null) {
            return this.visible && this.visibleParent.isVisible();
        }
        return this.visible;
    }
    setVisible(visible) {
        this.visible = visible;
    }
    add(component, location, referencePoint, xOffset = 0, yOffset = 0) {
        component.setPosition(location, referencePoint, xOffset, yOffset);
        this.components.push(component);
        component.setVisibleParent(this);
    }
    addAsColumn(components, location, xOffset, yOffset, spacing) {
        let currentY = location.y;
        for (const c of components) {
            this.add(c, { x: location.x, y: currentY }, ReferencePoint.TOPLEFT, xOffset, yOffset);
            currentY += c.getHeight() + spacing;
        }
    }
    addAsRow(components, location, xOffset, yOffset, spacing) {
        let currentX = location.x;
        for (const c of components) {
            if (c !== null) {
                this.add(c, { x: currentX, y: location.y }, ReferencePoint.TOPLEFT, xOffset, yOffset);
                currentX += c.getWidth() + spacing;
            }
        }
    }
    addAsGrid(components, location, _rows, cols, xOffset, yOffset, xSpacing, ySpacing) {
        let startIndex = 0;
        let currentY = location.y;
        while (startIndex < components.length) {
            const row = [];
            for (let x = 0; x < cols && startIndex + x < components.length; x++) {
                row.push(components[startIndex + x]);
            }
            this.addAsRow(row, { x: location.x, y: currentY }, xOffset, yOffset, xSpacing);
            currentY += components[startIndex].getHeight() + ySpacing;
            startIndex += cols;
        }
    }
    addAsGrid2D(components, xSpacing, ySpacing, location) {
        let currentY = location.y;
        for (const row of components) {
            this.addAsRow(row, { x: location.x, y: currentY }, 0, 0, xSpacing);
            if (row.length > 0) {
                currentY += row[0].getHeight() + ySpacing;
            }
        }
    }
    remove(component) {
        component.setVisibleParent(null);
        const index = this.components.indexOf(component);
        if (index > -1) {
            this.components.splice(index, 1);
        }
    }
    setPosition(location, referencePoint, xOffset = 0, yOffset = 0) {
        const offsetLocation = { x: location.x + xOffset, y: location.y + yOffset };
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
    getPosition(referencePoint) {
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
    getBackgroundColor() {
        return this.backgroundColor;
    }
    setBackgroundColor(color) {
        this.backgroundColor = color;
    }
    setBevel(bevelType) {
        this.beveled = bevelType;
    }
    setBevelWidth(width) {
        this.bevelWidth = width;
    }
    setBorderWidth(width) {
        this.setTopBorderWidth(width);
        this.setRightBorderWidth(width);
        this.setBottomBorderWidth(width);
        this.setLeftBorderWidth(width);
    }
    setTopBorderWidth(width) {
        this.topBorderWidth = width;
    }
    setRightBorderWidth(width) {
        this.rightBorderWidth = width;
    }
    setBottomBorderWidth(width) {
        this.bottomBorderWidth = width;
    }
    setLeftBorderWidth(width) {
        this.leftBorderWidth = width;
    }
    setBorderColor(color) {
        this.borderColor = color;
    }
    containsPoint(x, y) {
        return (x >= this.getX() &&
            x <= this.getX() + this.getWidth() &&
            y >= this.getY() &&
            y <= this.getY() + this.getHeight());
    }
    getWidth() {
        return this.width;
    }
    getHeight() {
        return this.height;
    }
    getX() {
        return this.origin.x + this.translation.x;
    }
    getY() {
        return this.origin.y + this.translation.y;
    }
    setLocation(x, y) {
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
    setTranslation(x, y) {
        if (this.components.length > 0) {
            for (const c of this.components) {
                c.setTranslation(x, y);
            }
        }
        this.translation.x = x;
        this.translation.y = y;
    }
    setAcceptingInput(acceptingInput) {
        this.acceptingInput = acceptingInput;
        if (!acceptingInput) {
            this.mouseOver = false;
        }
        for (const c of this.components) {
            c.setAcceptingInput(acceptingInput);
        }
    }
    isAcceptingInput() {
        return this.acceptingInput;
    }
    getVisibleParent() {
        return this.visibleParent;
    }
    mouseMoved(_oldx, _oldy, newx, newy) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        this.mouseOver = this.containsPoint(newx, newy);
        if (this.isMouseOver() && this.tooltipEnabled) {
            // TODO: Scene.showTooltip(newx, newy, this, tooltipMessage);
        }
    }
    mousePressed(_button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        this.mouseOver = this.containsPoint(mx, my);
    }
    mouseReleased(_button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        this.mouseOver = this.containsPoint(mx, my);
    }
    isMouseOver() {
        return this.mouseOver;
    }
    static changeDebugMode() {
        if (Component.debugMode === DebugMode.NONE) {
            Component.debugMode = DebugMode.VISIBLE;
        }
        else if (Component.debugMode === DebugMode.VISIBLE) {
            Component.debugMode = DebugMode.ALL;
        }
        else {
            Component.debugMode = DebugMode.NONE;
        }
    }
    isTooltipEnabled() {
        return this.tooltipEnabled;
    }
    setTooltipEnabled(tooltipEnabled) {
        this.tooltipEnabled = tooltipEnabled;
        if (this.tooltipMessage === '') {
            this.setTooltipMessage('');
        }
    }
    getTooltipMessage() {
        return this.tooltipMessage;
    }
    setTooltipMessage(tooltipMessage) {
        this.tooltipMessage = tooltipMessage;
    }
    hasFocus() {
        return this.hasFocusFlag;
    }
    setFocus(focus) {
        this.hasFocusFlag = focus;
    }
    addListener(listener) {
        this.componentListeners.push(listener);
    }
    removeListener(listener) {
        const index = this.componentListeners.indexOf(listener);
        if (index > -1) {
            this.componentListeners.splice(index, 1);
        }
    }
    notifyListeners() {
        for (const listener of this.componentListeners) {
            listener();
        }
    }
    keyReleased(_key, _char) {
        // Override in subclasses
    }
}
Component.debugMode = DebugMode.NONE;
//# sourceMappingURL=Component.js.map