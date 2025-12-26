<template>
  <div class="home-container">
    
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo">携程旅行</h1>
        <div class="nav-links">
          <span>首页</span>
          <span style="color: #ff9900; cursor: pointer; font-weight: bold; display: flex; align-items: center;" @click="$router.push('/user')">
            <el-icon style="margin-right: 4px;"><User /></el-icon> 我的账户
          </span>
        </div>
      </div>
    </div>

    <div class="hero-section">
      <div class="search-box">
        <el-tabs v-model="activeTab" type="border-card" class="ctrip-tabs">
          
          <el-tab-pane name="hotel">
            <template #label>
              <span class="custom-tabs-label">
                <el-icon><OfficeBuilding /></el-icon>
                <span> 酒店</span>
              </span>
            </template>
            <div class="tab-content">
              <div class="input-group">
                <span class="label">目的地</span>
                <el-input v-model="hotelForm.destination" placeholder="请输入城市/酒店/地标" size="large" />
              </div>
              <div class="input-group">
                <span class="label">入住/离店</span>
                <el-date-picker
                  v-model="hotelForm.dates"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="入住日期"
                  end-placeholder="离店日期"
                  size="large"
                  style="width: 100%"
                />
              </div>
              <div class="input-group">
                <span class="label">关键词</span>
                <el-input v-model="hotelForm.keyword" placeholder="价格/星级/品牌" size="large" />
              </div>
              <el-button type="primary" class="search-btn" @click="handleSearch('hotel')">搜索酒店</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane name="flight">
            <template #label>
              <span class="custom-tabs-label">
                <el-icon><Position /></el-icon>
                <span> 机票</span>
              </span>
            </template>
            <div class="tab-content">
              <div class="input-group">
                <span class="label">出发地</span>
                <el-input v-model="flightForm.from" placeholder="城市" size="large" />
              </div>
              <div class="input-group">
                <span class="label">目的地</span>
                <el-input v-model="flightForm.to" placeholder="城市" size="large" />
              </div>
              <div class="input-group">
                <span class="label">出发日期</span>
                <el-date-picker v-model="flightForm.date" type="date" placeholder="选择日期" size="large" style="width: 100%" />
              </div>
              <el-button type="primary" class="search-btn" @click="handleSearch('flight')">搜索机票</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane name="train">
            <template #label>
              <span class="custom-tabs-label">
                <el-icon><Van /></el-icon> 
                <span> 火车票</span>
              </span>
            </template>
            <div class="tab-content">
              <div class="input-group">
                <span class="label">出发站</span>
                <el-input v-model="trainForm.from" placeholder="北京" size="large" />
              </div>
              <div class="input-group">
                <span class="label">到达站</span>
                <el-input v-model="trainForm.to" placeholder="上海" size="large" />
              </div>
              <div class="input-group">
                <span class="label">出发日期</span>
                <el-date-picker v-model="trainForm.date" type="date" placeholder="选择日期" size="large" style="width: 100%" />
              </div>
              <el-button type="primary" class="search-btn" @click="handleSearch('train')">搜索列车</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane name="scenery">
            <template #label>
              <span class="custom-tabs-label">
                <el-icon><Camera /></el-icon> 
                <span> 景点</span>
              </span>
            </template>
            <div class="tab-content">
              <div class="input-group">
                <span class="label">目的地/景点</span>
                <el-input v-model="sceneryForm.keyword" placeholder="请输入景点名称/城市" size="large" />
              </div>
              <el-button type="primary" class="search-btn" @click="handleSearch('scenery')">搜索景点</el-button>
            </div>
          </el-tab-pane>

        </el-tabs>
      </div>
    </div>

    <div class="recommendation-section">
      <h2 class="section-title">旅行灵感 · 猜你喜欢</h2>
      <p class="section-sub">为您推荐热门景点 (刷新页面获取新灵感)</p>
      
      <div class="card-grid" v-if="loading">
        <el-skeleton
          v-for="i in 4"
          :key="i"
          class="rec-card"
          animated
        >
          <template #template>
            <el-skeleton-item variant="image" style="width: 100%; height: 160px" />
            <div style="padding: 14px">
              <el-skeleton-item variant="h3" style="width: 50%" />
              <div style="display: flex; justify-content: space-between; margin-top: 10px; align-items: center;">
                <el-skeleton-item variant="text" style="width: 30%" />
                <el-skeleton-item variant="text" style="width: 20%" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <div class="card-grid" v-else>
        <el-card 
          v-for="item in displayRecommendList" 
          :key="item.id" 
          class="rec-card" 
          shadow="hover"
          :body-style="{ padding: '0px' }"
          @click="goSceneryDetail(item)"
        >
          <img :src="item.imgUrl" class="image" />
          
          <div style="padding: 14px">
            <span class="card-title">{{ item.title }}</span>
            <div class="bottom">
              <el-tag size="small" effect="plain">{{ item.tag }}</el-tag>
              <span class="price">¥{{ item.price }} <span style="font-size:12px;color:#999">起</span></span>
            </div>
            <div class="rating-box">
               <span class="score">{{ item.rating }}分</span>
               <span class="comment-count">{{ item.comments }}人点评</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, OfficeBuilding, Position, Van, Camera } from '@element-plus/icons-vue'
import { getRecommendations } from '@/api/home'

const router = useRouter()
const activeTab = ref('hotel')

const hotelForm = ref({ destination: '', dates: '', keyword: '' })
const flightForm = ref({ from: '北京', to: '上海', date: new Date() })
const trainForm = ref({ from: '北京', to: '上海', date: new Date() })
const sceneryForm = ref({ keyword: '' })

const displayRecommendList = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    loading.value = true
    const res = await getRecommendations(4)
    // request.js interceptor already unwraps response if code === 1, so res is the data array
    if (res) {
      displayRecommendList.value = res
    }
  } catch (error) {
    console.error('Failed to fetch recommendations', error)
  } finally {
    loading.value = false
  }
})

const handleSearch = (type) => {
  if (type === 'hotel') {
    router.push('/hotel-list')
  } else if (type === 'flight') {
    const params = {}
    if (flightForm.value.from) params.from = flightForm.value.from
    if (flightForm.value.to) params.to = flightForm.value.to
    if (flightForm.value.date) {
      const d = new Date(flightForm.value.date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      params.date = `${year}-${month}-${day}`
    }
    router.push({ path: '/flight-list', query: params })
  } else if (type === 'train') {
    const params = {}
    if (trainForm.value.from) params.from = trainForm.value.from
    if (trainForm.value.to) params.to = trainForm.value.to
    if (trainForm.value.date) {
      const d = new Date(trainForm.value.date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      params.date = `${year}-${month}-${day}`
    }
    router.push({ path: '/train-list', query: params })
  } else if (type === 'scenery') {
    const params = {}
    if (sceneryForm.value.keyword) params.keyword = sceneryForm.value.keyword
    router.push({ path: '/scenery-list', query: params })
  }
}

const goSceneryDetail = (item) => {
  // 这里为了演示，如果是 mock 数据的 item 1，就跳到详情
  // 实际项目会根据 id 跳
  router.push(`/scenery/${item.id}`)
}
</script>

<style scoped>
/* 样式保持不变 */
.home-container {
  min-height: 100vh;
  background-color: #f0f2f5;
  padding-bottom: 50px;
}
.nav-bar {
  background: #fff;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  justify-content: center;
}
.nav-content {
  width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.logo {
  color: #0086f6;
  font-size: 24px;
  margin: 0;
  font-weight: 900;
  letter-spacing: 1px;
}
.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-item {
  font-size: 16px;
  color: #333;
  cursor: pointer;
  position: relative;
  font-weight: 500;
}

.nav-item:hover {
  color: #0086f6;
}

/* 首页下方加个小横条表示选中 */
.nav-item::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #0086f6;
  transform: scaleX(0);
  transition: transform 0.3s;
}
.nav-item:hover::after {
  transform: scaleX(1);
}

.user-entry {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #666;
  font-size: 14px;
  padding: 6px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  transition: all 0.3s;
}

.user-entry:hover {
  color: #0086f6;
  border-color: #0086f6;
  background-color: #f0f9ff;
}

.hero-section {
  background-image: linear-gradient(135deg, #0086f6 0%, #00a0f0 100%);
  padding: 50px 0 80px 0;
  display: flex;
  justify-content: center;
}

.search-box {
  width: 1000px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
  padding: 10px;
  margin-top: 20px;
}

.tab-content {
  padding: 20px 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  display: flex;
  align-items: center;
}
.input-group .label {
  width: 90px;
  font-weight: bold;
  color: #333;
  font-size: 15px;
}
.search-btn {
  width: 100%;
  height: 48px;
  font-size: 18px;
  background: linear-gradient(90deg, #ffb100 0%, #ff9900 100%);
  border: none;
  border-radius: 6px;
  margin-top: 15px;
  font-weight: bold;
  letter-spacing: 2px;
}
.search-btn:hover {
  background: linear-gradient(90deg, #ffc700 0%, #ffaa00 100%);
}

.recommendation-section {
  width: 1000px;
  margin: 20px auto 0;
  padding: 0 10px;
  position: relative;
  z-index: 1;
}
.section-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 5px;
}
.section-sub {
  color: #666;
  margin-bottom: 20px;
}
.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.rec-card {
  border-radius: 8px;
  overflow: hidden;
  border: none;
  cursor: pointer;
  transition: transform 0.2s;
}
.rec-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
}

.card-title {
  font-weight: bold;
  font-size: 16px;
  display: block;
  margin-bottom: 8px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #333;
}
.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.price {
  color: #ff4d4f;
  font-size: 20px;
  font-weight: bold;
  font-family: Arial, sans-serif;
}
.rating-box {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}
.score {
  color: #0086f6;
  font-weight: bold;
}
.comment-count {
  color: #999;
}
</style>