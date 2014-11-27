package com.bcmclient.rest;


import com.bcmclient.client.WSClient;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Gaston
 */
@Stateless
@Path("test")
public class TestResource {

    @Context
    private UriInfo context;

    @EJB
    WSClient wsClient;

    private final Logger log = Logger.getLogger(TestResource.class.getName());

    public TestResource() {
    }

    @GET
    @Produces("application/json")
    public Response listarUsuarios() {
        boolean response = wsClient.callWS();
        if (response){
            return Response.ok("").build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
    }
}
