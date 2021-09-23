import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Tables {
  private ArrayList<String[]> tokens;
  private final String[] directives = {"data", "dataend", "code", "codeend"};
  private ArrayList<String[]> dataDirec = new ArrayList<>();
  private ArrayList<String> position = new ArrayList<>();
  private ArrayList<String[]> symbols = new ArrayList<>();
  private ArrayList<String[]> labels = new ArrayList<>();
  private String fileOut;
  private final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                           "A", "B", "C", "D", "E", "F"};
  private final String[][] codes = { {"Add, Ax, Bx", "01"}, {"Add, Ax, xx", "02"}, {"Sub, Ax, Bx", "10"},
          {"Sub, Ax, xx", "11"}, {"Mul, Bx", "27"}, {"Dec, Bx", "3A"}, {"Inc, Bx", "3B"}, {"Mov, Ax, xx", "45"},
          {"Mov, Ax, Bx", "4E"}, {"JMP, xx", "5C"}, {"Push, Ax", "6B"}, {"Pop, Bx", "6D"}};
  private ArrayList<String[]> memoryPositions = new ArrayList<>();
  ArrayList<String[]> tokensCopy;
  ArrayList<String[]> tokensCopy2;

  public Tables(ArrayList<String[]> tokens, String fileIn, String fileOut) {
    this.tokens =  tokens;
    this.fileOut = fileOut;
    Tokens newTokens = new Tokens(fileIn, fileOut);
    this.tokensCopy2 = newTokens.getTokens();


    this.getSymbols();
    this.getLabel();
    this.getPosition();
  }

  public void getSymbols() {
    //this.tokens.forEach(t -> System.out.println(Arrays.toString(t)));
    int j = 0;
    for (String[] token : this.tokens) {
      for (String directive : this.directives) {
        if (token[0].equalsIgnoreCase(directive)) {
          this.position.add(directive);
          this.position.add(String.valueOf(j));
          this.dataDirec.add(this.position.toArray(new String[0]));
        }
      }
      this.position.clear();
      j++;
    }

    if(this.dataDirec.get(1)[0].contains(this.dataDirec.get(0)[0])) {
      for (int i = Integer.parseInt(this.dataDirec.get(0)[1]) + 1; i < Integer.parseInt(this.dataDirec.get(1)[1]); i++) {
        this.symbols.add(this.tokens.get(i));
      }
    }

    writeFileS();
  }

  public void getLabel() {
    tokensCopy = tokens;

    if(this.dataDirec.get(3)[0].contains(this.dataDirec.get(2)[0])) {
      for(int i = Integer.parseInt(this.dataDirec.get(2)[1]) + 1; i < Integer.parseInt(this.dataDirec.get(3)[1]); i++) {
        if(this.tokens.get(i)[0].contains(":")) {
          String[] label = {this.tokens.get(i)[0], ""};
          this.labels.add(label);
        }
      }
    }

    if(this.dataDirec.get(3)[0].contains(this.dataDirec.get(2)[0])) {
      for (int i = Integer.parseInt(this.dataDirec.get(2)[1]) + 1; i < Integer.parseInt(this.dataDirec.get(3)[1]); i++) {
        for (String[] symbol : symbols) {
          if(tokensCopy.get(i)[tokensCopy.get(i).length - 1].equalsIgnoreCase(symbol[0]))
           tokensCopy.get(i)[tokensCopy.get(i).length - 1] = "xx";
        }
        for (String[] label : labels) {
          if(tokensCopy.get(i)[tokensCopy.get(i).length - 1].contains(label[0]) || tokensCopy.get(i)[0].contains("JMP")) {
            tokensCopy.get(i)[tokensCopy.get(i).length - 1] = "xx";
          }
        }
        for (String h : hex) {
          if(tokensCopy.get(i)[tokensCopy.get(i).length - 1].contains(h) && !tokensCopy.get(i)[tokensCopy.get(i).length - 1].contains("x")) {
            tokensCopy.get(i)[tokensCopy.get(i).length - 1] = "xx";
          }
        }
      }
    }
  }

  public void getPosition() {
    int index1 = 0, index2 = 0;
    for (String[] tokenC : this.tokensCopy) {
      if(tokenC[tokenC.length - 1].equalsIgnoreCase("xx")) {
        for (String[] code : codes) {
          if(Arrays.toString(tokenC).equalsIgnoreCase("[" + code[0] + "]")) {
            generateMemory(2, index1, index2);
            index2 += 2;
            if(index2 >= 16) {index1++; index2 = 0;}
          }
        }
      }
      if(!Arrays.toString(tokenC).equalsIgnoreCase("[xx]") && !tokenC[tokenC.length - 1].equalsIgnoreCase("xx")) {
        for (String[] code : codes) {
          if(Arrays.toString(tokenC).equalsIgnoreCase("[" + code[0] + "]" )) {
            generateMemory(1, index1, index2);
            index2++;
            if(index2 >= 16) {index1++; index2 = 0;}
          }
        }
      }
    }

    //System.out.println(memoryPositions.size());

    setPosition();
    int empty = 0;
    for (String[] memoryPosition : memoryPositions) {
      if(memoryPosition[1].equalsIgnoreCase("")) empty++;
    }
    if(empty != 0) setPosition();

    for (String[] tok : this.tokensCopy2) {
      for (String[] label : labels) {
        if(tok[0].contentEquals("JMP") && tok[tok.length - 1].contentEquals(label[0].replace(":", ""))) {
          for (String[] memoryPosition : memoryPositions) {
            if(memoryPosition[1].equalsIgnoreCase("")) {
              memoryPosition[1] = label[1];
              break;
            }
          }
        }
      }
    }
    //memoryPositions.forEach(m -> System.out.println(Arrays.toString(m)));
    writeFileM();
    writeFileL();
  }

  public void writeFileS() {
    try {
      FileWriter file = new FileWriter(this.fileOut + ".sym");
      PrintWriter writer = new PrintWriter(file);
      writer.println("[Simbolo, Valor, Longitud]");

      for (int i = 0; i < this.symbols.size(); i++) {
        int log = this.symbols.get(i)[2].length() <= 2 ? 1 : 2;
        writer.println("[" + this.symbols.get(i)[0] + ",    " +
                this.symbols.get(i)[2] +
                ",    " + log + "]");
      }

      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void writeFileL() {
    try {
      FileWriter file = new FileWriter(this.fileOut + ".lab");
      PrintWriter writer = new PrintWriter(file);
      writer.println("[Etiqueta, Valor]");

      for (int i = 0; i < this.labels.size(); i++) {
        writer.println("[" + this.labels.get(i)[0].replace(":", "") + ", " +  this.labels.get(i)[1] +  "]");
      }

      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void writeFileM() {
    try {
      FileWriter file = new FileWriter(this.fileOut + ".hex");
      PrintWriter writer = new PrintWriter(file);
      writer.println("[Memoria, Codigos]");

      memoryPositions.forEach(m -> writer.println(Arrays.toString(m)));

      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void generateMemory(int spaces, int index1, int index2) {
    for(int i = 0; i < spaces; i++) {
      String[] num = { hex[index1]+hex[index2], "" };
      memoryPositions.add(num);
      index2++;
    }
  }

  public void setPosition() {
    int j = Integer.parseInt(this.dataDirec.get(2)[1]);

    for (int i = 0; i < memoryPositions.size(); i++) {
      j++;
      //System.out.println(Arrays.toString(tokens.get(j)));
      for (String[] c : codes) {
        if(Arrays.toString(tokens.get(j)).equalsIgnoreCase("[" + c[0] + "]" )) {
          memoryPositions.get(i)[1] = c[1];
        }
      }

      if(tokens.get(j)[tokens.get(j).length - 1].equalsIgnoreCase("xx")) {
        for (String[] symbol : symbols) {
          if(tokensCopy2.get(j)[tokens.get(j).length - 1].contains(symbol[0])) {
            i++;
            memoryPositions.get(i)[1] = symbol[2];
          }
        }
      }

      if(tokensCopy2.get(j)[tokens.get(j).length - 1].length() == 2 &&
              !tokensCopy2.get(j)[tokens.get(j).length - 1].contains("x")) {
        //System.out.println(tokensCopy2.get(j)[tokensCopy2.get(j).length - 1]);
        i++;
        memoryPositions.get(i)[1] = tokensCopy2.get(j)[tokensCopy2.get(j).length - 1];
      }

      if(tokensCopy2.get(j)[0].contains(":") && !memoryPositions.get(i+1)[1].equalsIgnoreCase("")) {
        for (String[] label : labels) {
          if(tokensCopy2.get(j)[0].equalsIgnoreCase(label[0])) {
            label[1] = memoryPositions.get(i+1)[0];
          }
        }
      }
      //System.out.println(Arrays.toString(memoryPositions.get(i)));
      //memoryPositions.forEach(m -> System.out.println(Arrays.toString(m)));
    }
  }
}
