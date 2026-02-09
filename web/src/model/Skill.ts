export enum Skill {
  MEDICAL = "MEDICAL",
  RIVERWORK = "RIVERWORK",
  SHARPSHOOTING = "SHARPSHOOTING",
  BLACKSMITHING = "BLACKSMITHING",
  CARPENTRY = "CARPENTRY",
  FARMING = "FARMING",
  TRACKING = "TRACKING",
  BOTANY = "BOTANY",
  COMMERCE = "COMMERCE",
  COOKING = "COOKING",
  MUSICAL = "MUSICAL",
  SEWING = "SEWING",
  SPANISH = "SPANISH",
  NONE = "NONE",
}

const SKILL_DATA: Record<Skill, { cost: number; name: string }> = {
  [Skill.MEDICAL]: { cost: 50, name: "Medical" },
  [Skill.RIVERWORK]: { cost: 50, name: "Riverwork" },
  [Skill.SHARPSHOOTING]: { cost: 50, name: "Sharpshooting" },
  [Skill.BLACKSMITHING]: { cost: 40, name: "Blacksmithing" },
  [Skill.CARPENTRY]: { cost: 40, name: "Carpentry" },
  [Skill.FARMING]: { cost: 40, name: "Farming" },
  [Skill.TRACKING]: { cost: 30, name: "Tracking" },
  [Skill.BOTANY]: { cost: 20, name: "Botany" },
  [Skill.COMMERCE]: { cost: 20, name: "Commerce" },
  [Skill.COOKING]: { cost: 20, name: "Cooking" },
  [Skill.MUSICAL]: { cost: 10, name: "Musical" },
  [Skill.SEWING]: { cost: 10, name: "Sewing" },
  [Skill.SPANISH]: { cost: 10, name: "Spanish" },
  [Skill.NONE]: { cost: 0, name: "" },
};

export function getSkillCost(skill: Skill): number {
  return SKILL_DATA[skill].cost;
}

export function getSkillName(skill: Skill): string {
  return SKILL_DATA[skill].name;
}
