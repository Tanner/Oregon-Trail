import { FontStore } from './core/FontStore';
import { GameDirector } from './core/GameDirector';
const canvas = document.getElementById("game");
async function init() {
    await FontStore.initialize();
    const gameDirector = new GameDirector(canvas);
    gameDirector.start();
    console.log("Game started");
}
init().catch(console.error);
//# sourceMappingURL=main.js.map