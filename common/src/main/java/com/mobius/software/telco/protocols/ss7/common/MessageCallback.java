package com.mobius.software.telco.protocols.ss7.common;

import com.mobius.software.common.dal.timers.TaskCallback;

public interface MessageCallback<T extends Exception> extends TaskCallback<T>
{
	void onSuccess(String aspName);

	@Override
	default void onSuccess() {
		onSuccess(null);
	}

	@Override
	void onError(T ex);

	static MessageCallback<Exception> EMPTY = new MessageCallback<Exception>() {
		@Override
		public void onSuccess(String aspName) {
			// stub method without any logic
		}

		@Override
		public void onSuccess() {
			// stub method without any logic
		}

		@Override
		public void onError(Exception exception) {
			// stub method without any logic
		}
	};
}
