import { FontStore, FontID } from './core/FontStore';
import { ImageStore } from './core/ImageStore';

const canvas = document.getElementById("game") as HTMLCanvasElement;
const ctx = canvas.getContext("2d")!;

async function init() {
  ctx.fillStyle = "#000";
  ctx.fillRect(0, 0, 1024, 576);

  let progress = 0;
  await ImageStore.initialize((loaded, total) => {
    progress = (loaded / total) * 100;
    console.log(`Loading images: ${loaded}/${total} (${progress.toFixed(0)}%)`);
  });

  await FontStore.initialize();

  ctx.imageSmoothingEnabled = false;
  const logo = ImageStore.getImage("LOGO");
  ctx.drawImage(logo, 300, 150);

  const h1Font = FontStore.getFont(FontID.H1);
  h1Font.drawText(ctx, "Oregon Trail", 300, 350);

  console.log("ImageStore and FontStore initialized successfully");
}

init().catch(console.error);
