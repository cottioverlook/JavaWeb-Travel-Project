<template>
  <div class="list-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <h2>火车票搜索结果</h2>
      </div>
    </div>

    <div class="main-content">
      <!-- 筛选侧边栏 -->
      <div class="filter-sidebar">
        <div class="filter-group">
          <h3>车次类型</h3>
          <el-checkbox-group v-model="filters.type">
            <el-checkbox label="high_speed">高铁/动车 (G/D)</el-checkbox>
            <el-checkbox label="normal">普通列车 (Z/K/T)</el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="filter-group">
          <h3>出发时间</h3>
          <el-checkbox-group v-model="filters.time">
            <el-checkbox label="morning">上午 (06:00-12:00)</el-checkbox>
            <el-checkbox label="afternoon">下午 (12:00-18:00)</el-checkbox>
            <el-checkbox label="evening">晚上 (18:00-24:00)</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>

      <div class="list-content">
        <!-- 排序栏 -->
        <div class="sort-bar">
          <span 
            class="sort-item" 
            :class="{ active: sortBy === 'price' }"
            @click="handleSort('price')"
          >
            价格 <el-icon><CaretBottom /></el-icon>
          </span>
          <span 
            class="sort-item" 
            :class="{ active: sortBy === 'time' }"
            @click="handleSort('time')"
          >
            出发时间 <el-icon><CaretBottom /></el-icon>
          </span>
          <span 
            class="sort-item" 
            :class="{ active: sortBy === 'duration' }"
            @click="handleSort('duration')"
          >
            耗时 <el-icon><CaretBottom /></el-icon>
          </span>
        </div>

        <div class="train-list">
          <div class="train-card" v-for="item in filteredList" :key="item.id">
            <div class="train-info">
            <div class="code">
              <h3>{{ item.code }}</h3>
            </div>
            <div class="time-box">
              <div class="time">
                <span class="big-time">{{ item.depTime }}</span>
                <span class="station">{{ item.depStation }}</span>
              </div>
              <div class="arrow">
                <span>{{ item.duration }}</span>
                <span class="line">—————</span>
              </div>
              <div class="time">
                <span class="big-time">{{ item.arrTime }}</span>
                <span class="station">{{ item.arrStation }}</span>
              </div>
            </div>
          </div>
          <div class="seat-box">
            <div class="seat-row" v-for="(seat, idx) in item.seats" :key="idx">
              <span class="seat-type">{{ seat.type }}</span>
              <span class="seat-price">¥{{ seat.price }}</span>
              <span class="seat-count">{{ seat.count }}张</span>
              <el-button size="small" type="primary" @click="handleBook(item, seat)">预订</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { searchTrains, getTrainSeats } from '@/api/train'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const list = ref([])
const loading = ref(false)
const filters = ref({
  type: [],
  time: []
})
const sortBy = ref('') // 'price' | 'time' | 'duration'

const filteredList = computed(() => {
  let result = [...list.value]

  // Filter by Type
  if (filters.value.type.length > 0) {
    result = result.filter(item => {
      const isHighSpeed = item.code.startsWith('G') || item.code.startsWith('D') || item.code.startsWith('C')
      const isNormal = !isHighSpeed
      
      return filters.value.type.some(t => {
        if (t === 'high_speed') return isHighSpeed
        if (t === 'normal') return isNormal
        return false
      })
    })
  }

  // Filter by Time
  if (filters.value.time.length > 0) {
    result = result.filter(item => {
      const hour = parseInt(item.depTime.split(':')[0])
      return filters.value.time.some(t => {
        if (t === 'morning') return hour >= 6 && hour < 12
        if (t === 'afternoon') return hour >= 12 && hour < 18
        if (t === 'evening') return hour >= 18 && hour <= 24
        return false
      })
    })
  }

  // Sorting
  if (sortBy.value === 'price') {
    // Sort by the lowest seat price
    result.sort((a, b) => {
      const minPriceA = a.seats.length > 0 ? Math.min(...a.seats.map(s => s.price)) : 999999
      const minPriceB = b.seats.length > 0 ? Math.min(...b.seats.map(s => s.price)) : 999999
      return minPriceA - minPriceB
    })
  } else if (sortBy.value === 'time') {
    result.sort((a, b) => a.depTime.localeCompare(b.depTime))
  } else if (sortBy.value === 'duration') {
    // Duration format "XhYm" -> convert to minutes for sorting
    const getMinutes = (d) => {
      if (!d) return 999999
      const h = parseInt(d.split('h')[0])
      const m = parseInt(d.split('h')[1].split('m')[0])
      return h * 60 + m
    }
    result.sort((a, b) => getMinutes(a.duration) - getMinutes(b.duration))
  }

  return result
})

const handleSort = (type) => {
  sortBy.value = type
}

const fetchTrains = async () => {
  loading.value = true
  try {
    const params = {
      departure_id: route.query.from || '北京',
      departure_type: 'city',
      arrival_id: route.query.to || '上海',
      arrival_type: 'city',
      date: route.query.date || new Date().toLocaleDateString('en-CA'),
      page: 1,
      size: 10
    }
    
    const res = await searchTrains(params)
    if (res && res.length > 0) {
      // Map basic info
      const trainList = res.map(t => ({
        id: t.id,
        code: t.trainNumber, // Backend uses trainNumber
        depTime: t.departureTime ? t.departureTime.substring(11, 16) : '00:00',
        depStation: t.departureStation,
        arrTime: t.arrivalTime ? t.arrivalTime.substring(11, 16) : '00:00',
        arrStation: t.arrivalStation,
        duration: calculateDuration(t.departureTime, t.arrivalTime),
        seats: []
      }))
      
      // Fetch seats for each train in parallel
      await Promise.all(trainList.map(async (train) => {
        try {
          const seats = await getTrainSeats(train.id)
          if (seats) {
            train.seats = seats.map(s => ({
              id: s.id,
              type: s.type,
              price: s.price,
              count: s.availableSeats
            }))
          }
        } catch (e) {
          console.error(e)
        }
      }))

      list.value = trainList
    } else {
      list.value = []
      ElMessage.info('暂无车次信息')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('获取车次失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchTrains()
})

const calculateDuration = (start, end) => {
  if (!start || !end) return '--'
  const s = new Date(start).getTime()
  const e = new Date(end).getTime()
  const diff = e - s
  const h = Math.floor(diff / (1000 * 60 * 60))
  const m = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  return `${h}h${m}m`
}

const handleBook = (item, seat) => {
  if (seat.count <= 0) {
    ElMessage.warning('余票不足')
    return
  }
  router.push({
    path: '/pay',
    query: {
      price: seat.price,
      name: `${item.code}次 ${item.depStation}-${item.arrStation} ${seat.type}`,
      id: `TRAIN_${item.id}_${seat.id}`
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
.train-card { background: #fff; padding: 20px; margin-bottom: 15px; border-radius: 4px; display: flex; gap: 40px; }
.train-info { width: 300px; display: flex; flex-direction: column; justify-content: center; }
.code h3 { font-size: 20px; margin-bottom: 10px; }
.time-box { display: flex; align-items: center; justify-content: space-between; }
.big-time { font-size: 24px; font-weight: bold; display: block; }
.station { font-size: 16px; }
.arrow { text-align: center; font-size: 12px; color: #999; }
.seat-box { flex: 1; display: flex; flex-direction: column; gap: 10px; justify-content: center; }
.seat-row { display: flex; align-items: center; justify-content: space-between; padding: 10px; background: #f8f8f8; border-radius: 4px; }
.seat-type { font-weight: bold; width: 80px; }
.seat-price { color: #ff9900; font-weight: bold; width: 80px; }
.seat-count { width: 80px; color: #666; }
</style>