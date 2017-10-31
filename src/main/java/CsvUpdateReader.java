import java.io.*;
import java.util.*;

public class CsvUpdateReader {
    private static final char SEPARATOR = ',';
    private static final char QUOTE = '"';
    private long lastFileSize = 0L;
    private int lastCheckedRow = 0;
    private File file = null;
    private List<String> headers;
    private boolean isLogging = false;

    public CsvUpdateReader(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        lastFileSize = file.length();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            //сохраняем заголовки
            headers = parseLine(reader.readLine());
            lastCheckedRow = 1;

            // проходим до конца файла, считая количество строк
            while (reader.readLine() != null) {
                lastCheckedRow++;
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new FileNotFoundException();
        }
        if (isLogging)
            System.out.println("Файл загружен. Обнаружено " + headers.size() + " заголовков, считано строк: " + lastCheckedRow + " размер: " + file.length());
    }


    public List<HashMap<String, String>> getChanges() {
        List<HashMap<String, String>> changesList = new ArrayList<>();
        if (isFileChangesDetected()) {

            if (isLogging) System.out.println("Обнаружено изменение файла.");
            putChangesIn(changesList);
        }
        return changesList;
    }

    private void putChangesIn(List<HashMap<String, String>> changesList) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            // сбрасываем счётчик линии, если файл стал меньше
            if (file.length() < lastFileSize) {
                lastCheckedRow = 1;
            }

            skipReadedRows(reader);


            while (reader.ready()) {
                String line = reader.readLine();

                List<String> valList = parseLine(line);

                boolean allColumnPresent = valList.size() == headers.size();

                if (allColumnPresent) {
                    lastCheckedRow++;
                    changesList.add(packVarsToHashMap(valList));
                } else {
                    if (isLogging)
                        System.err.println("В строке [" + (lastCheckedRow + 1) + "] несовпадение количества аргументов. (" + valList.size() + " из " + headers.size() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lastFileSize = file.length();
        if (isLogging) System.out.println("Считано " + changesList.size() + " строк.");
    }

    private HashMap<String, String> packVarsToHashMap(List<String> valList) {
        HashMap<String, String> hashMapFromLine = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            hashMapFromLine.put(headers.get(i), valList.get(i));
        }
        return hashMapFromLine;
    }


    private boolean isFileChangesDetected() {
        return file.length() != lastFileSize;
    }

    public void disableLog() {
        isLogging = false;
    }

    public void enableLog() {
        isLogging = true;
    }

    private void skipReadedRows(BufferedReader reader) throws IOException {
        for (int currentRow = 0; currentRow < lastCheckedRow; currentRow++) {
            // пропускаем считанное
            reader.readLine();
        }
    }

    protected List<String> parseLine(String cvsLine) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;

        char[] chars = cvsLine.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (inQuotes) {
                if (chars[i] == QUOTE) {
                    inQuotes = false;
                } else {
                    curVal.append(chars[i]);
                }
            } else {
                if (chars[i] == SEPARATOR) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                } else {
                    if (chars[i] == QUOTE) {
                        inQuotes = true; // входим в кавычки
                    } else {
                        curVal.append(chars[i]); // любой знак кноме сепаратора и кавычки
                    }
                }
            }
        }

        result.add(curVal.toString()); // добавляем последнее значение в строке
        return result;
    }
}