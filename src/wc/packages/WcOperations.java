package wc.packages;

import java.io.IOException;

public interface WcOperations {
    public String lines();
    public String words() throws IOException;
    public String chars();
    public String bytes();
    public String longLineLen();
}
