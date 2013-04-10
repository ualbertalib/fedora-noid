package ca.ualberta.library.fedora;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fcrepo.common.MalformedPIDException;
import org.fcrepo.common.PID;

public class testNOID {
	
 	private static final Log LOG = LogFactory.getLog(testNOID.class);

	public static void main(String[] args) {
		HttpClient m_client;
		PID m_lastPid;
		HttpMethod method = null;
		
		m_client = new HttpClient();
		m_client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
		
		
//     	HttpMethod method = new GetMethod("http://dundee/nd/noidu_kt5?mint+1");
	    	
		method = new PostMethod("http://dundee/nd/noidu_kt5?mint+1");
		//method.setFollowRedirects(true);
	
		String responseBody = null;
		String candidatePid = null;
		try{
	         m_client.executeMethod(method);
	         responseBody = method.getResponseBodyAsString();
	         candidatePid = responseBody.substring(responseBody.indexOf(":")+2, responseBody.length()-1);
	         LOG.error(candidatePid);
	         m_lastPid = new PID(candidatePid);
	    } catch (HttpException he) {
	         System.err.println(he.getMessage());
	    } catch (IOException ioe){
	         ioe.getMessage();
	    } catch (MalformedPIDException e) {
	         e.getMessage();
	    }
	}
	
    private String getUrl(String namespaceID) {
	    String base = "http://dundee/nd/noidu_{nsID}?mint+1";
	    String url = base.replaceAll("\\{nsID\\}", namespaceID);
	    return url;
    }
}
