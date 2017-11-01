import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException{
        CsvUpdateReader cur = new CsvUpdateReader("src\\main\\resources\\example.csv");
        cur.enableLog();

        while (true) {
            List<HashMap<String, String>> data = cur.getUpdates();
            if (!data.isEmpty()) {
                printData(data);
            } else {
                System.out.println("Нет изменений.\n");
            }

            System.out.println("Ожидание...");
            Thread.sleep(15000);
            System.out.println("Сейчас будет начато считывание!");
            Thread.sleep(1500);
        }
    }

    private static void printData(List<HashMap<String, String>> data) {
        System.out.println("-- начало -------------------------------");
        for (HashMap<String, String> map : data) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        System.out.println("-------------------------------- конец --\n");
    }
}
