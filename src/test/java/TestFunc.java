

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class TestFunc {
    CsvUpdateReader reader;

    @Before
    public void init() throws FileNotFoundException{
        reader = new CsvUpdateReader("src\\main\\resources\\example.csv");
    }

    @Test
    public void stringTest() {
        List<String> result = reader.parseLine("test1,tes sd es2,\"test3\",\"test,test4\"" );
        Assert.assertEquals("test1", result.get(0));
        Assert.assertEquals("tes sd es2", result.get(1));
        Assert.assertEquals("test3", result.get(2));
        Assert.assertEquals("test,test4", result.get(3));
    }
}
