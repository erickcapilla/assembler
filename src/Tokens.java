import java.io.*;
import java.util.*;

public class Tokens {
  private ArrayList<String> lines = new ArrayList<>();
  private ArrayList<String> splitLine = new ArrayList<>();
  private ArrayList<String[]> tokens = new ArrayList<>();
  private final String []delimiters = { " ", ",", "." };
  private String fileIn, fileOut;
  private ArrayList<String[]> macros = new ArrayList<>();

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
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void writeFile() {
    try {
      FileWriter file = new FileWriter(this.fileOut + ".asm");
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

    ArrayList<String> macro = new ArrayList<>();

    for (int i = 0; i < this.lines.size(); i++) {
      if(this.lines.get(i).toLowerCase().contains("macro")) {
        String[] position = { this.lines.get(i), String.valueOf(i) };
        macroPosition.add(position);
      }
    }

    int index = 0;
    for(int j = 0; j < macroPosition.size() / 2; j++) {
      macro.clear();
      macro.add(macroPosition.get(index)[0].toUpperCase().replace(".MACRO", ""));
      for(int i = Integer.parseInt(macroPosition.get(index)[1]) + 1; i < Integer.parseInt(macroPosition.get(index+1)[1]); i++) {
        macro.add(this.lines.get(i));
      }
      macros.add(macro.toArray(new String[0]));
      index += 2;
    }

    for(int i = Integer.parseInt(macroPosition.get(macroPosition.size()-1)[1]) + 1; i < lines.size(); i++) {
      if(this.lines.get(i) != "" || this.lines.get(i) != " ") {
        if(this.lines.get(i).contains("()") || this.lines.get(i).contains(")")) {
          for(int j = 0; j < macros.size(); j++) {
            if(this.lines.get(i).contains(macros.get(j)[0])) {
              for (int k = 1; k < macros.get(j).length; k++) {
                linesCopy.add(macros.get(j)[k]);
              }
            }
          }
        } else {
          linesCopy.add(this.lines.get(i));
        }
      }
    }

    lines.clear();
    lines.addAll(linesCopy);
  }

  public void writeMacros(ArrayList<String[]> macros) {
    try {
      FileWriter file = new FileWriter(this.fileOut + "Macros.txt");
      PrintWriter writer = new PrintWriter(file);

      writer.println("MACROS\n");
      macros.forEach(macro -> {
        for (String m : macro) {
          writer.println(m);
        }
        writer.println("--------------------------------------");
      });
      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public ArrayList<String[]> getMacros() {
    return macros;
  }
}