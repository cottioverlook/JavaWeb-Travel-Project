<template>
  <div class="user-container">
    <div class="nav-bar">
      <div class="nav-content">
        <h1 class="logo" @click="$router.push('/')">携程旅行</h1>
        <div class="user-profile">
          <el-avatar :size="32" :icon="UserFilled" :src="userInfo.avatarUrl" />
          <span class="username">{{ userInfo.name || 'Traveler' }}</span>
          <span class="logout" @click="handleLogout">退出</span>
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="sidebar">
        <div 
          class="menu-item" 
          :class="{ active: activeMenu === 'order' }"
          @click="activeMenu = 'order'"
        >我的订单</div>
        <div 
          class="menu-item"
          :class="{ active: activeMenu === 'info' }"
          @click="activeMenu = 'info'"
        >个人信息</div>
      </div>

      <!-- 订单列表模块 -->
      <div class="content-body" v-if="activeMenu === 'order'">
        <h2 class="page-title">全部订单</h2>
        
        <el-tabs v-model="activeTab" class="order-tabs">
          <el-tab-pane label="全部订单" name="all"></el-tab-pane>
          <el-tab-pane label="待支付" name="unpaid"></el-tab-pane>
          <el-tab-pane label="待出行" name="wait"></el-tab-pane>
          <el-tab-pane label="待评价" name="review"></el-tab-pane>
        </el-tabs>

        <div class="order-list">
          <div class="order-card" v-for="order in filteredOrders" :key="order.id">
            
            <div class="card-header">
              <div class="header-left">
                <el-tag v-if="order.type === 'hotel'" type="warning" effect="dark">酒店</el-tag>
                <el-tag v-else-if="order.type === 'flight'" type="primary" effect="dark">机票</el-tag>
                <el-tag v-else-if="order.type === 'train'" type="success" effect="dark">火车票</el-tag>
                <el-tag v-else type="danger" effect="dark">景点</el-tag>
                <span class="order-title">{{ order.title }}</span>
              </div>
              <span class="order-status">{{ order.status }}</span>
            </div>

            <div class="card-body">
              <div class="info-row">
                <span class="label">订单号：</span>{{ order.id }}
              </div>
              <div class="info-row">
                <span class="label">出行时间：</span>{{ order.date }}
              </div>
              <div class="info-row">
                <span class="label">总金额：</span>
                <span class="price">¥{{ order.price }}</span>
              </div>
            </div>

            <div class="card-footer">
              <el-button size="small">查看详情</el-button>
              
              <!-- 酒店和景点：已完成状态 -->
              <template v-if="(order.type === 'hotel' || order.type === 'scenery') && order.status === '已完成'">
                <!-- 已评价 -->
                <el-button 
                  v-if="order.hasReviewed"
                  type="success" 
                  size="small" 
                  plain
                  @click="viewMyReview(order)"
                >
                  已评价
                </el-button>
                <!-- 未评价 -->
                <el-button 
                  v-else
                  type="primary" 
                  size="small" 
                  plain
                  @click="openReviewDialog(order)"
                >
                  去评价
                </el-button>
              </template>

              <el-button 
                v-if="order.type === 'train' || order.type === 'flight'"
                size="small"
                disabled
                title="此类订单暂不支持评价"
              >
                不可评价
              </el-button>
              
              <!-- 已完成的机票/火车票：显示退票 -->
              <el-button 
                v-if="(order.type === 'train' || order.type === 'flight') && order.status === '已完成'"
                type="danger" 
                size="small" 
                plain 
                @click="handleCancelOrder(order)"
              >
                退票
              </el-button>

            </div>

          </div>
        </div>
      </div>

      <!-- 个人信息模块 -->
      <div class="content-body" v-if="activeMenu === 'info'">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <h2 class="page-title">个人信息</h2>
          <el-button type="primary" link @click="openEditDialog">编辑信息</el-button>
        </div>
        
        <div class="info-section">
          <h3>基础信息</h3>
          <div class="info-item">
            <span class="label">用户名：</span>
            <span>{{ userInfo.name || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">手机号：</span>
            <span>{{ userInfo.phone || '未绑定' }}</span>
          </div>
          <div class="info-item">
            <span class="label">邮箱：</span>
            <span>{{ userInfo.email || '未绑定' }}</span>
          </div>
        </div>

        <div class="info-section" style="margin-top: 40px;">
          <h3>安全设置</h3>
          <div class="security-row">
            <div class="sec-left">
              <span class="sec-title">登录密码</span>
              <span class="sec-desc">建议您定期更改密码以保护账户安全</span>
            </div>
            <el-button type="primary" link @click="handleChangePassword">修改</el-button>
          </div>
        </div>
      </div>

    </div>

    <!-- 评价弹窗 (写评价) -->
    <el-dialog
      v-model="dialogVisible"
      title="评价订单"
      width="500px"
      destroy-on-close
    >
      <div class="review-form">
        <h3 class="review-target">{{ currentOrder.title }}</h3>
        
        <div class="form-item">
          <span class="label">总体评分：</span>
          <el-rate v-model="reviewForm.score" allow-half show-text />
        </div>

        <div class="form-item">
          <span class="label">评价内容：</span>
          <el-input
            v-model="reviewForm.content"
            type="textarea"
            :rows="4"
            placeholder="分享您的出行体验，帮助更多小伙伴..."
          />
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReview">提交评价</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看评价弹窗 -->
    <el-dialog
      v-model="viewReviewVisible"
      title="我的评价"
      width="500px"
    >
      <div class="review-view">
        <h3>{{ currentOrder.title }}</h3>
        <div class="view-score">
          <el-rate v-model="currentOrder.reviewScore" disabled show-score text-color="#ff9900" />
        </div>
        <div class="view-content">
          {{ currentOrder.reviewContent }}
        </div>
        <div class="view-time">
          评价时间：{{ currentOrder.reviewDate || '2023-10-05' }}
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="viewReviewVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑个人信息弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑个人信息"
      width="400px"
    >
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" disabled placeholder="手机号暂不支持修改" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitProfileUpdate">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { UserFilled } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProfile, updateProfile, updatePassword, logout } from '@/api/auth'
import { getUserOrders } from '@/api/order'
import { submitReview as apiSubmitReview, getReviewByOrder } from '@/api/review'

const router = useRouter()
const activeMenu = ref('order') // 'order' | 'info'
const activeTab = ref('all')
const userInfo = ref({})
const orderList = ref([])

// === 初始化 ===
onMounted(() => {
  fetchUserInfo()
  fetchUserOrders()
})

const fetchUserInfo = async () => {
  try {
    const data = await getProfile()
    userInfo.value = data
  } catch (error) {
    console.error(error)
    // 如果用户不存在（可能是 Token 对应的用户已被删除或数据库重置），强制退出
    if (error.message.includes('User not found') || error.message.includes('404')) {
      ElMessage.error('用户信息失效，请重新登录')
      handleLogout()
    }
  }
}

const fetchUserOrders = async () => {
  try {
    const res = await getUserOrders()
    if (res) {
      // 获取所有订单的评价状态
      const ordersWithReviews = await Promise.all(res.map(async (order) => {
        let hasReviewed = false
        let reviewData = null
        if (order.status === 'Paid') { // 只有已支付（已完成）的订单才检查评价
            try {
                const review = await getReviewByOrder(order.id)
                if (review) {
                    hasReviewed = true
                    reviewData = review
                }
            } catch (e) {
                // Ignore 404 or other errors
            }
        }
        
        return {
          id: order.id,
          // 根据 productId 判断类型，暂时简单处理
          type: order.productId.includes('FLIGHT') ? 'flight' : (order.productId.includes('SCENERY') ? 'scenery' : (order.productId.includes('TRAIN') ? 'train' : 'hotel')), 
          title: order.productName,
          date: order.createdAt ? order.createdAt.split('T')[0] : 'Unknown',
          price: order.amount,
          status: mapStatus(order.status),
          hasReviewed: hasReviewed,
          reviewData: reviewData
        }
      }))
      orderList.value = ordersWithReviews
    }
  } catch (error) {
    console.error('Failed to fetch orders', error)
  }
}

const mapStatus = (serverStatus) => {
  switch(serverStatus) {
    case 'Pending': return '待支付'
    case 'Paid': return '已完成'
    case 'Cancelled': return '已取消'
    case 'Refunded': return '已退款'
    default: return serverStatus
  }
}

const handleCancelOrder = async (order) => {
  const isPaid = order.status === '已完成'
  const actionText = isPaid ? '退票' : '取消订单'
  
  ElMessageBox.confirm(
    `确定要${actionText}吗？${isPaid ? '退款将原路返回。' : ''}`, 
    actionText, 
    {
      confirmButtonText: '确定',
      cancelButtonText: '暂不',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await cancelOrder(order.id)
      ElMessage.success(`${actionText}成功`)
      fetchUserOrders() // 刷新列表
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const handleLogout = async () => {
  try {
    await logout()
  } catch (e) {
    console.warn('Logout API failed', e)
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('isLoggedIn')
    router.push('/login')
  }
}


// === 控制弹窗的变量 ===
const dialogVisible = ref(false)
const viewReviewVisible = ref(false)
const editDialogVisible = ref(false)
const currentOrder = ref({}) // 当前操作的订单
const reviewForm = ref({
  score: 5,
  content: ''
})
const editForm = ref({
  name: '',
  phone: '',
  email: ''
})

// === 个人信息编辑 ===
const openEditDialog = () => {
  editForm.value = { ...userInfo.value }
  editDialogVisible.value = true
}

const submitProfileUpdate = async () => {
  try {
    await updateProfile(editForm.value)
    ElMessage.success('个人信息更新成功')
    editDialogVisible.value = false
    fetchUserInfo() // 刷新数据
  } catch (error) {
    // 错误处理已在 request.js 中统一处理
  }
}

// 简单的筛选逻辑
const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orderList.value
  if (activeTab.value === 'unpaid') return orderList.value.filter(o => o.status === '待支付')
  if (activeTab.value === 'wait') return orderList.value.filter(o => o.status === '待出行')
  if (activeTab.value === 'review') return orderList.value.filter(o => !o.hasReviewed && (o.type === 'hotel' || o.type === 'scenery') && o.status === '已完成')
  return orderList.value
})

// === 打开评价弹窗 ===
const openReviewDialog = (order) => {
  currentOrder.value = order
  reviewForm.value = { score: 5, content: '' }
  dialogVisible.value = true
}

// === 查看评价 ===
const viewMyReview = (order) => {
  currentOrder.value = order
  viewReviewVisible.value = true
}

// === 提交评价 ===
const submitReview = async () => {
  if (!reviewForm.value.content) {
    ElMessage.warning('请填写评价内容')
    return
  }

  try {
    const payload = {
      orderId: currentOrder.value.id,
      score: reviewForm.value.score,
      content: reviewForm.value.content
    }
    
    await apiSubmitReview(payload)
    
    // 更新列表状态
    const target = orderList.value.find(o => o.id === currentOrder.value.id)
    if (target) {
      target.hasReviewed = true
      target.reviewScore = reviewForm.value.score
      target.reviewContent = reviewForm.value.content
      target.reviewDate = new Date().toISOString().split('T')[0]
    }
    
    dialogVisible.value = false
    ElMessage.success('评价提交成功！感谢您的反馈')
  } catch (error) {
    console.error(error)
    // 错误已由拦截器处理
  }
}

// === 修改密码 ===
const handleChangePassword = () => {
  ElMessageBox.confirm('您正在进行敏感操作，需要验证身份。', '修改密码', {
    confirmButtonText: '继续',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 1. 输入旧密码
    ElMessageBox.prompt('请输入当前密码', '验证身份', {
      confirmButtonText: '下一步',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPattern: /.+/,
      inputErrorMessage: '请输入当前密码'
    }).then(({ value: oldPassword }) => {
      // 2. 输入新密码
      ElMessageBox.prompt('请输入新密码', '设置新密码', {
        confirmButtonText: '确认修改',
        cancelButtonText: '取消',
        inputType: 'password',
        inputPattern: /.{6,}/,
        inputErrorMessage: '密码长度至少6位'
      }).then(async ({ value: newPassword }) => {
        try {
          await updatePassword({ 
            oldPassword: oldPassword,
            newPassword: newPassword 
          })
          ElMessage.success('密码修改成功！请重新登录')
          handleLogout()
        } catch (error) {
          // Error handling is managed by request interceptor
        }
      }).catch(() => {})
    }).catch(() => {})
  }).catch(() => {})
}
</script>

<style scoped>
.user-container { min-height: 100vh; background: #f5f7fa; }
.nav-bar { background: #fff; height: 60px; box-shadow: 0 1px 4px rgba(0,0,0,0.05); display: flex; justify-content: center; }
.nav-content { width: 1200px; display: flex; justify-content: space-between; align-items: center; }
.logo { color: #0086f6; font-size: 24px; cursor: pointer; }
.user-profile { display: flex; align-items: center; gap: 10px; cursor: pointer; }
.logout { font-size: 14px; color: #999; margin-left: 10px; }
.logout:hover { color: #ff4d4f; }

.main-content { width: 1200px; margin: 20px auto; display: flex; gap: 20px; }
.sidebar { width: 200px; background: #fff; border-radius: 4px; padding: 10px 0; height: fit-content; }
.menu-item { padding: 15px 20px; cursor: pointer; color: #333; border-left: 3px solid transparent; }
.menu-item:hover { background: #f0f9ff; color: #0086f6; }
.menu-item.active { background: #f0f9ff; color: #0086f6; border-left-color: #0086f6; font-weight: bold; }

.content-body { flex: 1; background: #fff; border-radius: 4px; padding: 30px; min-height: 500px; }
.page-title { margin: 0 0 20px 0; font-size: 20px; font-weight: bold; }

.order-card { border: 1px solid #eee; border-radius: 4px; margin-top: 20px; transition: all 0.2s; }
.order-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); border-color: #d1e9ff; }
.card-header { background: #f9f9f9; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; }
.header-left { display: flex; align-items: center; gap: 10px; }
.order-title { font-weight: bold; color: #333; }
.order-status { color: #666; font-size: 14px; }
.card-body { padding: 20px; display: flex; gap: 40px; }
.info-row { display: flex; flex-direction: column; gap: 5px; }
.info-row .label { color: #999; font-size: 12px; }
.price { color: #ff4d4f; font-size: 18px; font-weight: bold; }
.card-footer { padding: 10px 20px; border-top: 1px solid #eee; text-align: right; background: #fff; border-radius: 0 0 4px 4px; }

/* 个人信息样式 */
.info-section h3 { font-size: 16px; margin-bottom: 20px; border-left: 4px solid #0086f6; padding-left: 10px; }
.info-item { display: flex; margin-bottom: 15px; font-size: 14px; }
.info-item .label { width: 100px; color: #666; }
.security-row { display: flex; justify-content: space-between; align-items: center; padding: 20px 0; border-bottom: 1px solid #eee; }
.sec-left { display: flex; flex-direction: column; gap: 5px; }
.sec-title { font-weight: bold; color: #333; }
.sec-desc { font-size: 12px; color: #999; }

/* 查看评价样式 */
.review-view h3 { margin-top: 0; }
.view-content { margin: 15px 0; color: #333; line-height: 1.6; background: #f9f9f9; padding: 15px; border-radius: 4px; }
.view-time { color: #999; font-size: 12px; text-align: right; }
</style>