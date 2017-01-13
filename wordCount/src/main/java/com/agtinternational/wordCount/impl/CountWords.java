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

		String fileName = "/home/guel/agt/testdata/a/perro/Berlin.txt";
		String fileName2 = "home/guel/agt/testdata/b/juan/manolo";
		String fileName3 = "home/guel/agt/testdata/b/perro/paco";
		String folder = "home/guel/agt/testdata";
		// readFoldersWalk(folder);

		ArrayList<String> cortes = new ArrayList<String>();
		cortes.addAll(Arrays.asList(folder.split("/")));
		baseDirectory = addSubdirectory(baseDirectory, cortes);

		cortes.addAll(Arrays.asList(fileName2.split("/")));
		baseDirectory = addSubdirectory(baseDirectory, cortes);
		cortes.addAll(Arrays.asList(fileName3.split("/")));
		baseDirectory = addSubdirectory(baseDirectory, cortes);
		
		
		
		
		System.out.println(baseDirectory);
		Gson gson = new Gson();
		String jsonInString = gson.toJson(baseDirectory);
		System.out.println(jsonInString);
		// addToDirectoryTree(folder, fileName, folder);

		// readFolders(folder);
		// readFile(fileName);
	}

	private static void readFoldersWalk(String folder) throws IOException {

		try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
			paths.forEach(filePath -> {
				System.out.println(filePath);
				if (Files.isDirectory(filePath)) {

					if (baseDirectory.getSubdirectories() == null)
						baseDirectory.setSubdirectories(new HashMap<String, Directory>());
					baseDirectory.getSubdirectories().put(filePath.toString(), new Directory(filePath.toString()));

				}

				System.out.println("Directory " + filePath);

				if (Files.isRegularFile(filePath)) {
					// System.out.println("File" + filePath);
					try {

						Object file = readFile(filePath.toString());
						if (file instanceof BigFile) {
							BigFile bigFile = (BigFile) file;
							// System.out
							// .println("es big file " + bigFile.getName() + "
							// words: " + bigFile.getWordCount());
						}
						if (file instanceof GeneralFile) {
							GeneralFile generalFile = (GeneralFile) file;
							// System.out.println(
							// "es small file " + generalFile.getName() + "
							// words: " + generalFile.getWordCount());
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private static void readFolders(String folder) throws IOException {

		try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
			paths.forEach(FilePath -> {
				String path = FilePath.toString().replaceAll(folder, ".");
				System.out.println(FilePath);
				if (Files.isDirectory(FilePath))
					if (baseDirectory.getSubdirectories() != null)
						baseDirectory.getSubdirectories().put(path.toString(), new Directory(path.toString()));
				System.out.println("Directory " + path);

				if (Files.isRegularFile(FilePath)) {
					// System.out.println("File" + filePath);
					try {

						Object file = readFile(FilePath.toString());
						if (file instanceof BigFile) {
							BigFile bigFile = (BigFile) file;
							System.out
									.println("es big file " + bigFile.getName() + " words: " + bigFile.getWordCount());
						}
						if (file instanceof GeneralFile) {
							GeneralFile generalFile = (GeneralFile) file;
							System.out.println(
									"es small file " + generalFile.getName() + " words: " + generalFile.getWordCount());
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	private static void addToDirectoryTree(Object e, String path, String folder) {

		String array[] = cleanPath(path, folder).split("/");

		int x = 0;
		while (x < array.length) {
			if (baseDirectory.getSubdirectories().containsKey(array[x])) {

			} else {
				// addSubdirectory(baseDirectory, array, x);
			}

			x++;
		}

		System.out.println("in addToDirectory");
		// cortar path por / y recorrer hacia el final
		List<String> pathSplitted = Arrays.asList(cleanPath(path, folder).split("/"));

		for (String string : pathSplitted) {
			System.out.println(string);
			// if (baseDirectory)

		}

	}

	private static Directory addSubdirectory(Directory parentDirectory, ArrayList<String> cortes) {
		// List<String> cortes=Arrays.asList(url.split("/"));
		ArrayList<String> cortesDuplicated = cortes;
		String name = cortesDuplicated.get(0);
		cortesDuplicated.remove(0);
		System.out.println(name + " cortes " + cortes);
		Directory subdirectory ;
		
		if (parentDirectory.getSubdirectories().containsKey(name)) {
			subdirectory=parentDirectory.getSubdirectories().get(name);
		
		}else 
		{
			subdirectory= new Directory(name);
		}
		
		if (cortesDuplicated.size() > 0) {
			subdirectory = addSubdirectory(subdirectory, cortesDuplicated);

		}

		parentDirectory.getSubdirectories().put(name, subdirectory);

		return parentDirectory;
	}

	private static String cleanPath(String path, String folder) {
		return path.replace(folder, "");
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

		Map<String, Long> result = new HashMap<>();
		Files.lines(Paths.get(fileName), Charset.forName(CHARSET_ISO)).filter(line -> line.length() > 0)
				.forEach(String -> {
					countInLine(String, result);
				});

		long sumWords = result.values().stream().mapToLong(Long::longValue).sum();
		System.out.println(sumWords);
		if (sumWords > 1000) {
			BigFile file = new BigFile();
			file.setName(fileName);
			file.setWordCount(sumWords);
			List<Word> words = new ArrayList<Word>();
			Map<String, Long> wordsFiltered = result.entrySet().stream().filter(map -> map.getValue() > 40)

					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			wordsFiltered.forEach((k, v) -> {
				Word word = new Word(k, v);
				words.add(word);
			});
			file.setWords(words);

			System.out.println("@@@@@@@@@" + wordsFiltered);

			return file;
		}
		// System.out.println(sumWords);
		// System.out.println(result);
		GeneralFile file = new GeneralFile();
		file.setName(fileName);
		file.setWordCount(sumWords);

		return file;

	}

}
