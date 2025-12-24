<template>
  <div class="list-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <h2>火车票搜索结果</h2>
      </div>
    </div>

    <div class="main-content">
      <div class="train-list">
        <div class="train-card" v-for="item in list" :key="item.id">
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
      
      list.value = trainList

      // Fetch seats for each train
      trainList.forEach(async (train) => {
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
      })
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