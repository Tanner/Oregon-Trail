export var Skill;
(function (Skill) {
    Skill["MEDICAL"] = "MEDICAL";
    Skill["RIVERWORK"] = "RIVERWORK";
    Skill["SHARPSHOOTING"] = "SHARPSHOOTING";
    Skill["BLACKSMITHING"] = "BLACKSMITHING";
    Skill["CARPENTRY"] = "CARPENTRY";
    Skill["FARMING"] = "FARMING";
    Skill["TRACKING"] = "TRACKING";
    Skill["BOTANY"] = "BOTANY";
    Skill["COMMERCE"] = "COMMERCE";
    Skill["COOKING"] = "COOKING";
    Skill["MUSICAL"] = "MUSICAL";
    Skill["SEWING"] = "SEWING";
    Skill["SPANISH"] = "SPANISH";
    Skill["NONE"] = "NONE";
})(Skill || (Skill = {}));
const SKILL_DATA = {
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
export function getSkillCost(skill) {
    return SKILL_DATA[skill].cost;
}
export function getSkillName(skill) {
    return SKILL_DATA[skill].name;
}
//# sourceMappingURL=Skill.js.map