package com.agtinternational.webservice.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

import com.agtinternational.entities.Directory;
import com.agtinternational.entities.GeneralFile;
import com.agtinternational.entities.Word;
import com.google.gson.Gson;

public class App {
	public static void main(String[] args) {
		try {

			URL url = new URL("http://localhost:8080/FilesREST/files?url=/home/guel/testdata");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("content-type", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = br.readLine();

			Gson gson = new Gson();
			Directory directory = gson.fromJson(output, Directory.class);

			System.out.println("\n**** LONG FILES ****\n");
			printDirectory(directory, true, 0);
			System.out.println("\n**** SHORT FILES ****\n");
			printDirectory(directory, false, 0);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private static void printDirectory(Directory directory, Boolean big, int tabs) {
		String tab = "\t";
		String printTabs = "";
		printTabs = printTabs + String.join("", Collections.nCopies(tabs, tab));

		if (!directory.getName().equals(""))
			System.out.println(printTabs + "<" + directory.getName() + ">");

		if (directory.getFiles() != null) {
			for (GeneralFile file : directory.getFiles()) {

				String output = "<file " + file.getName() + "> <" + file.getWordCount() + " words> ";
				if (big == true) {
					if (file.getWords() != null) {

						for (Word word : file.getWords()) {
							output = output + "<word " + word.getWord() + " " + word.getRepetition() + "> ";

						}
						System.out.println(printTabs + output);
					}
				} else {
					System.out.println(printTabs + output);
				}

			}
		}

		if (directory.getSubdirectories() != null) {

			for (HashMap.Entry<String, Directory> subDirectory : directory.getSubdirectories().entrySet()) {

				printDirectory(subDirectory.getValue(), big, tabs + 1);
			}

		}

	}

}
