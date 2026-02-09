import { Condition } from "./Condition";
import { Inventory } from "./Inventory";
import { Item } from "./Item";
import { ItemType } from "./ItemType";
import {
  Profession,
  getProfessionStartingSkill,
  getProfessionStartingItem,
  getProfessionName,
} from "./Profession";
import { Skill, getSkillCost, getSkillName } from "./Skill";

export class Person {
  private readonly skillPoints: Condition;
  private readonly health: Condition;
  private dead: boolean;
  private isMale: boolean;
  private isLeader: boolean;
  private readonly skills: Skill[];
  private name: string;
  private profession: Profession | null;
  private readonly inventory: Inventory;
  private weight: number;

  static readonly BASE_SKILL_POINTS = 70;
  static readonly MAX_INVENTORY_SIZE = 5;
  static readonly MAX_INVENTORY_WEIGHT = 100;

  constructor(name: string) {
    this.name = name;
    this.skillPoints = new Condition(0, Person.BASE_SKILL_POINTS, 0);
    this.health = new Condition(100);
    this.dead = false;
    this.isMale = true;
    this.isLeader = false;
    this.skills = [];
    this.profession = null;
    this.inventory = new Inventory(
      Person.MAX_INVENTORY_SIZE,
      Person.MAX_INVENTORY_WEIGHT,
    );
    this.weight = Math.floor(Math.random() * 100) + 90;
    console.debug(`${name} was created`);
  }

  setProfession(profession: Profession): void {
    if (this.profession === null) {
      this.profession = profession;
      console.debug(`${this.name} became a ${getProfessionName(profession)}`);
      this.addSkill(getProfessionStartingSkill(profession));
      this.inventory.clear();
      const startingItem = getProfessionStartingItem(profession);
      if (startingItem) {
        this.addItemToInventory(new Item(startingItem as unknown as ItemType));
      }
      return;
    }

    if (this.profession === profession) {
      console.debug(
        `${this.name} is already a ${getProfessionName(profession)}`,
      );
      return;
    }

    this.skills.length = 0;
    console.debug(
      `${this.name} stopped being a ${getProfessionName(this.profession)} and lost all current skills`,
    );
    this.profession = null;
    this.setProfession(profession);
    this.inventory.clear();
    const startingItem = getProfessionStartingItem(profession);
    if (startingItem) {
      this.addItemToInventory(new Item(startingItem as unknown as ItemType));
    }
  }

  getSkillPoints(): number {
    return Math.floor(this.skillPoints.getCurrent());
  }

  clearSkills(): void {
    for (let i = this.skills.length - 1; i >= 0; i--) {
      const skill = this.skills[i];
      if (
        this.profession &&
        skill !== getProfessionStartingSkill(this.profession)
      ) {
        this.skills.splice(i, 1);
      }
    }
  }

  addSkill(newSkill: Skill): void {
    if (this.skills.includes(newSkill)) {
      console.debug(
        `${this.name} already has the skill ${getSkillName(newSkill)}`,
      );
      return;
    }

    if (this.profession && newSkill === getProfessionStartingSkill(this.profession)) {
      this.skills.push(newSkill);
      console.debug(
        `As a ${getProfessionName(this.profession)} ${this.name} gains the skill ${getSkillName(newSkill)}`,
      );
      return;
    }

    this.skills.push(newSkill);
    console.debug(`${this.name} gained the skill ${getSkillName(newSkill)}`);
  }

  buySkill(newSkill: Skill): void {
    if (this.skills.includes(newSkill)) {
      console.debug(
        `${this.name} already has the skill ${getSkillName(newSkill)}`,
      );
      return;
    }

    const cost = getSkillCost(newSkill);
    if (this.skillPoints.getCurrent() < cost) {
      console.debug(
        `${this.name} does not have enough skill points to obtain ${getSkillName(newSkill)}. Current skill points: ${this.skillPoints.getCurrent()} Cost of new skill: ${cost}`,
      );
      return;
    }

    this.skillPoints.decrease(cost);
    this.skills.push(newSkill);
    console.debug(`${this.name} gained the skill ${getSkillName(newSkill)}`);
    console.debug(
      `${this.name} has ${this.skillPoints.getCurrent()} skill points remaining.`,
    );
  }

  removeSkill(oldSkill: Skill): void {
    if (!this.skills.includes(oldSkill)) {
      console.debug("Cannot remove a skill that isn't already known.");
      return;
    }

    if (
      this.profession &&
      getProfessionStartingSkill(this.profession) === oldSkill
    ) {
      console.debug("Cannot remove profession's starting skill");
      return;
    }

    const index = this.skills.indexOf(oldSkill);
    if (index > -1) {
      this.skills.splice(index, 1);
    }
    this.skillPoints.increase(getSkillCost(oldSkill));
  }

  getSkills(): Skill[] {
    return this.skills;
  }

  getName(): string {
    return this.name;
  }

  setName(name: string): void {
    this.name = name;
    console.debug(`Name changed to ${name}`);
  }

  getProfession(): Profession | null {
    return this.profession;
  }

  toString(): string {
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

  setIsMale(isMale: boolean): void {
    this.isMale = isMale;
    console.debug(`Gender changed to ${isMale ? "Male" : "Female"}`);
  }

  getIsMale(): boolean {
    return this.isMale;
  }

  getInventory(): Inventory {
    return this.inventory;
  }

  addItemsToInventory(items: Item[]): void {
    this.inventory.addItemsToInventory(items);
  }

  addItemToInventory(item: Item): void {
    this.inventory.addItemToInventory(item);
  }

  removeItemFromInventory(itemType: ItemType, quantity: number): Item[] | null {
    return this.inventory.removeItemFromInventory(itemType, quantity);
  }

  getHealth(): Condition {
    return this.health;
  }

  decreaseHealth(amount: number): void {
    this.health.decrease(amount);
  }

  increaseHealth(amount: number): void {
    this.health.increase(amount);
  }

  getConditionPercentage(): number {
    return this.health.getPercentage();
  }

  canGetItem(itemType: ItemType, numberOf: number): boolean {
    return this.inventory.canGetItems(itemType, numberOf);
  }

  getMaxSize(): number {
    return Person.MAX_INVENTORY_SIZE;
  }

  getMaxWeight(): number {
    return Person.MAX_INVENTORY_WEIGHT;
  }

  getIsLeader(): boolean {
    return this.isLeader;
  }

  setLeader(isLeader: boolean): void {
    this.isLeader = isLeader;
  }

  getWeight(): number {
    return this.inventory.getWeight();
  }

  increaseSkillPoints(amount: number): void {
    this.skillPoints.increase(amount);
  }

  killForFood(): Item[] {
    const numberOf = Math.floor(this.weight / 5);
    const itemList: Item[] = [];
    for (let i = 0; i < Math.floor(numberOf / 2); i++) {
      itemList.push(new Item(ItemType.STRANGEMEAT));
    }
    return itemList;
  }

  getCondition(): Condition {
    return this.health;
  }

  isDead(): boolean {
    return this.dead;
  }

  setDead(dead: boolean): void {
    this.dead = dead;
  }

  makeRandom(): void {
    const professions = Object.values(Profession);
    this.setProfession(
      professions[Math.floor(Math.random() * professions.length)],
    );

    const personSkill: Skill[] = [];
    let skillPoints = 0;

    const allSkills = Object.values(Skill);
    let tempSkill = allSkills[Math.floor(Math.random() * allSkills.length)];
    while (
      tempSkill !== Skill.NONE &&
      personSkill.length < 3 &&
      skillPoints + getSkillCost(tempSkill) < 120
    ) {
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

  getSkillsAsString(): string {
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
