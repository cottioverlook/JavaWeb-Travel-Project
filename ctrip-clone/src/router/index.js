import { createRouter, createWebHistory } from 'vue-router'

// 直接引入所有页面，防止加载卡顿
import HomeView from '../views/HomeView.vue'
import HotelList from '../views/HotelList.vue'
import HotelDetail from '../views/HotelDetail.vue'
import Login from '../views/Login.vue'
import Pay from '../views/Pay.vue'
import UserCenter from '../views/UserCenter.vue'
import SceneryList from '../views/SceneryList.vue'
import SceneryDetail from '../views/SceneryDetail.vue'
import FlightList from '../views/FlightList.vue'
import TrainList from '../views/TrainList.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/hotel-list', name: 'HotelList', component: HotelList },
    { path: '/hotel/:id', name: 'HotelDetail', component: HotelDetail },
    { path: '/login', name: 'Login', component: Login },
    { path: '/pay', name: 'Pay', component: Pay },
    { path: '/user', name: 'UserCenter', component: UserCenter },
    { path: '/scenery-list', name: 'SceneryList', component: SceneryList },
    { path: '/scenery/:id', name: 'SceneryDetail', component: SceneryDetail },
    { path: '/flight-list', name: 'FlightList', component: FlightList },
    { path: '/train-list', name: 'TrainList', component: TrainList }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router