import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '') // 如果后端本身就有 /api 前缀则不需要 rewrite，我们的后端 AuthController 是 /auth，UserController 是 /api，需要注意区分
      },
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/attractions': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
