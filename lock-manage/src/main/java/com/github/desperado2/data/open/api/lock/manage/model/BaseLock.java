package com.github.desperado2.data.open.api.lock.manage.model;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseLock {

	public static class CacheLock extends BaseLock {

		private static final ConcurrentHashMap<String, Long> LOCKS = new ConcurrentHashMap<>();
		private static final ConcurrentHashMap<String, Long> TTLS = new ConcurrentHashMap<>();

		private long timestamp;

		public CacheLock(String key, int timeout) {
			super(key, timeout);
		}

		@Override
		public boolean getLock() {
			synchronized (key) {
				timestamp = System.currentTimeMillis();
				long ttl = timestamp + timeout * 1000L;
				if (!LOCKS.containsKey(key)) {
					return null == LOCKS.putIfAbsent(key, timestamp) && null == TTLS.putIfAbsent(key, ttl);
				}
				if (TTLS.get(key) < timestamp) {
					long exTimestamp = LOCKS.get(key);
					long exTtl = TTLS.get(key);
					return exTimestamp == LOCKS.replace(key, timestamp) && exTtl == TTLS.replace(key, ttl);
				}
				return false;
			}
		}

		@Override
		public boolean release() {
			synchronized (key) {
				long ttl = timestamp + timeout * 1000L;
				if (isHolding()) {
					return timestamp == LOCKS.remove(key) && ttl == TTLS.remove(key);
				}
				return false;
			}
		}

		@Override
		public boolean isHolding() {
			if (!LOCKS.containsKey(key) || !TTLS.containsKey(key)) {
				return false;
			}
			return timestamp == LOCKS.get(key);
		}
	}

	public static class RedisLock extends BaseLock {

		private long currentTime;

		RedisUtil redisUtils;

		public RedisLock(RedisUtil redisUtils, String key, int timeout) {
			super(key, timeout);
			this.redisUtils = redisUtils;
		}

		@Override
		public boolean getLock() {
			currentTime = System.currentTimeMillis();
			return redisUtils.set(key, String.valueOf(currentTime), (long) timeout);
		}

		@Override
		public boolean release() {
			if (isHolding()) {
				return redisUtils.remove(key);
			}
			return false;
		}

		@Override
		public boolean isHolding() {
			long value = Long.parseLong(redisUtils.get(key));
			return currentTime == value;
		}

	}

	protected final String key;
	protected final int timeout;

	public BaseLock(String key, int timeout) {
		this.key = key;
		this.timeout = timeout;
	}

	public abstract boolean getLock();

	public abstract boolean release();

	public abstract boolean isHolding();
}
