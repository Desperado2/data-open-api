-- 业务标识
local businessTag = KEYS[1]

-- 默认并发量
local limit_default = tonumber(KEYS[3] or 100)

-- 限流开关标识
local tag = KEYS[2]

-- 限流时间范围
local time_scope = KEYS[4]

-- 限流计数器
local key = tag..":counter:"..businessTag

-- 限流开关，当request_limit_on_off 设置大于零时，关闭限流开关
local request_limit_on_off = tonumber(redis.call('get', tag..":request_limit_on_off") or 0)
if(request_limit_on_off > 0) then
    return 1
end

-- 限流大小
local limit = tonumber(redis.call('get', tag..":request_limit") or limit_default)

-- 获取key，当前请求数
local v = redis.call('get', key)

-- 限流时间窗 如果没有设置默认为1秒(单位/秒)
-- 流量限制在单个时间窗内有效
local timeout_setting = tonumber(time_scope or 1)

-- 初始化限流key数量
local current = tonumber(0)
if not v then
    redis.call("set", key ,"0")
    redis.call("expire", key , timeout_setting)
else
    current = tonumber(v)
end

if current + 1 > limit then --如果超出限流大小
    return 0
else --请求数+1
    redis.call("INCRBY", key ,"1")
    return (current + 1)
end