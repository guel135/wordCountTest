package com.agtinternational.service;

import com.agtinternational.entities.Directory;

public interface ReadFolderService {

	 public Directory read(String folder, long wordAmount, long wordRepeat);

}
