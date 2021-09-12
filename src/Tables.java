import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Tables {
  private ArrayList<String[]> tokens;
  private String[] directives = {"data", "dataend", "code", "codeend"};
  private ArrayList<String[]> dataDirec = new ArrayList<>();
  private ArrayList<String> position = new ArrayList<>();
  private ArrayList<String[]> symbols = new ArrayList<>();
  private String fileOut;

  public Tables(ArrayList<String[]> tokens, String fileOut) {
    this.tokens = tokens;
    this.fileOut = fileOut;

    this.getSymbols();
  }

  public void getSymbols() {
    String[] colums = { "Simbolo", "Valor", "Longitud" };
    symbols.add(colums);
    int j = 0;
    for (String[] token : this.tokens) {
      for (String directive : this.directives) {
        if (token[0].equalsIgnoreCase(directive)) {
          this.position.add(directive);
          this.position.add(String.valueOf(j));
          System.out.println(this.position.get(0) + " " + this.position.get(1));
          this.dataDirec.add(this.position.toArray(new String[0]));
        }
      }
      this.position.clear();
      j++;
    }

    System.out.println(this.dataDirec.get(2)[0].contains(this.dataDirec.get(0)[0]));
    if(this.dataDirec.get(1)[0].contains(this.dataDirec.get(1)[0])) {
      for(int i = Integer.parseInt(this.dataDirec.get(0)[1]) + 1; i < Integer.parseInt(this.dataDirec.get(1)[1]); i++) {
        this.symbols.add(this.tokens.get(i));
      }
    }

    writeFileS();
  }



  public void writeFileS() {
    try {
      FileWriter file = new FileWriter(this.fileOut);
      PrintWriter writer = new PrintWriter(file);

      for (int i = 0; i < this.symbols.size(); i++) {
        if(i == 0) {
          writer.println(Arrays.toString(this.symbols.get(i)));
        } else {
          int log = this.symbols.get(i)[2].length() <= 2 ? 1 : 2;
          writer.println("[" + this.symbols.get(i)[0] + ",    " +
                  this.symbols.get(i)[2] +
                  ",    " + log + "]");
        }
      }
      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }
}
