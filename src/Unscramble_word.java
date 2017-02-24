import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Unscramble_word {
	/**
	 * dictionary for collecting the resource data.
	 * jumbled word for collecting the words which need to be matched.
	 * result for store the result after search.
	 * (The reason for using LinkedHashMap is because it follow the insertion order.)
	 */
	public static HashMap<Integer, ArrayList<String>> dictionary = new HashMap<>();
	public static ArrayList<String> jumbled_word_list = new ArrayList<>();
	public static LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
	
	/**
	 * This function for collecting the data from input file to dictionary and build a jumbled word list.
	 * @param n_inputFile
	 */
	public static void readDataFromFile(String n_inputFile){
		try {
			File input_file = new File(n_inputFile);
			BufferedReader br = new BufferedReader(new FileReader(input_file));
			String currentLine;
			Boolean part_change_flag = false;
			while((currentLine = br.readLine()) != null){
				if(!part_change_flag){
					if(currentLine.contains("-")){
						part_change_flag = true;
						continue;
					}
					int word_length = currentLine.length();
					if(dictionary.containsKey(word_length)){
						if(!dictionary.get(word_length).contains(currentLine)){
							dictionary.get(word_length).add(currentLine);
						}
					}else{
						ArrayList<String> word_list = new ArrayList<>();
						word_list.add(currentLine);
						dictionary.put(word_length, word_list);
					}
				}else{
					if(currentLine.contains("-")){
						continue;
					}else{
						if(!jumbled_word_list.contains(currentLine)){
							jumbled_word_list.add(currentLine);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function for checking how many word in dictionary can match the jumbled word, then put all of them
	 * in the result hash map.
	 */
	public static void analyseAndSearch(){
		for(String base_word:jumbled_word_list){
			if(dictionary.containsKey(base_word.length())){
				char[] sorted_baseword = base_word.toCharArray();
				Arrays.sort(sorted_baseword);
				ArrayList<String> searched_list = new ArrayList<>();
				for(String searchword:dictionary.get(base_word.length())){
					char[] sorted_searchword = searchword.toCharArray();
					Arrays.sort(sorted_searchword);
					if(new String(sorted_baseword).equals(new String(sorted_searchword))){
						searched_list.add(searchword);
					}
				}
				result.put(base_word, searched_list);
			}else{
				result.put(base_word, null);
			}
		}
	}
	
	/**
	 * This function for write the result hash map to output file.
	 * @param n_outputFile
	 */
	public static void writeResultToFile(String n_outputFile){
		try {
			File output_file = new File(n_outputFile);
			if(output_file.exists()){
				output_file.delete();
				try {
					output_file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					output_file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(output_file));
			for(ArrayList<String> output:result.values()){
				if(output.isEmpty()){
					bw.write("No Answer Found"+"\n");
					bw.write("--------------------"+"\n");
				}else{
					for(String word:output){
						bw.write(word+"\n");
					}
					bw.write("--------------------"+"\n");
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// Set input file and output file.
		String input_File = "Sample_Input_1.txt";
		String output_File = "Sample_Output_1.txt";
		
		// Start measure the execution time
		long startTime = System.currentTimeMillis();
		// Start execution the program
		readDataFromFile(input_File);
		analyseAndSearch();
		writeResultToFile(output_File);
		long endTime = System.currentTimeMillis();
		long durationTime = endTime - startTime; //milliseconds
		
		/* For test content of each data structure
		*************************************
		System.out.println(dictionary);
		System.out.println(jumbled_word_list);
		System.out.println(result);
		*************************************
		*/
		System.out.println("Total time is: "+durationTime+" milliseconds");
	}
}
