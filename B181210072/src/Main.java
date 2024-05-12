/**
* İrem Ustabaş 
* irem.ustabas@ogr.sakarya.edu.tr
* 07.04.2024 
* B181210072 
* 1.Öğretim C Grubu
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // kullannıcıdan repo adresi al ve repo managere gönder
        	System.out.print("GitHub Repository URL: ");
            String repoUrl = reader.readLine();

            RepositoryManager repositoryManager = new RepositoryManager(repoUrl);
            repositoryManager.runAnalysis();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}