package edu.cmu.cs.cs214.hw5.plugin;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderTest {
    private CSVReader reader;

    @Before
    public void setup() throws IOException {
        reader = new CSVReader();
    }

    @Test
    public void testInputData() throws IOException {
        List<Integer> list = new ArrayList<>();
//        FileReader z = new FileReader("resources/test.csv");
        reader.inputData("resources/test.csv", 0,0,0, list);
    }

}