package com.agtinternational.service.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.junit.Test;

import com.agtinternational.entities.BigFile;
import com.agtinternational.entities.Directory;
import com.agtinternational.entities.GeneralFile;
import com.agtinternational.entities.Word;
import com.agtinternational.service.ReadFolderService;
import com.agtinternational.service.impl.ReadFolderServiceImpl;

public class ReadFolderServiceTest {


	
	ReadFolderService service;

	@Test (expected=NoSuchFileException.class)
	public void readNotExistingDirectory() throws IOException {
		service=new ReadFolderServiceImpl();
		service.read("/dog/miau","txt", 100, 100);
	}
	@Test 
	public void readExistingDirectoryWithTxtExtension() throws IOException {
		service=new ReadFolderServiceImpl();
		
		
		
		
		
		File file=new File("src/test/resources");
		String absolutePath=file.getAbsolutePath();

		
		Directory directory=service.read(absolutePath,"txt", 500, 5);
//		Directory directory=service.read("/home/guel/testdata","txt", 500, 5);
		List<GeneralFile> files=directory.getSubdirectories().get("a").getFiles();
		
		
		
		for (GeneralFile archive : files) {
			System.out.println("@@@@@@@@"+archive.getName());
			
			if (archive instanceof BigFile) {
				System.out.println("--------"+archive.getName());
				BigFile dummy = (BigFile) archive;

				for (Word word : dummy.getWords()) {
					System.out.println(word.getWord() + "repetitions: " + word.getRepetition());
				}
				
			}
		};
		
	}

}
