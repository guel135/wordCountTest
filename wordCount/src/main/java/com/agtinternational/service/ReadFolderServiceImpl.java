package com.agtinternational.service;

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

import com.agtinternational.entities.BigFile;
import com.agtinternational.entities.Directory;
import com.agtinternational.entities.GeneralFile;
import com.agtinternational.entities.Word;
import com.agtinternational.service.ReadFolderService;

public class ReadFolderServiceImpl implements ReadFolderService {

	private static final String ONLY_LETTERS_REGEX = "\\p{L}+";
	private static final String CHARSET_ISO = "ISO-8859-1";
	Directory baseDirectory;

	public Directory read(String folder, long wordAmount, long wordRepeat) {

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

				}

				if (Files.isRegularFile(filePath)) {
					try {

						GeneralFile file = readFile(filePath.toString(), wordAmount, wordRepeat);
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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return baseDirectory;
	}

	private static Directory addSubdirectory(GeneralFile file, Directory parentDirectory,
			ArrayList<String> splittedPaths) {
		String name = splittedPaths.get(0);
		System.out.println(name);
		splittedPaths.remove(0);
		if (splittedPaths.size() == 0 && file != null) {

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
			}
			if (parentDirectory.getSubdirectories() == null)
				parentDirectory.setSubdirectories(new HashMap<String, Directory>());
			parentDirectory.getSubdirectories().put(name, subdirectory);
		}
		return parentDirectory;
	}

	private static void countInLine(String line, Map<String, Long> result) {

		Map<String, Long> counter = Arrays.asList(line.split(",|\\s+|\\.")).stream().map(String::toLowerCase)
				.filter(word -> word.length() > 0).filter(word -> word.matches(ONLY_LETTERS_REGEX))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		counter.forEach((key, value) -> result.merge(key, value, Long::sum));
	}

	private static GeneralFile readFile(String fileName, long wordAmount, long wordRepeat) throws IOException {

		String cleanName = cleanFileName(fileName);

		Map<String, Long> result = new HashMap<>();
		Files.lines(Paths.get(fileName), Charset.forName(CHARSET_ISO)).filter(line -> line.length() > 0)
				.forEach(String -> {
					countInLine(String, result);
				});

		long sumWords = result.values().stream().mapToLong(Long::longValue).sum();
		if (sumWords > wordAmount) {
			BigFile file = new BigFile(cleanName);
			file.setWordCount(sumWords);
			List<Word> words = new ArrayList<Word>();
			Map<String, Long> wordsFiltered = result.entrySet().stream().filter(map -> map.getValue() > wordRepeat)

					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			wordsFiltered.forEach((k, v) -> {
				Word word = new Word(k, v);
				words.add(word);
			});
			file.setWords(words);

			return file;
		}
		GeneralFile file = new GeneralFile(cleanName);
		file.setWordCount(sumWords);

		return file;
	}

	private static String cleanFileName(String fileName) {
		return Arrays.asList(fileName.split("/")).get(fileName.split("/").length - 1);
	}

}
