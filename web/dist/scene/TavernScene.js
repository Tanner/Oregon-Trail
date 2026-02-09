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
import { getProfessionName } from '../model/Profession';
export class TavernScene extends Scene {
    constructor(canvasWidth, canvasHeight, party) {
        super();
        this.persons = [];
        this.personButtons = [];
        this.chosenPersonIndex = -1;
        this.maleNames = ['Alfred', 'Bob', 'David', 'Geoff', 'Henry'];
        this.femaleNames = ['Carlotta', 'Elizabeth', 'Francine', 'Irene'];
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
    randomPersonName() {
        const allNames = [...this.maleNames, ...this.femaleNames];
        if (allNames.length === 0) {
            return 'Stranger';
        }
        const randomIndex = Math.floor(Math.random() * allNames.length);
        const name = allNames[randomIndex];
        const maleIndex = this.maleNames.indexOf(name);
        if (maleIndex !== -1) {
            this.maleNames.splice(maleIndex, 1);
        }
        else {
            const femaleIndex = this.femaleNames.indexOf(name);
            if (femaleIndex !== -1) {
                this.femaleNames.splice(femaleIndex, 1);
            }
        }
        return name;
    }
    init(canvasWidth, canvasHeight) {
        const saloonImage = ImageStore.getImage('SALOON_BACKGROUND');
        const backgroundPanel = new Panel(canvasWidth, canvasHeight, saloonImage);
        this.backgroundLayer.add(backgroundPanel);
        backgroundPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.partyFullModal = new MessageModal(canvasWidth, canvasHeight, this, "Your party is full, so you can't recruit any more members!");
        for (let i = 0; i < this.persons.length; i++) {
            const person = this.persons[i];
            const personButton = new Button(TavernScene.BUTTON_WIDTH, TavernScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, person.getName()));
            personButton.addClickListener(() => this.onPersonButtonClick(i));
            personButton.layout();
            const xOffset = (220) * (i - 2) + 110;
            this.mainLayer.add(personButton);
            personButton.setPosition({ x: canvasWidth / 2, y: canvasHeight }, ReferencePoint.BOTTOMCENTER, xOffset, -100);
            const iconImage = person.getIsMale()
                ? ImageStore.getImage('HILLBILLY_LEFT')
                : ImageStore.getImage('MAIDEN_LEFT');
            const sprite = new Sprite(iconImage.width * 2, undefined, iconImage);
            this.mainLayer.add(sprite);
            sprite.setPosition(personButton.getPosition(ReferencePoint.TOPCENTER), ReferencePoint.BOTTOMCENTER, 0, -10);
            this.personButtons.push(personButton);
        }
        const leaveLabel = Label.withTextWidth(fieldFont, Color.white, LITERALS.get('GENERAL')?.get('LEAVE') || 'Leave');
        const leaveButton = new Button((canvasWidth - TavernScene.PADDING * 4) / 4, TavernScene.BUTTON_HEIGHT, leaveLabel);
        leaveButton.addClickListener(() => this.onLeaveButtonClick());
        leaveButton.layout();
        this.mainLayer.add(leaveButton);
        leaveButton.setPosition({ x: 0, y: canvasHeight }, ReferencePoint.BOTTOMLEFT, TavernScene.PADDING, -TavernScene.PADDING);
    }
    generateDetails(person) {
        const skills = person.getSkillsAsString();
        const profession = person.getProfession();
        const professionName = profession
            ? getProfessionName(profession).charAt(0).toUpperCase() +
                getProfessionName(profession).slice(1).toLowerCase()
            : 'None';
        return `Name: ${person.getName()}\nProfession: ${professionName}\nSkills: ${skills}`;
    }
    onPersonButtonClick(index) {
        if (this.party.getPartyMembers().length >= TavernScene.MAX_PARTY_SIZE) {
            this.showModal(this.partyFullModal);
            return;
        }
        this.chosenPersonIndex = index;
        const detailsMessage = this.generateDetails(this.persons[index]);
        const choiceModal = new ChoiceModal(this.getWidth(), this.getHeight(), this, detailsMessage, 2);
        choiceModal.setButtonText(0, LITERALS.get('GENERAL')?.get('CANCEL') || 'Cancel');
        choiceModal.setButtonText(1, LITERALS.get('GENERAL')?.get('CONFIRM') || 'Confirm');
        this.showModal(choiceModal);
    }
    onLeaveButtonClick() {
        console.log('Leave tavern - return to TownScene');
    }
    dismissModal(modal, button) {
        super.dismissModal(modal, button);
        if (modal === this.partyFullModal) {
            return;
        }
        if (button !== 0) {
            this.party.addPartyMember(this.persons[this.chosenPersonIndex]);
            this.personButtons[this.chosenPersonIndex].setDisabled(true);
        }
    }
    getWidth() {
        return 1024;
    }
    getHeight() {
        return 576;
    }
    getID() {
        return SceneID.TAVERN;
    }
}
TavernScene.PADDING = 20;
TavernScene.BUTTON_HEIGHT = 30;
TavernScene.BUTTON_WIDTH = 200;
TavernScene.MAX_PARTY_SIZE = 4;
//# sourceMappingURL=TavernScene.js.map