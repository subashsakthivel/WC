package reader.wc;

import java.io.IOException;

public class Wc extends Utils {

    private static void version(){
        System.out.println("Version 1.01");
    }

    private  static  void help() {
        System.out.println("""
                            wc file (or) files -> bytes chars words lines
                            wc -l file -> no of lines
                            wc -w file -> no of words
                            wc -m file -> no of characters
                            wc -c file -> no of bytes
                            wc -L file -> no of Longest line
                            Try this""");
    }

    private void readWithCmd(char cmd ,String path){
        try {
            switch (cmd) {
                case 'l' -> System.out.println(path + " " + getLines(path));
                case 'w' -> System.out.println(path + " " + getWords(path));
                case 'm' -> System.out.println(path + " " + getChars(path));
                case 'c' -> System.out.println(path + " " + getBytes(path));
                case 'L' -> System.out.println(path + " " + getLongLines(path));
                case 'i' -> System.out.println(path + " " + getInfo(path , true));
                default -> help();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Wc()  {

    }

    public void printResult(String[] args){
        int length = args.length;
        switch (length){
            case 0:
                help();// nothing is passed
                break;
            case 1:
                if(args[0].equals("--help")) help();
                else if(args[0].equals("--version")) version();
                else readWithCmd('i', args[0]);
                break;
            case 2:
                if(args[0].charAt(0)=='-') {
                    readWithCmd(args[0].charAt(1), args[1]);
                    break;
                }
            default:
                try { getInfo(args); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}

