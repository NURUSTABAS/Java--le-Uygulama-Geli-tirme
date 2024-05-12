/**
* İrem Ustabaş 
* irem.ustabas@ogr.sakarya.edu.tr
* 07.04.2024 
* B181210072 
* 1.Öğretim C Grubu
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;


public class RepositoryManager {

    public String repoUrl;
    public File[] javaFiles;
    public Analayzer analayzer;

    public RepositoryManager(String repoUrl) {
        this.repoUrl = repoUrl;
        this.analayzer=new Analayzer();
    }

    // clone ve analiz fonksiyonlarını çağırır
    public void runAnalysis() throws InterruptedException {
        try {
            cloneRepository();
            analyzeRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // gelen repo adresini clone yapar
    private void cloneRepository() throws IOException, InterruptedException {
    	String dir= System.getProperty("user.dir")+"/"+this.extractProjectName(repoUrl);
    	File file = new File(dir);
        if (file.exists() && file.isDirectory() && file.list().length > 0) {
            System.out.println("Repo zaten mevcuttur.");
            return;
        }
    	
    	String cloneCommand = "git clone " + repoUrl;
        Process cloneProcess = Runtime.getRuntime().exec(cloneCommand);
        int cloneExitCode = cloneProcess.waitFor();

        if (cloneExitCode == 0) {
            return;
        } else {
            System.out.println("Error cloning the repository. Exit code: " + cloneExitCode);
        }
    }
    
    // repodaki her dosyayı liste olarak gelir ve listedeki her dosyayı analiz sınıfına gönderir
    private void analyzeRepository() throws FileNotFoundException, IOException {
    	File directory = new File(System.getProperty("user.dir")+"/"+this.extractProjectName(repoUrl));
        File[] javaFiles = this.findFiles(directory);
        if (javaFiles != null && javaFiles.length > 0) {
            for (File javaFile : javaFiles) {
            	this.analayzer.analyzeFile(javaFile);
            }
        } else {
            System.out.println("No Java files found in the repository.");
        }
    }
    
    // repo ismini alır
    private String extractProjectName(String githubRepoUrl) {
        
        String regex = "/([^/]+)\\.git$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(githubRepoUrl);
        if(githubRepoUrl.contains(".git")) {
        	if (matcher.find()) {
        		return matcher.group(1);
        	} else {
        		System.out.println("Proje ismi alınamadı.");
        		return null;
        	}        	
        }
        else {
        	String[] s = githubRepoUrl.split("/");
        	return s[s.length-1];
        }
        
    }
    
    // repodaki bütün dosyaları bulmayı başlatır
    private  File[] findFiles(File directory) {
        List<File> javaDosyaListesi = new ArrayList<>();
        findFilesRekursif(directory, javaDosyaListesi);
        return javaDosyaListesi.toArray(new File[0]);
    }

    // repodaki bütün dosyaları bir listeye ekler
    private  void findFilesRekursif(File directory, List<File> javaFileList) {
        File[] dosyalar = directory.listFiles();

        if (dosyalar != null) {
            for (File dosya : dosyalar) {
                if (dosya.isDirectory()) {
                	findFilesRekursif(dosya, javaFileList);
                } else if (dosya.getName().endsWith(".java")) {
                	javaFileList.add(dosya);
                }
            }
        }
    }
}
