import { Player } from "./Player";
import { WorldMap } from "./WorldMap";
import { Inventory } from "./Inventory";
import { Item } from "./Item";
import { ItemType, getItemTypeNecessaryQuality } from "./ItemType";
export class Game {
    constructor(worldMap) {
        this.worldMap = worldMap;
        this.player = new Player();
        this.storeInventory = new Inventory(16, 10000);
        if (this.player) {
            console.info("Player was created successfully");
        }
        this.resetStoreInventory(worldMap.getMapHead());
    }
    getPlayer() {
        return this.player;
    }
    getStoreInventory() {
        return this.storeInventory;
    }
    getWorldMap() {
        return this.worldMap;
    }
    reset() {
        this.worldMap.resetMap();
        this.worldMap = new WorldMap();
        this.player = new Player();
        this.resetStoreInventory(this.worldMap.getMapHead());
    }
    resetStoreInventory(location) {
        this.storeInventory.clear();
        const startItems = [];
        for (const itemType of Object.values(ItemType)) {
            if (getItemTypeNecessaryQuality(itemType) <=
                location.getCondition().getCurrent()) {
                startItems.push(itemType);
            }
        }
        const indexToRemove = [];
        for (let i = 0; i < startItems.length; i++) {
            if (startItems[i] === ItemType.STRANGEMEAT ||
                startItems[i] === ItemType.SONIC ||
                startItems[i] === ItemType.TRADEGOODS) {
                indexToRemove.push(i);
            }
        }
        for (let i = indexToRemove.length - 1; i >= 0; i--) {
            startItems.splice(indexToRemove[i], 1);
        }
        if (location.getRank() === 0) {
            for (let i = 0; i < Math.floor(Math.random() * 50) + 50; i++) {
                this.storeInventory.addItemToInventory(new Item(ItemType.TRADEGOODS));
            }
        }
        let numberOf;
        for (const itemType of startItems) {
            if (isItemTypeFood(itemType)) {
                numberOf =
                    Math.floor((Math.random() * 50 + 25) / (Math.floor(location.getRank() / 4) + 1));
            }
            else if (itemType === ItemType.MAP ||
                itemType === ItemType.WAGON) {
                numberOf = 1;
            }
            else {
                numberOf =
                    Math.floor((Math.random() * 10 + 5) / (Math.floor(location.getRank() / 4) + 1));
            }
            for (let i = 0; i < numberOf; i++) {
                this.storeInventory.addItemToInventory(new Item(itemType));
            }
        }
    }
    toString() {
        return "Game";
    }
}
function isItemTypeFood(itemType) {
    return (itemType === ItemType.APPLE ||
        itemType === ItemType.BREAD ||
        itemType === ItemType.MEAT ||
        itemType === ItemType.STRANGEMEAT);
}
//# sourceMappingURL=Game.js.map