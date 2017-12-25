package com.googlecode.jsonrpc4j;


/**
 * <p>When an address failure or beyond the time of setting, automatic switch to the alternate address.
 * Because there may have been the request is sent to the destination address, the other server may have to do the corresponding operation, so need to be at the front of the operation are rolled back</p>
 * @author zl.shi
 *
 */
public interface FailoverHandle {
	
	public Object doSomething( String methodName, Object argument, String originalUrl );

}
