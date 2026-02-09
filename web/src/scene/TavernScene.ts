import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Button } from '../component/Button';
import { Label } from '../component/Label';
import { Sprite } from '../component/Sprite';
import { ReferencePoint } from '../component/Component';
import { ChoiceModal } from '../component/modal/ChoiceModal';
import { MessageModal } from '../component/modal/MessageModal';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { ImageStore } from '../core/ImageStore';
import { LITERALS } from '../core/ConstantStore';
import { Person } from '../model/Person';
import { Party } from '../model/Party';
import { getProfessionName } from '../model/Profession';

export class TavernScene extends Scene {
  private static readonly PADDING = 20;
  private static readonly BUTTON_HEIGHT = 30;
  private static readonly BUTTON_WIDTH = 200;
  private static readonly MAX_PARTY_SIZE = 4;

  private persons: Person[] = [];
  private personButtons: Button[] = [];
  private party: Party;
  private partyFullModal!: MessageModal;
  private chosenPersonIndex: number = -1;

  private maleNames: string[] = ['Alfred', 'Bob', 'David', 'Geoff', 'Henry'];
  private femaleNames: string[] = ['Carlotta', 'Elizabeth', 'Francine', 'Irene'];

  constructor(canvasWidth: number, canvasHeight: number, party: Party) {
    super();
    this.party = party;

    for (let i = 0; i < TavernScene.MAX_PARTY_SIZE; i++) {
      const name = this.randomPersonName();
      const person = new Person(name);
      person.makeRandom();

      if (this.femaleNames.includes(name)) {
        person.setIsMale(false);
      }

      this.persons.push(person);
    }

    this.init(canvasWidth, canvasHeight);
  }

  private randomPersonName(): string {
    const allNames = [...this.maleNames, ...this.femaleNames];
    if (allNames.length === 0) {
      return 'Stranger';
    }

    const randomIndex = Math.floor(Math.random() * allNames.length);
    const name = allNames[randomIndex];

    const maleIndex = this.maleNames.indexOf(name);
    if (maleIndex !== -1) {
      this.maleNames.splice(maleIndex, 1);
    } else {
      const femaleIndex = this.femaleNames.indexOf(name);
      if (femaleIndex !== -1) {
        this.femaleNames.splice(femaleIndex, 1);
      }
    }

    return name;
  }

  private init(canvasWidth: number, canvasHeight: number): void {
    const saloonImage = ImageStore.getImage('SALOON_BACKGROUND');
    const backgroundPanel = new Panel(canvasWidth, canvasHeight, saloonImage);
    this.backgroundLayer.add(backgroundPanel);
    backgroundPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    const fieldFont = FontStore.getFont(FontID.FIELD);

    this.partyFullModal = new MessageModal(
      canvasWidth,
      canvasHeight,
      this,
      "Your party is full, so you can't recruit any more members!"
    );

    for (let i = 0; i < this.persons.length; i++) {
      const person = this.persons[i];
      const personButton = new Button(
        TavernScene.BUTTON_WIDTH,
        TavernScene.BUTTON_HEIGHT,
        Label.withTextWidth(fieldFont, Color.white, person.getName())
      );

      personButton.addClickListener(() => this.onPersonButtonClick(i));
      personButton.layout();

      const xOffset = (220) * (i - 2) + 110;
      (this.mainLayer as any).add(personButton);
      personButton.setPosition(
        { x: canvasWidth / 2, y: canvasHeight },
        ReferencePoint.BOTTOMCENTER,
        xOffset,
        -100
      );

      const iconImage = person.getIsMale()
        ? ImageStore.getImage('HILLBILLY_LEFT')
        : ImageStore.getImage('MAIDEN_LEFT');

      const sprite = new Sprite(iconImage.width * 2, undefined, iconImage);
      (this.mainLayer as any).add(sprite);
      sprite.setPosition(
        personButton.getPosition(ReferencePoint.TOPCENTER),
        ReferencePoint.BOTTOMCENTER,
        0,
        -10
      );

      this.personButtons.push(personButton);
    }

    const leaveLabel = Label.withTextWidth(
      fieldFont,
      Color.white,
      LITERALS.get('GENERAL')?.get('LEAVE') || 'Leave'
    );
    const leaveButton = new Button(
      (canvasWidth - TavernScene.PADDING * 4) / 4,
      TavernScene.BUTTON_HEIGHT,
      leaveLabel
    );
    leaveButton.addClickListener(() => this.onLeaveButtonClick());
    leaveButton.layout();

    (this.mainLayer as any).add(leaveButton);
    leaveButton.setPosition(
      { x: 0, y: canvasHeight },
      ReferencePoint.BOTTOMLEFT,
      TavernScene.PADDING,
      -TavernScene.PADDING
    );
  }

  private generateDetails(person: Person): string {
    const skills = person.getSkillsAsString();
    const profession = person.getProfession();
    const professionName = profession
      ? getProfessionName(profession).charAt(0).toUpperCase() +
        getProfessionName(profession).slice(1).toLowerCase()
      : 'None';

    return `Name: ${person.getName()}\nProfession: ${professionName}\nSkills: ${skills}`;
  }

  private onPersonButtonClick(index: number): void {
    if (this.party.getPartyMembers().length >= TavernScene.MAX_PARTY_SIZE) {
      this.showModal(this.partyFullModal);
      return;
    }

    this.chosenPersonIndex = index;
    const detailsMessage = this.generateDetails(this.persons[index]);

    const choiceModal = new ChoiceModal(
      this.getWidth(),
      this.getHeight(),
      this,
      detailsMessage,
      2
    );

    choiceModal.setButtonText(
      0,
      LITERALS.get('GENERAL')?.get('CANCEL') || 'Cancel'
    );
    choiceModal.setButtonText(
      1,
      LITERALS.get('GENERAL')?.get('CONFIRM') || 'Confirm'
    );

    this.showModal(choiceModal);
  }

  private onLeaveButtonClick(): void {
    console.log('Leave tavern - return to TownScene');
  }

  override dismissModal(modal: any, button: number): void {
    super.dismissModal(modal, button);

    if (modal === this.partyFullModal) {
      return;
    }

    if (button !== 0) {
      this.party.addPartyMember(this.persons[this.chosenPersonIndex]);
      this.personButtons[this.chosenPersonIndex].setDisabled(true);
    }
  }

  private getWidth(): number {
    return 1024;
  }

  private getHeight(): number {
    return 576;
  }

  getID(): SceneID {
    return SceneID.TAVERN;
  }
}
