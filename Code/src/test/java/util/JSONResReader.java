package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONResReader {

    public JSONResReader() {

    }

    public static Object readJSONResource(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader;

        reader = new FileReader("src/test/resources/" + filePath);

        //Read JSON file
        return jsonParser.parse(reader);
    }

    public static String readProperty(JSONObject jsonObject, String propertyPath) throws IOException, ParseException {
        String[] pathParts = propertyPath.split("\\.");

        for(int i=0; i<pathParts.length-1; i++)
            jsonObject = (JSONObject) jsonObject.get(pathParts[i]);

        return (String) jsonObject.get(pathParts[pathParts.length-1]);
    }
}
