class ImageStoreClass {
    constructor() {
        this.images = new Map();
        this.initialized = false;
    }
    async initialize(onProgress) {
        if (this.initialized)
            return;
        const manifest = this.buildManifest();
        const total = manifest.length;
        let loaded = 0;
        const loadPromises = manifest.map(({ key, path }) => new Promise((resolve, reject) => {
            const img = new Image();
            img.onload = () => {
                this.images.set(key, img);
                loaded++;
                if (onProgress) {
                    onProgress(loaded, total);
                }
                resolve();
            };
            img.onerror = () => reject(new Error(`Failed to load image: ${path}`));
            img.src = path;
        }));
        await Promise.all(loadPromises);
        this.initialized = true;
    }
    getImage(key) {
        const img = this.images.get(key);
        if (!img) {
            throw new Error(`Image ${key} not loaded`);
        }
        return img;
    }
    buildManifest() {
        const manifest = [];
        manifest.push({ key: "NULL", path: "/graphics/logo/null.png" });
        manifest.push({ key: "VOID", path: "/graphics/logo/void.png" });
        manifest.push({ key: "LOGO", path: "/graphics/logo.png" });
        manifest.push({ key: "MAP_BACKGROUND", path: "/graphics/backgrounds/map.png" });
        manifest.push({ key: "TRAIL_MAP", path: "/graphics/backgrounds/playerMap.png" });
        manifest.push({ key: "DIRT_BACKGROUND", path: "/graphics/backgrounds/dark_dirt.png" });
        manifest.push({ key: "CAMP_ICON", path: "/graphics/icons/fire.png" });
        manifest.push({ key: "INVENTORY_ICON", path: "/graphics/icons/pack.png" });
        manifest.push({ key: "TRAIL_ICON", path: "/graphics/icons/trail.png" });
        manifest.push({ key: "HORSE_ICON", path: "/graphics/icons/items/horse.png" });
        manifest.push({ key: "MULE_ICON", path: "/graphics/icons/items/mule.png" });
        manifest.push({ key: "OX_ICON", path: "/graphics/icons/items/ox.png" });
        for (let i = 1; i <= 6; i++) {
            manifest.push({ key: `MAP_POINTER${i}`, path: `/graphics/icons/mapPointer${i}.png` });
        }
        for (let i = 1; i <= 4; i++) {
            manifest.push({ key: `MAP_PARTY${i}`, path: `/graphics/icons/partyMapRep${i}.png` });
        }
        manifest.push({ key: "GRASS", path: "/graphics/ground/grass.png" });
        manifest.push({ key: "TRAIL", path: "/graphics/ground/trail.png" });
        manifest.push({ key: "HILL_A", path: "/graphics/backgrounds/hill_a.png" });
        manifest.push({ key: "HILL_B", path: "/graphics/backgrounds/hill_b.png" });
        manifest.push({ key: "TREE", path: "/graphics/ground/tree.png" });
        manifest.push({ key: "DEER", path: "/graphics/animals/deer.png" });
        manifest.push({ key: "CLOUD_A", path: "/graphics/backgrounds/cloud_a.png" });
        manifest.push({ key: "CLOUD_B", path: "/graphics/backgrounds/cloud_b.png" });
        manifest.push({ key: "CLOUD_C", path: "/graphics/backgrounds/cloud_c.png" });
        manifest.push({ key: "TRAIL_WAGON", path: "/graphics/trail/trailWagon.png" });
        manifest.push({ key: "SMALL_WHEEL_1", path: "/graphics/trail/smallwheel1.png" });
        manifest.push({ key: "SMALL_WHEEL_2", path: "/graphics/trail/smallwheel2.png" });
        manifest.push({ key: "SMALL_WHEEL_3", path: "/graphics/trail/smallwheel3.png" });
        manifest.push({ key: "BIG_WHEEL_1", path: "/graphics/trail/wheel1.png" });
        manifest.push({ key: "BIG_WHEEL_2", path: "/graphics/trail/wheel2.png" });
        manifest.push({ key: "BIG_WHEEL_3", path: "/graphics/trail/wheel3.png" });
        manifest.push({ key: "HUNT_RETICLE", path: "/graphics/hunt/huntReticle.png" });
        manifest.push({ key: "HUNTER_LEFT", path: "/graphics/hunt/hunterLeftFaceSide1.png" });
        manifest.push({ key: "HUNTER_RIGHT", path: "/graphics/hunt/hunterRightFaceSide1.png" });
        manifest.push({ key: "HUNTER_FRONT", path: "/graphics/hunt/hunterFaceFront1.png" });
        manifest.push({ key: "HUNTER_BACK", path: "/graphics/hunt/hunterFaceBack1.png" });
        manifest.push({ key: "HUNTER_UPPERLEFT", path: "/graphics/hunt/hunterLeftFaceBack1.png" });
        manifest.push({ key: "HUNTER_UPPERRIGHT", path: "/graphics/hunt/hunterRightFaceBack1.png" });
        manifest.push({ key: "HUNTER_LOWERLEFT", path: "/graphics/hunt/hunterLeftFaceFront1.png" });
        manifest.push({ key: "HUNTER_LOWERRIGHT", path: "/graphics/hunt/hunterRightFaceFront1.png" });
        for (let incr = 1; incr <= 6; incr++) {
            manifest.push({ key: `HUNT_PIGBACK${incr}`, path: `/graphics/hunt/prey/pigBack${incr}.png` });
            manifest.push({ key: `HUNT_COWBACK${incr}`, path: `/graphics/hunt/prey/cowBack${incr}.png` });
            manifest.push({ key: `HUNT_PIGFRONT${incr}`, path: `/graphics/hunt/prey/pigFront${incr}.png` });
            manifest.push({ key: `HUNT_COWFRONT${incr}`, path: `/graphics/hunt/prey/cowFront${incr}.png` });
            manifest.push({ key: `HUNT_PIGRIGHT${incr}`, path: `/graphics/hunt/prey/pigRight${incr}.png` });
            manifest.push({ key: `HUNT_COWRIGHT${incr}`, path: `/graphics/hunt/prey/cowRight${incr}.png` });
            manifest.push({ key: `HUNT_PIGLEFT${incr}`, path: `/graphics/hunt/prey/pigLeft${incr}.png` });
            manifest.push({ key: `HUNT_COWLEFT${incr}`, path: `/graphics/hunt/prey/cowLeft${incr}.png` });
        }
        for (let incr = 7; incr <= 9; incr++) {
            manifest.push({ key: `HUNT_COWBACK${incr}`, path: `/graphics/hunt/prey/cowBack${incr}.png` });
            manifest.push({ key: `HUNT_COWFRONT${incr}`, path: `/graphics/hunt/prey/cowFront${incr}.png` });
            manifest.push({ key: `HUNT_COWRIGHT${incr}`, path: `/graphics/hunt/prey/cowRight${incr}.png` });
            manifest.push({ key: `HUNT_COWLEFT${incr}`, path: `/graphics/hunt/prey/cowLeft${incr}.png` });
        }
        manifest.push({ key: "HUNT_GRASS", path: "/graphics/hunt/backgrounds/grassBG.png" });
        manifest.push({ key: "HUNT_SNOW", path: "/graphics/hunt/backgrounds/snowBG.png" });
        manifest.push({ key: "HUNT_MOUNTAIN", path: "/graphics/hunt/backgrounds/mountainBG.png" });
        manifest.push({ key: "HUNT_DESERT", path: "/graphics/hunt/backgrounds/desertBG.png" });
        for (let incr = 0; incr < 16; incr++) {
            const imageDir = incr < 10 ? incr.toString() : String.fromCharCode(97 + (incr - 10));
            const imageDirUpper = imageDir.toUpperCase();
            manifest.push({ key: `HUNT_TREE1${imageDirUpper}0`, path: `/graphics/hunt/terrain/tree1${imageDir}0.png` });
            manifest.push({ key: `HUNT_TREE1${imageDirUpper}1`, path: `/graphics/hunt/terrain/tree1${imageDir}1.png` });
            manifest.push({ key: `HUNT_TREE1${imageDirUpper}0SHAD`, path: `/graphics/hunt/terrain/tree1${imageDir}0Shad.png` });
            manifest.push({ key: `HUNT_TREE1${imageDirUpper}1SHAD`, path: `/graphics/hunt/terrain/tree1${imageDir}1Shad.png` });
            manifest.push({ key: `HUNT_ROCK1${imageDirUpper}0`, path: `/graphics/hunt/terrain/rock1${imageDir}0.png` });
            manifest.push({ key: `HUNT_ROCK1${imageDirUpper}1`, path: `/graphics/hunt/terrain/rock1${imageDir}1.png` });
            manifest.push({ key: `HUNT_ROCK1${imageDirUpper}0SHAD`, path: `/graphics/hunt/terrain/rock1${imageDir}0Shad.png` });
            manifest.push({ key: `HUNT_ROCK1${imageDirUpper}1SHAD`, path: `/graphics/hunt/terrain/rock1${imageDir}1Shad.png` });
        }
        manifest.push({ key: "TRAPPER_LEFT", path: "/graphics/people/hunterLeftFaceSide1.png" });
        manifest.push({ key: "TRAPPER_RIGHT", path: "/graphics/people/hunterRightFaceSide1.png" });
        manifest.push({ key: "MAIDEN_LEFT", path: "/graphics/people/maidenLeftFaceSide1.png" });
        manifest.push({ key: "MAIDEN_RIGHT", path: "/graphics/people/maidenRightFaceSide1.png" });
        manifest.push({ key: "HILLBILLY_LEFT", path: "/graphics/people/hillbillyLeftFaceSide1.png" });
        manifest.push({ key: "HILLBILLY_RIGHT", path: "/graphics/people/hillbillyRightFaceSide1.png" });
        manifest.push({ key: "STORE_BUILDING", path: "/graphics/buildings/general-store.png" });
        manifest.push({ key: "SALOON_BUILDING", path: "/graphics/buildings/saloon.png" });
        manifest.push({ key: "SALOON_BACKGROUND", path: "/graphics/backgrounds/saloonBkg.png" });
        return manifest;
    }
}
export const ImageStore = new ImageStoreClass();
//# sourceMappingURL=ImageStore.js.map