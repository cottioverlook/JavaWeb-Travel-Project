<template>
  <div class="list-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <div class="search-bar-small">
          <el-input 
            v-model="keyword" 
            placeholder="搜索景点" 
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
          <h3>景点等级</h3>
          <el-checkbox-group v-model="filters.level">
            <el-checkbox label="5A景区" />
            <el-checkbox label="4A景区" />
            <el-checkbox label="3A景区" />
          </el-checkbox-group>
        </div>
      </div>

      <div class="list-content">
        <div class="card-item" v-for="item in list" :key="item.id">
          <img :src="item.image" class="card-img" />
          <div class="card-info">
            <h3>{{ item.name }} <el-tag size="small" type="success">{{ item.level }}</el-tag></h3>
            <p class="desc">{{ item.desc }}</p>
            <div class="bottom-box">
              <span class="price">¥{{ item.price }} <span class="qi">起</span></span>
              <el-button type="primary" @click="$router.push(`/scenery/${item.id}`)">查看详情</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { searchAttractions } from '@/api/home'

const route = useRoute()
const router = useRouter()
const keyword = ref(route.query.keyword || '')
const filters = ref({ level: [] })
const list = ref([])
const loading = ref(false)

const fetchList = async () => {
  loading.value = true
  try {
    const res = await searchAttractions({
      keyword: keyword.value,
      page: 1,
      size: 20
    })
    if (res) {
      list.value = res.map(item => ({
        id: item.id,
        name: item.title,
        level: '4A/5A景区', // 后端暂无 level 字段，mock 一个
        desc: item.tag,
        price: item.price,
        image: item.imgUrl
      }))
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchList()
})

// 监听路由参数变化，重新搜索
watch(() => route.query.keyword, (newVal) => {
  keyword.value = newVal || ''
  fetchList()
})

const handleSearch = () => {
  router.push({ query: { ...route.query, keyword: keyword.value } })
}
</script>

<style scoped>
.list-container { background: #f5f7fa; min-height: 100vh; }
.nav-bar { background: #fff; padding: 10px 0; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.nav-content { width: 1200px; margin: 0 auto; display: flex; align-items: center; gap: 40px; }
.logo { color: #0086f6; font-size: 24px; cursor: pointer; margin: 0; }
.search-bar-small { width: 400px; }
.main-content { width: 1200px; margin: 20px auto; display: flex; gap: 20px; }
.filter-sidebar { width: 260px; background: #fff; padding: 20px; border-radius: 4px; height: fit-content; }
.list-content { flex: 1; }
.card-item { display: flex; background: #fff; padding: 20px; margin-bottom: 15px; border-radius: 4px; gap: 20px; }
.card-img { width: 200px; height: 140px; object-fit: cover; border-radius: 4px; }
.card-info { flex: 1; display: flex; flex-direction: column; justify-content: space-between; }
.card-info h3 { margin: 0; font-size: 18px; }
.desc { color: #666; font-size: 14px; margin: 10px 0; }
.bottom-box { display: flex; justify-content: space-between; align-items: flex-end; }
.price { color: #ff4d4f; font-size: 24px; font-weight: bold; }
.qi { font-size: 12px; color: #999; font-weight: normal; }
</style>