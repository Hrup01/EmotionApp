# 情绪App后端技术说明文档

## 1. 项目概述

### 1.1 项目简介
情绪App是一个基于Spring Boot的情绪日记管理应用，旨在帮助用户记录和管理日常情绪状态，提供情绪分析和社区交流功能。

### 1.2 技术栈
- **Spring Boot 3.5.5**: 后端框架，提供快速开发和自动配置
- **MyBatis 3.0.5**: 持久层框架，简化数据库操作和SQL映射
- **MySQL 8.0**: 关系型数据库，存储核心业务数据
- **Redis**: 内存数据库，用于缓存和会话管理
- **Spring Security**: 安全框架，提供认证和授权
- **JWT (JSON Web Token)**: 无状态认证机制
- **Maven**: 项目构建和依赖管理工具
- **Logback**: 日志框架，提供灵活的日志配置
- **Lombok**: 代码生成工具，减少样板代码
- **BCrypt**: 密码加密算法
- **Jackson**: JSON序列化和反序列化
- **Hibernate Validator**: 数据验证框架

## 2. 项目架构

### 2.1 整体架构
```
src/main/java/com/groupb/
├── EmotionAppApplication.java     # 启动类
├── config/                        # 配置类
│   ├── RedisConfig.java
│   └── StaticResourceConfig.java
├── controller/                     # 控制器层
│   ├── CommunityController.java
│   ├── EmotionController.java
│   ├── LoginController.java
│   ├── FileUploadController.java
│   └── EmotionApiController.java
├── service/                        # 服务层
│   ├── CommunityService.java
│   ├── EmotionService.java
│   ├── UserService.java
│   ├── RedisService.java
│   └── serviceImpl/
├── mapper/                         # 数据访问层
├── pojo/                          # 实体类
│   ├── User.java
│   ├── EmotionDiary.java
│   ├── CommunityPost.java
│   └── dto/                       # 数据传输对象
├── util/                          # 工具类
│   ├── jwt/                       # JWT工具
│   ├── security/                  # 安全工具
│   └── EmotionColorUtil.java
└── websocket/                     # WebSocket支持
```

### 2.2 分层架构
- **Controller层**: 处理HTTP请求，参数验证，调用Service层
- **Service层**: 业务逻辑处理，事务管理
- **Mapper层**: 数据访问，SQL映射
- **POJO层**: 实体类和DTO定义

## 3. 核心模块详解

### 3.1 用户认证模块

#### 3.1.1 功能特性
- 用户注册/登录
- JWT Token认证
- 密码BCrypt加密
- 用户状态管理

#### 3.1.2 核心类
- `LoginController`: 处理登录/注册请求
- `UserService`: 用户业务逻辑
- `JwtTokenProvider`: JWT Token生成和验证

#### 3.1.3 API接口
```
POST /api/auth/login      # 用户登录
POST /api/auth/register   # 用户注册
GET  /api/auth/health     # 健康检查
```

### 3.2 情绪日记模块

#### 3.2.1 功能特性
- 情绪日记CRUD操作
- 12种情绪类型支持
- 连续打卡统计
- 日历视图展示
- 背景音乐支持

#### 3.2.2 支持的情绪类型
```
开心, 伤心, 自责, 晕, 邪恶, 生气, 困, 期待, 无奈, 疑问, 满足, 叹气
```

#### 3.2.3 核心类
- `EmotionController`: 情绪日记API控制器
- `EmotionService`: 情绪日记业务逻辑
- `EmotionDiary`: 情绪日记实体类
- `EmotionDTO`: 情绪日记数据传输对象

#### 3.2.4 API接口
```
POST   /api/emotion/diary                    # 保存情绪日记
GET    /api/emotion/diary?date=2024-01-01    # 获取指定日期日记
GET    /api/emotion/diaries/recent           # 获取最近日记
GET    /api/emotion/diaries/range             # 获取时间范围内日记
DELETE /api/emotion/diary/{id}                # 删除日记
GET    /api/emotion/check-in-count            # 获取连续打卡天数
GET    /api/emotion/calendar                  # 获取日历数据
POST   /api/emotion/quick-diary               # 快速记录情绪
```

### 3.3 社区模块

#### 3.3.1 功能特性
- 帖子发布和管理
- 点赞/取消点赞
- 评论系统
- 用户关注/取消关注
- 私信功能
- 收藏功能
- 社区封禁管理

#### 3.3.2 核心类
- `CommunityController`: 社区功能API控制器
- `CommunityService`: 社区业务逻辑
- `CommunityPost`: 帖子实体类
- `CommunityComment`: 评论实体类
- `PrivateMessage`: 私信实体类

#### 3.3.3 API接口
```
POST   /api/community/posts                    # 发布帖子
GET    /api/community/feed                    # 获取动态流
GET    /api/community/posts/{id}              # 获取帖子详情
POST   /api/community/posts/{id}/like         # 点赞帖子
DELETE /api/community/posts/{id}/like         # 取消点赞
POST   /api/community/posts/{id}/comments     # 添加评论
GET    /api/community/posts/{id}/comments     # 获取评论列表
POST   /api/community/follow/{userId}         # 关注用户
DELETE /api/community/follow/{userId}         # 取消关注
POST   /api/community/dm/{userId}             # 发送私信
GET    /api/community/dm/{userId}             # 获取私信对话
```

### 3.4 文件上传模块

#### 3.4.1 功能特性
- 图片文件上传
- 文件类型验证
- 文件大小限制
- 静态资源访问

#### 3.4.2 配置参数
```yaml
file:
  upload:
    path: F:/image                    # 上传路径
    max-size: 10485760                # 最大文件大小(10MB)
    allowed-types:                    # 允许的文件类型
      - image/jpeg
      - image/png
      - image/gif
      - image/webp
```

## 4. 数据库设计

### 4.1 核心表结构

#### 4.1.1 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    status ENUM('ONLINE', 'OFFLINE', 'DISABLED', 'DELETED'),
    last_login_time TIMESTAMP NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 4.1.2 情绪日记表 (emotion_diaries)
```sql
CREATE TABLE emotion_diaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    diary_date DATE NOT NULL,
    emotion_type VARCHAR(20) NOT NULL,
    content TEXT,
    background_music VARCHAR(50),
    mood_color VARCHAR(10),
    location VARCHAR(100),
    check_in_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 4.1.3 社区帖子表 (community_post)
```sql
CREATE TABLE community_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    author_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    images_json JSON NULL,
    like_count INT NOT NULL DEFAULT 0,
    comment_count INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 4.2 索引设计
- 用户表: `idx_username`, `idx_status`
- 情绪日记表: `idx_user_date`, `idx_user_emotion`, `idx_diary_date`
- 社区帖子表: `idx_author_created`, `idx_created`

## 5. 配置说明

### 5.1 应用配置 (application.yml)
```yaml
spring:
  application:
    name: EmotionApp
  datasource:
    url: jdbc:mysql://localhost:3306/emotionApp
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: Hrup041001.
  data:
    redis:
      host: 192.168.80.130
      port: 6379
      password: 123321
      database: 0

server:
  port: 8080

jwt:
  secret: mySecretKey123456789012345678901234567890
  expiration: 86400000  # 24小时
```

### 5.2 Maven依赖
```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.5</version>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
```

## 6. 安全机制

### 6.1 JWT认证流程
1. 用户登录成功后生成JWT Token
2. Token包含用户信息，有效期24小时
3. 后续请求在Header中携带Token
4. 服务端验证Token有效性

### 6.2 密码安全
- 使用BCrypt算法加密存储密码
- 密码长度最少6位
- 支持密码重置功能

### 6.3 跨域配置
```java
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
```

## 7. 缓存策略

### 7.1 Redis配置
- 主机: 192.168.80.130
- 端口: 6379
- 数据库: 0
- 连接池: 最大8个连接

### 7.2 缓存应用
- 用户会话信息
- 热点数据缓存
- 临时数据存储

## 8. 日志管理

### 8.1 日志配置
```yaml
logging:
  level:
    com.groupb: DEBUG
    org.springframework.web: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 8.2 日志文件
- 日志文件位置: `logs/emotion-app-{date}-{index}.log`
- 支持按日期滚动
- 包含详细的操作记录

## 9. 部署说明

### 9.1 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 9.2 部署步骤
1. 克隆项目代码
2. 配置数据库连接信息
3. 执行SQL初始化脚本
4. 配置Redis连接
5. 编译打包: `mvn clean package`
6. 运行应用: `java -jar target/EmotionApp-0.0.1-SNAPSHOT.jar`

### 9.3 端口配置
- 应用端口: 8080
- 数据库端口: 3306
- Redis端口: 6379

## 10. API响应格式

### 10.1 统一响应结构
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        // 具体数据
    },
    "timestamp": "2024-01-01T12:00:00"
}
```

### 10.2 错误码说明
- 200: 成功
- 400: 请求参数错误
- 401: 未授权
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器内部错误

## 11. 性能优化

### 11.1 数据库优化
- 合理设计索引
- 使用连接池
- 分页查询优化

### 11.2 缓存优化
- Redis缓存热点数据
- 合理设置缓存过期时间
- 避免缓存穿透

### 11.3 代码优化
- 使用Lombok减少样板代码
- 合理使用事务
- 异常处理优化

## 12. 扩展功能

### 12.1 已实现功能
- 情绪日记管理
- 社区交流
- 用户认证
- 文件上传
- 私信功能

### 12.2 可扩展功能
- AI情绪分析
- 周报统计
- 情绪趋势分析
- 社交推荐算法
- 消息推送

---

**文档版本**: v2.0  
**最后更新**: 2025年10月  
**维护人员**: 我觉得不对开发团队
