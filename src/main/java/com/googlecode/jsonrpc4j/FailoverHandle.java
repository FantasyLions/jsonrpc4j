package com.googlecode.jsonrpc4j;


/**
 * <p>当一个地址失效或者超出设置的时间，自动切换到备用地址。
 * 因为这里有可能已经把请求发送到了目标地址，对方服务器可能已经做了相应的操作，所以需要将前面的操作进行回滚</p>
 * @author zl.shi
 *
 */
public interface FailoverHandle {
	
	public Object doSomething( String methodName, Object argument, String originalUrl );

}
