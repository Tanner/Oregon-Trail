import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Button } from '../component/Button';
import { Label, Alignment } from '../component/Label';
import { TextField, AcceptedCharacters } from '../component/TextField';
import { SegmentedControl } from '../component/SegmentedControl';
import { ComponentModal } from '../component/modal/ComponentModal';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { ImageStore } from '../core/ImageStore';
import { COLORS } from '../core/ConstantStore';
import { Person } from '../model/Person';
import { Profession, getProfessionName, getProfessionMoney, getProfessionStartingSkill } from '../model/Profession';
import { Skill, getSkillName } from '../model/Skill';
import { Party, Pace, Rations } from '../model/Party';
import { Time } from '../model/Time';

interface PersonData {
  person: Person | null;
  name: string;
  gender: 'Male' | 'Female' | null;
  profession: Profession | null;
  skills: Skill[];
}

export class PartyCreationScene extends Scene {
  private static readonly PADDING = 20;
  private static readonly INNER_PADDING = 10;
  private static readonly NUM_PEOPLE = 4;
  private static readonly NUM_SKILLS = 3;
  private static readonly NEW_PERSON_BUTTON_HEIGHT = 100;
  private static readonly REGULAR_BUTTON_HEIGHT = 30;

  private canvasWidth: number;
  private canvasHeight: number;
  private buttonWidth: number;

  private peopleData: PersonData[] = [];

  private newPersonButtons: Button[] = [];
  private personNameTextFields: TextField[] = [];
  private personGenderControls: SegmentedControl[] = [];
  private personProfessionPanels: Panel[] = [];
  private personChangeProfessionButtons: Button[] = [];
  private personProfessionLabels: Label[] = [];
  private personMoneyLabels: Label[] = [];
  private personSkillPanels: Panel[] = [];
  private personChangeSkillButtons: Button[] = [];
  private personSkillLabels: Label[][] = [];

  private rationsSegmentedControl!: SegmentedControl;
  private paceSegmentedControl!: SegmentedControl;
  private confirmButton!: Button;

  private professionSegmentedControl!: SegmentedControl;
  private skillSegmentedControl!: SegmentedControl;

  private currentPersonModifying: number = -1;

  constructor(canvasWidth: number, canvasHeight: number) {
    super();
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;
    this.buttonWidth = Math.floor((canvasWidth - PartyCreationScene.PADDING * (PartyCreationScene.NUM_PEOPLE + 1)) / PartyCreationScene.NUM_PEOPLE);

    for (let i = 0; i < PartyCreationScene.NUM_PEOPLE; i++) {
      this.peopleData.push({
        person: null,
        name: '',
        gender: null,
        profession: null,
        skills: []
      });
    }

    this.init();
  }

  private init(): void {
    const dirtBackground = ImageStore.getImage('DIRT_BACKGROUND');
    const backgroundPanel = new Panel(this.canvasWidth, this.canvasHeight, dirtBackground);
    backgroundPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
    this.backgroundLayer.add(backgroundPanel);

    const fieldFont = FontStore.getFont(FontID.FIELD);

    this.createProfessionControl();
    this.createSkillControl();

    for (let i = 0; i < PartyCreationScene.NUM_PEOPLE; i++) {
      this.createPersonColumn(i, fieldFont);
    }

    this.createBottomControls(fieldFont);
  }

  private createProfessionControl(): void {
    const professions = Object.values(Profession);
    const professionLabels = professions.map(p => getProfessionName(p));
    const professionTooltips = professions.map(p => {
      const money = getProfessionMoney(p);
      const skill = getProfessionStartingSkill(p);
      return `$${money.toLocaleString()}\n${getSkillName(skill)}`;
    });

    this.professionSegmentedControl = new SegmentedControl(
      800, 200, 5, 5, 5, true, 1, ...professionLabels
    );
    this.professionSegmentedControl.setTooltips(professionTooltips);
  }

  private createSkillControl(): void {
    const skills = Object.values(Skill).filter(s => s !== Skill.NONE) as Exclude<Skill, Skill.NONE>[];
    const skillLabels = skills.map(s => getSkillName(s));

    this.skillSegmentedControl = new SegmentedControl(
      800, 200, 5, 3, 5, true, PartyCreationScene.NUM_SKILLS, ...skillLabels
    );
  }

  private createPersonColumn(index: number, font: any): void {
    const prevButton = index === 0 ? null : this.newPersonButtons[index - 1];

    const newPersonButton = new Button(
      this.buttonWidth,
      PartyCreationScene.NEW_PERSON_BUTTON_HEIGHT,
      Label.withTextWidth(font, Color.white, 'New Person')
    );
    newPersonButton.addClickListener(() => this.onNewPerson(index));
    newPersonButton.layout();

    if (prevButton) {
      newPersonButton.setPosition(
        prevButton.getPosition(ReferencePoint.TOPRIGHT),
        ReferencePoint.TOPLEFT,
        PartyCreationScene.PADDING,
        0
      );
    } else {
      newPersonButton.setPosition({ x: PartyCreationScene.PADDING, y: PartyCreationScene.PADDING }, ReferencePoint.TOPLEFT);
    }
    this.mainLayer.add(newPersonButton);
    this.newPersonButtons[index] = newPersonButton;

    const nameField = new TextField(this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT, font);
    nameField.setPlaceholderText('Name');
    nameField.setAcceptedCharacters(AcceptedCharacters.LETTERS);
    nameField.setVisible(false);
    nameField.setPosition(
      newPersonButton.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT,
      0,
      PartyCreationScene.INNER_PADDING
    );
    this.mainLayer.add(nameField);
    this.personNameTextFields[index] = nameField;

    const genderControl = new SegmentedControl(
      this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      1, 2, 0, false, 1, 'Male', 'Female'
    );
    genderControl.setVisible(false);
    genderControl.setPosition(
      nameField.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT,
      0,
      PartyCreationScene.INNER_PADDING
    );
    this.mainLayer.add(genderControl);
    this.personGenderControls[index] = genderControl;

    const professionPanel = new Panel(this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT * 2);
    professionPanel.setVisible(false);
    professionPanel.setBorderColor(COLORS.get('INTERACTIVE_BORDER_DARK') || Color.black);
    professionPanel.setBorderWidth(2);

    const changeProfessionButton = new Button(
      this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(font, Color.white, 'Change Profession')
    );
    changeProfessionButton.setAcceptingInput(false);
    changeProfessionButton.setBottomBorderWidth(0);
    changeProfessionButton.addClickListener(() => this.onChangeProfession(index));
    changeProfessionButton.layout();
    professionPanel.add(
      changeProfessionButton,
      professionPanel.getPosition(ReferencePoint.TOPLEFT),
      ReferencePoint.TOPLEFT
    );
    this.personChangeProfessionButtons[index] = changeProfessionButton;

    const professionLabel = new Label(
      this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      font, Color.white, 'No Profession'
    );
    professionLabel.setBackgroundColor(Color.darkGray);
    professionLabel.setAlignment(Alignment.CENTER);
    professionPanel.add(
      professionLabel,
      changeProfessionButton.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT
    );
    this.personProfessionLabels[index] = professionLabel;

    professionPanel.setPosition(
      genderControl.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT,
      0,
      PartyCreationScene.INNER_PADDING
    );
    this.mainLayer.add(professionPanel);
    this.personProfessionPanels[index] = professionPanel;

    const moneyLabel = new Label(this.buttonWidth, font.getLineHeight(), font, Color.white, '$0');
    moneyLabel.setAlignment(Alignment.CENTER);
    moneyLabel.setVisible(false);
    moneyLabel.setPosition(
      professionLabel.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT,
      0,
      PartyCreationScene.INNER_PADDING
    );
    this.mainLayer.add(moneyLabel);
    this.personMoneyLabels[index] = moneyLabel;

    const skillPanel = new Panel(this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT * 4);
    skillPanel.setVisible(false);
    skillPanel.setBorderColor(COLORS.get('INTERACTIVE_BORDER_DARK') || Color.black);
    skillPanel.setBorderWidth(2);

    const changeSkillButton = new Button(
      this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(font, Color.white, 'Change Skills')
    );
    changeSkillButton.setAcceptingInput(false);
    changeSkillButton.setBottomBorderWidth(0);
    changeSkillButton.addClickListener(() => this.onChangeSkills(index));
    changeSkillButton.layout();
    skillPanel.add(
      changeSkillButton,
      skillPanel.getPosition(ReferencePoint.TOPLEFT),
      ReferencePoint.TOPLEFT
    );
    this.personChangeSkillButtons[index] = changeSkillButton;

    this.personSkillLabels[index] = [];
    for (let j = 0; j < PartyCreationScene.NUM_SKILLS; j++) {
      const skillLabel = new Label(
        this.buttonWidth, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
        font, Color.white, 'Empty'
      );
      skillLabel.setBackgroundColor(Color.darkGray);
      skillLabel.setAlignment(Alignment.CENTER);

      const prevLabel = j === 0 ? changeSkillButton : this.personSkillLabels[index][j - 1];
      skillPanel.add(
        skillLabel,
        prevLabel.getPosition(ReferencePoint.BOTTOMLEFT),
        ReferencePoint.TOPLEFT
      );
      this.personSkillLabels[index][j] = skillLabel;
    }

    skillPanel.setPosition(
      moneyLabel.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.TOPLEFT,
      0,
      PartyCreationScene.INNER_PADDING
    );
    this.mainLayer.add(skillPanel);
    this.personSkillPanels[index] = skillPanel;
  }

  private createBottomControls(font: any): void {
    this.rationsSegmentedControl = new SegmentedControl(
      200, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      1, 3, 0, true, 1, 'Barebones', 'Meager', 'Filling'
    );
    this.rationsSegmentedControl.setSelection([1]);
    this.rationsSegmentedControl.setPosition(
      { x: PartyCreationScene.PADDING, y: this.canvasHeight - PartyCreationScene.PADDING - PartyCreationScene.REGULAR_BUTTON_HEIGHT },
      ReferencePoint.TOPLEFT
    );
    this.mainLayer.add(this.rationsSegmentedControl);

    this.paceSegmentedControl = new SegmentedControl(
      200, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      1, 3, 0, true, 1, 'Steady', 'Strenuous', 'Grueling'
    );
    this.paceSegmentedControl.setSelection([0]);
    this.paceSegmentedControl.setPosition(
      this.rationsSegmentedControl.getPosition(ReferencePoint.TOPRIGHT),
      ReferencePoint.TOPLEFT,
      PartyCreationScene.PADDING,
      0
    );
    this.mainLayer.add(this.paceSegmentedControl);

    this.confirmButton = new Button(
      150, PartyCreationScene.REGULAR_BUTTON_HEIGHT,
      Label.withTextWidth(font, Color.white, 'Confirm')
    );
    this.confirmButton.addClickListener(() => this.onConfirm());
    this.confirmButton.layout();
    this.confirmButton.setPosition(
      { x: this.canvasWidth - PartyCreationScene.PADDING, y: this.canvasHeight - PartyCreationScene.PADDING - PartyCreationScene.REGULAR_BUTTON_HEIGHT },
      ReferencePoint.TOPRIGHT
    );
    this.mainLayer.add(this.confirmButton);
  }

  private onNewPerson(index: number): void {
    this.newPersonButtons[index].setVisible(false);
    this.personNameTextFields[index].setVisible(true);
    this.personNameTextFields[index].setFocus(true);
  }

  override keyReleased(key: string, code: string): void {
    super.keyReleased(key, code);

    for (let i = 0; i < PartyCreationScene.NUM_PEOPLE; i++) {
      if (this.personNameTextFields[i].hasFocus() && key === 'Enter') {
        const name = this.personNameTextFields[i].getText();
        if (name.length > 0) {
          this.peopleData[i].name = name;
          this.personNameTextFields[i].setFocus(false);
          this.personGenderControls[i].setVisible(true);
        }
      }
    }
  }

  override mouseReleased(button: number, x: number, y: number): void {
    super.mouseReleased(button, x, y);

    for (let i = 0; i < PartyCreationScene.NUM_PEOPLE; i++) {
      if (this.personGenderControls[i].isVisible() && !this.personProfessionPanels[i].isVisible()) {
        const selection = this.personGenderControls[i].getSelection();
        if (selection.length > 0 && selection[0] !== -1) {
          this.peopleData[i].gender = selection[0] === 0 ? 'Male' : 'Female';
          this.personProfessionPanels[i].setVisible(true);
          this.personChangeProfessionButtons[i].setAcceptingInput(true);
        }
      }
    }
  }

  private onChangeProfession(index: number): void {
    this.currentPersonModifying = index;

    const modal = new ComponentModal(
      this.canvasWidth,
      this.canvasHeight,
      this,
      'Select Profession',
      2,
      this.professionSegmentedControl
    );

    this.showModal(modal);
  }

  private onChangeSkills(index: number): void {
    this.currentPersonModifying = index;

    if (this.peopleData[index].profession !== null) {
      const starterSkill = getProfessionStartingSkill(this.peopleData[index].profession!);
      const skills = Object.values(Skill).filter(s => s !== Skill.NONE) as Exclude<Skill, Skill.NONE>[];
      const starterIndex = skills.indexOf(starterSkill);
      if (starterIndex !== -1) {
        this.skillSegmentedControl.setPermanent([starterIndex]);
      }
    }

    const modal = new ComponentModal(
      this.canvasWidth,
      this.canvasHeight,
      this,
      'Select Skills',
      2,
      this.skillSegmentedControl
    );

    this.showModal(modal);
  }

  override dismissModal(modal: any, button: number): void {
    super.dismissModal(modal, button);

    if (button !== 0 && this.currentPersonModifying !== -1) {
      const index = this.currentPersonModifying;

      if (modal.getComponent() === this.professionSegmentedControl) {
        const selection = this.professionSegmentedControl.getSelection();
        if (selection.length > 0 && selection[0] !== -1) {
          const professions = Object.values(Profession);
          const profession = professions[selection[0]];
          this.peopleData[index].profession = profession;

          this.personProfessionLabels[index].setText(getProfessionName(profession));
          this.personMoneyLabels[index].setText(`$${getProfessionMoney(profession).toLocaleString()}`);
          this.personMoneyLabels[index].setVisible(true);
          this.personSkillPanels[index].setVisible(true);
          this.personChangeSkillButtons[index].setAcceptingInput(true);

          this.peopleData[index].person = new Person(this.peopleData[index].name);
          this.peopleData[index].person!.setIsMale(this.peopleData[index].gender === 'Male');
          this.peopleData[index].person!.setProfession(profession);
        }
      } else if (modal.getComponent() === this.skillSegmentedControl) {
        const selection = this.skillSegmentedControl.getSelection();
        const skills = Object.values(Skill).filter(s => s !== Skill.NONE) as Exclude<Skill, Skill.NONE>[];

        this.peopleData[index].skills = selection.map(i => skills[i]);

        for (let i = 0; i < PartyCreationScene.NUM_SKILLS; i++) {
          if (i < selection.length) {
            this.personSkillLabels[index][i].setText(getSkillName(skills[selection[i]]));
          } else {
            this.personSkillLabels[index][i].setText('Empty');
          }
        }

        for (const skill of this.peopleData[index].skills) {
          if (skill !== Skill.NONE) {
            this.peopleData[index].person!.addSkill(skill);
          }
        }
      }
    }

    this.currentPersonModifying = -1;
  }

  private onConfirm(): void {
    const validPeople = this.peopleData.filter(p => p.person !== null);

    if (validPeople.length === 0) {
      console.log('Validation failed: No party members');
      return;
    }

    for (const personData of validPeople) {
      if (personData.profession === null) {
        console.log('Validation failed: Member without profession');
        return;
      }
      if (personData.skills.length < PartyCreationScene.NUM_SKILLS) {
        console.log('Validation failed: Member without all skills');
        return;
      }
    }

    const names = validPeople.map(p => p.name);
    const uniqueNames = new Set(names);
    if (names.length !== uniqueNames.size) {
      console.log('Validation failed: Duplicate names');
      return;
    }

    const paceMap: Record<number, Pace> = {
      0: Pace.STEADY,
      1: Pace.STRENUOUS,
      2: Pace.GRUELING
    };
    const rationsMap: Record<number, Rations> = {
      0: Rations.BAREBONES,
      1: Rations.MEAGER,
      2: Rations.FILLING
    };

    const pace = paceMap[this.paceSegmentedControl.getSelection()[0]];
    const rations = rationsMap[this.rationsSegmentedControl.getSelection()[0]];
    const people = validPeople.map(p => p.person!);
    const leader = people[0];
    const time = new Time();

    const party = new Party(pace, rations, leader, people, time);

    console.log('Party created successfully:', {
      pace,
      rations,
      leader: leader.getName(),
      members: people.map(p => p.getName()),
      money: party.getMoney()
    });

    console.log('TODO: Set party on Player and transition to TownScene');
  }

  getID(): SceneID {
    return SceneID.PARTYCREATION;
  }
}
