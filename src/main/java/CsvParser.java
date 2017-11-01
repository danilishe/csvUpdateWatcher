import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CsvParser {
    Map<String, String> getNext();
    String getData();
    List<String> parse(String line);
    List<String> getHeader();
}
