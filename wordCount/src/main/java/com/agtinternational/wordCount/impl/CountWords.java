package com.agtinternational.wordCount.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.agtinternational.wordCount.entities.BigFile;
import com.agtinternational.wordCount.entities.Directory;
import com.agtinternational.wordCount.entities.GeneralFile;
import com.agtinternational.wordCount.entities.Word;
import com.google.gson.Gson;

public class CountWords {

	private static final String ONLY_LETTERS_REGEX = "\\p{L}+";
	private static final String CHARSET_ISO = "ISO-8859-1";
	static Directory baseDirectory = new Directory("baseFolder");

	public static void main(String args[]) throws IOException {

		// String fileName = "/home/guel/agt/testdata/a/perro/Berlin.txt";
		// String fileName2 = "home/guel/agt/testdata/b/juan/manolo";
		// String fileName3 = "home/guel/agt/testdata/b/perro/paco";
		String folder = "/home/guel/testdata";
		readFoldersWalk(folder);
		//
		// ArrayList<String> cortes = new ArrayList<String>();
		// cortes.addAll(Arrays.asList(folder.split("/")));
		// baseDirectory = addSubdirectory(baseDirectory, cortes);
		//
		// cortes.addAll(Arrays.asList(fileName2.split("/")));
		// baseDirectory = addSubdirectory(baseDirectory, cortes);
		// cortes.addAll(Arrays.asList(fileName3.split("/")));
		// baseDirectory = addSubdirectory(baseDirectory, cortes);

		// System.out.println(baseDirectory);
		Gson gson = new Gson();
		String jsonInString = gson.toJson(baseDirectory);
		System.out.println(jsonInString);
		// addToDirectoryTree(folder, fileName, folder);

		// readFolders(folder);
		// readFile(fileName);
	}

	private static void readFoldersWalk(String folder) throws IOException {

		baseDirectory.setName(folder);
		
		try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
			paths.forEach(filePath -> {
				System.out.println(filePath);
				ArrayList<String> splittedPaths = new ArrayList<String>();
				String filePathName = filePath.toString().replaceAll(folder, "");
				splittedPaths.addAll(Arrays.asList(filePathName.split("/")));
				if (splittedPaths.get(0).equals("/"))
					splittedPaths.remove(0);
				if (Files.isDirectory(filePath)) {
					baseDirectory = addSubdirectory(null, baseDirectory, splittedPaths);

					// if (baseDirectory.getSubdirectories() == null)
					// baseDirectory.setSubdirectories(new HashMap<String,
					// Directory>());
					// baseDirectory.getSubdirectories().put(filePath.toString(),
					// new Directory(filePath.toString()));

				}

				// System.out.println("Directory " + filePath);

				if (Files.isRegularFile(filePath)) {
					// System.out.println("File" + filePath);
					try {

						GeneralFile file = readFile(filePath.toString());
						if (file instanceof BigFile) {
							BigFile bigFile = (BigFile) file;
							System.out.println("Big" + bigFile.getName() + "words:" + bigFile.getWordCount());
						}
						if (file instanceof GeneralFile) {
							GeneralFile generalFile = (GeneralFile) file;
							System.out
									.println("Small " + generalFile.getName() + "words:" + generalFile.getWordCount());
						}
						baseDirectory = addSubdirectory(file, baseDirectory, splittedPaths);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private static Directory addSubdirectory(GeneralFile file, Directory parentDirectory,
			ArrayList<String> splittedPaths) {
		String name = splittedPaths.get(0);
		System.out.println(name);
		splittedPaths.remove(0);
		// System.out.println(name + " cortes " + cortes);
		if (splittedPaths.size() == 0 && file != null) {

			System.out.println("entra if add FILE" + file);
			if (parentDirectory.getFiles() == null) {
				parentDirectory.setFiles(new ArrayList<GeneralFile>());
			}
			parentDirectory.getFiles().add(file);

		} else {
			Directory subdirectory;

			if (parentDirectory.getSubdirectories() != null && parentDirectory.getSubdirectories().containsKey(name)) {
				subdirectory = parentDirectory.getSubdirectories().get(name);

			} else {
				subdirectory = new Directory(name);
			}
			if (splittedPaths.size() > 0) {
				subdirectory = addSubdirectory(file, subdirectory, splittedPaths);
				System.out.println("entra if add subdirectory name:" + name + file);
			}
			if (parentDirectory.getSubdirectories() == null)
				parentDirectory.setSubdirectories(new HashMap<String, Directory>());
			parentDirectory.getSubdirectories().put(name, subdirectory);
		}
		return parentDirectory;
	}

	private static void countInLine(String line, Map<String, Long> result) {
		// System.out.println("result entrada " + result);
		Map<String, Long> counter = Arrays.asList(line.split(",|\\s+|\\.")).stream().map(String::toLowerCase)
				.filter(word -> word.length() > 0).filter(word -> word.matches(ONLY_LETTERS_REGEX))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		counter.forEach((key, value) -> result.merge(key, value, Long::sum));

		// System.out.println(counter);
		// System.out.println("result " + result);
		// System.out.println("total of words " + sum + "\n \n");
	}

	public static GeneralFile readFile(String fileName) throws IOException {

		String cleanName=cleanFileName(fileName);
		
		Map<String, Long> result = new HashMap<>();
		Files.lines(Paths.get(fileName), Charset.forName(CHARSET_ISO)).filter(line -> line.length() > 0)
				.forEach(String -> {
					countInLine(String, result);
				});

		
		
		long sumWords = result.values().stream().mapToLong(Long::longValue).sum();
		// System.out.println(sumWords);
		if (sumWords > 1000) {
			BigFile file = new BigFile(cleanName);
			file.setWordCount(sumWords);
			List<Word> words = new ArrayList<Word>();
			Map<String, Long> wordsFiltered = result.entrySet().stream().filter(map -> map.getValue() > 50)

					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			wordsFiltered.forEach((k, v) -> {
				Word word = new Word(k, v);
				words.add(word);
			});
			file.setWords(words);

			// System.out.println("@@@@@@@@@" + wordsFiltered);

			return file;
		}
		// System.out.println(sumWords);
		// System.out.println(result);
		GeneralFile file = new GeneralFile(cleanName);
		file.setWordCount(sumWords);

		return file;

	}

	private static String cleanFileName(String fileName) {
		return Arrays.asList(fileName.split("/")).get(fileName.split("/").length-1);
	}

}
