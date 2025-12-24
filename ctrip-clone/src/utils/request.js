import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
  baseURL: '', // 留空，由 api 调用处的 /api 或 /auth 前缀决定，配合 Vite 代理
  timeout: 5000 // 请求超时时间
})

// request 拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    if (token) {
      // 让每个请求携带自定义 token
      // 根据后端 JwtUtils 实现，通常放在 Authorization 头中，或者直接作为参数
      // 这里假设是 Bearer token 标准格式，或者后端直接读取 header
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    // 对请求错误做些什么
    console.log(error)
    return Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据后端的 Result 结构 (code: 1 success, 0 failure)
    // 如果 code 不为 1，则判断为错误
    if (res.code !== 1) {
      ElMessage({
        message: res.msg || res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.msg || res.message || 'Error'))
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error) // for debug
    console.log(error.response)
    
    let message = 'Request Error'
    if (error.response && error.response.data) {
        // 尝试匹配不同的错误格式
        message = error.response.data.message || error.response.data.error || error.response.data.msg || error.message
    } else {
        message = error.message
    }

    ElMessage({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
