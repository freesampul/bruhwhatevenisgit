import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class Blob
{
    private String sha;

    public Blob(String inputFile) throws Exception
    {
        Utils.checkIfObjectsExists();
        String content = Utils.readFile(inputFile);
        sha = Utils.sha1(content);
        //Check if same file exists
        if (Files.exists(Paths.get("./objects/" + sha)))
        {
            return;
        }
           Utils.writeToFile(content, "./objects/" + sha);
    }

    public String getSha()
    {
        return sha;
    }
}