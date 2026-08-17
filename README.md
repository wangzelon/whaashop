# WhaaShop

WhaaShop 是一个 Java 17 + Spring Boot 4.1 + Vue 3 的模块化单体商城 MVP。工程由后端 `server`、管理端和用户商城组成，主题色为橙色。

## 环境要求

- JDK 17（Maven Enforcer 会拒绝其他主版本）
- Maven 3.9+
- Node.js 22+、pnpm 11+
- MySQL 8、Redis、MinIO、Milvus、Ollama
- Ollama 模型：`ollama pull gemma3:4b`、`ollama pull qwen3-embedding:4b`

项目不编排基础设施。复制 `server/.env.example`，将变量注入终端或 IDE 后再启动服务。首次启动由 Flyway 创建 `whaashop` 库内表结构；数据库本身需提前创建。

开发环境初始化账号：后台 `admin / password`，商城用户 `demo / password`。这些账号仅用于本地演示，部署前必须修改或删除。

## 启动

```powershell
cd server
mvn spring-boot:run
```

```powershell
cd client
pnpm install
pnpm dev:shop   # http://localhost:5173
pnpm dev:admin  # http://localhost:5174
```

后端默认地址为 `http://localhost:8080`，OpenAPI UI 位于 `http://localhost:8080/swagger-ui/index.html`。前端可通过 `VITE_API_BASE_URL` 修改 API 根地址。

## 核心规则

- 管理员与普通用户使用 JWT 角色隔离。注册接口只创建普通用户；管理员应通过安全的初始化 SQL 或运维流程创建，密码必须先 BCrypt 加密。
- 订单状态：待支付 → 已支付 → 管理员虚拟发货 → 用户确认收货 → 已完成。
- 每个已完成订单项仅能首次评价一次；首次评价 1–5 星、文字及最多 9 图；每条评价最多追评 3 次。
- 用户可维护头像、昵称、300 字个人简介、性别和生日；头像使用独立 MinIO 对象前缀，用户名和角色不可通过资料接口修改。
- 商品富文本由服务端 Jsoup 白名单清洗后保存，图片通过 MinIO 富文本目录上传。
- 智能客服使用 `gemma3:4b`，知识向量使用 `qwen3-embedding:4b` 和 Milvus 2560 维 COSINE collection。
- 支付采用 `PaymentGateway` 抽象；未配置支付宝沙箱时会返回明确错误，不会模拟支付成功。沙箱异步回调地址必须可被支付宝公网访问。

## 构建验证

```powershell
cd server
mvn test

cd ../client
pnpm build
```

## 目录

- `server/src/main/java/com/whaa/shop`：按业务领域划分的后端模块
- `server/src/main/resources/db/migration`：MySQL Flyway 迁移
- `client/apps/admin-web`：运营管理后台
- `client/apps/shop-web`：响应式用户商城
- `client/packages/shared`：共享 API 客户端和 TypeScript 类型
