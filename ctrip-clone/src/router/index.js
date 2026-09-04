import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'

const HotelList = () => import('../views/HotelList.vue')
const HotelDetail = () => import('../views/HotelDetail.vue')
const Login = () => import('../views/Login.vue')
const Pay = () => import('../views/Pay.vue')
const UserCenter = () => import('../views/UserCenter.vue')
const SceneryList = () => import('../views/SceneryList.vue')
const SceneryDetail = () => import('../views/SceneryDetail.vue')
const FlightList = () => import('../views/FlightList.vue')
const TrainList = () => import('../views/TrainList.vue')

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
