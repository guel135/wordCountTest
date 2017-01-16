package com.agtinternational.read.folder.rest;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.agtinternational.entities.Directory;
import com.agtinternational.service.ReadFolderService;
import com.agtinternational.service.impl.ReadFolderServiceImpl;
import com.google.gson.Gson;

@Path("/")
public class FilesWebService {

	/**
	 * @param url
	 *            : url should start with /
	 * @param extension
	 *            : extension of the files that should be filter in. By default
	 *            is txt
	 * @param wordAmount:
	 *            amount of word after a file is big.
	 *             By default is 1000
	 * @param wordRepeat:
	 *            repetition number of the same word after the word will be
	 *            display (if is a file bigger than wordAmount)
	 *            Default 50
	 * 
	 * @return 200 and json response if all is ok
	 * 	       Bad Request if there was any problem
	 */
	@GET
	@Path("/files")
	@Produces(MediaType.TEXT_PLAIN)

	public Response verifyRESTService(@QueryParam("url") String url,
			@DefaultValue("txt") @QueryParam("extension") String extension,
			@DefaultValue("1000") @QueryParam("longerthan") int wordAmount,
			@DefaultValue("50") @QueryParam("morethan") int wordRepeat) {

		ReadFolderService readFolderService = new ReadFolderServiceImpl();
		Directory baseDirectory = null;
		try {
			baseDirectory = readFolderService.read(url, extension, wordAmount, wordRepeat);
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Gson gson = new Gson();
		String jsonInString = gson.toJson(baseDirectory);

		return Response.ok(jsonInString, MediaType.APPLICATION_JSON).build();

	}
}
