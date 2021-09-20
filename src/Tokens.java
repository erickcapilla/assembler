import java.io.*;
import java.util.*;

public class Tokens {
  private ArrayList<String> lines = new ArrayList<>();
  private ArrayList<String> splitLine = new ArrayList<>();
  private ArrayList<String[]> tokens = new ArrayList<>();
  private final String []delimiters = { " ", ",", "." };
  private String fileIn, fileOut;

  public Tokens(String fileIn, String fileOut) {
    this.fileIn = fileIn;
    this.fileOut = fileOut;

    this.getToken();
  }

  public void getToken() {
    String[] row;

    readFile(this.fileIn); //Leer el archivo

    for (String line : this.lines) {
      for (String delimiter : this.delimiters) {
        String[] split = line.split(delimiter);
        this.splitLine.addAll(Arrays.asList(split));
      }

      for (int i = 0; i < this.splitLine.size(); i++) {
        if(this.splitLine.get(i).contains(" ") || this.splitLine.isEmpty() || this.splitLine.get(i).contains("  ")) {
          this.splitLine.remove(i);
        }
      }

      for (int i = 0; i < this.splitLine.size(); i++) {
        for (String delimiter : this.delimiters) {
          if(this.splitLine.get(i).contains(delimiter)) {
            this.splitLine.set(i, this.splitLine.get(i).replace(delimiter, ""));
          }
        }
      }

      this.deleteDuplicate();
      row = splitLine.toArray(new String[0]);
      this.tokens.add(row);
      this.splitLine.clear();
    }

    this.writeFile();
  }

  public void readFile(String source) {
    String line;

    try {
      FileReader file = new FileReader(source);
      BufferedReader reader = new BufferedReader(file);

      while ((line = reader.readLine()) != null) this.lines.add(line.replaceFirst("  ", "")); //Guarda cada linea del archivo en un ArrayList

      reader.close();

      this.macro();
      //this.lines.forEach(l -> System.out.println(l));
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void writeFile() {
    try {
      FileWriter file = new FileWriter(this.fileOut);
      PrintWriter writer = new PrintWriter(file);

      this.tokens.forEach(t -> writer.println(Arrays.toString(t)
              .replace(":", "")+
              ". Hay " + t.length + " tokens"));
      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }
  public void deleteDuplicate() {
    for (int i = 0; i < this.splitLine.size(); i++) {
      int count = 0;
      for (int j = 0; j < this.splitLine.size(); j++) {
        if(this.splitLine.get(i).equalsIgnoreCase(this.splitLine.get(j))){
          count++;
          if(count >= 2) {
            count = 0;
            this.splitLine.remove(j);
          }
        }
      }
    }
  }
  public ArrayList<String[]> getTokens() {
    return this.tokens;
  }
  public void macro() {
    ArrayList<String> linesCopy = new ArrayList<>();
    ArrayList<String[]> macroPosition = new ArrayList<>();

    for (int i = 0; i < this.lines.size(); i++) {
      if(this.lines.get(i).toLowerCase().contains("macro")) {
        String[] position = { this.lines.get(i), String.valueOf(i) };
        macroPosition.add(position);
      }
    }



    macroPosition.forEach(m -> System.out.println(Arrays.toString(m)));
  }
}