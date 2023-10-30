import java.io.IOException;

public class main {

    public static void main(String[] args) {
        try {

            // Tests the index works
            Index.init();
            Commit a = new Commit("85b56c844bc7cd0aaaf6141013dc9fb7455d3fbd", "Sam2", "Testing delete?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}