import java.io.IOException;

public class main
{

    public static void main (String [] args)
    {
        try
        {

            //Tests the index works 
                Index.init();
            //Tests the tree works :D
                // Tree t = new Tree();
                // // t.remove("blob : 640ab2bae07bedc4c163f679a746f7ab7fb5d1fa : input.txt");
                // // t.remove("tree : a2e712c6beb40fb2715838996f13a2ddf8a78d7c");
                //  t.remove("input.txt");
                //  t.remove("a2e712c6beb40fb2715838996f13a2ddf8a78d7c");
                Commit a = new Commit("9161f3d53be073ff9b4cdb19eb072d9c96a06b22", "sam", "3th?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}