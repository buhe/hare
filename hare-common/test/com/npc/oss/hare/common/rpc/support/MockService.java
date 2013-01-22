package com.npc.oss.hare.common.rpc.support;

public class MockService implements MockInterface{

	@Override
	public String hello() {
		System.out.println("hello");
		return "hello";
	}

	@Override
	public String ex() throws Exception {
		throw new RuntimeException("EXXX");
	}

}
