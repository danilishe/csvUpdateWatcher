import java.util.List;

public interface FileReader {
    List<String> getLines();
    String getHeader();
    void checkSize();
}
