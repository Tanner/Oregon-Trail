const canvas = document.getElementById("game") as HTMLCanvasElement;
const ctx = canvas.getContext("2d")!;
ctx.fillStyle = "#000";
ctx.fillRect(0, 0, 1024, 576);
ctx.fillStyle = "#fff";
ctx.font = "24px monospace";
ctx.fillText("Oregon Trail - Canvas OK", 350, 290);

const img = new Image();
img.onload = () => {
  ctx.drawImage(img, 400, 200);
  console.log("Image loaded:", img.width, img.height);
};
img.src = "/graphics/logo.png";
