-- 插入用户数据
INSERT INTO `sys_user` (`username`, `password`, `name`, `gender`, `age`, `avatar`) VALUES
('admin', '123456', '超级管理员', 1, 30, 'https://example.com/avatar/admin.jpg'),
('manager', '123456', '管理员', 1, 28, 'https://example.com/avatar/manager.jpg'),
('user', '123456', '普通用户', 0, 25, 'https://example.com/avatar/user.jpg');

-- 插入角色数据
INSERT INTO `sys_role` (`name`, `code`, `description`) VALUES
('超级管理员', 'ROLE_SUPER_ADMIN', '拥有所有权限'),
('管理员', 'ROLE_ADMIN', '拥有大部分权限'),
('普通用户', 'ROLE_USER', '拥有基本权限');

-- 插入权限数据 - 菜单
INSERT INTO `sys_permission` (`name`, `code`, `type`, `url`, `parent_id`, `sort`) VALUES
('系统管理', 'SYSTEM_MANAGE', 'MENU', '/system', 0, 1),
('用户管理', 'USER_MANAGE', 'MENU', '/system/user', 1, 1),
('角色管理', 'ROLE_MANAGE', 'MENU', '/system/role', 1, 2),
('权限管理', 'PERMISSION_MANAGE', 'MENU', '/system/permission', 1, 3),
('商品管理', 'PRODUCT_MANAGE', 'MENU', '/product', 0, 2),
('商品列表', 'PRODUCT_LIST', 'MENU', '/product/list', 5, 1),
('商品分类', 'PRODUCT_CATEGORY', 'MENU', '/product/category', 5, 2);

-- 插入权限数据 - 按钮
INSERT INTO `sys_permission` (`name`, `code`, `type`, `url`, `parent_id`, `sort`) VALUES
('用户新增', 'USER_ADD', 'BUTTON', NULL, 2, 1),
('用户编辑', 'USER_EDIT', 'BUTTON', NULL, 2, 2),
('用户删除', 'USER_DELETE', 'BUTTON', NULL, 2, 3),
('角色新增', 'ROLE_ADD', 'BUTTON', NULL, 3, 1),
('角色编辑', 'ROLE_EDIT', 'BUTTON', NULL, 3, 2),
('角色删除', 'ROLE_DELETE', 'BUTTON', NULL, 3, 3),
('商品新增', 'PRODUCT_ADD', 'BUTTON', NULL, 6, 1),
('商品编辑', 'PRODUCT_EDIT', 'BUTTON', NULL, 6, 2),
('商品删除', 'PRODUCT_DELETE', 'BUTTON', NULL, 6, 3);

-- 插入权限数据 - API接口
INSERT INTO `sys_permission` (`name`, `code`, `type`, `url`, `parent_id`, `sort`) VALUES
('用户列表接口', 'API_USER_LIST', 'API', '/api/user/list', 2, 1),
('用户详情接口', 'API_USER_DETAIL', 'API', '/api/user/{id}', 2, 2),
('用户新增接口', 'API_USER_ADD', 'API', '/api/user', 2, 3),
('用户修改接口', 'API_USER_UPDATE', 'API', '/api/user', 2, 4),
('用户删除接口', 'API_USER_DELETE', 'API', '/api/user/{id}', 2, 5),
('商品列表接口', 'API_PRODUCT_LIST', 'API', '/api/product/list', 6, 1),
('商品详情接口', 'API_PRODUCT_DETAIL', 'API', '/api/product/{id}', 6, 2),
('商品新增接口', 'API_PRODUCT_ADD', 'API', '/api/product', 6, 3),
('商品修改接口', 'API_PRODUCT_UPDATE', 'API', '/api/product', 6, 4),
('商品删除接口', 'API_PRODUCT_DELETE', 'API', '/api/product/{id}', 6, 5);

-- 用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- 超级管理员拥有超级管理员角色
(2, 2), -- 管理员拥有管理员角色
(3, 3); -- 普通用户拥有普通用户角色

-- 角色权限关联 - 超级管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `sys_permission`;

-- 角色权限关联 - 管理员拥有部分权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 2, id FROM `sys_permission` WHERE `code` NOT IN ('PERMISSION_MANAGE', 'API_USER_DELETE', 'API_PRODUCT_DELETE');

-- 角色权限关联 - 普通用户拥有基本权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 3, id FROM `sys_permission` WHERE `type` = 'MENU' OR `code` IN ('API_PRODUCT_LIST', 'API_PRODUCT_DETAIL');