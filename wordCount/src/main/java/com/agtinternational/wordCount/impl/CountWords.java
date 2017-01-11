package com.agtinternational.wordCount.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountWords {

	private static final String ONLY_LETTERS_REGEX = "\\p{L}+";
	private static final String CHARSET_ISO = "ISO-8859-1";
	static Map<String, Long> result = new HashMap<>();

	public static void main(String args[]) throws IOException {


		
		String fileName = "/home/guel/agt/testdata/a/Berlin.txt";
		String folder = "/home/guel/agt/testdata";
		readFolders(folder); 
//
//		
//		readFile(fileName);
		long sum = result.values().stream().mapToLong(Long::longValue).sum();
		System.out.println(sum);
		System.out.println(result);
	}


	private static void readFolders(String folder) throws IOException {
		try(Stream<Path> paths = Files.walk(Paths.get(folder))) {
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	
		            System.out.println(filePath);
		            try {
						readFile(filePath.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
		}
	}

	
	// mayusculas y minusculas
	// limpiar comas, espacios, y demas
	private static Map<String, Long> countInLine(String line, Map<String, Long> result) {
		System.out.println("result entrada " + result);
		Map<String, Long> counter = Arrays.asList(line.split(",|\\s+")).stream().map(String::toLowerCase)
				.filter(word -> word.length() > 0)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		counter.forEach((key, value) -> result.merge(key, value, Long::sum));

		long sum = result.values().stream().mapToLong(Long::longValue).sum();

		System.out.println(counter);
		System.out.println("result " + result);
		System.out.println("total of words " + sum + "\n \n");
		return result;
	}

	private static void countInLine(String line) {
		// System.out.println("result entrada " + result);
		Map<String, Long> counter = Arrays.asList(line.split(",|\\s+|\\.")).stream().map(String::toLowerCase)
				.filter(word -> word.length() > 0)
				.filter(word->word.matches(ONLY_LETTERS_REGEX))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		counter.forEach((key, value) -> result.merge(key, value, Long::sum));

		// System.out.println(counter);
		// System.out.println("result " + result);
		// System.out.println("total of words " + sum + "\n \n");
	}

	private static void readFile(String fileName) throws IOException {

		Files.lines(Paths.get(fileName), Charset.forName(CHARSET_ISO)).filter(line -> line.length() > 0)
				.forEach(String -> countInLine(String));

	}

}
