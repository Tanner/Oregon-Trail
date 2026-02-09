import { FontStore, FontID } from './core/FontStore';

const canvas = document.getElementById("game") as HTMLCanvasElement;
const ctx = canvas.getContext("2d")!;

async function init() {
  ctx.fillStyle = "#000";
  ctx.fillRect(0, 0, 1024, 576);

  await FontStore.initialize();

  const h1Font = FontStore.getFont(FontID.H1);
  h1Font.drawText(ctx, "Oregon Trail", 300, 250);

  const h2Font = FontStore.getFont(FontID.H2);
  h2Font.drawText(ctx, "Bitmap Font Test", 320, 320);

  console.log("FontStore initialized successfully");
}

init().catch(console.error);
