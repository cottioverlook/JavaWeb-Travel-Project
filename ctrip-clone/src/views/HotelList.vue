<template>
  <div class="list-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <div class="search-bar-small">
          <el-input 
            v-model="keyword" 
            placeholder="搜索酒店/地名" 
            :prefix-icon="Search"
          >
            <template #append>
              <el-button type="primary">搜索</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </div>

    <div class="main-content">
      
      <div class="filter-sidebar">
        <div class="filter-group">
          <h3>价格范围</h3>
          <el-checkbox-group v-model="filters.price">
            <el-checkbox label="150以下" />
            <el-checkbox label="150-300" />
            <el-checkbox label="301-450" />
            <el-checkbox label="450-600" />
            <el-checkbox label="600以上" />
          </el-checkbox-group>
        </div>
        <div class="filter-group">
          <h3>酒店星级</h3>
          <el-checkbox-group v-model="filters.star">
            <el-checkbox label="经济/客栈" />
            <el-checkbox label="三星/舒适" />
            <el-checkbox label="四星/高档" />
            <el-checkbox label="五星/豪华" />
          </el-checkbox-group>
        </div>
      </div>

      <div class="hotel-list">
        <div class="sort-bar">
          <span class="active">推荐排序</span>
          <span>价格低到高</span>
          <span>好评优先</span>
          <span>距离最近</span>
        </div>

        <div class="hotel-card" v-for="hotel in hotelList" :key="hotel.id">
          <div class="card-img">
            <img :src="hotel.image" alt="hotel">
          </div>
          <div class="card-info">
            <div class="info-main">
              <h2 class="hotel-name">{{ hotel.name }}</h2>
              <div class="hotel-score">
                <span class="score-num">{{ hotel.score }}</span>
                <span class="score-text">分 棒极了</span>
                <span class="comments">{{ hotel.comments }}条点评</span>
              </div>
              <div class="hotel-tags">
                <el-tag size="small" type="info" v-for="tag in hotel.tags" :key="tag" class="tag-item">{{ tag }}</el-tag>
              </div>
              <div class="hotel-address">{{ hotel.address }}</div>
            </div>
            <div class="info-price">
              <div class="price-box">
                <span class="currency">¥</span>
                <span class="amount">{{ hotel.price }}</span>
                <span class="suffix">起</span>
              </div>
              <el-button type="primary" size="large" @click="handleDetail(hotel.id)">查看详情</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { getHotels } from '@/api/hotel'

const router = useRouter()
const keyword = ref('') 
const filters = ref({
  price: [],
  star: []
})

const hotelList = ref([])
const loading = ref(true)

onMounted(async () => {
  await fetchHotels()
})

const fetchHotels = async () => {
  loading.value = true
  try {
    const res = await getHotels({
      name_keyword: keyword.value,
      // 可以在这里映射更多筛选条件
    })
    if (res) {
      hotelList.value = res.map(h => ({
        ...h,
        score: h.rating || 4.5,
        comments: '1000+', // 暂时 mock 点评数
        tags: h.amenities ? h.amenities.split(',').slice(0, 3) : ['舒适', '便捷'],
        price: 300, // 后端暂时没有返回起价，mock 一个
        image: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' // 默认图
      }))
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// === 修改后的跳转逻辑 ===
const handleDetail = (id) => {
  // 跳转到 /hotel/101 这样的路径
  router.push(`/hotel/${id}`)
}
</script>

<style scoped>
/* 样式保持不变 */
.list-container {
  background-color: #f0f2f5;
  min-height: 100vh;
}
.nav-bar {
  background: #fff;
  padding: 10px 0;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.nav-content {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 40px;
}
.logo {
  color: #0086f6;
  font-size: 24px;
  cursor: pointer;
  margin: 0;
}
.search-bar-small {
  width: 400px;
}

.main-content {
  width: 1200px;
  margin: 20px auto;
  display: flex;
  gap: 20px;
}

/* 左侧筛选栏 */
.filter-sidebar {
  width: 260px;
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  height: fit-content;
}
.filter-group {
  margin-bottom: 30px;
  border-bottom: 1px solid #eee;
  padding-bottom: 20px;
}
.filter-group h3 {
  font-size: 14px;
  margin-bottom: 10px;
  color: #333;
}
.el-checkbox-group {
  display: flex;
  flex-direction: column;
}

/* 右侧列表 */
.hotel-list {
  flex: 1;
}
.sort-bar {
  background: #fff;
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  gap: 30px;
  font-size: 14px;
  color: #666;
  border-radius: 4px 4px 0 0;
}
.sort-bar span {
  cursor: pointer;
}
.sort-bar .active {
  color: #0086f6;
  font-weight: bold;
}

/* 酒店卡片 */
.hotel-card {
  display: flex;
  background: #fff;
  padding: 20px;
  margin-bottom: 15px;
  border-radius: 4px;
  transition: box-shadow 0.2s;
}
.hotel-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.card-img {
  width: 200px;
  height: 150px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 20px;
  background: #f0f0f0;
}
.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.card-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
}
.hotel-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 0;
}
.hotel-score {
  margin-bottom: 10px;
}
.score-num {
  color: #fff;
  background: #0086f6;
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: bold;
  margin-right: 5px;
}
.score-text {
  color: #0086f6;
  font-weight: bold;
  margin-right: 10px;
}
.comments {
  color: #999;
  font-size: 13px;
}
.hotel-tags {
  margin-bottom: 10px;
}
.tag-item {
  margin-right: 5px;
}
.hotel-address {
  font-size: 13px;
  color: #666;
}

.info-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
}
.price-box {
  color: #ff4d4f;
  margin-bottom: 10px;
}
.currency {
  font-size: 14px;
}
.amount {
  font-size: 24px;
  font-weight: bold;
}
.suffix {
  color: #999;
  font-size: 12px;
}
</style>