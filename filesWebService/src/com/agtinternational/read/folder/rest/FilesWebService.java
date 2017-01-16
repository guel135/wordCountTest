package com.agtinternational.read.folder.rest;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.agtinternational.entities.Directory;
import com.agtinternational.service.ReadFolderService;
import com.agtinternational.service.impl.ReadFolderServiceImpl;
import com.google.gson.Gson;

@Path("/")
public class FilesWebService {

	@GET
	@Path("/files")
	@Produces(MediaType.TEXT_PLAIN)

	public Response verifyRESTService(@Context UriInfo info,
			@QueryParam("url") String url,
			@DefaultValue("txt") @QueryParam("extension") String extension,@DefaultValue("100") @QueryParam("longerthan") int wordAmount,
			@DefaultValue("50") @QueryParam("morethan") int wordRepeat) {
		
		ReadFolderService readFolderService=new ReadFolderServiceImpl();
		Directory baseDirectory = null;
		try {
			baseDirectory = readFolderService.read(url,extension, wordAmount, wordRepeat);
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(baseDirectory);
		
		return Response.ok(jsonInString, MediaType.APPLICATION_JSON).build();

	}
}
