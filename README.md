# JavaWeb Travel Project

一个使用 Spring Boot、MyBatis、MySQL、Vue 3 与 Vite 实现的旅行预订演示项目，包含酒店、机票、火车票、景点、订单和用户中心等功能。

## 本地运行

后端需要 Java 8+ 和 MySQL。启动前至少配置以下环境变量：

```text
DB_URL=jdbc:mysql://localhost:3306/ctrip_db
DB_USERNAME=root
DB_PASSWORD=<your-database-password>
JWT_SECRET=<at-least-32-random-bytes>
```

仅在本地演示时可额外启用：

```text
SEED_DEMO_DATA=true
MOCK_PAYMENT_ENABLED=true
```

`SEED_DEMO_DATA` 会创建演示账号并刷新部分演示数据；`MOCK_PAYMENT_ENABLED` 会启用不具备真实签名校验的伪支付宝流程，二者都不应在生产环境开启。

```powershell
cd ctrip_backend_0.1
.\mvnw.cmd spring-boot:run
```

前端需要 Node.js 20.19+：

```powershell
cd ctrip-clone
npm ci
npm run dev
```

## 验证

```powershell
cd ctrip_backend_0.1
.\mvnw.cmd test

cd ..\ctrip-clone
npm ci
npm run build
```

## 安全说明

- JWT 密钥和数据库凭据必须通过环境变量提供，不要提交到仓库。
- 用户资料接口只允许修改昵称和头像；邮箱、手机号、密码和权限需要独立验证流程。
- 当前未提供公开的“忘记密码”接口；接入邮件或短信服务并实现一次性验证码后再启用。
- 历史 SHA-512 密码会在用户成功登录时自动迁移到 BCrypt；完成迁移后应删除旧哈希兼容代码。
