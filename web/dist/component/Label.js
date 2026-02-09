import { Component } from './Component';
export var Alignment;
(function (Alignment) {
    Alignment["CENTER"] = "CENTER";
    Alignment["LEFT"] = "LEFT";
})(Alignment || (Alignment = {}));
export var VerticalAlignment;
(function (VerticalAlignment) {
    VerticalAlignment["CENTER"] = "CENTER";
    VerticalAlignment["TOP"] = "TOP";
})(VerticalAlignment || (VerticalAlignment = {}));
export class Label extends Component {
    constructor(width, height, font, color, text = '') {
        super(width, height);
        this.lines = [];
        this.alignment = Alignment.CENTER;
        this.verticalAlignment = VerticalAlignment.CENTER;
        this.clip = true;
        this.font = font;
        this.color = color;
        this.text = text;
        this.parseLines();
    }
    static withAutoHeight(width, font, color, text) {
        const height = Label.calculateHeight(width, text, font);
        const label = new Label(width, height, font, color, text);
        return label;
    }
    static withTextWidth(font, color, text) {
        const width = font.getWidth(text);
        const label = new Label(width, font.getLineHeight(), font, color, text);
        label.clip = false;
        return label;
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
        ctx.save();
        if (this.clip) {
            ctx.beginPath();
            ctx.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            ctx.clip();
        }
        let startY;
        if (this.verticalAlignment === VerticalAlignment.CENTER) {
            startY = this.getY() + (this.getHeight() - this.lines.length * this.font.getLineHeight()) / 2;
        }
        else {
            startY = this.getY();
        }
        startY += Label.Y_OFFSET;
        ctx.fillStyle = this.color.toCSS();
        for (let i = 0; i < this.lines.length; i++) {
            const line = this.lines[i];
            const lineY = startY + this.font.getLineHeight() * i;
            if (this.alignment === Alignment.CENTER) {
                const lineWidth = this.font.getWidth(line);
                const lineX = this.getX() + 1 + (this.getWidth() - lineWidth) / 2;
                this.font.drawText(ctx, line, lineX, lineY);
            }
            else if (this.alignment === Alignment.LEFT) {
                this.font.drawText(ctx, line, this.getX(), lineY);
            }
        }
        ctx.restore();
    }
    static calculateHeight(width, text, font) {
        const textLines = text.split('\n');
        let lines = 0;
        for (const textLine of textLines) {
            const words = textLine.split(/\s+/);
            let currentText = '';
            for (const word of words) {
                const testText = currentText ? currentText + ' ' + word : word;
                if (font.getWidth(testText) > width) {
                    if (currentText) {
                        currentText = '';
                        lines++;
                    }
                }
                currentText = currentText ? currentText + ' ' + word : word;
            }
            if (currentText) {
                lines++;
            }
        }
        return lines * font.getLineHeight();
    }
    parseLines() {
        this.lines = [];
        const textLines = this.text.split('\n');
        const maxLines = Math.floor(this.getHeight() / this.getFontHeight()) - 1;
        let currentLine = 0;
        for (const textLine of textLines) {
            const words = textLine.split(/\s+/);
            let currentText = '';
            for (const word of words) {
                const testText = currentText ? currentText + ' ' + word : word;
                if (currentLine !== maxLines && this.font.getWidth(testText) > this.getWidth()) {
                    if (currentText) {
                        this.lines.push(currentText.trim());
                        currentText = '';
                        currentLine++;
                    }
                }
                currentText = currentText ? currentText + ' ' + word : word;
            }
            if (currentText) {
                this.lines.push(currentText.trim());
                currentLine++;
            }
        }
    }
    static getNumberOfNewlines(text) {
        return text.split('\n').length;
    }
    setFont(font) {
        this.font = font;
        this.parseLines();
    }
    getFontWidth() {
        return this.font.getWidth(this.text);
    }
    getFontHeight() {
        return this.font.getLineHeight();
    }
    setText(text) {
        this.text = text;
        this.parseLines();
    }
    getText() {
        return this.text;
    }
    setColor(color) {
        this.color = color;
    }
    setAlignment(alignment) {
        this.alignment = alignment;
    }
    setVerticalAlignment(verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }
    toString() {
        return `Label[text: "${this.text}"]`;
    }
}
Label.Y_OFFSET = 1;
//# sourceMappingURL=Label.js.map