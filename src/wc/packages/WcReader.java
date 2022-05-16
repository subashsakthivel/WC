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
        } catch ( Exception e) {
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
    public String lines() {
        reader();
        return file.getName() + " " +noLines;
    }

    @Override
    public String words() {
        reader();
        return file.getName() + " " +noWords;
    }

    @Override
    public String chars() {
        reader();
        return file.getName() + " :" +noChars;
    }

    @Override
    public String bytes() {
        reader();
        return file.getName() + " " +noBytes*Character.BYTES;
    }

    @Override
    public String longLineLen() {
        reader();
        return file.getName() + " " +longLine;
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
