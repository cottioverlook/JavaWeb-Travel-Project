<template>
  <div class="list-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <h2>机票搜索结果</h2>
      </div>
    </div>

    <div class="main-content">
      <div class="flight-list">
        <div class="flight-card" v-for="item in list" :key="item.id">
          <div class="flight-info">
            <div class="airline">
              <h3>{{ item.airline }}</h3>
              <p>{{ item.flightNo }}</p>
            </div>
            <div class="time-box">
              <div class="time">
                <span class="big-time">{{ item.depTime }}</span>
                <span class="airport">{{ item.depAirport }}</span>
              </div>
              <div class="arrow">
                <span>{{ item.duration }}</span>
                <el-icon><Right /></el-icon>
              </div>
              <div class="time">
                <span class="big-time">{{ item.arrTime }}</span>
                <span class="airport">{{ item.arrAirport }}</span>
              </div>
            </div>
          </div>
          <div class="price-box">
            <span class="price">¥{{ item.price }}</span>
            <el-button type="warning" @click="handleBook(item)">预订</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Right } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import { getFlights, getFlightCabins } from '@/api/flight'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const list = ref([])
const loading = ref(false)

onMounted(() => {
  fetchFlights()
})

const fetchFlights = async () => {
  loading.value = true
  try {
    // 构造查询参数
    const params = {
      departure_id: route.query.from || '北京', // 默认值仅供测试
      departure_type: 'city',
      arrival_id: route.query.to || '上海',
      arrival_type: 'city',
      // 使用本地日期字符串，避免 UTC 转换导致日期错误
      date: route.query.date || new Date().toLocaleDateString('en-CA'), // YYYY-MM-DD
      page: 1,
      size: 10
    }
    console.log('Fetching flights with params:', params)

    const res = await getFlights(params)
    if (res && res.length > 0) {
      // 获取航班基础信息
      const flightList = res.map(f => ({
        id: f.id,
        airline: f.airline,
        flightNo: f.number,
        depTime: f.departureTime ? f.departureTime.substring(11, 16) : '00:00',
        depAirport: f.departureAirport,
        arrTime: f.arrivalTime ? f.arrivalTime.substring(11, 16) : '00:00',
        arrAirport: f.arrivalAirport,
        duration: calculateDuration(f.departureTime, f.arrivalTime),
        price: 'Checking...', // 初始状态
        cabins: []
      }))

      list.value = flightList

      // 并行获取价格（获取每个航班的舱位信息）
      flightList.forEach(async (flight) => {
        try {
          const cabins = await getFlightCabins(flight.id)
          if (cabins && cabins.length > 0) {
            // 找到最低价
            const minPrice = Math.min(...cabins.map(c => c.price))
            flight.price = minPrice
            flight.cabins = cabins
          } else {
            flight.price = 'N/A'
          }
        } catch (e) {
          console.error(`Failed to fetch cabins for flight ${flight.id}`, e)
          flight.price = 'N/A'
        }
      })
    } else {
      list.value = []
      ElMessage.info('暂无航班信息')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('获取航班列表失败')
  } finally {
    loading.value = false
  }
}

const calculateDuration = (start, end) => {
  if (!start || !end) return '--'
  const s = new Date(start).getTime()
  const e = new Date(end).getTime()
  const diff = e - s
  const h = Math.floor(diff / (1000 * 60 * 60))
  const m = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  return `${h}h${m}m`
}

const handleBook = (item) => {
  if (item.price === 'Checking...' || item.price === 'N/A') {
    return ElMessage.warning('该航班暂无价格信息')
  }
  router.push({
    path: '/pay',
    query: {
      price: item.price,
      name: `${item.airline} ${item.flightNo} (${item.depAirport} - ${item.arrAirport})`,
      id: `FLIGHT_${item.id}`
    }
  })
}
</script>

<style scoped>
.list-container { background: #f5f7fa; min-height: 100vh; }
.nav-bar { background: #fff; padding: 10px 0; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.nav-content { width: 1000px; margin: 0 auto; display: flex; align-items: center; gap: 40px; }
.logo { color: #0086f6; font-size: 24px; cursor: pointer; margin: 0; }
.main-content { width: 1000px; margin: 20px auto; }
.flight-card { background: #fff; padding: 30px; margin-bottom: 15px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center; }
.flight-info { display: flex; gap: 60px; align-items: center; flex: 1; }
.airline h3 { margin: 0; font-size: 16px; }
.airline p { margin: 5px 0 0; color: #999; font-size: 13px; }
.time-box { display: flex; align-items: center; gap: 30px; text-align: center; }
.big-time { font-size: 24px; font-weight: bold; display: block; }
.airport { font-size: 14px; color: #666; }
.arrow { display: flex; flex-direction: column; color: #ccc; font-size: 12px; }
.price-box { text-align: right; }
.price { display: block; color: #ff4d4f; font-size: 28px; font-weight: bold; margin-bottom: 10px; }
</style>