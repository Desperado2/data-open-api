/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package com.github.desperado2.data.open.api.lock.manage.factory;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import com.github.desperado2.data.open.api.common.manage.config.SpringContextUtils;
import com.github.desperado2.data.open.api.lock.manage.enums.LockType;
import com.github.desperado2.data.open.api.lock.manage.model.BaseLock;
import org.springframework.stereotype.Component;


@Component
public class LockFactory {

	private static RedisUtil staticRedisUtils;
	
	public static BaseLock getLock(String key, int timeout, LockType type) {
		switch (type) {
		case REDIS:
			staticRedisUtils = SpringContextUtils.getApplicationContext().getBean(RedisUtil.class);
			if(staticRedisUtils == null){
				throw new RuntimeException("Redis配置不存在");
			}
			return new BaseLock.RedisLock(staticRedisUtils, key, timeout);
		default:
			return new BaseLock.CacheLock(key, timeout);
		}
	}
	
	public static BaseLock getLock(String key, int timeout) {
		return getLock(key, timeout, LockType.LOCAL);
	}
}
