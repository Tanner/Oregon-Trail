import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Party } from '../model/Party';
import { Panel } from '../component/Panel';
import { Color } from '../core/Color';
import { ImageStore } from '../core/ImageStore';
import { SoundStore } from '../core/SoundStore';
import { Sprite } from '../component/Sprite';
import { AnimatingSprite } from '../component/sprite/AnimatingSprite';
import { ParallaxPanel } from '../component/parallax/ParallaxPanel';
import { ParallaxComponent } from '../component/parallax/ParallaxComponent';
import { ParallaxComponentLoop } from '../component/parallax/ParallaxComponentLoop';
import { ComponentModal } from '../component/modal/ComponentModal';
import { MessageModal } from '../component/modal/MessageModal';
import { SegmentedControl } from '../component/SegmentedControl';
import { ReferencePoint } from '../component/Component';
import { Component } from '../component/Component';

/**
 * River crossing scene where the player chooses how to cross:
 * Ford, Caulk and float, Pay ferry toll, or Wait for conditions to improve.
 */
export class RiverScene extends Scene {
  private static readonly NUM_CLOUDS = 5;
  private static readonly CLOUD_OFFSET = 20;
  private static readonly CLOUD_DISTANCE_VARIANCE = 10;
  private static readonly CLOUD_OFFSET_VARIANCE = 10;
  private static readonly CLOUD_DISTANCE = 300;

  private static readonly MAX_RIVER_DEPTH = 8;
  private static readonly FORD_DANGER_DEPTH = 3;
  private static readonly CAULK_DANGER_DEPTH = 4;
  private static readonly RIVER_CROSS_TIME = 3000;
  private static readonly BRIDGE_CROSS_TIME = 5500;

  private party: Party;
  private canvasWidth: number;
  private canvasHeight: number;

  private riverParallaxPanel!: ParallaxPanel;
  private cloudParallaxPanel!: ParallaxPanel;

  private wagon!: AnimatingSprite;
  private wagonWheels!: AnimatingSprite;
  private bridge!: Sprite;

  private crossingChoicesModal!: ComponentModal<SegmentedControl>;
  private successModal!: MessageModal;
  private nextModal: ComponentModal<SegmentedControl> | MessageModal | null = null;

  private riverDepth: number;
  private tollPrice: number;
  private crossTime: number = 0;

  private haveWaited: boolean = false;
  private waiting: boolean = false;
  private crossingBridge: boolean = false;
  private crossingRiver: boolean = false;
  private didTakeDamage: boolean = false;
  private bobbingUp: boolean = true;

  constructor(party: Party, canvasWidth: number, canvasHeight: number) {
    super();
    this.party = party;
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;

    this.riverDepth = Math.floor(Math.random() * RiverScene.MAX_RIVER_DEPTH) + 1;
    this.tollPrice = (Math.floor(Math.random() * 20) + 1) * (party.getLocation().getRank() + 1);
  }

  init(): void {

    // Sky background
    const skyColor = new Color(0x57 / 255, 0x9c / 255, 0xdd / 255);
    const sky = new Panel(this.canvasWidth, this.canvasHeight, skyColor);
    this.backgroundLayer.add(sky);

    // Cloud parallax
    this.cloudParallaxPanel = new ParallaxPanel(this.canvasWidth, this.canvasHeight);
    const cloudImages = [
      ImageStore.getImage('CLOUD_A'),
      ImageStore.getImage('CLOUD_B'),
      ImageStore.getImage('CLOUD_C')
    ];

    for (let i = 0; i < RiverScene.NUM_CLOUDS; i++) {
      const distance = RiverScene.CLOUD_DISTANCE +
        Math.floor(Math.random() * RiverScene.CLOUD_DISTANCE_VARIANCE * 2) -
        RiverScene.CLOUD_DISTANCE_VARIANCE;
      const cloudImage = cloudImages[Math.floor(Math.random() * cloudImages.length)];
      const offset = RiverScene.CLOUD_OFFSET +
        Math.floor(Math.random() * RiverScene.CLOUD_OFFSET_VARIANCE * 2) -
        RiverScene.CLOUD_OFFSET_VARIANCE;

      const cloud = new ParallaxComponent(cloudImage, distance, true);
      this.cloudParallaxPanel.add(cloud);
      cloud.setPosition({ x: 0, y: offset }, ReferencePoint.TOPLEFT);
    }
    this.backgroundLayer.add(this.cloudParallaxPanel);

    // Hills
    const hillB = new Sprite(800, undefined, ImageStore.getImage('HILL_B'));
    this.backgroundLayer.add(hillB);
    hillB.setPosition({ x: this.canvasWidth, y: 50 }, ReferencePoint.TOPRIGHT);

    const hillA = new Sprite(800, undefined, ImageStore.getImage('HILL_A'));
    this.backgroundLayer.add(hillA);
    hillA.setPosition({ x: 0, y: 50 }, ReferencePoint.TOPLEFT);

    // River edge top
    const riverEdgeTop = new Sprite(this.canvasWidth + 1, undefined, ImageStore.getImage('RIVER_EDGE_TOP'));
    this.backgroundLayer.add(riverEdgeTop);
    riverEdgeTop.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    // River parallax (water)
    this.riverParallaxPanel = new ParallaxPanel(this.canvasWidth, this.canvasHeight);
    const water = new ParallaxComponentLoop(this.canvasWidth + 1, ImageStore.getImage('WATER'), 1);
    this.riverParallaxPanel.add(water);
    water.setPosition({ x: 0, y: this.canvasHeight - 200 }, ReferencePoint.BOTTOMLEFT);
    this.backgroundLayer.add(this.riverParallaxPanel);

    // River edge
    const riverEdge = new Sprite(this.canvasWidth + 1, undefined, ImageStore.getImage('RIVER_EDGE'));
    this.backgroundLayer.add(riverEdge);
    riverEdge.setPosition({ x: 0, y: this.canvasHeight }, ReferencePoint.BOTTOMLEFT);

    // Bridge
    this.bridge = new Sprite(200, undefined, ImageStore.getImage('BRIDGE'));
    this.mainLayer.add(this.bridge);
    this.bridge.setPosition(
      { x: this.canvasWidth / 2, y: this.canvasHeight / 2 },
      ReferencePoint.CENTERCENTER,
      100,
      100
    );

    // Wagon animation (3 frames)
    const wagonFrames = [
      ImageStore.getImage('WAGON_RIVER_1'),
      ImageStore.getImage('WAGON_RIVER_2'),
      ImageStore.getImage('WAGON_RIVER_3')
    ];
    this.wagon = new AnimatingSprite(wagonFrames, 100, 'left');
    this.wagon.setSpeed(50);
    this.mainLayer.add(this.wagon);
    this.wagon.setPosition(
      { x: this.canvasWidth / 2, y: this.canvasHeight / 2 },
      ReferencePoint.CENTERCENTER,
      -50,
      120
    );
    this.wagon.setVisible(false);

    // Wagon wheels animation (2 frames)
    const wheelFrames = [
      ImageStore.getImage('WAGON_WHEELS_1'),
      ImageStore.getImage('WAGON_WHEELS_2')
    ];
    this.wagonWheels = new AnimatingSprite(wheelFrames, 100, 'left');
    this.wagonWheels.setSpeed(44);
    this.mainLayer.add(this.wagonWheels);
    this.wagonWheels.setPosition(
      { x: this.canvasWidth / 2, y: this.canvasHeight / 2 },
      ReferencePoint.CENTERCENTER,
      -50,
      120
    );
    this.wagonWheels.setVisible(false);

    // Initial choice modal
    this.makeChoiceModal();
  }

  override enter(): void {
    super.enter();
    this.showModal(this.crossingChoicesModal);
    this.wagon.setVisible(true);
    SoundStore.loopMusic('River');
  }

  override update(delta: number): void {
    super.update(delta);

    // Update parallax
    for (const component of this.riverParallaxPanel.getComponents()) {
      if (component instanceof ParallaxComponent) {
        component.update(delta);
      }
    }
    for (const component of this.cloudParallaxPanel.getComponents()) {
      if (component instanceof ParallaxComponent) {
        component.update(delta);
      }
    }

    if (!this.isPaused()) {
      this.crossTime += delta;

      if (this.waiting) {
        if (this.crossTime > RiverScene.RIVER_CROSS_TIME) {
          this.crossTime = 0;
          this.waiting = false;
          this.wagon.setVisible(true);
          if (this.nextModal) {
            this.showModal(this.nextModal);
          }
        }
      } else {
        this.wagon.update(delta);

        if (this.crossingRiver) {
          // Bobbing animation
          if (this.bobbingUp) {
            this.wagon.setPosition(
              { x: this.wagon.getX(), y: this.wagon.getY() - 1 },
              ReferencePoint.TOPLEFT
            );
          }
          this.bobbingUp = !this.bobbingUp;

          if (this.crossTime > RiverScene.RIVER_CROSS_TIME) {
            if (this.didTakeDamage) {
              SoundStore.playSound('Splash');
            }
            this.crossTime = 0;
            if (this.nextModal) {
              this.showModal(this.nextModal);
            }
          }
        } else if (this.crossingBridge) {
          this.wagon.setPosition(
            { x: this.wagon.getX(), y: this.wagon.getY() - 1 },
            ReferencePoint.TOPLEFT
          );

          if (this.crossTime > RiverScene.BRIDGE_CROSS_TIME) {
            this.crossTime = 0;
            if (this.nextModal) {
              this.showModal(this.nextModal);
            }
          }
        }
      }
    }
  }

  override dismissModal(modal: Component, button: number): void {
    super.dismissModal(modal, button);

    const currentModal = modal;

    if (currentModal === this.crossingChoicesModal) {
      const choice = this.crossingChoicesModal.getComponent().getSelection();
      if (choice.length > 0) {
        switch (choice[0]) {
          case 0: this.ford(); break;
          case 1: this.caulk(); break;
          case 2: this.payToll(); break;
          case 3: this.delay(); break;
        }
      }
    } else if (currentModal === this.successModal) {
      SoundStore.stopMusic();
      this.exit();
    }
  }

  getID(): SceneID {
    return SceneID.RIVER;
  }

  /**
   * Create the modal with crossing choices
   */
  private makeChoiceModal(): void {
    const choices = ['Ford the river', 'Caulk your wagon', 'Pay the toll', 'Wait for an hour'];
    const disabled: number[] = [];

    if (this.party.getMoney() < this.tollPrice) {
      disabled.push(2);
    }
    if (this.haveWaited) {
      disabled.push(3);
    }

    const control = new SegmentedControl(
      600,
      150,
      2,
      2,
      20,
      true,
      1,
      choices
    );

    if (disabled.length > 0) {
      control.setDisabled(disabled);
    }

    const message = `You've come to a river. It is ${this.riverDepth} ${this.riverDepth === 1 ? 'foot' : 'feet'} deep.\nThere is a bridge with a $${this.tollPrice} toll, and you have $${this.party.getMoney()}.\nWhat do you want to do?`;

    this.crossingChoicesModal = new ComponentModal(message, 1, control);
  }

  /**
   * Ford the river (wade across)
   */
  private ford(): void {
    this.crossingRiver = true;

    if (this.riverDepth >= RiverScene.FORD_DANGER_DEPTH && Math.random() > 0.5) {
      const deaths = this.damage();
      this.successModal = new MessageModal(
        `Oh no! Why would you ford a ${this.riverDepth} foot deep river? Your party was damaged` +
        (deaths === '' ? ', but at least no one died!' : ` and you lost ${deaths}.`)
      );
    } else {
      this.successModal = new MessageModal(
        'You successfully forded the river! Your party sighs in relief'
      );
    }

    this.nextModal = this.successModal;
  }

  /**
   * Caulk and float across
   */
  private caulk(): void {
    this.crossingRiver = true;

    if (this.riverDepth >= RiverScene.CAULK_DANGER_DEPTH && Math.random() > 0.5) {
      const deaths = this.damage();
      this.successModal = new MessageModal(
        "Oh no! Your caulk didn't hold up and water leaked into your wagon. Your party was damaged" +
        (deaths === '' ? ', but at least no one died!' : ` and you lost ${deaths}.`)
      );
    } else {
      this.successModal = new MessageModal(
        'Water started seeping into your wagon just as you reached the shore, but you make it! ' +
        'You dump the water out of your boots, take a big swill of whiskey, and get back on the trail.'
      );
    }

    this.nextModal = this.successModal;
  }

  /**
   * Pay the toll and cross the bridge
   */
  private payToll(): void {
    this.crossingBridge = true;
    this.party.setMoney(this.party.getMoney() - this.tollPrice);

    // Switch to wheel sprite
    this.wagon.setVisible(false);
    this.wagonWheels.setVisible(true);
    this.wagon = this.wagonWheels;
    this.wagon.setPosition(
      { x: this.wagon.getX() + 150, y: 576 },
      ReferencePoint.TOPLEFT
    );

    SoundStore.playSound('RK');

    this.successModal = new MessageModal(
      'Your party decided to take the easy way out and pay the bridge toll. Your party members thank you.'
    );

    this.nextModal = this.successModal;
  }

  /**
   * Wait to see if conditions improve
   */
  private delay(): void {
    this.haveWaited = true;
    this.waiting = true;
    this.wagon.setVisible(false);

    // Random new depth
    this.riverDepth = Math.floor(Math.random() * RiverScene.MAX_RIVER_DEPTH) + 1;

    this.makeChoiceModal();
    this.nextModal = this.crossingChoicesModal;
  }

  /**
   * Apply damage to party and vehicle
   * @returns String describing who died
   */
  private damage(): string {
    const deadMembers: string[] = [];
    this.didTakeDamage = true;

    // Damage vehicle
    const vehicle = this.party.getVehicle();
    if (vehicle) {
      vehicle.decreaseStatus(Math.floor(Math.random() * 100));
    }

    // Damage party members
    const partyMembers = this.party.getPartyMembers();
    for (let i = partyMembers.length - 1; i >= 0; i--) {
      const person = partyMembers[i];
      const stillAlive = this.party.decreaseHealth(person, Math.floor(Math.random() * 100));
      if (!stillAlive) {
        deadMembers.push(person.getName());
      }
    }

    // Format death list
    if (deadMembers.length === 0) {
      return '';
    } else if (deadMembers.length === 1) {
      return deadMembers[0];
    } else if (deadMembers.length === 2) {
      return `${deadMembers[0]} and ${deadMembers[1]}`;
    } else {
      let deaths = '';
      for (let i = 0; i < deadMembers.length; i++) {
        if (i === deadMembers.length - 1) {
          deaths += 'and ';
        }
        deaths += deadMembers[i];
        if (i !== deadMembers.length - 1) {
          deaths += ', ';
        }
      }
      return deaths;
    }
  }
}
