import java.util.ArrayList;
import java.io.*;
public class FileManager {
   public static void write(ArrayList<String> input, String filename) {
		
        String fileContent = "";

        for(String r: input) {
            fileContent = fileContent + r + "\n";
        }
        try {

        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(fileContent);
        fileWriter.close();
        }
        catch (Exception e) {
            System.out.println("file not found");
        }
		
	}

    public static ArrayList<String> readFile(String filename) {
		
		ArrayList<String> file = new ArrayList<String>();

       try {
			FileReader fr = new FileReader (filename);
			BufferedReader br = new BufferedReader (fr); 
			String str;
			String line = br.readLine();
			while (line !=null) 
			{
				file.add(line);
				line = br.readLine();
			}
			br.close();
		} 
		catch (IOException e)
			{
				System.out.println ("File not found");
			}
		
		return file;

    	} 
}