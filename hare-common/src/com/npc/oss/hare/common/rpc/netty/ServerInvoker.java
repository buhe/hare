package com.npc.oss.hare.common.rpc.netty;

import com.npc.oss.hare.common.rpc.support.Invocation;

interface ServerInvoker {
	Object invoke(Invocation invocation) throws Exception;
}