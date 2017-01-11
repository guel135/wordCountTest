package com.agtinternational.wordCount.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountFiles {

	public static void main(String[] args) throws IOException {
		// countInArray();

		String fileName = "/home/guel/agt/testdata/a/Lomond.txt";

		// readFile(fileName);
		readLines(fileName);
		

	}

	private static void readFile(String fileName) throws IOException {
		String line1 = "Lomond still";
		String line2 = "From Wikipedia, the free encyclopedia";
		String line3 = "A Lomond still is a type of still ";
		// read file into stream, try-with-resources

		Map<String, Long> total = new HashMap<String, Long>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			// 1. filter line 3
			// 2. convert all content to upper case
			// 3. convert it into a List
			stream.map(line -> line.split(" "))
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			// try {
			// Map<String, Long> result = new HashMap<String, Long>();
			// Files.lines(Paths.get(fileName)).map(line::countInLine(line));
			//
			// result = countInLine(line1, result);
			// result = countInLine(line2, result);
			// result = countInLine(line3, result);
			// System.out.println("-------------");
			// System.out.println(result);
			//
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			System.out.println(stream);
		}
		System.out.println();
		//
	}

	private static void readLines(String fileName) {
		String line1 = "Lomond still";
		String line2 = "From Wikipedia, the free encyclopedia";
		String line3 = "A Lomond still lomond    is a type of still ";
		// read file into stream, try-with-resources

		Map<String, Long> total = new HashMap<String, Long>();

		Map<String, Long> result = new HashMap<String, Long>();

		result = countInLine(line1, result);
		result = countInLine(line2, result);
		result = countInLine(line3, result);
		System.out.println("-------------");
		System.out.println(result);
		System.out.println(total);
	}

	// mayusculas y minusculas
	// limpiar comas, espacios, y demas
	private static Map<String, Long> countInLine(String line, Map<String, Long> result) {
		System.out.println("result entrada " + result);
		Map<String, Long> counter = Arrays.asList(line.split(",|\\s+")).stream()
				.map(String::toLowerCase)
				.filter(word->word.length()>0)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		counter.forEach((key, value) -> result.merge(key, value, Long::sum));

		long sum=result.values().stream().mapToLong(Long::longValue).sum();
		
		System.out.println(counter);
		System.out.println("result " + result);
		System.out.println("total of words "+sum+"\n \n");
		return result;
	}

	private static List<String> splitLine(String line) {
		List<String> words = Arrays.asList(line.split(",|\\s+"));
		System.out.println("tamanano"+words.size());

		return words;
	}

	// private static void countWords2(String fileName){
	// Path path = Paths.get(fileName);
	// Map<String, Integer> wordCount = Files.lines(path)
	// .flatMap(line -> Arrays.stream(line.trim().split(" ")))
	// .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
	// .filter(word -> word.length() > 0)
	// .map(word -> new SimpleEntry<>(word, 1))
	// .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
	// .reduce(new LinkedHashMap<>(), (acc, entry) -> {
	// acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ?
	// 1 : v + 1));
	// return acc;
	// }, (m1, m2) -> m1);
	//
	// wordCount.forEach((k, v) -> System.out.println(String.format("%s ==>>
	// %d", k, v)));
	//
	// }
}
