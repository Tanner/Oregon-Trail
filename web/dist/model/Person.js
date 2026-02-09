import { Condition } from "./Condition";
import { Inventory } from "./Inventory";
import { Item } from "./Item";
import { ItemType } from "./ItemType";
import { Profession, getProfessionStartingSkill, getProfessionStartingItem, getProfessionName, } from "./Profession";
import { Skill, getSkillCost, getSkillName } from "./Skill";
export class Person {
    constructor(name) {
        this.name = name;
        this.skillPoints = new Condition(0, Person.BASE_SKILL_POINTS, 0);
        this.health = new Condition(100);
        this.dead = false;
        this.isMale = true;
        this.isLeader = false;
        this.skills = [];
        this.profession = null;
        this.inventory = new Inventory(Person.MAX_INVENTORY_SIZE, Person.MAX_INVENTORY_WEIGHT);
        this.weight = Math.floor(Math.random() * 100) + 90;
        console.debug(`${name} was created`);
    }
    setProfession(profession) {
        if (this.profession === null) {
            this.profession = profession;
            console.debug(`${this.name} became a ${getProfessionName(profession)}`);
            this.addSkill(getProfessionStartingSkill(profession));
            this.inventory.clear();
            const startingItem = getProfessionStartingItem(profession);
            if (startingItem) {
                this.addItemToInventory(new Item(startingItem));
            }
            return;
        }
        if (this.profession === profession) {
            console.debug(`${this.name} is already a ${getProfessionName(profession)}`);
            return;
        }
        this.skills.length = 0;
        console.debug(`${this.name} stopped being a ${getProfessionName(this.profession)} and lost all current skills`);
        this.profession = null;
        this.setProfession(profession);
        this.inventory.clear();
        const startingItem = getProfessionStartingItem(profession);
        if (startingItem) {
            this.addItemToInventory(new Item(startingItem));
        }
    }
    getSkillPoints() {
        return Math.floor(this.skillPoints.getCurrent());
    }
    clearSkills() {
        for (let i = this.skills.length - 1; i >= 0; i--) {
            const skill = this.skills[i];
            if (this.profession &&
                skill !== getProfessionStartingSkill(this.profession)) {
                this.skills.splice(i, 1);
            }
        }
    }
    addSkill(newSkill) {
        if (this.skills.includes(newSkill)) {
            console.debug(`${this.name} already has the skill ${getSkillName(newSkill)}`);
            return;
        }
        if (this.profession && newSkill === getProfessionStartingSkill(this.profession)) {
            this.skills.push(newSkill);
            console.debug(`As a ${getProfessionName(this.profession)} ${this.name} gains the skill ${getSkillName(newSkill)}`);
            return;
        }
        this.skills.push(newSkill);
        console.debug(`${this.name} gained the skill ${getSkillName(newSkill)}`);
    }
    buySkill(newSkill) {
        if (this.skills.includes(newSkill)) {
            console.debug(`${this.name} already has the skill ${getSkillName(newSkill)}`);
            return;
        }
        const cost = getSkillCost(newSkill);
        if (this.skillPoints.getCurrent() < cost) {
            console.debug(`${this.name} does not have enough skill points to obtain ${getSkillName(newSkill)}. Current skill points: ${this.skillPoints.getCurrent()} Cost of new skill: ${cost}`);
            return;
        }
        this.skillPoints.decrease(cost);
        this.skills.push(newSkill);
        console.debug(`${this.name} gained the skill ${getSkillName(newSkill)}`);
        console.debug(`${this.name} has ${this.skillPoints.getCurrent()} skill points remaining.`);
    }
    removeSkill(oldSkill) {
        if (!this.skills.includes(oldSkill)) {
            console.debug("Cannot remove a skill that isn't already known.");
            return;
        }
        if (this.profession &&
            getProfessionStartingSkill(this.profession) === oldSkill) {
            console.debug("Cannot remove profession's starting skill");
            return;
        }
        const index = this.skills.indexOf(oldSkill);
        if (index > -1) {
            this.skills.splice(index, 1);
        }
        this.skillPoints.increase(getSkillCost(oldSkill));
    }
    getSkills() {
        return this.skills;
    }
    getName() {
        return this.name;
    }
    setName(name) {
        this.name = name;
        console.debug(`Name changed to ${name}`);
    }
    getProfession() {
        return this.profession;
    }
    toString() {
        let str = `Name: ${this.name}`;
        if (this.profession !== null) {
            str += `, Profession: ${getProfessionName(this.profession)}`;
        }
        for (const skill of this.skills) {
            str += `, Skill: ${getSkillName(skill)}`;
        }
        str += this.isMale ? ", Gender: Male" : ", Gender: Female";
        return str;
    }
    setIsMale(isMale) {
        this.isMale = isMale;
        console.debug(`Gender changed to ${isMale ? "Male" : "Female"}`);
    }
    getIsMale() {
        return this.isMale;
    }
    getInventory() {
        return this.inventory;
    }
    addItemsToInventory(items) {
        this.inventory.addItemsToInventory(items);
    }
    addItemToInventory(item) {
        this.inventory.addItemToInventory(item);
    }
    removeItemFromInventory(itemType, quantity) {
        return this.inventory.removeItemFromInventory(itemType, quantity);
    }
    getHealth() {
        return this.health;
    }
    decreaseHealth(amount) {
        this.health.decrease(amount);
    }
    increaseHealth(amount) {
        this.health.increase(amount);
    }
    getConditionPercentage() {
        return this.health.getPercentage();
    }
    canGetItem(itemType, numberOf) {
        return this.inventory.canGetItems(itemType, numberOf);
    }
    getMaxSize() {
        return Person.MAX_INVENTORY_SIZE;
    }
    getMaxWeight() {
        return Person.MAX_INVENTORY_WEIGHT;
    }
    getIsLeader() {
        return this.isLeader;
    }
    setLeader(isLeader) {
        this.isLeader = isLeader;
    }
    getWeight() {
        return this.inventory.getWeight();
    }
    increaseSkillPoints(amount) {
        this.skillPoints.increase(amount);
    }
    killForFood() {
        const numberOf = Math.floor(this.weight / 5);
        const itemList = [];
        for (let i = 0; i < Math.floor(numberOf / 2); i++) {
            itemList.push(new Item(ItemType.STRANGEMEAT));
        }
        return itemList;
    }
    getCondition() {
        return this.health;
    }
    isDead() {
        return this.dead;
    }
    setDead(dead) {
        this.dead = dead;
    }
    makeRandom() {
        const professions = Object.values(Profession);
        this.setProfession(professions[Math.floor(Math.random() * professions.length)]);
        const personSkill = [];
        let skillPoints = 0;
        const allSkills = Object.values(Skill);
        let tempSkill = allSkills[Math.floor(Math.random() * allSkills.length)];
        while (tempSkill !== Skill.NONE &&
            personSkill.length < 3 &&
            skillPoints + getSkillCost(tempSkill) < 120) {
            if (!personSkill.includes(tempSkill)) {
                personSkill.push(tempSkill);
                skillPoints += getSkillCost(tempSkill);
            }
            tempSkill = allSkills[Math.floor(Math.random() * allSkills.length)];
        }
        for (const skill of personSkill) {
            this.addSkill(skill);
        }
        this.inventory.addRandomItems();
    }
    getSkillsAsString() {
        let str = "";
        let isFirst = true;
        for (const skill of this.skills) {
            if (skill !== Skill.NONE) {
                if (!isFirst) {
                    str += ", ";
                }
                str += getSkillName(skill);
                if (isFirst) {
                    isFirst = false;
                }
            }
        }
        return str;
    }
}
Person.BASE_SKILL_POINTS = 70;
Person.MAX_INVENTORY_SIZE = 5;
Person.MAX_INVENTORY_WEIGHT = 100;
//# sourceMappingURL=Person.js.map