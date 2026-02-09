import { FontStore } from './core/FontStore';
import { GameDirector } from './core/GameDirector';

const canvas = document.getElementById("game") as HTMLCanvasElement;

async function init() {
  await FontStore.initialize();

  const gameDirector = new GameDirector(canvas);
  gameDirector.start();

  console.log("Game started");
}

init().catch(console.error);
