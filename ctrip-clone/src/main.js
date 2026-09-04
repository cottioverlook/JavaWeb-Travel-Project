import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {
  ElAvatar,
  ElButton,
  ElCard,
  ElCheckbox,
  ElCheckboxGroup,
  ElDatePicker,
  ElDialog,
  ElDivider,
  ElForm,
  ElFormItem,
  ElIcon,
  ElInput,
  ElRate,
  ElSkeleton,
  ElSkeletonItem,
  ElTabPane,
  ElTabs,
  ElTag,
} from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)

const elementComponents = [
  ElAvatar,
  ElButton,
  ElCard,
  ElCheckbox,
  ElCheckboxGroup,
  ElDatePicker,
  ElDialog,
  ElDivider,
  ElForm,
  ElFormItem,
  ElIcon,
  ElInput,
  ElRate,
  ElSkeleton,
  ElSkeletonItem,
  ElTabPane,
  ElTabs,
  ElTag,
]

for (const component of elementComponents) {
  app.use(component)
}

app.use(createPinia())
app.use(router)

app.mount('#app')
