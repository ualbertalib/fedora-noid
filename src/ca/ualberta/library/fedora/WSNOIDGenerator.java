package ca.ualberta.library.fedora;

import java.io.File;
import java.io.IOException;
 
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fcrepo.server.management.PIDGenerator;
import org.fcrepo.server.errors.ModuleInitializationException;
import org.fcrepo.common.MalformedPIDException;
import org.fcrepo.common.PID;
 
import org.fcrepo.server.Module;
import org.fcrepo.server.Server;
import org.fcrepo.server.errors.ModuleInitializationException;
import org.fcrepo.server.storage.ConnectionPoolManager;
 
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class WSNOIDGenerator 
    extends Module
	implements PIDGenerator {

    /** Logger for this class. */
    private static final Logger logger =
            LoggerFactory.getLogger(WSNOIDGenerator.class);

    private HttpClient m_client;
    private PID m_lastPid;
 
    /**
     * Constructs a WSNOIDGenerator.
     * 
     * @param moduleParameters
     *        A pre-loaded Map of name-value pairs comprising the intended
     *        configuration of this Module.
     * @param server
     *        The <code>Server</code> instance.
     * @param role
     *        The role this module fulfills, a java class name.
     * @throws ModuleInitializationException
     *         If initilization values are invalid or initialization fails for
     *         some other reason.
     */
    public WSNOIDGenerator(Map moduleParameters, Server server, String role)
            throws ModuleInitializationException {
        super(moduleParameters, server, role);
    }
 
    @Override
    public void initModule() {
    }
 
    /**
     * Get a reference to the HttpClient
     */
    @Override
    public void postInitModule() throws ModuleInitializationException {
    	m_client = new HttpClient();
    	m_client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
    	
    }
    
    public PID generatePID(String namespaceID) throws IOException {
    	HttpMethod method = null;
 
    	String url = getUrl(namespaceID);
    	logger.info(url);
 
    	method = new PostMethod(url);
    	
    	//method.setFollowRedirects(true);
 
    	String responseBody = null;
    	String candidatePid = null;
    	try{
             m_client.executeMethod(method);
             responseBody = method.getResponseBodyAsString();
//           candidatePid = responseBody.substring(0, responseBody.indexOf("\n"));
	         candidatePid = namespaceID + ":" + responseBody.substring(responseBody.indexOf(":")+2, 11);
	         logger.error(candidatePid);
             m_lastPid = new PID(candidatePid);
        } catch (HttpException he) {
        	 logger.error("Http error connecting to '" + url + "'");
        	 logger.error(he.getMessage());
        } catch (IOException ioe){
             throw ioe;
        } catch (MalformedPIDException e) {
             throw new IOException(e.getMessage());
        }
 
   	
        logger.info("NOID: " + m_lastPid.getObjectId());
        
       	return m_lastPid;
 
    }
 
    public PID getLastPID() throws IOException {
        return m_lastPid;
    }
 
    public void neverGeneratePID(String pid) throws IOException {
    	//nop
    }
 
    private String getUrl(String namespaceID) {
	    String base = "http://dundee/nd/noidu_{nsID}?mint+1";
	    String url = base.replaceAll("\\{nsID\\}", namespaceID);
	    return url;
    }
}
