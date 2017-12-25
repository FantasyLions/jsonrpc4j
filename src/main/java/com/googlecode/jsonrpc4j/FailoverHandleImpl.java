package com.googlecode.jsonrpc4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>The default if the mainline request fails, immediately send a rollback operation, whether to send rollback success enabled with the line before resending failed requests</p>
 * @author zl.shi
 *
 */
public class FailoverHandleImpl implements FailoverHandle {
	
	private static final Logger logger = LoggerFactory.getLogger(FailoverHandleImpl.class);
	
	private String primaryBaseUrl;
	
	private String failOverBaseUrl;
	
	
	@Override
	public Object doSomething( String methodName, Object argument, String originalUrl ) {
		
		// To prevent infinite loop request
		if ( originalUrl.equals(failOverBaseUrl) ) {
			logger.error("The failover failed! This url is the same with failover url, So won't repeat send the request again.");
			return null;
		}
		
		
		return sendToNewTarget( methodName, argument );
		
	}
	
	
	/**
	 * Request the standby server
	 * @param methodName
	 * @param argument
	 * @return
	 */
	public Object sendToNewTarget( String methodName, Object argument ) {
		
		if ( failOverBaseUrl == null ) {
			logger.warn("No failOverBaseUrl found!");
			return null;
		}
		
		try {
			logger.warn("Connection timeout or exception so reverse and try it again by another url: {}", failOverBaseUrl);
			JsonRpcHttpClient client = getSimpleClient( failOverBaseUrl );

			return client.invoke(methodName, argument, List.class);
			
		} catch (MalformedURLException e) {
			logger.error( "Failed to sendToNewTarget!", e );
		} catch (Throwable e) {
			logger.error( "Failed to sendToNewTarget!", e );
		}
		return null;
	}
	
	
	/**
	 * To obtain an initialization JsonRpcHttpClient
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public JsonRpcHttpClient getSimpleClient( String url ) throws MalformedURLException {
		JsonRpcHttpClient client = new JsonRpcHttpClient( new URL(url) );
//		client.setConnectionTimeoutMillis(GivexConstant.getConnectionTimeOutMillis());
//		client.setReadTimeoutMillis(GivexConstant.getReadTimeoutMillis());
		client.setConnectionTimeoutMillis(60 * 1000);
		client.setReadTimeoutMillis(60 * 1000 * 2);
		return client;
	}


	public void setFailOverBaseUrl(String failOverBaseUrl) {
		this.failOverBaseUrl = failOverBaseUrl;
	}

	public void setPrimaryBaseUrl(String primaryBaseUrl) {
		this.primaryBaseUrl = primaryBaseUrl;
	}
	

}
