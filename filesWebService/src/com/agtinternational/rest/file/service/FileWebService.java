package com.agtinternational.rest.file.service;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class FileWebService  {

//	@PUT
//	@Path("/files")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response files(InputStream incomingData) {
//		StringBuilder crunchifyBuilder = new StringBuilder();
//		try {
//			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				crunchifyBuilder.append(line);
//			}
//		} catch (Exception e) {
//			System.out.println("Error Parsing: - ");
//		}
//		System.out.println("Data Received: " + crunchifyBuilder.toString());
// 
//		// return HTTP response 200 in case of success
//		return Response.status(200).entity(crunchifyBuilder.toString()).build();
//	}
	@GET
	@Path("/files")
	@Produces(MediaType.TEXT_PLAIN)
	
	public Response verifyRESTService(@Context UriInfo info,
	
		
		@QueryParam("url") String url,
		@DefaultValue("100")@QueryParam("longerthan") long wordlimit,
		@DefaultValue("50") @QueryParam("morethan") long wordamount) {

		String result = "Hello Service";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result+" "+url+" "+wordamount+" "+wordlimit).build();
	}

}
