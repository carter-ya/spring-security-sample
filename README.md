## 介绍
基于`Spring Boot, Spring Data JPA, Spring Security, Swagger-UI`的样例，适合于前后端分离场景。
## 如何使用
1. 适当修改`src/main/resources/schema.sql`结构（可使用默认）
2. 在数据库中执行`SQL`定义
3. 运行`SecurityApplication`
4. 访问`localhost:8080/swagger-ui.html`
5. `UserController`使用用户登录接口登录并获取`token`(令牌)
> 用户名:admin 密码:1234qwer