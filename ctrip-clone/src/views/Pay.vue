<template>
  <div class="pay-container">
    <div class="pay-box">
      <div class="pay-header">
        <h2>收银台</h2>
        <p>订单提交成功，请尽快支付！</p>
      </div>

      <div class="order-info">
        <div class="row">
          <span>订单金额</span>
          <span class="price">¥{{ price }}</span>
        </div>
        <div class="row">
          <span>订单名称</span>
          <span class="name">{{ name }}</span>
        </div>
      </div>

      <div class="pay-methods">
        <p>选择支付方式</p>
        <div class="methods">
          <div 
            class="method-item" 
            :class="{ active: payType === 'wechat' }"
            @click="payType = 'wechat'"
          >
            <el-icon color="#09bb07" size="24"><ChatDotRound /></el-icon>
            <span>微信支付</span>
          </div>
          <div 
            class="method-item" 
            :class="{ active: payType === 'alipay' }"
            @click="payType = 'alipay'"
          >
            <el-icon color="#1677ff" size="24"><Money /></el-icon>
            <span>支付宝</span>
          </div>
        </div>
      </div>

      <div class="qrcode-section" v-if="payType === 'wechat'">
        <div class="qrcode-box">
          <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=PayMock" alt="Payment QR">
          <div v-if="loading" class="loading-mask">
            <el-icon class="is-loading" size="30" color="#fff"><Loading /></el-icon>
            <p>正在支付...</p>
          </div>
        </div>
        <p class="tip">请使用微信扫一扫</p>
      </div>
      
      <div class="alipay-section" v-else>
         <el-icon color="#1677ff" size="60"><Money /></el-icon>
         <p class="tip">将跳转至支付宝收银台</p>
      </div>

      <el-button type="primary" size="large" class="pay-btn" @click="handlePay" :loading="loading">
        {{ payType === 'alipay' ? '立即支付' : '我已完成支付' }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ChatDotRound, Money, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { createOrder, payOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()

const price = ref(0)
const name = ref('')
const payType = ref('wechat')
const loading = ref(false)
const orderId = ref(null)

onMounted(() => {
  // 从路由参数里获取金额和名字
  price.value = route.query.price || 999
  name.value = route.query.name || '携程旅行订单'
})

const productId = ref('')

onMounted(() => {
  // 从路由参数里获取金额和名字
  price.value = route.query.price || 999
  name.value = route.query.name || '携程旅行订单'
  productId.value = route.query.id || (name.value.includes('机票') ? 'FLIGHT_TEST' : 'HOTEL_TEST')
})

const handlePay = async () => {
  // Check if logged in
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 1. Create Order first
  if (!orderId.value) {
    try {
      loading.value = true
      const res = await createOrder({
        productId: productId.value,
        productName: name.value,
        amount: price.value
      })
      if (res && res.id) {
        orderId.value = res.id
      } else {
        ElMessage.error('创建订单失败')
        loading.value = false
        return
      }
    } catch (e) {
      console.error(e)
      ElMessage.error('创建订单失败')
      loading.value = false
      return
    }
  }

  // 2. Proceed to Pay
  if (payType.value === 'alipay') {
    loading.value = true
    const outTradeNo = orderId.value
    const subject = name.value
    const totalAmount = price.value
    // Use absolute URL for backend redirect
    window.location.href = `http://localhost:8080/alipay/pay?outTradeNo=${outTradeNo}&subject=${subject}&totalAmount=${totalAmount}`
    return
  }

  // WeChat Pay Mock
  loading.value = true
  console.log('Starting WeChat mock pay for order:', orderId.value)
  // 模拟网络请求，2秒后支付成功
  setTimeout(async () => {
    try {
        console.log('Calling payOrder API for:', orderId.value)
        await payOrder(orderId.value)
        console.log('payOrder API success')
        loading.value = false
        ElMessage.success('支付成功！')
        // 跳转到个人中心
        setTimeout(() => {
          router.push('/user')
        }, 1000)
    } catch (e) {
        loading.value = false
        ElMessage.error('支付失败，请重试')
    }
  }, 2000)
}
</script>

<style scoped>
.pay-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding-top: 50px;
  display: flex;
  justify-content: center;
}
.pay-box {
  background: #fff;
  width: 100%;
  max-width: 480px;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  height: fit-content;
}
.pay-header {
  text-align: center;
  margin-bottom: 30px;
}
.pay-header h2 { margin: 0 0 10px; font-size: 24px; color: #333; }
.pay-header p { margin: 0; color: #666; }

.order-info {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  color: #666;
}
.row:last-child { margin-bottom: 0; }
.price { color: #f60; font-weight: bold; font-size: 18px; }
.name { color: #333; }

.pay-methods { margin-bottom: 30px; }
.methods { display: flex; gap: 15px; margin-top: 10px; }
.method-item {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
}
.method-item:hover { border-color: #409eff; color: #409eff; }
.method-item.active {
  border-color: #409eff;
  background: #ecf5ff;
  color: #409eff;
}

.qrcode-section {
  text-align: center;
  margin-bottom: 30px;
}
.qrcode-box {
  width: 150px;
  height: 150px;
  margin: 0 auto 10px;
  position: relative;
  border: 1px solid #eee; padding: 5px;
}
.qrcode-box img { width: 100%; height: 100%; }
.loading-mask {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.7);
  color: #fff;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
}
.tip { font-size: 12px; color: #999; }

.alipay-section {
  text-align: center;
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.pay-btn { width: 100%; font-weight: bold; letter-spacing: 1px; }
</style>
