import java.util.List;

public interface Buffer {
    void putData(List<String> data);
    List<String> getDate();
    void setHeaders(String header);
    List<String> getHeaders();
}
