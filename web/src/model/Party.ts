import { Person } from "./Person";
import { Animal } from "./Animal";
import { Vehicle } from "./Vehicle";
import { Item } from "./Item";
import { ItemType, isItemTypeFood, isItemTypePlant, isItemTypeAnimal, isItemTypeTool, getItemTypeFactor } from "./ItemType";
import { Notification } from "./Notification";
import { Skill } from "./Skill";
import { Time } from "./Time";
import { LocationNode } from "./LocationNode";
import { TrailEdge } from "./TrailEdge";
import { getProfessionMoney } from "./Profession";

export enum Pace {
  STEADY = "STEADY",
  STRENUOUS = "STRENUOUS",
  GRUELING = "GRUELING",
}

const paceData: Record<Pace, { name: string; speed: number }> = {
  [Pace.STEADY]: { name: "Steady", speed: 30 },
  [Pace.STRENUOUS]: { name: "Strenuous", speed: 55 },
  [Pace.GRUELING]: { name: "Grueling", speed: 80 },
};

export function getPaceName(pace: Pace): string {
  return paceData[pace].name;
}

export function getPaceSpeed(pace: Pace): number {
  return paceData[pace].speed;
}

export enum Rations {
  FILLING = "FILLING",
  MEAGER = "MEAGER",
  BAREBONES = "BAREBONES",
}

const rationsData: Record<Rations, { name: string; rationAmount: number }> = {
  [Rations.FILLING]: { name: "Filling", rationAmount: 100 },
  [Rations.MEAGER]: { name: "Meager", rationAmount: 75 },
  [Rations.BAREBONES]: { name: "Barebones", rationAmount: 50 },
};

export function getRationsName(rations: Rations): string {
  return rationsData[rations].name;
}

export function getRationsAmount(rations: Rations): number {
  return rationsData[rations].rationAmount;
}

interface Inventoried {
  getInventory(): any;
  addItemsToInventory(items: Item[]): void;
  addItemToInventory(item: Item): void;
  removeItemFromInventory(itemType: ItemType, quantity: number): Item[] | null;
  canGetItem(itemType: ItemType, numberOf: number): boolean;
}

export class Party {
  private static readonly MAX_ANIMALS = 6;
  private static readonly MAX_SPEED = 5;
  private readonly MAX_MEMBERS = 4;

  private partyLeader: Person;
  private members: Person[];
  private money: number;
  private currentPace: Pace;
  private currentRations: Rations;
  private vehicle: Vehicle | null;
  private trail: TrailEdge | null;
  private location: LocationNode | null;
  private animals: Animal[];
  private totalDistanceTravelled: number;
  private time: Time;

  constructor(
    currentPace: Pace,
    currentRations: Rations,
    partyLeader: Person,
    party: Person[],
    time: Time,
  ) {
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

    const partyCreationLog: string[] = [];
    partyCreationLog.push(`${this.members.length} members were created successfully: `);

    for (const person of party) {
      const professionMoney = person.getProfession()
        ? getProfessionMoney(person.getProfession()!)
        : 0;
      this.money += professionMoney;
      console.info(
        `${person === partyLeader ? "Party leader " : ""}${person.getName()} as a ${person.getProfession()} brings $${professionMoney} to the party.`,
      );
      partyCreationLog.push(`${person.getName()} `);
    }

    console.info(partyCreationLog.join(""));
    console.info(`Party starting money is: $${this.money}`);
    console.info(
      `Current pace is: ${getPaceName(this.currentPace)} and current rations is: ${getRationsName(this.currentRations)}`,
    );
  }

  buyItemForInventory(items: Item[], buyer: Inventoried): void {
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
      } else {
        buyer.addItemsToInventory(items);
      }
      this.money -= cost;
    }
  }

  canGetItem(itemType: ItemType, numberOf: number): Inventoried[] {
    const ableList: Inventoried[] = [];

    if (
      this.animals.length + numberOf > Party.MAX_ANIMALS
    ) {
      return ableList;
    }

    for (const person of this.members) {
      if (person.canGetItem(itemType, numberOf)) {
        ableList.push(person as unknown as Inventoried);
      }
    }

    if (this.vehicle && this.vehicle.canGetItem(itemType, numberOf)) {
      ableList.push(this.vehicle as unknown as Inventoried);
    }

    return ableList;
  }

  getPartyMembers(): Person[] {
    return this.members;
  }

  addPartyMember(person: Person): void {
    if (this.members.length < this.MAX_MEMBERS) {
      this.members.push(person);
    }
  }

  getSkills(): Skill[] {
    const skillList: Skill[] = [];

    for (const person of this.members) {
      for (const skill of person.getSkills()) {
        if (!skillList.includes(skill) && skill !== Skill.NONE) {
          skillList.push(skill);
        }
      }
    }

    return skillList;
  }

  getMoney(): number {
    return this.money;
  }

  setMoney(money: number): void {
    this.money = money;
  }

  getPace(): Pace {
    return this.currentPace;
  }

  setPace(pace: Pace): void {
    this.currentPace = pace;
  }

  getRations(): Rations {
    return this.currentRations;
  }

  setRations(rations: Rations): void {
    this.currentRations = rations;
  }

  getVehicle(): Vehicle | null {
    return this.vehicle;
  }

  setVehicle(vehicle: Vehicle): void {
    this.vehicle = vehicle;
  }

  getTrail(): TrailEdge | null {
    return this.trail;
  }

  setTrail(trail: TrailEdge): void {
    this.trail = trail;
  }

  getLocation(): LocationNode | null {
    return this.location;
  }

  setLocation(location: LocationNode): void {
    this.location = location;
  }

  walk(): Notification[] {
    const messages: Notification[] = [];
    const movement = (getPaceSpeed(this.getPace()) * this.getMoveModifier()) / 30;

    if (this.trail) {
      this.trail.advance(movement);
    }
    this.totalDistanceTravelled += movement;

    const slaughterHouse: Animal[] = [];
    for (const animal of this.animals) {
      this.grazeAnimals();
      if (animal.getStatus().getCurrent() === 0) {
        if (this.vehicle) {
          this.vehicle.addItemsToInventory(animal.killForFood());
        }
        slaughterHouse.push(animal);
      }
    }

    const deathList: Person[] = [];
    let finalResult = 0;

    let eatFoodRemnant: number;
    this.partyLeader.increaseSkillPoints(Math.floor(getPaceSpeed(this.getPace()) / 10));

    if (!this.personHasFood(this.partyLeader) && !this.vehicleHasFood()) {
      this.partyLeader.decreaseHealth(getPaceSpeed(this.getPace()));
      eatFoodRemnant = getRationsAmount(this.getRations());
    } else {
      eatFoodRemnant = this.eatFood(
        this.partyLeader,
        getRationsAmount(this.getRations()),
      );
      finalResult =
        getRationsAmount(this.getRations()) -
        eatFoodRemnant -
        getPaceSpeed(this.getPace());
      if (finalResult > 0) {
        this.partyLeader.increaseHealth(finalResult);
      } else {
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
        } else {
          finalResult =
            getRationsAmount(this.getRations()) -
            this.eatFood(person, getRationsAmount(this.getRations())) -
            getPaceSpeed(this.getPace());
          if (finalResult > 0) {
            person.increaseHealth(finalResult);
          } else {
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

    this.partyLeader.increaseHealth(
      eatFoodRemnant - this.eatFood(this.partyLeader, eatFoodRemnant),
    );

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

    messages.push(
      new Notification(
        `Current Distance Travelled: ${this.getTotalDistanceTravelled().toLocaleString()}`,
        false,
      ),
    );

    return messages;
  }

  rest(): Notification[] {
    const messages: Notification[] = [];

    const deathList: Person[] = [];
    let finalResult = 0;

    let eatFoodRemnant: number;
    this.partyLeader.increaseSkillPoints(Math.floor(getPaceSpeed(this.getPace()) / 10));

    if (!this.personHasFood(this.partyLeader) && !this.vehicleHasFood()) {
      this.partyLeader.decreaseHealth(5);
      eatFoodRemnant = getRationsAmount(this.getRations());
    } else {
      eatFoodRemnant = this.eatFood(
        this.partyLeader,
        getRationsAmount(this.getRations()),
      );
      finalResult =
        getRationsAmount(this.getRations()) -
        eatFoodRemnant -
        getPaceSpeed(this.getPace());
      if (finalResult > 0) {
        this.partyLeader.increaseHealth(finalResult);
      } else {
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
        } else {
          finalResult =
            getRationsAmount(this.getRations()) -
            this.eatFood(person, getRationsAmount(this.getRations())) -
            5;
          if (finalResult > 0) {
            person.increaseHealth(finalResult);
          } else {
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
        this.partyLeader.increaseHealth(
          eatFoodRemnant - this.eatFood(this.partyLeader, eatFoodRemnant),
        );
      }
      if (person.getHealth().getCurrent() === 0) {
        person.setDead(true);
        const index = this.members.indexOf(person);
        if (index > -1) {
          this.members.splice(index, 1);
        }
      }
    }

    messages.push(
      new Notification(
        `Current Distance Travelled: ${this.getTotalDistanceTravelled().toLocaleString()}`,
        false,
      ),
    );

    return messages;
  }

  private grazeAnimals(): void {
    for (const animal of this.animals) {
      const amount = this.getAnimalStrain(animal);
      if (amount < 0) {
        animal.decreaseStatus(-amount);
      } else {
        animal.increaseStatus(amount);
      }
    }
  }

  private getAnimalStrain(animal: Animal): number {
    if (!this.vehicle) {
      return 10;
    }

    if (this.getPace() === Pace.GRUELING) {
      return -Math.floor(
        animal.getMoveFactor() *
          5 *
          (this.vehicle.getWeight() / this.vehicle.getMaxWeight()),
      );
    } else if (this.getPace() === Pace.STRENUOUS) {
      return (
        -Math.floor(
          animal.getMoveFactor() *
            5 *
            (this.vehicle.getWeight() / this.vehicle.getMaxWeight()),
        ) / 2
      );
    }
    return 10;
  }

  getTotalDistanceTravelled(): number {
    return Math.floor(this.totalDistanceTravelled);
  }

  private getMoveModifier(): number {
    let moveModifier = 1;
    for (const animal of this.getAnimals()) {
      moveModifier += animal.getMoveFactor();
    }

    const result = moveModifier / 10;
    if (result > Party.MAX_SPEED) {
      return Party.MAX_SPEED;
    } else if (result < 1) {
      return 1;
    }
    return result;
  }

  getAnimals(): Animal[] {
    return this.animals;
  }

  getNumberOfAnimal(type: ItemType): number {
    let numberOf = 0;
    for (const animal of this.animals) {
      if (animal.getType() === type) {
        numberOf++;
      }
    }
    return numberOf;
  }

  getAnimalsAsString(): string {
    let str = "";
    let isFirst = true;
    for (const animal of this.animals) {
      if (!isFirst) {
        str += ", ";
      } else {
        isFirst = false;
      }
      str += animal.toString();
    }
    return str;
  }

  private eatFood(person: Person, amount: number): number {
    const restoreNeeded = getRationsAmount(this.getRations());
    if (restoreNeeded === 0) {
      return 0;
    }

    if (!this.personHasFood(person) && !this.vehicleHasFood()) {
      return amount;
    }

    let donator: Inventoried | null = null;
    if (this.personHasFood(person)) {
      donator = person as unknown as Inventoried;
    } else if (!this.personHasFood(person) && this.vehicleHasFood()) {
      donator = this.vehicle as unknown as Inventoried;
    }

    if (!donator) {
      return amount;
    }

    let firstFood: ItemType | null = null;
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
    } else {
      const restoreAmount = Math.floor(food.getStatus().getCurrent()) * foodFactor;
      return this.eatFood(person, restoreNeeded - restoreAmount);
    }
  }

  private vehicleHasFood(): boolean {
    if (this.vehicle) {
      for (const itemType of this.vehicle.getInventory().getPopulatedSlots()) {
        if (isItemTypeFood(itemType)) {
          return true;
        }
      }
    }
    return false;
  }

  private personHasFood(person: Person): boolean {
    for (const itemType of person.getInventory().getPopulatedSlots()) {
      if (isItemTypeFood(itemType)) {
        return true;
      }
    }
    return false;
  }

  checkHungerStatus(): string | null {
    const deadMembers: string[] = [];
    const hungryMembers: string[] = [];
    let hasMessage = false;

    const people = [...this.members];

    for (const person of people) {
      const currentHealth = Math.floor(person.getHealth().getCurrent());
      if (currentHealth === 0) {
        hasMessage = true;
        deadMembers.push(person.getName());
      } else if (
        person.getHealth().getCurrent() <
        getPaceSpeed(this.getPace()) -
          (this.personHasFood(person) || this.vehicleHasFood()
            ? getRationsAmount(this.getRations())
            : 0)
      ) {
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

  private buildNameList(names: string[]): string {
    if (names.length === 1) {
      return names[0];
    } else if (names.length === 2) {
      return `${names[0]} and ${names[1]}`;
    } else if (names.length > 2) {
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

  decreaseHealth(person: Person, health: number): boolean {
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

  private checkAnimalHunger(): string | null {
    let str = "";
    let hasMessage = false;

    for (const animal of this.animals) {
      const currentHealth = Math.floor(animal.getStatus().getCurrent());
      if (currentHealth === 0) {
        hasMessage = true;
        str += `${animal.getName()} has died of starvation!\n`;
      } else if (
        animal.getStatus().getCurrent() <
        getPaceSpeed(this.getPace()) - 75
      ) {
        hasMessage = true;
        str += `${animal.getName()} is in danger of starvation.\n`;
      }
    }

    if (hasMessage) {
      return str;
    }
    return null;
  }

  addAnimals(animals: Animal[] | Animal): boolean {
    const animalArray = Array.isArray(animals) ? animals : [animals];
    let failedAdd = true;

    for (const animal of animalArray) {
      if (this.animals.length < Party.MAX_ANIMALS) {
        this.animals.push(animal);
      } else {
        failedAdd = false;
      }
    }
    return failedAdd;
  }

  getTime(): Time {
    return this.time;
  }

  damageVehicle(amount: number): void {
    if (this.vehicle) {
      this.vehicle.decreaseStatus(amount);
    }
  }

  repairVehicle(): number {
    if (!this.vehicle) {
      return 0;
    }

    const restoreNeeded = Math.floor(
      this.vehicle.getCondition().getMax() -
        this.vehicle.getCondition().getCurrent(),
    );
    if (restoreNeeded === 0) {
      return 0;
    }

    let repairPossible = false;
    let donator: Inventoried | null = null;

    GetToolLoop: for (const person of this.members) {
      for (const itemType of person.getInventory().getPopulatedSlots()) {
        if (isItemTypeTool(itemType)) {
          repairPossible = true;
          donator = person as unknown as Inventoried;
          break GetToolLoop;
        }
      }
    }

    if (!repairPossible && this.vehicle) {
      for (const itemType of this.vehicle.getInventory().getPopulatedSlots()) {
        if (isItemTypeTool(itemType)) {
          repairPossible = true;
          donator = this.vehicle as unknown as Inventoried;
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

    let firstTool: ItemType | null = null;
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
    } else {
      const restoreAmount =
        Math.floor(tool.getStatus().getCurrent()) * repairFactor;
      this.vehicle.repair(restoreAmount);
      return this.repairVehicle();
    }
  }

  getNumberOfAnimals(animalType: ItemType): number {
    let count = 0;
    for (const animal of this.animals) {
      if (animal.getType() === animalType) {
        count++;
      }
    }
    return count;
  }

  toString(): string {
    return `Party: ${this.members.length} members, $${this.money}`;
  }
}
