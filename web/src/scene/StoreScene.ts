import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Button } from '../component/Button';
import { Label, Alignment } from '../component/Label';
import { Counter } from '../component/Counter';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { get as getLiteral } from '../core/ConstantStore';
import { Party } from '../model/Party';
import { Inventory } from '../model/Inventory';
import { ItemType, getItemTypeName, getItemTypeCost, getItemTypeWeight } from '../model/ItemType';

export class StoreScene extends Scene {
  private static readonly PADDING = 20;
  private static readonly REGULAR_BUTTON_HEIGHT = 30;
  private static readonly INVENTORY_BUTTON_WIDTH = 130;
  private static readonly INVENTORY_BUTTON_HEIGHT = 90;
  private static readonly BACKGROUND_COLOR = Color.fromHex(0x0C5DA5);
  private static readonly TEXT_PANEL_COLOR = Color.fromHex(0x679FD2);
  private static readonly CANVAS_WIDTH = 1024;
  private static readonly CANVAS_HEIGHT = 576;

  private party: Party;
  private storeInventory: Inventory;
  private priceModifier: number;
  private buttonMap: ItemType[] = [];
  private storeCounters: Counter[] = [];
  private currentItem: ItemType | null = null;

  private textPanel!: Panel;
  private partyMoneyLabel!: Label;
  private itemNameLabel!: Label;
  private itemDescLabel!: Label;
  private itemWeightLabel!: Label;
  private itemCostLabel!: Label;
  private qtyLabel!: Label;
  private totalWeightLabel!: Label;
  private totalCostLabel!: Label;

  private cancelButton!: Button;
  private clearButton!: Button;
  private inventoryButton!: Button;
  private buyButton!: Button;

  constructor(party: Party, storeInventory: Inventory, priceModifier: number) {
    super();
    this.party = party;
    this.storeInventory = storeInventory;
    this.priceModifier = priceModifier;
    this.init();
  }

  private init(): void {
    const backgroundPanel = new Panel(
      StoreScene.CANVAS_WIDTH,
      StoreScene.CANVAS_HEIGHT,
      StoreScene.BACKGROUND_COLOR
    );
    this.backgroundLayer.add(backgroundPanel);
    backgroundPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    this.createComponents();
    this.layoutComponents();
  }

  private createComponents(): void {
    const fieldFont = FontStore.getFont(FontID.FIELD);
    const populatedSlots = this.storeInventory.getPopulatedSlots();

    this.partyMoneyLabel = new Label(
      StoreScene.INVENTORY_BUTTON_WIDTH * 4 + StoreScene.PADDING * 3,
      StoreScene.REGULAR_BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      ''
    );
    this.partyMoneyLabel.setAlignment(Alignment.CENTER);
    this.updatePartyMoneyLabel();

    for (let i = 0; i < populatedSlots.length && i < 16; i++) {
      const itemType = populatedSlots[i];
      this.buttonMap.push(itemType);

      const itemLabel = Label.withTextWidth(fieldFont, Color.white, '');
      const counter = new Counter(
        StoreScene.INVENTORY_BUTTON_WIDTH,
        StoreScene.INVENTORY_BUTTON_HEIGHT,
        itemLabel
      );

      const quantity = this.storeInventory.getNumberOf(itemType);
      counter.setMax(quantity);
      counter.setCount(quantity);
      counter.addListener(() => this.onCounterClick(itemType));

      this.storeCounters.push(counter);
    }

    this.textPanel = new Panel(400, 450, StoreScene.TEXT_PANEL_COLOR);

    this.itemNameLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.itemDescLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.itemWeightLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.itemCostLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.qtyLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.totalWeightLabel = Label.withTextWidth(fieldFont, Color.white, '');
    this.totalCostLabel = Label.withTextWidth(fieldFont, Color.white, '');

    this.cancelButton = new Button(
      200,
      StoreScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(fieldFont, Color.white, getLiteral('GENERAL', 'LEAVE') || 'Leave')
    );
    this.cancelButton.addClickListener(() => this.onCancelClick());
    this.cancelButton.layout();

    this.clearButton = new Button(
      120,
      StoreScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(fieldFont, Color.white, 'Clear')
    );
    this.clearButton.addClickListener(() => this.onClearClick());
    this.clearButton.setDisabled(true);
    this.clearButton.layout();

    this.inventoryButton = new Button(
      120,
      StoreScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(fieldFont, Color.white, 'Inventory')
    );
    this.inventoryButton.addClickListener(() => this.onInventoryClick());
    this.inventoryButton.layout();

    this.buyButton = new Button(
      120,
      StoreScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(fieldFont, Color.white, getLiteral('GENERAL', 'CONFIRM') || 'Buy')
    );
    this.buyButton.addClickListener(() => this.onBuyClick());
    this.buyButton.setDisabled(true);
    this.buyButton.layout();
  }

  private layoutComponents(): void {
    const storePanel = new Panel(
      StoreScene.INVENTORY_BUTTON_WIDTH * 4 + StoreScene.PADDING * 5,
      StoreScene.INVENTORY_BUTTON_HEIGHT * 4 + StoreScene.PADDING * 5
    );

    (this.mainLayer as any).add(storePanel);
    storePanel.setPosition(
      { x: 0, y: 0 },
      ReferencePoint.TOPLEFT,
      StoreScene.PADDING,
      StoreScene.PADDING
    );

    (storePanel as any).addAsGrid(
      this.storeCounters,
      storePanel.getPosition(ReferencePoint.TOPLEFT),
      4,
      4,
      StoreScene.PADDING,
      StoreScene.PADDING,
      StoreScene.PADDING,
      StoreScene.PADDING
    );

    (this.mainLayer as any).add(this.partyMoneyLabel);
    this.partyMoneyLabel.setPosition(
      storePanel.getPosition(ReferencePoint.BOTTOMCENTER),
      ReferencePoint.TOPCENTER,
      0,
      StoreScene.PADDING
    );

    (this.mainLayer as any).add(this.textPanel);
    this.textPanel.setPosition(
      { x: StoreScene.CANVAS_WIDTH, y: 0 },
      ReferencePoint.TOPRIGHT,
      -StoreScene.PADDING,
      StoreScene.PADDING
    );

    (this.textPanel as any).addAsColumn(
      [
        this.itemNameLabel,
        this.itemDescLabel,
        this.itemWeightLabel,
        this.itemCostLabel,
        this.qtyLabel,
        this.totalWeightLabel,
        this.totalCostLabel
      ],
      this.textPanel.getPosition(ReferencePoint.TOPLEFT),
      StoreScene.PADDING,
      StoreScene.PADDING,
      StoreScene.PADDING
    );

    (this.mainLayer as any).add(this.clearButton);
    this.clearButton.setPosition(
      { x: StoreScene.CANVAS_WIDTH, y: StoreScene.CANVAS_HEIGHT },
      ReferencePoint.BOTTOMRIGHT,
      -StoreScene.PADDING,
      -StoreScene.PADDING
    );

    (this.mainLayer as any).add(this.buyButton);
    this.buyButton.setPosition(
      this.clearButton.getPosition(ReferencePoint.TOPLEFT),
      ReferencePoint.TOPRIGHT,
      -StoreScene.PADDING,
      0
    );

    (this.mainLayer as any).add(this.cancelButton);
    this.cancelButton.setPosition(
      {
        x: storePanel.getPosition(ReferencePoint.BOTTOMLEFT).x,
        y: this.buyButton.getPosition(ReferencePoint.TOPLEFT).y
      },
      ReferencePoint.TOPLEFT,
      0,
      0
    );

    (this.mainLayer as any).add(this.inventoryButton);
    this.inventoryButton.setPosition(
      {
        x: storePanel.getPosition(ReferencePoint.BOTTOMRIGHT).x,
        y: this.buyButton.getPosition(ReferencePoint.TOPLEFT).y
      },
      ReferencePoint.TOPRIGHT,
      0,
      0
    );
  }

  private getButtonIndex(itemType: ItemType): number {
    return this.buttonMap.indexOf(itemType);
  }

  private updatePartyMoneyLabel(): void {
    const money = this.party.getMoney();
    this.partyMoneyLabel.setText(`Party Money: $${money.toFixed(2)}`);
  }

  private updateLabels(itemType: ItemType): void {
    const index = this.getButtonIndex(itemType);
    if (index === -1) return;

    const counter = this.storeCounters[index];
    const quantityPurchasing = counter.getMax() - counter.getCount();
    const itemCost = getItemTypeCost(itemType) * this.priceModifier;
    const itemWeight = getItemTypeWeight(itemType);

    this.itemNameLabel.setText(`Item: ${getItemTypeName(itemType)}`);
    this.itemDescLabel.setText(''); // Description would come from ConstantStore
    this.itemWeightLabel.setText(`Weight: ${itemWeight} lbs`);
    this.itemCostLabel.setText(`Cost: $${itemCost.toFixed(2)}`);
    this.qtyLabel.setText(`Quantity: ${quantityPurchasing}`);
    this.totalWeightLabel.setText(`Total Weight: ${(itemWeight * quantityPurchasing).toFixed(1)} lbs`);
    this.totalCostLabel.setText(`Total Cost: $${(itemCost * quantityPurchasing).toFixed(2)}`);
  }

  private onCounterClick(itemType: ItemType): void {
    this.currentItem = itemType;
    this.updateLabels(itemType);

    for (let i = 0; i < this.storeCounters.length; i++) {
      if (this.buttonMap[i] !== itemType) {
        this.storeCounters[i].setDisabled(true);
      }
    }

    const index = this.getButtonIndex(itemType);
    const quantityPurchasing = this.storeCounters[index].getMax() - this.storeCounters[index].getCount();

    this.buyButton.setDisabled(quantityPurchasing === 0);
    this.clearButton.setDisabled(false);
  }

  private onClearClick(): void {
    if (this.currentItem === null) return;

    const index = this.getButtonIndex(this.currentItem);
    this.storeCounters[index].setCount(this.storeCounters[index].getMax());

    this.buyButton.setDisabled(true);
    this.clearButton.setDisabled(true);

    for (const counter of this.storeCounters) {
      counter.setDisabled(false);
    }

    this.currentItem = null;
  }

  private onBuyClick(): void {
    console.log('Buy button clicked - implement purchase modal');
  }

  private onInventoryClick(): void {
    console.log('Inventory button clicked - request PartyInventoryScene');
  }

  private onCancelClick(): void {
    console.log('Cancel button clicked - return to TownScene');
  }

  override update(delta: number): void {
    super.update(delta);

    if (this.currentItem !== null) {
      const index = this.getButtonIndex(this.currentItem);
      const counter = this.storeCounters[index];

      if (counter.getCount() === counter.getMax()) {
        this.onClearClick();
      } else {
        this.updateLabels(this.currentItem);
      }
    }
  }

  getID(): SceneID {
    return SceneID.STORE;
  }
}
