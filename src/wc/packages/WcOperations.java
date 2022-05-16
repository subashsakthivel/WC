package wc.packages;

import java.io.IOException;

public interface WcOperations {
    public int lines();
    public int words() throws IOException;
    public int chars();
    public int bytes();
    public int longLineLen();
    public void details();
}
