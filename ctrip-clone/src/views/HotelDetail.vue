<template>
  <div class="detail-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <span class="breadcrumb">酒店详情 > {{ hotelInfo.name }}</span>
      </div>
    </div>

    <div class="main-content">
      <div class="hotel-header">
        <div class="header-left">
          <h1 class="title">{{ hotelInfo.name }} <el-tag type="warning">五星级</el-tag></h1>
          <p class="address"><el-icon><Location /></el-icon> {{ hotelInfo.address }}</p>
        </div>
        <div class="header-right">
          <div class="price-box">
            <span class="price">¥{{ hotelInfo.price }}</span> 起
          </div>
        </div>
      </div>

      <div class="gallery">
        <div class="main-img">
          <img :src="hotelInfo.images[0]" alt="Main">
        </div>
        <div class="sub-imgs">
          <img v-for="(img, index) in hotelInfo.images.slice(1)" :key="index" :src="img" alt="Sub">
        </div>
      </div>

      <el-tabs type="border-card" class="detail-tabs">
        <el-tab-pane label="房型预订">
          <div class="room-list">
            <div class="room-item" v-for="room in roomList" :key="room.id">
              <div class="room-info">
                <h3>{{ room.name }}</h3>
                <p>{{ room.desc }}</p>
              </div>
              <div class="room-price">
                <span class="price">¥{{ room.price }}</span>
               <el-button 
  type="primary" 
  size="small" 
  @click="handleBook(room)"
>预订</el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="用户评价 (230)">
          <div class="review-section">
            <div class="score-summary">
              <span class="big-score">4.8</span>
              <span>/ 5分 棒极了</span>
            </div>
            
            <div class="review-list">
              <div class="review-item" v-for="review in reviewList" :key="review.id">
                <div class="user-info">
                  <el-avatar :size="30" icon="UserFilled" />
                  <span class="username">{{ review.user }}</span>
                  <span class="date">{{ review.date }}</span>
                </div>
                <div class="review-content">
                  <p>{{ review.content }}</p>
                  <el-tag size="small" type="success" v-if="review.verified">已入住用户</el-tag>
                </div>
                <el-divider />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Location, UserFilled } from '@element-plus/icons-vue'
import { getHotelDetail, getHotelRooms } from '@/api/hotel'

const router = useRouter()
const route = useRoute()
const hotelId = route.params.id // 获取URL里的ID

const hotelInfo = ref({
  name: '正在加载...',
  address: '',
  price: 0,
  images: []
})

const roomList = ref([])

const reviewList = ref([])

const averageScore = computed(() => {
  if (reviewList.value.length === 0) return 0
  const sum = reviewList.value.reduce((acc, curr) => acc + (curr.score || 0), 0)
  return (sum / reviewList.value.length).toFixed(1)
})

onMounted(async () => {
  if (hotelId) {
    try {
      // 1. 获取酒店基本信息
      const res = await getHotelDetail(hotelId)
      if (res) {
        hotelInfo.value = {
          name: res.name,
          address: res.address,
          price: 300, // 暂无起价字段
          images: [
            'https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80',
            'https://images.unsplash.com/photo-1582719508461-905c673771fd?ixlib=rb-1.2.1&auto=format&fit=crop&w=300&q=60',
            'https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?ixlib=rb-1.2.1&auto=format&fit=crop&w=300&q=60'
          ]
        }
      }

      // 2. 获取房型列表
      const rooms = await getHotelRooms(hotelId)
      if (rooms) {
        roomList.value = rooms.map(r => ({
          id: r.id,
          name: r.type,
          desc: `${r.occupancy}人入住 | ${r.amenities || '含早'}`,
          price: r.price
        }))
      }

      // 3. 获取评价列表
      // 注意：订单中的 productId 是 HOTEL_ + id
      const reviews = await getProductReviews(`HOTEL_${hotelId}`)
      if (reviews) {
        reviewList.value = reviews.map(r => ({
          id: r.id,
          user: r.userName || '匿名用户',
          date: r.createdAt ? r.createdAt.replace('T', ' ').split(' ')[0] : '',
          content: r.content,
          score: r.score,
          verified: true // 只有已完成订单才能评价，所以都是已验证
        }))
      }
    } catch (e) {
      console.error(e)
    }
  }
})

const handleBook = (room) => {
  // 带着价格和房间名跳到支付页
  router.push({
    path: '/pay',
    query: {
      price: room.price,
      name: `${hotelInfo.value.name} - ${room.name}`,
      id: `HOTEL_${hotelId}`
    }
  })
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: 40px;
}
.nav-bar {
  background: #fff;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  display: flex;
  justify-content: center;
}
.nav-content {
  width: 1000px;
  display: flex;
  align-items: center;
  gap: 20px;
}
.logo {
  color: #0086f6;
  font-size: 24px;
  cursor: pointer;
}
.breadcrumb {
  color: #666;
  font-size: 14px;
}

.main-content {
  width: 1000px;
  margin: 20px auto;
}

/* 头部信息 */
.hotel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.title {
  font-size: 24px;
  color: #333;
  margin-bottom: 5px;
}
.address {
  color: #666;
  font-size: 14px;
}
.price {
  color: #ff4d4f;
  font-size: 28px;
  font-weight: bold;
}

/* 图片画廊 */
.gallery {
  display: flex;
  height: 400px;
  gap: 10px;
  margin-bottom: 20px;
}
.main-img {
  flex: 2;
  overflow: hidden;
  border-radius: 4px;
}
.main-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.sub-imgs {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.sub-imgs img {
  height: 195px;
  width: 100%;
  object-fit: cover;
  border-radius: 4px;
}

/* 选项卡区域 */
.detail-tabs {
  background: #fff;
  min-height: 400px;
}

/* 房型列表 */
.room-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
  padding: 20px 0;
}
.room-info h3 { margin: 0 0 5px 0; font-size: 16px; }
.room-info p { color: #999; font-size: 13px; margin: 0; }
.room-price { text-align: right; }
.room-price .price { display: block; color: #ff4d4f; font-size: 20px; margin-bottom: 5px; }

/* 评论区 */
.review-section { padding: 10px; }
.score-summary {
  background: #f0f9ff;
  padding: 15px;
  color: #0086f6;
  font-weight: bold;
  margin-bottom: 20px;
  border-radius: 4px;
}
.big-score { font-size: 24px; margin-right: 5px; }

.review-item { padding: 15px 0; }
.user-info { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.username { font-weight: bold; font-size: 14px; }
.date { color: #999; font-size: 12px; margin-left: auto; }
.review-content p { color: #333; font-size: 14px; line-height: 1.6; }
</style>