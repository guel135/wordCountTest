package com.agtinternational.service;

import java.io.IOException;

import com.agtinternational.entities.Directory;

public interface ReadFolderService {

	 public Directory read(String folder,String extension, int wordAmount, int wordRepeat) throws IOException;

}
