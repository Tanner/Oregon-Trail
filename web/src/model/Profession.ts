import { Skill } from "./Skill";

export enum Profession {
  BANKER = "BANKER",
  DOCTOR = "DOCTOR",
  MERCHANT = "MERCHANT",
  PHARMACIST = "PHARMACIST",
  WAINWRIGHT = "WAINWRIGHT",
  GUNSMITH = "GUNSMITH",
  BLACKSMITH = "BLACKSMITH",
  MASON = "MASON",
  WHEELWRIGHT = "WHEELWRIGHT",
  CARPENTER = "CARPENTER",
  SADDLEMAKER = "SADDLEMAKER",
  BRICKMAKER = "BRICKMAKER",
  PROSPECTOR = "PROSPECTOR",
  TRAPPER = "TRAPPER",
  SURVEYOR = "SURVEYOR",
  SHOEMAKER = "SHOEMAKER",
  JOURNALIST = "JOURNALIST",
  PRINTER = "PRINTER",
  BUTCHER = "BUTCHER",
  BAKER = "BAKER",
  TAILOR = "TAILOR",
  FARMER = "FARMER",
  PASTOR = "PASTOR",
  ARTIST = "ARTIST",
  TEACHER = "TEACHER",
}

const BASE_MONEY = 1600;

interface ProfessionData {
  moneyDivider: number;
  startingSkill: Skill;
  name: string;
  startingItem: string | null;
}

const PROFESSION_DATA: Record<Profession, ProfessionData> = {
  [Profession.BANKER]: { moneyDivider: 1, startingSkill: Skill.COMMERCE, name: "Banker", startingItem: null },
  [Profession.DOCTOR]: { moneyDivider: 1.2, startingSkill: Skill.MEDICAL, name: "Doctor", startingItem: null },
  [Profession.MERCHANT]: { moneyDivider: 1.4, startingSkill: Skill.COMMERCE, name: "Merchant", startingItem: null },
  [Profession.PHARMACIST]: { moneyDivider: 1.6, startingSkill: Skill.MEDICAL, name: "Pharmacist", startingItem: null },
  [Profession.WAINWRIGHT]: { moneyDivider: 1.8, startingSkill: Skill.CARPENTRY, name: "Wainwright", startingItem: "WHEEL" },
  [Profession.GUNSMITH]: { moneyDivider: 2, startingSkill: Skill.SHARPSHOOTING, name: "Gunsmith", startingItem: "GUN" },
  [Profession.BLACKSMITH]: { moneyDivider: 2.2, startingSkill: Skill.BLACKSMITHING, name: "Blacksmith", startingItem: null },
  [Profession.MASON]: { moneyDivider: 2.4, startingSkill: Skill.CARPENTRY, name: "Mason", startingItem: null },
  [Profession.WHEELWRIGHT]: { moneyDivider: 2.6, startingSkill: Skill.CARPENTRY, name: "Wheelwright", startingItem: "WHEEL" },
  [Profession.CARPENTER]: { moneyDivider: 2.8, startingSkill: Skill.CARPENTRY, name: "Carpenter", startingItem: "WHEEL" },
  [Profession.SADDLEMAKER]: { moneyDivider: 3, startingSkill: Skill.FARMING, name: "Saddlemaker", startingItem: null },
  [Profession.BRICKMAKER]: { moneyDivider: 3.2, startingSkill: Skill.CARPENTRY, name: "Brickmaker", startingItem: null },
  [Profession.PROSPECTOR]: { moneyDivider: 3.4, startingSkill: Skill.RIVERWORK, name: "Prospector", startingItem: null },
  [Profession.TRAPPER]: { moneyDivider: 3.6, startingSkill: Skill.TRACKING, name: "Trapper", startingItem: null },
  [Profession.SURVEYOR]: { moneyDivider: 3.8, startingSkill: Skill.RIVERWORK, name: "Surveyor", startingItem: null },
  [Profession.SHOEMAKER]: { moneyDivider: 4, startingSkill: Skill.SEWING, name: "Shoemaker", startingItem: null },
  [Profession.JOURNALIST]: { moneyDivider: 4.1, startingSkill: Skill.NONE, name: "Journalist", startingItem: null },
  [Profession.PRINTER]: { moneyDivider: 4.2, startingSkill: Skill.COMMERCE, name: "Printer", startingItem: null },
  [Profession.BUTCHER]: { moneyDivider: 4.3, startingSkill: Skill.COOKING, name: "Butcher", startingItem: "MEAT" },
  [Profession.BAKER]: { moneyDivider: 4.4, startingSkill: Skill.COOKING, name: "Baker", startingItem: "BREAD" },
  [Profession.TAILOR]: { moneyDivider: 4.5, startingSkill: Skill.SEWING, name: "Tailor", startingItem: null },
  [Profession.FARMER]: { moneyDivider: 4.5, startingSkill: Skill.FARMING, name: "Farmer", startingItem: "APPLE" },
  [Profession.PASTOR]: { moneyDivider: 4.6, startingSkill: Skill.NONE, name: "Pastor", startingItem: null },
  [Profession.ARTIST]: { moneyDivider: 4.8, startingSkill: Skill.MUSICAL, name: "Artist", startingItem: null },
  [Profession.TEACHER]: { moneyDivider: 5, startingSkill: Skill.NONE, name: "Teacher", startingItem: null },
};

export function getProfessionMoney(profession: Profession): number {
  return Math.floor(BASE_MONEY / PROFESSION_DATA[profession].moneyDivider);
}

export function getProfessionStartingSkill(profession: Profession): Skill {
  return PROFESSION_DATA[profession].startingSkill;
}

export function getProfessionName(profession: Profession): string {
  return PROFESSION_DATA[profession].name;
}

export function getProfessionStartingItem(profession: Profession): string | null {
  return PROFESSION_DATA[profession].startingItem;
}
