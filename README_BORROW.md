# 图书借阅管理模块API文档

## 概述
图书借阅管理模块提供了完整的图书借阅、归还、查询和统计功能。

## API接口

### 1. 借阅图书
- **接口地址**: `POST /api/borrow`
- **请求参数**:
```json
{
  "bookId": 1,
  "remarks": "学习需要"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "message": "借阅成功",
  "data": {
    "borrowRecord": {
      "id": 1,
      "userId": 2,
      "bookId": 1,
      "borrowDate": "2024-01-15T10:30:00",
      "dueDate": "2024-02-15T10:30:00",
      "status": 1,
      "remarks": "学习需要"
    },
    "dueDate": "2024-02-15T10:30:00"
  }
}
```

### 2. 归还图书
- **接口地址**: `POST /api/borrow/return`
- **请求参数**:
```json
{
  "borrowRecordId": 1,
  "remarks": "已读完"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "message": "归还成功",
  "data": {
    "borrowRecord": {
      "id": 1,
      "returnDate": "2024-02-10T14:20:00",
      "status": 2
    },
    "returnDate": "2024-02-10T14:20:00"
  }
}
```

### 3. 获取借阅记录列表
- **接口地址**: `GET /api/borrow/records`
- **请求参数**:
  - `userId`: 用户ID（可选）
  - `bookId`: 图书ID（可选）
  - `status`: 状态（1-借阅中，2-已归还，3-逾期）（可选）
  - `keyword`: 关键词搜索（可选）
  - `page`: 页码（默认1）
  - `size`: 每页大小（默认10）
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 2,
        "bookId": 1,
        "borrowDate": "2024-01-15T10:30:00",
        "dueDate": "2024-02-15T10:30:00",
        "returnDate": "2024-02-10T14:20:00",
        "status": 2,
        "username": "zhangsan",
        "realName": "张三",
        "bookTitle": "红楼梦",
        "bookAuthor": "曹雪芹",
        "bookIsbn": "9787020002207",
        "categoryName": "文学小说"
      }
    ],
    "total": 10,
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

### 4. 获取借阅历史
- **接口地址**: `GET /api/borrow/history`
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "borrowHistory": [
      {
        "id": 1,
        "userId": 2,
        "bookId": 1,
        "borrowDate": "2024-01-15T10:30:00",
        "dueDate": "2024-02-15T10:30:00",
        "returnDate": "2024-02-10T14:20:00",
        "status": 2,
        "bookTitle": "红楼梦",
        "bookAuthor": "曹雪芹",
        "bookIsbn": "9787020002207",
        "categoryName": "文学小说"
      }
    ],
    "total": 1
  }
}
```

### 5. 获取逾期记录（管理员）
- **接口地址**: `GET /api/borrow/overdue`
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "overdueRecords": [
      {
        "id": 5,
        "userId": 4,
        "bookId": 4,
        "borrowDate": "2024-01-10T08:30:00",
        "dueDate": "2024-02-10T08:30:00",
        "status": 3,
        "username": "wangwu",
        "realName": "王五",
        "bookTitle": "史记",
        "bookAuthor": "司马迁",
        "bookIsbn": "9787108018274",
        "categoryName": "历史传记"
      }
    ],
    "total": 1
  }
}
```

### 6. 获取即将到期的记录
- **接口地址**: `GET /api/borrow/expiring`
- **请求参数**:
  - `expireDays`: 即将到期天数（默认3天）
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "expiringRecords": [
      {
        "id": 2,
        "userId": 2,
        "bookId": 3,
        "borrowDate": "2024-02-01T09:15:00",
        "dueDate": "2024-03-01T09:15:00",
        "status": 1,
        "username": "zhangsan",
        "realName": "张三",
        "bookTitle": "Python编程：从入门到实践",
        "bookAuthor": "Eric Matthes",
        "bookIsbn": "9787115428028",
        "categoryName": "科技计算机"
      }
    ],
    "total": 1
  }
}
```

### 7. 获取借阅统计
- **接口地址**: `GET /api/borrow/statistics`
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "totalBorrows": 10,
    "currentBorrows": 5,
    "overdueCount": 2
  }
}
```

### 8. 获取热门图书统计
- **接口地址**: `GET /api/borrow/popular`
- **请求参数**:
  - `limit`: 返回数量（默认10）
- **响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "popularBooks": [
      {
        "bookId": 1,
        "bookTitle": "红楼梦",
        "bookAuthor": "曹雪芹",
        "bookIsbn": "9787020002207",
        "categoryName": "文学小说",
        "borrowCount": 5
      }
    ],
    "total": 10
  }
}
```

## 业务规则

### 借阅规则
1. 用户最多可同时借阅5本图书
2. 每本图书借阅期限为30天
3. 用户不能重复借阅同一本图书
4. 只有库存大于0的图书才能借阅
5. 被禁用的用户无法借阅图书

### 归还规则
1. 用户只能归还自己借阅的图书
2. 管理员可以归还任何图书
3. 已归还的图书不能重复归还
4. 归还后自动恢复图书库存

### 状态说明
- `1`: 借阅中
- `2`: 已归还
- `3`: 逾期

### 库存变动类型
- `1`: 入库
- `2`: 出库
- `3`: 借出
- `4`: 归还

## 定时任务
系统每天凌晨2点自动更新借阅状态，将已过期的借阅记录状态更新为逾期（状态3）。

## 错误码说明
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 权限不足
- `500`: 服务器内部错误 