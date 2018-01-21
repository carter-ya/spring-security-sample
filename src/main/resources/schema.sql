DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  f_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version    BIGINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_username   VARCHAR(50)     NOT NULL
  COMMENT '用户名',
  f_name       VARCHAR(50)     NOT NULL DEFAULT ''
  COMMENT '姓名',
  f_password   CHAR(60)        NOT NULL
  COMMENT '密码',
  f_state      TINYINT         NOT NULL DEFAULT 1
  COMMENT '用户状态 1 valid 有效 2 locked 冻结 3 invalid 无效',
  f_created_at BIGINT UNSIGNED NOT NULL
  COMMENT '创建时间',
  f_updated_at BIGINT UNSIGNED NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_username (f_username)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '用户表';

DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
  f_id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version      BIGINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_role_name_en VARCHAR(20)     NOT NULL
  COMMENT '角色英文名称',
  f_role_name_cn VARCHAR(100)    NOT NULL
  COMMENT '角色中文名称',
  f_state        TINYINT         NOT NULL DEFAULT 1
  COMMENT '角色状态 1 valid 有效 2 invalid 删除',
  f_created_at   BIGINT UNSIGNED NOT NULL
  COMMENT '创建时间',
  f_updated_at   BIGINT UNSIGNED NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_role_name_en (f_role_name_en)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '角色表';

DROP TABLE IF EXISTS t_authority;
CREATE TABLE t_authority (
  f_id         BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version    BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_type       TINYINT UNSIGNED NOT NULL
  COMMENT '权限类型 1 menu 菜单; 2 resource 资源',
  f_link_id    BIGINT UNSIGNED  NOT NULL
  COMMENT '关联表主键',
  f_state      TINYINT          NOT NULL DEFAULT 1
  COMMENT '权限状态 1 valid 有效 2 invalid 删除',
  f_created_at BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_type_link_id (f_type, f_link_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '权限表';

DROP TABLE IF EXISTS t_menu;
CREATE TABLE t_menu (
  f_id         BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version    BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_parent_id  BIGINT UNSIGNED  NOT NULL DEFAULT 0
  COMMENT '父级菜单ID',
  f_menu_name  VARCHAR(100)     NOT NULL
  COMMENT '菜单名称',
  f_url        VARCHAR(100)     NOT NULL
  COMMENT '菜单连接',
  f_icon       VARCHAR(20)      NOT NULL DEFAULT ''
  COMMENT '图标',
  f_state      TINYINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '状态 1 valid 有效 2 invalid 删除',
  f_created_at BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '菜单表';

DROP TABLE IF EXISTS t_resource;
CREATE TABLE t_resource (
  f_id            BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version       BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_resource_name VARCHAR(100)     NOT NULL DEFAULT ''
  COMMENT '资源名称',
  f_url           VARCHAR(100)     NOT NULL
  COMMENT '资源链接',
  f_state         TINYINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '状态 1 valid 有效 2 invalid 删除',
  f_created_at    BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at    BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '资源表';

DROP TABLE IF EXISTS t_role_authority;
CREATE TABLE t_role_authority (
  f_id           BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version      BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_role_id      BIGINT UNSIGNED  NOT NULL
  COMMENT '角色ID',
  f_authority_id BIGINT UNSIGNED  NOT NULL
  COMMENT '权限ID',
  f_state        TINYINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '状态 1 valid 有效 2 invalid 删除',
  f_created_at   BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at   BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_role_id_authority_id (f_role_id, f_authority_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '角色权限映射表';

DROP TABLE IF EXISTS t_role_user;
CREATE TABLE t_role_user (
  f_id         BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version    BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_role_id    BIGINT UNSIGNED  NOT NULL
  COMMENT '角色ID',
  f_user_id    BIGINT UNIQUE    NOT NULL
  COMMENT '用户ID',
  f_state      TINYINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '状态 1 valid 有效 2 invalid 删除',
  f_created_at BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_role_id_user_id (f_role_id, f_user_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '角色用户映射表';

DROP TABLE IF EXISTS t_user_authority;
CREATE TABLE t_user_authority (
  f_id           BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version      BIGINT UNSIGNED  NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_user_id      BIGINT           NOT NULL
  COMMENT '用户ID',
  f_authority_id BIGINT UNSIGNED  NOT NULL
  COMMENT '权限ID',
  f_state        TINYINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '状态 1 valid 有效 2 invalid 删除',
  f_created_at   BIGINT UNSIGNED  NOT NULL
  COMMENT '创建时间',
  f_updated_at   BIGINT UNSIGNED  NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_user_id_authority_id (f_user_id, f_authority_id)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '用户权限映射表';

DROP TABLE IF EXISTS t_user_token;
CREATE TABLE t_user_token (
  f_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  f_version    BIGINT UNSIGNED NOT NULL DEFAULT 1
  COMMENT '乐观锁版本号',
  f_username   VARCHAR(50)     NOT NULL
  COMMENT '用户名',
  f_token      CHAR(32)        NOT NULL
  COMMENT '令牌',
  f_expired_at BIGINT UNSIGNED NOT NULL
  COMMENT '过期时间',
  f_created_at BIGINT UNSIGNED NOT NULL
  COMMENT '创建时间',
  f_updated_at BIGINT UNSIGNED NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (f_id),
  UNIQUE KEY uniq_username (f_username),
  UNIQUE KEY uniq_token (f_token)
)
  ENGINE InnoDB
  DEFAULT CHARSET utf8mb4
  COMMENT '用户令牌表';

## 初始化基础权限信息

## 初始化用户
## 初始密码:1234qwer
INSERT INTO t_user (f_id, f_username, f_password, f_created_at, f_updated_at) VALUE
  (1, 'admin', '$2a$10$RZreGrE0bNz0.IoQaA2GyOMsahcwnvHSOsNe5gwV0Vj0M4/9NeRKm', 1516349780171, 1516349780171);

## 初始化角色
INSERT INTO t_role (f_id, f_role_name_en, f_role_name_cn, f_created_at, f_updated_at) VALUES
  (1, 'ADMIN', '管理员', 1516349780171, 1516349780171),
  (2, 'REVIEW_ADMIN', '审核管理员', 1516349780171, 1516349780171),
  (3, 'COMPANY_ADMIN', '企业管理员', 1516349780171, 1516349780171),
  (4, 'USER_ADMIN', '用户管理员', 1516349780171, 1516349780171);

## 初始化菜单
INSERT INTO t_menu (f_id, f_parent_id, f_menu_name, f_url, f_icon, f_created_at, f_updated_at) VALUES
  (1, 0, '企业认证管理', '', '', 1516349780171, 1516349780171),
  (2, 1, '审核列表', '', '', 1516349780171, 1516349780171),
  (3, 1, '审核历史', '', '', 1516349780171, 1516349780171),
  (4, 0, '用户管理', '', '', 1516349780171, 1516349780171),
  (5, 4, '企业用户列表', '', '', 1516349780171, 1516349780171),
  (6, 4, '个人用户列表', '', '', 1516349780171, 1516349780171);

## 初始化权限-菜单映射
INSERT INTO t_authority (f_id, f_type, f_link_id, f_created_at, f_updated_at) VALUES
  (1, 1, 1, 1516349780171, 1516349780171),
  (2, 1, 2, 1516349780171, 1516349780171),
  (3, 1, 3, 1516349780171, 1516349780171),
  (4, 1, 4, 1516349780171, 1516349780171),
  (5, 1, 5, 1516349780171, 1516349780171),
  (6, 1, 6, 1516349780171, 1516349780171);

## 初始化角色-权限映射
INSERT INTO t_role_authority (f_id, f_role_id, f_authority_id, f_created_at, f_updated_at) VALUES
  (1, 1, 1, 1516349780171, 1516349780171),
  (2, 1, 2, 1516349780171, 1516349780171),
  (3, 1, 3, 1516349780171, 1516349780171),
  (4, 1, 4, 1516349780171, 1516349780171),
  (5, 1, 5, 1516349780171, 1516349780171),
  (6, 1, 6, 1516349780171, 1516349780171);

## 初始化用户-角色映射
INSERT INTO t_role_user (f_id, f_role_id, f_user_id, f_created_at, f_updated_at) VALUES
  (1, 1, 1, 1516349780171, 1516349780171);