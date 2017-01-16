package com.agtinternational.service.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.agtinternational.entities.BigFile;
import com.agtinternational.entities.Directory;
import com.agtinternational.entities.GeneralFile;
import com.agtinternational.entities.Word;
import com.agtinternational.service.ReadFolderService;
import com.agtinternational.service.impl.ReadFolderServiceImpl;

public class ReadFolderServiceTest {

	ReadFolderService service;

	@Test(expected = NoSuchFileException.class)
	public void readNotExistingDirectory() throws IOException {
		service = new ReadFolderServiceImpl();
		service.read("/dog/miau", "txt", 100, 100);
	}

	@Test
	public void readExistingDirectoryWithTxtExtension() throws IOException {
		service = new ReadFolderServiceImpl();

		File file = new File("src/test/resources");
		String absolutePath = file.getAbsolutePath();

		Directory result = new Directory(absolutePath);

		Directory a = new Directory("a");

		BigFile reynolds = new BigFile("Reinolds.txt");
		reynolds.setWordCount(new Long(832));
		Word the = new Word("the", new Long(60));
		Word aWord = new Word("a", new Long(29));
		Word and = new Word("and", new Long(26));
		Word was = new Word("was", new Long(28));

		List<Word> words = new ArrayList<>();
		words.add(the);
		words.add(aWord);
		words.add(and);
		words.add(was);

		reynolds.setWords(words);

		GeneralFile andromeda = new GeneralFile("Andromeda.txt");
		andromeda.setWordCount(new Long(271));

		GeneralFile mordellistena = new GeneralFile("Mordellistena.txt");
		mordellistena.setWordCount(new Long(47));

		List<GeneralFile> filesA = new ArrayList<GeneralFile>();

		filesA.add(reynolds);
		filesA.add(andromeda);
		filesA.add(mordellistena);

		a.setFiles(filesA);

		Directory b = new Directory("b");

		GeneralFile document = new GeneralFile("document.txt");
		document.setWordCount(new Long(0));

		List<GeneralFile> filesB = new ArrayList<GeneralFile>();
		filesB.add(document);

		b.setFiles(filesB);

		Map<String, Directory> subs = new HashMap<String, Directory>();
		subs.put("a", a);
		subs.put("b", b);

		result.setSubdirectories(subs);

		Directory directory = service.read(absolutePath, "txt", 500, 25);

		assertEquals("Directories are not the same", result, directory);

	}

}
