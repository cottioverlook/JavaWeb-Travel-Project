import { createApp } from 'vue'
import { createPinia } from 'pinia' // 如果你创建项目没选Pinia，这行可能会报错，没选的话删掉这行
import ElementPlus from 'element-plus' // 引入组件库
import 'element-plus/dist/index.css' // 引入样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // 引入所有图标

import App from './App.vue'
import router from './router'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia()) // 如果没选Pinia，删掉这行
app.use(router)
app.use(ElementPlus) // 告诉Vue使用这个库

app.mount('#app')