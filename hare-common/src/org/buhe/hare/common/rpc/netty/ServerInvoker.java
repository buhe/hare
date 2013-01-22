package org.buhe.hare.common.rpc.netty;

import org.buhe.hare.common.rpc.support.Invocation;

interface ServerInvoker {
	Object invoke(Invocation invocation) throws Exception;
}