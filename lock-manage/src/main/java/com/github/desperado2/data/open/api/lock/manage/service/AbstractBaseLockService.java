package com.github.desperado2.data.open.api.lock.manage.service;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.lock.manage.enums.CheckEntityEnum;
import com.github.desperado2.data.open.api.lock.manage.enums.LockType;
import com.github.desperado2.data.open.api.lock.manage.factory.LockFactory;
import com.github.desperado2.data.open.api.lock.manage.model.BaseLock;
import org.springframework.stereotype.Component;


@Component
public abstract class AbstractBaseLockService {

	protected BaseLock getLock(CheckEntityEnum entity, String name, Long domainId) {

		return LockFactory.getLock(
				entity.getSource().toUpperCase() + Constants.AT_SYMBOL + name + Constants.AT_SYMBOL + domainId, 5,
				LockType.REDIS);
	}

	protected void releaseLock(BaseLock lock) {
		// workaround for very high concurrency
		// do nothing, wait for the service layer transaction to commit
	}
}
