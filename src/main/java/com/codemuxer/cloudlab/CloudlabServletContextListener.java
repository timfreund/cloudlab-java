package com.codemuxer.cloudlab;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
//import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class CloudlabServletContextListener implements ServletContextListener {
	//private final static Logger log = Logger.getLogger(CloudlabServletContextListener.class.getName());
	private final static Log log = LogFactory.getLog(CloudlabServletContextListener.class.getName());
	private String deploymentId = null;
	private String scorekeeperUrl = "http://cloudlab.codemuxer.com/scorekeeper/deployment";
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// no op
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		log.info(this.getClass().getName() + " context initializing");

		String deploymentIdParameter = context.getInitParameter("deploymentId");
		if((deploymentIdParameter != null) && (!deploymentIdParameter.isEmpty())){
			log.info("setting deploymentId to " + deploymentIdParameter);
			deploymentId = deploymentIdParameter;
		}
		
		String scorekeeperUrlParameter = context.getInitParameter("scorekeeperUrl");
		if((scorekeeperUrlParameter != null) && (!scorekeeperUrlParameter.isEmpty())){
			log.info("setting scorekeeperUrl to " + scorekeeperUrlParameter);
			scorekeeperUrl = scorekeeperUrlParameter;
		}
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(scorekeeperUrl);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("deploymentId", deploymentId));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(post);
			StatusLine status = response.getStatusLine();
			status.getStatusCode();
			status.getReasonPhrase();
		} catch(Exception e){
			log.error(e.getMessage(), e);
		}
		log.info(this.getClass().getName() + " context initialized");
	}
}
