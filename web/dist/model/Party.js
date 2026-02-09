import { Animal } from "./Animal";
import { isItemTypeFood, isItemTypePlant, isItemTypeAnimal, isItemTypeTool, getItemTypeFactor } from "./ItemType";
import { Notification } from "./Notification";
import { Skill } from "./Skill";
import { getProfessionMoney } from "./Profession";
export var Pace;
(function (Pace) {
    Pace["STEADY"] = "STEADY";
    Pace["STRENUOUS"] = "STRENUOUS";
    Pace["GRUELING"] = "GRUELING";
})(Pace || (Pace = {}));
const paceData = {
    [Pace.STEADY]: { name: "Steady", speed: 30 },
    [Pace.STRENUOUS]: { name: "Strenuous", speed: 55 },
    [Pace.GRUELING]: { name: "Grueling", speed: 80 },
};
export function getPaceName(pace) {
    return paceData[pace].name;
}
export function getPaceSpeed(pace) {
    return paceData[pace].speed;
}
export var Rations;
(function (Rations) {
    Rations["FILLING"] = "FILLING";
    Rations["MEAGER"] = "MEAGER";
    Rations["BAREBONES"] = "BAREBONES";
})(Rations || (Rations = {}));
const rationsData = {
    [Rations.FILLING]: { name: "Filling", rationAmount: 100 },
    [Rations.MEAGER]: { name: "Meager", rationAmount: 75 },
    [Rations.BAREBONES]: { name: "Barebones", rationAmount: 50 },
};
export function getRationsName(rations) {
    return rationsData[rations].name;
}
export function getRationsAmount(rations) {
    return rationsData[rations].rationAmount;
}
export class Party {
    constructor(currentPace, currentRations, partyLeader, party, time) {
        this.MAX_MEMBERS = 4;
        this.members = [];
        for (const person of party) {
            if (person) {
                this.members.push(person);
            }
        }
        this.partyLeader = partyLeader;
        this.partyLeader.setLeader(true);
        this.money = 0;
        this.currentPace = currentPace;
        this.currentRations = currentRations;
        this.location = null;
        this.time = time;
        this.vehicle = null;
        this.trail = null;
        this.animals = [];
        this.totalDistanceTravelled = 0;
        const partyCreationLog = [];
        partyCreationLog.push(`${this.members.length} members were created successfully: `);
        for (const person of party) {
            const professionMoney = person.getProfession()
                ? getProfessionMoney(person.getProfession())
                : 0;
            this.money += professionMoney;
            console.info(`${person === partyLeader ? "Party leader " : ""}${person.getName()} as a ${person.getProfession()} brings $${professionMoney} to the party.`);
            partyCreationLog.push(`${person.getName()} `);
        }
        console.info(partyCreationLog.join(""));
        console.info(`Party starting money is: $${this.money}`);
        console.info(`Current pace is: ${getPaceName(this.currentPace)} and current rations is: ${getRationsName(this.currentRations)}`);
    }
    buyItemForInventory(items, buyer) {
        let cost = 0;
        const itemType = items[0].getType();
        const numberOf = items.length;
        for (const item of items) {
            cost += item.getCost();
        }
        if (this.money >= cost && buyer.canGetItem(itemType, numberOf)) {
            if (isItemTypeAnimal(itemType)) {
                for (let i = 0; i < numberOf; i++) {
                    this.addAnimals(new Animal(itemType));
                }
            }
            else {
                buyer.addItemsToInventory(items);
            }
            this.money -= cost;
        }
    }
    canGetItem(itemType, numberOf) {
        const ableList = [];
        if (this.animals.length + numberOf > Party.MAX_ANIMALS) {
            return ableList;
        }
        for (const person of this.members) {
            if (person.canGetItem(itemType, numberOf)) {
                ableList.push(person);
            }
        }
        if (this.vehicle && this.vehicle.canGetItem(itemType, numberOf)) {
            ableList.push(this.vehicle);
        }
        return ableList;
    }
    getPartyMembers() {
        return this.members;
    }
    addPartyMember(person) {
        if (this.members.length < this.MAX_MEMBERS) {
            this.members.push(person);
        }
    }
    getSkills() {
        const skillList = [];
        for (const person of this.members) {
            for (const skill of person.getSkills()) {
                if (!skillList.includes(skill) && skill !== Skill.NONE) {
                    skillList.push(skill);
                }
            }
        }
        return skillList;
    }
    getMoney() {
        return this.money;
    }
    setMoney(money) {
        this.money = money;
    }
    getPace() {
        return this.currentPace;
    }
    setPace(pace) {
        this.currentPace = pace;
    }
    getRations() {
        return this.currentRations;
    }
    setRations(rations) {
        this.currentRations = rations;
    }
    getVehicle() {
        return this.vehicle;
    }
    setVehicle(vehicle) {
        this.vehicle = vehicle;
    }
    getTrail() {
        return this.trail;
    }
    setTrail(trail) {
        this.trail = trail;
    }
    getLocation() {
        return this.location;
    }
    setLocation(location) {
        this.location = location;
    }
    walk() {
        const messages = [];
        const movement = (getPaceSpeed(this.getPace()) * this.getMoveModifier()) / 30;
        if (this.trail) {
            this.trail.advance(movement);
        }
        this.totalDistanceTravelled += movement;
        const slaughterHouse = [];
        for (const animal of this.animals) {
            this.grazeAnimals();
            if (animal.getStatus().getCurrent() === 0) {
                if (this.vehicle) {
                    this.vehicle.addItemsToInventory(animal.killForFood());
                }
                slaughterHouse.push(animal);
            }
        }
        const deathList = [];
        let finalResult = 0;
        let eatFoodRemnant;
        this.partyLeader.increaseSkillPoints(Math.floor(getPaceSpeed(this.getPace()) / 10));
        if (!this.personHasFood(this.partyLeader) && !this.vehicleHasFood()) {
            this.partyLeader.decreaseHealth(getPaceSpeed(this.getPace()));
            eatFoodRemnant = getRationsAmount(this.getRations());
        }
        else {
            eatFoodRemnant = this.eatFood(this.partyLeader, getRationsAmount(this.getRations()));
            finalResult =
                getRationsAmount(this.getRations()) -
                    eatFoodRemnant -
                    getPaceSpeed(this.getPace());
            if (finalResult > 0) {
                this.partyLeader.increaseHealth(finalResult);
            }
            else {
                this.partyLeader.decreaseHealth(-finalResult);
            }
        }
        if (this.partyLeader.getHealth().getCurrent() === 0) {
            deathList.push(this.partyLeader);
        }
        for (const person of this.members) {
            if (person !== this.partyLeader) {
                person.increaseSkillPoints(Math.floor(getPaceSpeed(this.getPace()) / 10));
                if (!this.personHasFood(person) && !this.vehicleHasFood()) {
                    person.decreaseHealth(getPaceSpeed(this.getPace()));
                }
                else {
                    finalResult =
                        getRationsAmount(this.getRations()) -
                            this.eatFood(person, getRationsAmount(this.getRations())) -
                            getPaceSpeed(this.getPace());
                    if (finalResult > 0) {
                        person.increaseHealth(finalResult);
                    }
                    else {
                        person.decreaseHealth(-finalResult);
                    }
                }
                if (person.getHealth().getCurrent() === 0) {
                    if (this.vehicle) {
                        this.vehicle.addItemsToInventory(person.killForFood());
                    }
                    deathList.push(person);
                }
            }
        }
        this.partyLeader.increaseHealth(eatFoodRemnant - this.eatFood(this.partyLeader, eatFoodRemnant));
        const hungerStatus = this.checkHungerStatus();
        if (hungerStatus) {
            messages.push(new Notification(hungerStatus, true));
        }
        const animalHunger = this.checkAnimalHunger();
        if (animalHunger) {
            messages.push(new Notification(animalHunger, true));
        }
        for (const person of deathList) {
            if (person.getHealth().getCurrent() === 0) {
                person.setDead(true);
                const index = this.members.indexOf(person);
                if (index > -1) {
                    this.members.splice(index, 1);
                }
            }
        }
        for (const animal of slaughterHouse) {
            animal.setDead(true);
            const index = this.animals.indexOf(animal);
            if (index > -1) {
                this.animals.splice(index, 1);
            }
        }
        messages.push(new Notification(`Current Distance Travelled: ${this.getTotalDistanceTravelled().toLocaleString()}`, false));
        return messages;
    }
    rest() {
        const messages = [];
        const deathList = [];
        let finalResult = 0;
        let eatFoodRemnant;
        this.partyLeader.increaseSkillPoints(Math.floor(getPaceSpeed(this.getPace()) / 10));
        if (!this.personHasFood(this.partyLeader) && !this.vehicleHasFood()) {
            this.partyLeader.decreaseHealth(5);
            eatFoodRemnant = getRationsAmount(this.getRations());
        }
        else {
            eatFoodRemnant = this.eatFood(this.partyLeader, getRationsAmount(this.getRations()));
            finalResult =
                getRationsAmount(this.getRations()) -
                    eatFoodRemnant -
                    getPaceSpeed(this.getPace());
            if (finalResult > 0) {
                this.partyLeader.increaseHealth(finalResult);
            }
            else {
                this.partyLeader.decreaseHealth(-finalResult);
            }
        }
        if (this.partyLeader.getHealth().getCurrent() === 0) {
            deathList.push(this.partyLeader);
        }
        for (const person of this.members) {
            if (person !== this.partyLeader) {
                if (!this.personHasFood(person) && !this.vehicleHasFood()) {
                    person.decreaseHealth(5);
                }
                else {
                    finalResult =
                        getRationsAmount(this.getRations()) -
                            this.eatFood(person, getRationsAmount(this.getRations())) -
                            5;
                    if (finalResult > 0) {
                        person.increaseHealth(finalResult);
                    }
                    else {
                        person.decreaseHealth(-finalResult);
                    }
                }
                if (person.getHealth().getCurrent() === 0) {
                    if (this.vehicle) {
                        this.vehicle.addItemsToInventory(person.killForFood());
                    }
                    deathList.push(person);
                }
            }
        }
        const hungerStatus = this.checkHungerStatus();
        if (hungerStatus) {
            messages.push(new Notification(hungerStatus, true));
        }
        for (const person of deathList) {
            if (person === this.partyLeader) {
                this.partyLeader.increaseHealth(eatFoodRemnant - this.eatFood(this.partyLeader, eatFoodRemnant));
            }
            if (person.getHealth().getCurrent() === 0) {
                person.setDead(true);
                const index = this.members.indexOf(person);
                if (index > -1) {
                    this.members.splice(index, 1);
                }
            }
        }
        messages.push(new Notification(`Current Distance Travelled: ${this.getTotalDistanceTravelled().toLocaleString()}`, false));
        return messages;
    }
    grazeAnimals() {
        for (const animal of this.animals) {
            const amount = this.getAnimalStrain(animal);
            if (amount < 0) {
                animal.decreaseStatus(-amount);
            }
            else {
                animal.increaseStatus(amount);
            }
        }
    }
    getAnimalStrain(animal) {
        if (!this.vehicle) {
            return 10;
        }
        if (this.getPace() === Pace.GRUELING) {
            return -Math.floor(animal.getMoveFactor() *
                5 *
                (this.vehicle.getWeight() / this.vehicle.getMaxWeight()));
        }
        else if (this.getPace() === Pace.STRENUOUS) {
            return (-Math.floor(animal.getMoveFactor() *
                5 *
                (this.vehicle.getWeight() / this.vehicle.getMaxWeight())) / 2);
        }
        return 10;
    }
    getTotalDistanceTravelled() {
        return Math.floor(this.totalDistanceTravelled);
    }
    getMoveModifier() {
        let moveModifier = 1;
        for (const animal of this.getAnimals()) {
            moveModifier += animal.getMoveFactor();
        }
        const result = moveModifier / 10;
        if (result > Party.MAX_SPEED) {
            return Party.MAX_SPEED;
        }
        else if (result < 1) {
            return 1;
        }
        return result;
    }
    getAnimals() {
        return this.animals;
    }
    getNumberOfAnimal(type) {
        let numberOf = 0;
        for (const animal of this.animals) {
            if (animal.getType() === type) {
                numberOf++;
            }
        }
        return numberOf;
    }
    getAnimalsAsString() {
        let str = "";
        let isFirst = true;
        for (const animal of this.animals) {
            if (!isFirst) {
                str += ", ";
            }
            else {
                isFirst = false;
            }
            str += animal.toString();
        }
        return str;
    }
    eatFood(person, amount) {
        const restoreNeeded = getRationsAmount(this.getRations());
        if (restoreNeeded === 0) {
            return 0;
        }
        if (!this.personHasFood(person) && !this.vehicleHasFood()) {
            return amount;
        }
        let donator = null;
        if (this.personHasFood(person)) {
            donator = person;
        }
        else if (!this.personHasFood(person) && this.vehicleHasFood()) {
            donator = this.vehicle;
        }
        if (!donator) {
            return amount;
        }
        let firstFood = null;
        const typeList = donator.getInventory().getPopulatedSlots();
        for (const itemType of typeList) {
            if (isItemTypeFood(itemType)) {
                firstFood = itemType;
                break;
            }
        }
        if (!firstFood) {
            return amount;
        }
        const foodList = donator.removeItemFromInventory(firstFood, 1);
        if (!foodList || foodList.length === 0) {
            return amount;
        }
        const food = foodList[0];
        let foodFactor = getItemTypeFactor(food.getType());
        if (isItemTypePlant(food.getType()) && this.getSkills().includes(Skill.BOTANY)) {
            foodFactor += 1;
        }
        if (this.getSkills().includes(Skill.COOKING)) {
            foodFactor += 1;
        }
        const foodToEat = Math.floor(restoreNeeded / foodFactor) + 1;
        if (food.getStatus().getCurrent() > foodToEat) {
            food.decreaseStatus(foodToEat);
            donator.addItemToInventory(food);
            return 0;
        }
        else {
            const restoreAmount = Math.floor(food.getStatus().getCurrent()) * foodFactor;
            return this.eatFood(person, restoreNeeded - restoreAmount);
        }
    }
    vehicleHasFood() {
        if (this.vehicle) {
            for (const itemType of this.vehicle.getInventory().getPopulatedSlots()) {
                if (isItemTypeFood(itemType)) {
                    return true;
                }
            }
        }
        return false;
    }
    personHasFood(person) {
        for (const itemType of person.getInventory().getPopulatedSlots()) {
            if (isItemTypeFood(itemType)) {
                return true;
            }
        }
        return false;
    }
    checkHungerStatus() {
        const deadMembers = [];
        const hungryMembers = [];
        let hasMessage = false;
        const people = [...this.members];
        for (const person of people) {
            const currentHealth = Math.floor(person.getHealth().getCurrent());
            if (currentHealth === 0) {
                hasMessage = true;
                deadMembers.push(person.getName());
            }
            else if (person.getHealth().getCurrent() <
                getPaceSpeed(this.getPace()) -
                    (this.personHasFood(person) || this.vehicleHasFood()
                        ? getRationsAmount(this.getRations())
                        : 0)) {
                hasMessage = true;
                hungryMembers.push(person.getName());
            }
        }
        if (hasMessage) {
            let str = "";
            if (deadMembers.length > 0) {
                str += this.buildNameList(deadMembers);
                str += deadMembers.length > 1 ? " have " : " has ";
                str += "died of starvation!\n";
            }
            if (hungryMembers.length > 0) {
                str += this.buildNameList(hungryMembers);
                str += hungryMembers.length > 1 ? " are " : " is ";
                str += "in danger of starvation.\n";
            }
            return str;
        }
        return null;
    }
    buildNameList(names) {
        if (names.length === 1) {
            return names[0];
        }
        else if (names.length === 2) {
            return `${names[0]} and ${names[1]}`;
        }
        else if (names.length > 2) {
            let nameList = "";
            for (let i = 0; i < names.length; i++) {
                if (i === names.length - 1) {
                    nameList += "and ";
                }
                nameList += names[i];
                if (i !== names.length - 1) {
                    nameList += ", ";
                }
            }
            return nameList;
        }
        return "";
    }
    decreaseHealth(person, health) {
        person.decreaseHealth(health);
        if (person.getHealth().getCurrent() <= 0) {
            person.setDead(true);
            person.killForFood();
            const index = this.members.indexOf(person);
            if (index > -1) {
                this.members.splice(index, 1);
            }
        }
        return !person.isDead();
    }
    checkAnimalHunger() {
        let str = "";
        let hasMessage = false;
        for (const animal of this.animals) {
            const currentHealth = Math.floor(animal.getStatus().getCurrent());
            if (currentHealth === 0) {
                hasMessage = true;
                str += `${animal.getName()} has died of starvation!\n`;
            }
            else if (animal.getStatus().getCurrent() <
                getPaceSpeed(this.getPace()) - 75) {
                hasMessage = true;
                str += `${animal.getName()} is in danger of starvation.\n`;
            }
        }
        if (hasMessage) {
            return str;
        }
        return null;
    }
    addAnimals(animals) {
        const animalArray = Array.isArray(animals) ? animals : [animals];
        let failedAdd = true;
        for (const animal of animalArray) {
            if (this.animals.length < Party.MAX_ANIMALS) {
                this.animals.push(animal);
            }
            else {
                failedAdd = false;
            }
        }
        return failedAdd;
    }
    getTime() {
        return this.time;
    }
    damageVehicle(amount) {
        if (this.vehicle) {
            this.vehicle.decreaseStatus(amount);
        }
    }
    repairVehicle() {
        if (!this.vehicle) {
            return 0;
        }
        const restoreNeeded = Math.floor(this.vehicle.getCondition().getMax() -
            this.vehicle.getCondition().getCurrent());
        if (restoreNeeded === 0) {
            return 0;
        }
        let repairPossible = false;
        let donator = null;
        GetToolLoop: for (const person of this.members) {
            for (const itemType of person.getInventory().getPopulatedSlots()) {
                if (isItemTypeTool(itemType)) {
                    repairPossible = true;
                    donator = person;
                    break GetToolLoop;
                }
            }
        }
        if (!repairPossible && this.vehicle) {
            for (const itemType of this.vehicle.getInventory().getPopulatedSlots()) {
                if (isItemTypeTool(itemType)) {
                    repairPossible = true;
                    donator = this.vehicle;
                    break;
                }
            }
        }
        if (!repairPossible) {
            if (this.vehicle.getConditionPercentage() === 0) {
                this.vehicle = null;
            }
            return restoreNeeded;
        }
        if (!donator) {
            return restoreNeeded;
        }
        let firstTool = null;
        const typeList = donator.getInventory().getPopulatedSlots();
        for (const itemType of typeList) {
            if (isItemTypeTool(itemType)) {
                firstTool = itemType;
                break;
            }
        }
        if (!firstTool) {
            return restoreNeeded;
        }
        const toolList = donator.removeItemFromInventory(firstTool, 1);
        if (!toolList || toolList.length === 0) {
            return restoreNeeded;
        }
        const tool = toolList[0];
        const repairFactor = getItemTypeFactor(tool.getType());
        const toolToUse = Math.floor(restoreNeeded / repairFactor) + 1;
        if (tool.getStatus().getCurrent() > toolToUse) {
            this.vehicle.repair(toolToUse * repairFactor);
            tool.decreaseStatus(toolToUse);
            donator.addItemToInventory(tool);
            return 0;
        }
        else {
            const restoreAmount = Math.floor(tool.getStatus().getCurrent()) * repairFactor;
            this.vehicle.repair(restoreAmount);
            return this.repairVehicle();
        }
    }
    getNumberOfAnimals(animalType) {
        let count = 0;
        for (const animal of this.animals) {
            if (animal.getType() === animalType) {
                count++;
            }
        }
        return count;
    }
    toString() {
        return `Party: ${this.members.length} members, $${this.money}`;
    }
}
Party.MAX_ANIMALS = 6;
Party.MAX_SPEED = 5;
//# sourceMappingURL=Party.js.map