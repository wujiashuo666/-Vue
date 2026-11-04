-- ============================================================
-- V12 删除聊天/系统通知相关表（CS 化清理）
-- 在线聊天模块已整体移除（ChatController/WsController/WebSocketConfig
-- 及前端 FriendChat/store 聊天逻辑），其依赖的 sysmsg / msgcontent
-- 两张表（系统消息通知）一并删除；chatmsg 表历史上未创建。
-- ============================================================

DROP TABLE IF EXISTS `sysmsg`;
DROP TABLE IF EXISTS `msgcontent`;
