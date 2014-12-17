package kr.ac.jejuuniv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws Exception {
		MaximumSentenceSearcher maximumSentenceSearcher = new MaximumSentenceSearcher();
		MaximumSentenceSearcherResult maximumSentenceFile = maximumSentenceSearcher.getMaximumSentenceFile("/Users/hms/Desktop/");
		System.out.println("최대로 긴 문장길이 : " + maximumSentenceFile.getSentenceLength());
		System.out.println("파일 경로들 : " + maximumSentenceFile.getTextFilePaths());
	}
}

class MaximumSentenceSearcher {
	private Map<Integer, List<String>> SentenceInfoHashMap = new HashMap<Integer, List<String>>();
	private List<String> textFiles = new ArrayList<String>();

	public MaximumSentenceSearcherResult getMaximumSentenceFile(String directoryPath) throws Exception {
		List<String> searchFileResults = searchTextFiles(directoryPath);
		for (String path : searchFileResults) {
			makeSentenceLength(path);
		}

		List<Integer> sentenceLengths = new ArrayList<Integer>(SentenceInfoHashMap.keySet());

		Collections.sort(sentenceLengths);
		Integer key = sentenceLengths.get(sentenceLengths.size() - 1);
		return new MaximumSentenceSearcherResult(key, SentenceInfoHashMap.get(key));
	}

	public void makeSentenceLength(String path) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File(path));
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String text = null;
		int textLength = 0;

		while ((text = bufferedReader.readLine()) != null) {
			textLength += text.length();
		}

		makeDataGroup(textLength, path);

		fileInputStream.close();
		inputStreamReader.close();
		bufferedReader.close();
	}

	private void makeDataGroup(int sentenceLength, String textFilePath) {
		List<String> paths = SentenceInfoHashMap.get(sentenceLength);
		if (paths == null) {
			paths = new ArrayList<String>();
			SentenceInfoHashMap.put(sentenceLength, paths);
		}
		paths.add(textFilePath);
	}

	public List<String> searchTextFiles(String directory) {
		File source = new File(directory);
		File[] files = source.listFiles();

		for (File file : files) {
			if (file.isFile() && getFileExtension(file.getAbsolutePath()).equals(".txt")) {
				textFiles.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				searchTextFiles(file.getAbsolutePath());
			}
		}
		return textFiles;
	}

	public String getFileExtension(String filePath) {
		int idxOf = filePath.lastIndexOf(".");
		return filePath.substring(idxOf);
	}

	@Override
	public String toString() {
		return SentenceInfoHashMap.toString();
	}
}

class MaximumSentenceSearcherResult {

	private int sentenceLength;
	private List<String> textFilePaths;

	public MaximumSentenceSearcherResult(int sentenceLength, List<String> textFilePaths) {
		this.sentenceLength = sentenceLength;
		this.textFilePaths = textFilePaths;
	}

	public int getSentenceLength() {
		return sentenceLength;
	}

	public List<String> getTextFilePaths() {
		return textFilePaths;
	}
}