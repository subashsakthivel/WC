package wc.packages;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WcReader implements WcOperations, Runnable {
    public static volatile int totalLine = 0;
    public static volatile int totalChars = 0;
    public static volatile int totalWords = 0;
    public static volatile int totalBytes = 0;
    public File file = null;
    public FileInputStream fis = null;
    private boolean fileRead = false;
    public BufferedInputStream bufferedInputStream = null;
    private int noLines = 0;
    private int noWords = 0;
    private int noChars = 0;
    private int noBytes = 0;
    private int longLine = 0;

    public  WcReader(String  path) throws IOException {
        file = new File(path);
        fis = new FileInputStream(file);
        bufferedInputStream = new BufferedInputStream(fis);
    }

    public WcReader() {

    }

    private void reader() {
        int read;
        int lineChars =0;
        int longLineChars = 0;
        try {
            if (this.noLines == 0 || this.noWords == 0 || this.noChars == 0) {
                while ((read = bufferedInputStream.read()) > -1) {
                    this.noChars++;
                    lineChars++;
                    if ((char) read == ' ') {
                        this.noWords++;
                    } else if ((char) read == '\n') {
                        this.noLines++;
                        this.noWords++;
                        if (lineChars > longLineChars) {
                            longLineChars = lineChars;
                            this.longLine = this.noLines;
                        }
                        lineChars = 0;
                    }
                }
            }
            if(this.noChars > 0)
            {
                this.noLines++;
                this.noWords++;
                if(lineChars > longLineChars)
                {
                    this.longLine = this.noLines;
                }
            }
            this.noBytes = this.noChars * Character.BYTES;
            this.fileRead = true;
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    private void checking(){
        try {
            if (!this.fileRead)
                reader();
        } catch ( Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        reader();
        try {
            bufferedInputStream.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.getName() + "  "+ noLines + " " + noBytes + " " + noChars + " " + noWords;
    }
    public void totals() {
        System.out.println("Totals " + totalLine + " " + totalBytes + " " + totalChars + " " + totalWords);
    }

    @Override
    public int lines() {
        System.out.print(file.getName());
        checking();
        return noLines;
    }

    @Override
    public int words() {
        System.out.print(file.getName());
        checking();
        return noWords;
    }

    @Override
    public int chars() {
        System.out.print(file.getName());
        checking();
        return noChars;
    }

    @Override
    public int bytes() {
        System.out.print(file.getName());
        checking();
        return noBytes*Character.BYTES;
    }

    @Override
    public int longLineLen() {
        System.out.print(file.getName());
        checking();
        return longLine;
    }


    @Override
    public void details() {
        checking();
        System.out.println(file.getName());
        System.out.println(this.noLines + " lines");
        System.out.println(this.noBytes + " bytes");
        System.out.println(this.noWords + " words");
        System.out.println(this.noChars + " chars");
        System.out.println("----------------------");
    }

    @Override
    public void run() {
        System.out.println(this);
        totalLine = totalLine + noLines;
        totalWords = totalWords + noWords;
        totalBytes= totalBytes + noBytes;
        totalChars= totalChars + noChars;
    }
}
