import { defineConfig } from "vite";

export default defineConfig({
  root: ".",
  base: process.env.NODE_ENV === 'production' ? '/Oregon-Trail/' : '/',
  build: {
    outDir: "dist",
  },
});
