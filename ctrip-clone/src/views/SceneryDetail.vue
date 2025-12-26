<template>
  <div class="detail-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <span class="breadcrumb">景点详情 > {{ info.name }}</span>
      </div>
    </div>

    <div class="main-content">
      <div class="header">
        <h1>{{ info.name }}</h1>
        <p class="address">{{ info.address }}</p>
      </div>

      <div class="gallery">
        <img :src="info.image" class="main-img" />
      </div>

      <div class="ticket-section">
        <h2>门票预订</h2>
        <div class="ticket-list">
          <div class="ticket-item" v-for="t in tickets" :key="t.id">
            <div class="t-info">
              <h3>{{ t.name }}</h3>
              <p>{{ t.desc }}</p>
            </div>
            <div class="t-action">
              <span class="price">¥{{ t.price }}</span>
              <el-button type="warning" @click="handleBook(t)">预订</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAttractionDetail } from '@/api/home'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const info = ref({})
const tickets = ref([])

onMounted(async () => {
  const id = route.params.id
  if (id) {
    try {
      const res = await getAttractionDetail(id)
      if (res) {
        info.value = {
          name: res.title,
          address: res.tag, // 使用 tag 作为地址的替代，或者后端添加 address 字段
          image: res.imgUrl
        }
        // 模拟门票数据，也可以后续后端添加
        tickets.value = [
          { id: 1, name: '成人一日票', desc: '入园当日有效', price: res.price },
          { id: 2, name: '儿童/老人一日票', desc: '入园当日有效，需出示证件', price: Math.floor(res.price * 0.7) },
          { id: 3, name: '双日联票', desc: '连续两日入园', price: Math.floor(res.price * 1.8) }
        ]
      }
    } catch (e) {
      console.error(e)
      ElMessage.error('获取景点详情失败')
    }
  }
})

const handleBook = (ticket) => {
  router.push({
    path: '/pay',
    query: {
      price: ticket.price,
      name: `${info.value.name} - ${ticket.name}`,
      id: `SCENERY_${route.params.id}`
    }
  })
}
</script>

<style scoped>
.detail-container { background: #f5f7fa; min-height: 100vh; }
.nav-bar { background: #fff; height: 60px; display: flex; justify-content: center; box-shadow: 0 1px 4px rgba(0,0,0,0.05); }
.nav-content { width: 1000px; display: flex; align-items: center; gap: 20px; }
.logo { color: #0086f6; font-size: 24px; cursor: pointer; }
.main-content { width: 1000px; margin: 20px auto; background: #fff; padding: 30px; border-radius: 8px; }
.gallery .main-img { width: 100%; height: 400px; object-fit: cover; border-radius: 8px; margin: 20px 0; }
.ticket-list { display: flex; flex-direction: column; gap: 20px; }
.ticket-item { display: flex; justify-content: space-between; border: 1px solid #eee; padding: 20px; border-radius: 4px; }
.t-info h3 { margin: 0 0 5px 0; }
.t-info p { margin: 0; color: #999; font-size: 14px; }
.t-action { display: flex; align-items: center; gap: 20px; }
.price { color: #ff4d4f; font-size: 24px; font-weight: bold; }

.review-section { margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px; }
.no-reviews { color: #999; text-align: center; padding: 20px; }
.review-item { border-bottom: 1px solid #f5f5f5; padding: 15px 0; }
.r-header { display: flex; align-items: center; gap: 15px; margin-bottom: 10px; }
.user { font-weight: bold; color: #333; }
.date { color: #999; font-size: 12px; margin-left: auto; }
.r-content { color: #666; line-height: 1.6; }
</style>