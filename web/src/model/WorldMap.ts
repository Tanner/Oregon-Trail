import { LocationNode } from "./LocationNode";
import { TrailEdge } from "./TrailEdge";
import { MapObject } from "./MapObject";
import { Condition } from "./Condition";
import { StateIdx, TOWN_NAMES } from "../core/ConstantStore";

export class WorldMap {
  public static readonly GEN_LOC = 120;
  private readonly MAX_LOC_QUAL = 100;
  private readonly RANK_WEIGHT = 3;
  private readonly MAX_DANGER = 100;
  private readonly MAX_TRAILS_OUT = 3;
  private readonly MIN_TRAILS_OUT = 1;
  private readonly MAX_RANK = 25;
  private readonly MAX_X = 1200;
  private readonly MAX_Y = 500;

  private mapNodes: Map<number, LocationNode[]>;
  private orphanNodes: Map<number, LocationNode[]>;
  private mapHead!: LocationNode;
  private currTrail!: TrailEdge;
  private currLocationNode!: LocationNode;
  private finalDestination!: LocationNode;
  private numLocations: number;
  private numTrails: number = 0;
  private devMode: boolean;
  private townNamesUsed: Map<StateIdx, boolean[]>;

  constructor(numNodes: number = WorldMap.GEN_LOC, devMode: string = "") {
    this.devMode = devMode.length > 0;
    this.numLocations = this.devMode ? 3000 : numNodes;
    
    this.mapNodes = new Map();
    this.orphanNodes = new Map();
    this.townNamesUsed = new Map();

    for (const idx of Object.values(StateIdx)) {
      const townList = TOWN_NAMES.get(idx as StateIdx);
      if (townList) {
        this.townNamesUsed.set(idx as StateIdx, new Array(townList.length).fill(false));
      }
    }

    this.generateMap(this.numLocations);
    this.currLocationNode = this.mapHead;
    this.currTrail = new TrailEdge(this.mapHead, this.mapHead, 0);
  }

  private generateLocationNode(curRank: number, numExitTrails: number): LocationNode {
    let tmpZ = Math.floor(Math.random() * (this.MAX_X / this.MAX_RANK)) - 
               (this.MAX_X / (2 * this.MAX_RANK));
    let tmpX = this.MAX_X - (((this.MAX_X / this.MAX_RANK) * curRank) + tmpZ);

    if (tmpX > 10 && tmpX < 50) {
      tmpX -= Math.floor(Math.random() * 20);
    }

    while (tmpX < 10) {
      tmpX += Math.floor(Math.random() * (this.MAX_X / this.MAX_RANK));
    }
    while (tmpX > this.MAX_X) {
      tmpX -= Math.floor(Math.random() * (this.MAX_X / this.MAX_RANK));
    }

    tmpZ = Math.floor(Math.random() * (this.MAX_Y / (2 * this.MAX_RANK))) - 
           (this.MAX_Y / this.MAX_RANK);
    
    const rnkMult = curRank <= (this.MAX_RANK / 2) ? curRank : (this.MAX_RANK - curRank);
    const tmpW = Math.random() < 0.5 ? 1 : -1;
    let tmpY = (Math.floor(Math.random() * (this.MAX_Y / this.MAX_RANK)) * 
                Math.floor(Math.random() * (rnkMult + 1)) * tmpW + tmpZ);

    while (tmpY > this.MAX_Y / 2) {
      tmpY -= Math.floor(Math.random() * (this.MAX_Y / this.MAX_RANK));
    }
    while (tmpY < -1 * (this.MAX_Y / 2)) {
      tmpY += Math.floor(Math.random() * (this.MAX_Y / this.MAX_RANK));
    }

    numExitTrails = Math.floor(Math.random() * (this.MAX_TRAILS_OUT + 1 - this.MIN_TRAILS_OUT)) + 
                    this.MIN_TRAILS_OUT;
    
    const tempNode = new LocationNode(
      `tmp name - ${tmpX} | ${tmpY}`,
      tmpX, tmpY, 0, 90,
      numExitTrails, curRank,
      Math.floor(Math.random() * this.MAX_LOC_QUAL),
      this.MAX_X, this.MAX_Y
    );

    tempNode.setName(this.nameLocation(curRank, tempNode));
    return tempNode;
  }

  private nameLocation(_curRank: number, node: LocationNode): string {
    const rankIndex = this.determineTerritory(node);
    node.setTerritory(rankIndex);

    const townList = TOWN_NAMES.get(rankIndex);
    const usedList = this.townNamesUsed.get(rankIndex);

    if (!townList || !usedList) {
      return `Location ${node.getID()}`;
    }

    for (let i = 0; i < townList.length; i++) {
      if (!usedList[i]) {
        usedList[i] = true;
        return townList[i];
      }
    }

    return `${townList[0]} ${node.getID()}`;
  }

  private determineTerritory(node: LocationNode): StateIdx {
    const x = node.getPlayerMapX();
    const y = node.getPlayerMapY();

    if (x <= 255) {
      if ((0.2414 * x + 54) < y && (-1.75 * x + 561) > y) {
        return StateIdx.OREGON;
      } else {
        return StateIdx.WASHINGTON_TERRITORY;
      }
    } else if (x <= 325) {
      if (0.3 * x + 210 > y) {
        return StateIdx.WASHINGTON_TERRITORY;
      } else {
        return StateIdx.UTAH_TERRITORY;
      }
    } else if (x <= 460) {
      if (0.7855 * x - 92 > y) {
        return StateIdx.DAKOTA_TERRITORY;
      } else if (0.21277 * x + 199 > y) {
        return StateIdx.WASHINGTON_TERRITORY;
      } else if (0.2364 * x + 270 > y) {
        return StateIdx.NEBRASKA_TERRITORY;
      } else {
        return StateIdx.COLORADO_TERRITORY;
      }
    } else if (x <= 680) {
      if (0.7855 * x - 92 > y) {
        return StateIdx.DAKOTA_TERRITORY;
      } else if (0.2364 * x + 270 > y) {
        return StateIdx.NEBRASKA_TERRITORY;
      } else {
        return StateIdx.COLORADO_TERRITORY;
      }
    } else if (x <= 900) {
      if (0.625 * x - 241 > y) {
        return StateIdx.DAKOTA_TERRITORY;
      } else {
        return StateIdx.NEBRASKA_TERRITORY;
      }
    } else {
      if (y > 300) {
        return StateIdx.KANSAS_TERRITORY;
      } else {
        return StateIdx.NEBRASKA_TERRITORY;
      }
    }
  }

  private generateMap(numLocations: number): void {
    for (let i = 0; i <= this.MAX_RANK; i++) {
      this.mapNodes.set(i, []);
      this.orphanNodes.set(i, []);
    }

    this.mapHead = new LocationNode(
      "Independence, Missouri",
      this.MAX_X,
      0,
      0,
      0,
      this.MAX_TRAILS_OUT,
      0,
      100,
      this.MAX_X,
      this.MAX_Y
    );
    this.mapHead.setTerritory(StateIdx.MISSOURI);
    this.mapHead.setOnTheTrail(true);
    this.mapNodes.get(0)!.push(this.mapHead);

    this.finalDestination = new LocationNode(
      "Oregon City, Oregon",
      0, 0, 0, 90, 0, this.MAX_RANK, 100,
      this.MAX_X, this.MAX_Y
    );
    this.finalDestination.setVisible(true);
    this.finalDestination.setTerritory(StateIdx.OREGON);

    for (let i = 1; i < this.MAX_RANK; i++) {
      const tmp = this.generateLocationNode(i, 0);
      this.mapNodes.get(i)!.push(tmp);
    }

    for (let i = this.MAX_RANK; i < numLocations - 1; i++) {
      const curRankIter = (i % (this.MAX_RANK - 1)) + 1;
      const curRank = Math.floor(Math.random() * this.RANK_WEIGHT) === 0 ? 
                      curRankIter - 1 : curRankIter;
      const tmp = this.generateLocationNode(curRank, 0);
      this.mapNodes.get(tmp.getRank())!.push(tmp);
    }

    this.mapNodes.get(this.finalDestination.getRank())!.push(this.finalDestination);

    this.buildTrailOuts();

    const headTrails = this.mapHead.getOutboundTrails();
    if (headTrails.length > 0) {
      this.currTrail = headTrails[Math.floor(Math.random() * headTrails.length)];
    }
  }

  private buildTrailOuts(): void {
    for (let curRank = 0; curRank < this.MAX_RANK; curRank++) {
      const nodes = this.mapNodes.get(curRank) || [];
      
      for (const node of nodes) {
        const trailDest: number[] = [];
        let trailForward = false;

        for (let tNum = 0; tNum < node.getTrails(); tNum++) {
          let nextRank = Math.floor(Math.random() * this.RANK_WEIGHT) === 0 ? 
                         curRank + 1 : curRank;
          
          if (nextRank === this.MAX_RANK) {
            nextRank = this.MAX_RANK - 1;
          }

          let newTrail: TrailEdge;
          this.numTrails++;
          const dangerLevel = Math.floor(Math.random() * this.MAX_DANGER);

          if (curRank === this.MAX_RANK - 1) {
            node.setTrails(1);
            newTrail = new TrailEdge(this.finalDestination, node, dangerLevel);
            this.finalDestination.setHasInTrail(true);
          } else {
            if (tNum === node.getTrails() - 1 && !trailForward) {
              nextRank = node.getRank() + 1;
            }

            if (nextRank === 0) {
              nextRank = 1;
            }

            const nextRankNodes = this.mapNodes.get(nextRank) || [];
            let nextTown = Math.floor(Math.random() * nextRankNodes.length);
            let randDestNode = nextRankNodes[nextTown];

            if (Math.floor(Math.random() * 100) < 90 || randDestNode.getID() === node.getID()) {
              while (trailDest.includes(randDestNode.getID())) {
                if (tNum === node.getTrails() - 1 && !trailForward) {
                  nextRank = node.getRank() + 1;
                } else {
                  nextRank = Math.floor(Math.random() * this.RANK_WEIGHT) === 0 ? 
                            curRank + 1 : curRank;
                }
                const updatedNodes = this.mapNodes.get(nextRank) || [];
                nextTown = Math.floor(Math.random() * updatedNodes.length);
                randDestNode = updatedNodes[nextTown];
              }
            }

            trailDest.push(randDestNode.getID());
            newTrail = new TrailEdge(randDestNode, node, dangerLevel);
            randDestNode.setHasInTrail(true);

            if (newTrail.getOrigin().getRank() !== newTrail.getDestination().getRank()) {
              trailForward = true;
            }
          }

          node.addTrail(newTrail);
        }

        if (!node.getHasInTrail()) {
          node.setQuality(new Condition(0, 100, 100));
          this.orphanNodes.get(node.getRank())!.push(node);
        }

        if (node.getConditionPercentage() > 0.9) {
          node.setVisible(true);
        }
      }
    }
  }

  getMapNodes(): Map<number, LocationNode[]> {
    return this.mapNodes;
  }

  getMaxRank(): number {
    return this.MAX_RANK;
  }

  getMapHead(): LocationNode {
    return this.mapHead;
  }

  getCurrLocationNode(): LocationNode {
    return this.currLocationNode;
  }

  getNextRankOrphanLocationList(): LocationNode[] {
    const nextRank = this.currLocationNode.getRank() + 1;
    return this.orphanNodes.get(nextRank) || [];
  }

  getNextRankOrphanLocation(): LocationNode | null {
    const list = this.getNextRankOrphanLocationList();
    return list.length > 0 ? list[0] : null;
  }

  setVisibleByArea(visibleState: StateIdx): void {
    for (let i = 0; i <= this.MAX_RANK; i++) {
      const nodes = this.mapNodes.get(i) || [];
      for (const node of nodes) {
        if (node.getTerritory() === visibleState) {
          node.setVisible(true);
        }
      }
    }
  }

  getCurrTrail(): TrailEdge {
    return this.currTrail;
  }

  getNumLocations(): number {
    return this.numLocations;
  }

  setNumLocations(numLocations: number): void {
    this.numLocations = numLocations;
  }

  getNumTrails(): number {
    return this.numTrails;
  }

  setNumTrails(numTrails: number): void {
    this.numTrails = numTrails;
  }

  setCurrLocationNode(currLocationNode: LocationNode): void {
    this.currLocationNode = currLocationNode;
  }

  resetMap(): void {
    MapObject.resetCount();
    TrailEdge.resetTrails();
    this.mapNodes.clear();
    this.orphanNodes.clear();
    this.numTrails = 0;
    this.generateMap(this.numLocations);
  }

  setCurrTrail(currTrail: TrailEdge): void {
    this.currTrail = currTrail;
  }

  makePCInboundTrail(destNode: LocationNode): void {
    const dangerLevel = Math.floor(Math.random() * this.MAX_DANGER);
    const newTrail = new TrailEdge(destNode, this.currLocationNode, dangerLevel);
    this.currLocationNode.addTrail(newTrail);
    destNode.setHasInTrail(true);
    destNode.setOnTheTrail(true);
  }
}
