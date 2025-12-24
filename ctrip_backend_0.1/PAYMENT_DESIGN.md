# 支付流程设计文档

## 1. 概述

本文档旨在为当前的旅游出行应用设计一个简单的支付流程。设计将涵盖数据库变更和后端架构设计，支持机票、火车票和酒店的独立支付流程。

## 2. 数据库设计

为了实现订单与支付的分离以及不同业务线的隔离，我们设计了独立的订单表，并统一通过支付表管理交易状态。

### 2.1 订单表设计

针对三种不同的业务（机票、火车、酒店），设计三张独立的订单表。

#### 2.1.1 机票订单表 (tb_flight_orders)

```sql
CREATE TABLE tb_flight_orders (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36) NOT NULL,
    flight_id CHAR(36) NOT NULL,
    cabin_id CHAR(36) NOT NULL, -- 对应舱位
    passenger_info JSON NOT NULL, -- 乘客信息列表
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'CANCELLED', 'REFUNDED')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (flight_id) REFERENCES tb_flights(id),
    FOREIGN KEY (cabin_id) REFERENCES tb_flight_cabins(id)
);

CREATE INDEX idx_flight_orders_user ON tb_flight_orders(user_id);
```

#### 2.1.2 火车票订单表 (tb_train_orders)

```sql
CREATE TABLE tb_train_orders (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36) NOT NULL,
    train_id CHAR(36) NOT NULL,
    seat_id CHAR(36) NOT NULL, -- 对应席别
    passenger_info JSON NOT NULL, -- 乘客信息列表
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'CANCELLED', 'REFUNDED')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (train_id) REFERENCES tb_trains(id),
    FOREIGN KEY (seat_id) REFERENCES tb_train_seats(id)
);

CREATE INDEX idx_train_orders_user ON tb_train_orders(user_id);
```

#### 2.1.3 酒店订单表 (tb_hotel_orders)

```sql
CREATE TABLE tb_hotel_orders (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36) NOT NULL,
    hotel_id CHAR(36) NOT NULL,
    room_id CHAR(36) NOT NULL, -- 对应房型
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    guest_info JSON NOT NULL, -- 入住人信息
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'CANCELLED', 'REFUNDED')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (hotel_id) REFERENCES tb_hotels(id),
    FOREIGN KEY (room_id) REFERENCES tb_hotel_rooms(id)
);

CREATE INDEX idx_hotel_orders_user ON tb_hotel_orders(user_id);
```

### 2.2 支付记录表 (tb_payments)

统一管理所有业务的支付交易记录。

```sql
CREATE TABLE tb_payments (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    order_id CHAR(36) NOT NULL, -- 关联具体的订单ID
    order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('FLIGHT', 'TRAIN', 'HOTEL')),
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(20) CHECK (payment_method IN ('ALIPAY', 'WECHAT', 'CREDIT_CARD')),
    transaction_id VARCHAR(100), -- 第三方支付平台的交易流水号
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    paid_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_payments_order ON tb_payments(order_id, order_type);
```

## 3. 后端设计

### 3.1 实体类 (Entities)

在 `top.potatohub.ctrip.backend.entities` 包下新增以下实体类：

*   `FlightOrder`, `TrainOrder`, `HotelOrder`
*   `Payment`

### 3.2 数据访问层 (Mapper)

在 `top.potatohub.ctrip.backend.mapper` 包下新增以下接口：

*   `FlightOrderMapper`: 管理机票订单的 CRUD。
*   `TrainOrderMapper`: 管理火车票订单的 CRUD。
*   `HotelOrderMapper`: 管理酒店订单的 CRUD。
*   `PaymentMapper`: 管理支付记录的 CRUD。

### 3.3 业务逻辑层 (Service)

采用策略模式或分别独立的 Service 来处理不同类型的订单，统一的 PaymentService 处理支付逻辑。

#### 3.3.1 订单服务接口

```java
// 通用订单服务接口（可选，用于统一规范）
public interface OrderService<T> {
    T createOrder(T orderDTO);
    T getOrderById(String id);
    void updateOrderStatus(String id, String status);
}
```

具体实现：
*   `FlightOrderService`: 处理机票库存校验、锁座、订单创建。
*   `TrainOrderService`: 处理火车票库存校验、席位锁定、订单创建。
*   `HotelOrderService`: 处理酒店房态校验、订单创建。

#### 3.3.2 支付服务 (PaymentService)

负责处理支付请求、与第三方支付网关交互（模拟）、回调处理。

**核心方法：**

*   `createPayment(String orderId, String orderType, BigDecimal amount, String method)`: 创建支付记录，返回支付参数（如支付链接或二维码内容）。
*   `processPaymentCallback(String paymentId, boolean success, String transactionId)`: 处理支付结果回调。
    1.  更新 `tb_payments` 状态。
    2.  根据 `order_type` 调用对应的 OrderService 更新订单状态为 `PAID`。

### 3.4 控制层 (Controller)

*   `OrderController`: (或者拆分为 `FlightOrderController` 等)
    *   `POST /api/orders/flight`: 创建机票订单
    *   `POST /api/orders/train`: 创建火车订单
    *   `POST /api/orders/hotel`: 创建酒店订单
    *   `GET /api/orders/{type}/{id}`: 查询订单详情

*   `PaymentController`:
    *   `POST /api/payments`: 发起支付
        *   参数: `orderId`, `orderType`, `paymentMethod`
    *   `POST /api/payments/callback`: (模拟) 接收支付回调

## 4. 业务流程

### 4.1 下单流程

1.  用户在前端选择航班/车次/酒店及相应规格。
2.  前端调用对应的创建订单 API (e.g., `/api/orders/flight`)。
3.  后端 Service 层校验库存/价格。
4.  后端创建订单记录，状态为 `PENDING`。
5.  返回订单 ID 给前端。

### 4.2 支付流程

1.  前端携带订单 ID 调用 `/api/payments`。
2.  `PaymentService` 创建 `tb_payments` 记录，状态为 `PENDING`。
3.  后端返回模拟的支付跳转 URL 或参数。
4.  用户完成支付（模拟）。
5.  支付网关回调 `/api/payments/callback`。
6.  `PaymentService` 验证回调，更新 `tb_payments` 为 `SUCCESS`。
7.  `PaymentService` 根据订单类型，调用对应 OrderService 将订单状态更新为 `PAID`。
8.  (可选) 发送预订成功通知。

## 5. 接口定义示例 (DTO)

### 5.1 创建机票订单请求

```java
public class CreateFlightOrderRequest {
    private String flightId;
    private String cabinId;
    private List<PassengerDTO> passengers;
    private String contactName;
    private String contactPhone;
}
```

### 5.2 发起支付请求

```java
public class PaymentRequest {
    private String orderId;
    private String orderType; // "FLIGHT", "TRAIN", "HOTEL"
    private String paymentMethod;
}
```

### 5.3 支付响应

```java
public class PaymentResponse {
    private String paymentId;
    private String payUrl; // 模拟的支付链接
    private String status;
}
```
