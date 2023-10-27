import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Tree {

    public HashMap<String, String> treeContent = new HashMap<>();
    public String sha = "";

    public Tree() throws Exception{
        checkIfTreeExists();
    }

    public static void checkIfTreeExists() throws Exception
    {
        Path treePath = Paths.get("Tree");
        if(!Files.exists(treePath))
        Files.createFile(treePath);
    }

    public void add(String addString) throws Exception
    {
        if(addString.substring(0,4).equals("blob"))
        {
            checkIfTreeExists();
            String blobAndFileName = addString.substring(7);
            String fileName = blobAndFileName.substring(blobAndFileName.indexOf(":") + 2);
            treeContent.put(fileName, addString.substring(7));
        }
        else if(addString.substring(0,4).equals("tree"))
        {
            checkIfTreeExists();
            treeContent.put("tree", addString.substring(7));
        }
        writeTree();
    }

    public void remove(String removeString) throws Exception
    {
            //Loops through the treeContent and remove the key that has the same value as removeString This removes FILES
            for(String key : treeContent.keySet())
            {
                if(key.equals(removeString))
                {
                    treeContent.remove(key);
                    System.out.println("Removed FILE " + key);
                    writeTree();
                    return;
                }
            }
            //Loops through treeContent and removes the value that has the same value as remove This removes TREES
            for(String value : treeContent.values())
            {
                if(value.equals(removeString))
                {
                     treeContent.remove("tree", value);
                     System.out.println("Removed TREE " + value);
                     writeTree();
                     return;
                }
            }
    }


    public void writeTree() throws Exception
    {
        //clears Tree file content
        Path index = Paths.get("Tree");
        Files.write(index, "".getBytes());

        int currentNum = 1;

        for(String key : treeContent.keySet())
        {
            if(key.equals("tree"))
            {
                Utils.writeToFile("tree : " + treeContent.get(key), "Tree");
            }
            else
            {
                Utils.writeToFile("blob : " + treeContent.get(key), "Tree");
            }

            if(currentNum < treeContent.size())
            {
                Utils.writeToFile("\n", "Tree");
            }
            currentNum++;
        }
        String treeContent = Utils.readFile("Tree");
        Utils.checkIfObjectsExists();
        String treePath = Utils.sha1(treeContent);
        Path treePath2 = Paths.get("./objects/" + treePath);
        Files.write(treePath2, "".getBytes());
        Utils.writeToFile(treeContent, "./objects/" + treePath);
    }



    public void addIndex() throws Exception
    {
        //reads content in index file
        String indexContent = Utils.readFile("index");
        String[] indexContentArray = indexContent.split("\n");
        for(int i = 0; i < indexContentArray.length; i++)
        {
            if(indexContentArray[i].contains("*edited") || indexContentArray[i].contains("*deleted*"))
            {
                return;
            }

            String[] indexContentArray2 = indexContentArray[i].split(" : ");
            if(!indexContentArray2[0].equals("") || !indexContentArray2[1].equals(""))
            {
               treeContent.put(indexContentArray2[0], indexContentArray2[1]);
            }
        }
    }

    public String getSha()
    {
         String treeContent = Utils.readFile("Tree");
        return Utils.sha1(treeContent);
    }   

    public String addDirectory(String directoryName) throws Exception
    {
        //Traverse every file in directoryName
        StringBuilder files = new StringBuilder();
        File directory = new File(directoryName);
        if (directory.exists() && directory.isDirectory()) {
            File[] filesList = directory.listFiles();
            if (files != null) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        add("blob : " + Utils.sha1(Utils.readFile(file.getName())) + " : " + file.getName());
                    } else if (file.isDirectory()) {
                       addDirectory(file.getName());
                    }
                }
            }       
    }
        Path treePath = Paths.get(directoryName + "/Tree");
        if (!Files.exists(treePath))
            Files.createFile(treePath);
        new Blob(directoryName + "/Tree");
        String treeSha = Utils.sha1(Utils.readFile(directoryName + "/Tree"));
        add("tree : " + treeSha + " : " + directoryName);
        return treeSha;
        }

        public String getContents() throws Exception
        {
            String treeContent = Utils.readFile("Tree");
            return treeContent;
        }
}
