/**
* İrem Ustabaş 
* irem.ustabas@ogr.sakarya.edu.tr
* 07.04.2024 
* B181210072 
* 1.Öğretim C Grubu
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;

public class Analayzer {
	private String className;
    private int javadocLineNumber;
    private int commentLineNumber;
    private int codeLineNumber =0;
    private int LOC;
    private int functionNumber;
    private double commentDeviation;
    
    // bütün analiz yöntemlerini başlatır, en son ekrana yazdırır
    public void analyzeFile(File file) throws FileNotFoundException, IOException {
    	
    	if(!this.isClass(file)) return;
    	this.countJavadocCommentLines(file);
    	this.countCommentLines(file);
    	this.countCodeLines(file);
    	this.countLOC(file);
    	this.countFunctions(file);
    	this.commentDeviation();
    	
        System.out.println("Sınıf: "+this.className);
        System.out.println("Javadoc Satır Sayısı: "+this.javadocLineNumber);
        System.out.println("Yorum Satır Sayısı: "+this.commentLineNumber);
        System.out.println("Kod Satır Sayısı: "+this.codeLineNumber);
        System.out.println("LOC: "+this.LOC);
        System.out.println("Fonksiyon Sayısı: "+this.functionNumber);
        System.out.println("Yorum Sapma Yüzdesi: % "+String.format("%.2f", this.commentDeviation));
        System.out.println("-----------------------------------------");
    	  
    }
    
    // javadoc satırları sayıcı 
    private void countJavadocCommentLines(File file) throws IOException {
    	int javadocLinesCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inJavadocComment = false;
            boolean inNormalComment = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if(line.contains("/**")&&!inJavadocComment) {
                	if(line.contains("//")) {
                		if(line.indexOf("//")>line.indexOf("/**")) {
                			if (line.contains("*/")) {
                    			javadocLinesCount++;
                    		}else {
                    			inJavadocComment=true;
                    		}
                		}
                	}
                	else {
                		if (line.contains("*/")) {
                			System.out.println("burada");
                			javadocLinesCount++;
                		}else {
                			inJavadocComment=true;
                		}
                		
                	}
                }
                if(line.contains("*/")&&inJavadocComment) {
                	inJavadocComment=false;
                }

                if (inJavadocComment &&!line.contains("/**") ) {
                    javadocLinesCount++;
                }
            }
        }

        this.javadocLineNumber= javadocLinesCount;

    }
    
    // yorum satırı sayıcı 
    private void countCommentLines(File file) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(file));
        int commentLineCount = 0;
        boolean inJavadocComment = false;
        boolean inComment=false;
        String line;
        
        while ((line = br.readLine()) != null) {
            String trimmedLine = line.trim();
            if(!line.isBlank()) {   	 
           	 if(line.contains("/**")&&!inJavadocComment&&!line.substring(0, line.indexOf("/**")).contains("//")&&!line.substring(0, line.indexOf("/**")).contains("/*")) {
           		inJavadocComment=true;
       			if (line.contains("*/")) {
       				inJavadocComment=false;
      			}
       			continue;
               }
           	 if(line.contains("*/")&&inJavadocComment) {
           		 inJavadocComment=false;
           		 continue;
           	 }
           	 
           	 if(inJavadocComment) {
           		 continue;
           	 }
           	 
           	 if(line.contains("/*")&&!inComment&&!line.substring(0, line.indexOf("/*")).contains("//")&&!line.substring(0, line.indexOf("/*")).contains("/**")) {
           		 inComment=true;
           		 		
        			if (line.contains("*/")) {
        				commentLineCount++;
        				inComment=false;
        			}
        			continue;
                }
            	 if(line.contains("*/")&&inComment) {
            		 inComment=false;
            		 continue;
            	 }
            	 
            	 if(inComment&&!line.contains("/*")) {
            		 commentLineCount++;
            		 continue;
            	 }
            	 
            	 if(line.contains("//")&&!inComment&&!inJavadocComment) {
            		 commentLineCount++;
            		continue;
            	 }
            }
        }    
        br.close();  
        this.commentLineNumber=commentLineCount;
    }
    
    // tpolam kod satırı sayıcı
    private void countCodeLines(File file) throws IOException {
    	 BufferedReader br = new BufferedReader(new FileReader(file));
         int codeLineCount = 0;
         boolean inJavadocComment = false;
         boolean inComment=false;
         String line;
         
         while ((line = br.readLine()) != null) {
             String trimmedLine = line.trim();
             if(!line.isBlank()) {   	 
            	 if(line.contains("/**")&&!inJavadocComment&&!line.substring(0, line.indexOf("/**")).contains("//")&&!line.substring(0, line.indexOf("/**")).contains("/*")) {
            		 inJavadocComment=true;
            		if(!line.substring(0, line.indexOf("/**")).isBlank()) {
            			codeLineCount++;
            		}
            			
        			if (line.contains("*/")) {
        				inJavadocComment=false;
        				if(!line.substring(line.indexOf("*/"),line.length()-1).isBlank()&&line.substring(0, line.indexOf("/**")).isBlank()) {
        					codeLineCount++;
       					}
        				continue;
       				}
        			continue;
                }
            	 if(line.contains("*/")&&inJavadocComment) {
            		 inJavadocComment=false;
            		 
            		 if(!line.substring(line.indexOf("*/")+1,line.length()-1).isBlank()) {
     					codeLineCount++;
            		 }
            		 continue;
            	 }
            	 
            	 if(inJavadocComment) {
            		 continue;
            	 }
            	 
            	 if(line.contains("/*")&&!inComment&&!line.substring(0, line.indexOf("/*")).contains("//")&&!line.substring(0, line.indexOf("/*")).contains("/**")) {
            		 inComment=true;
            		 if(!line.substring(0, line.indexOf("/*")).isBlank()) {
            			 codeLineCount++;            			      			
             		}
             			
         			if (line.contains("*/")) {
         				inComment=false;
         				if(!line.substring(line.indexOf("*/"),line.length()-1).isBlank()&&line.substring(0, line.indexOf("/*")).isBlank()) {
         					codeLineCount++;
        				}
         				continue;
        			}
         			continue;
                 }
             	 if(line.contains("*/")&&inComment) {
             		 inComment=false;
             		 
             		 if(!line.substring(line.indexOf("*/")+1,line.length()-1).isBlank()) {
      					codeLineCount++;
             		 }
             		 continue;
             	 }
             	 
             	 if(inComment) {
             		 continue;
             	 }
             	 
             	 if(line.contains("//")) {
             		if(!line.substring(0,line.indexOf("//")).isBlank()) {
     					codeLineCount++;
    				}
             		continue;
             	 }
            	 
         		codeLineCount++;
             }
         }    
         br.close();        
        this.codeLineNumber= codeLineCount;
    }

    // toplam satır sayıcı
    private void countLOC(File file) {
    	try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int lineCount = 0;
            String line;

            while ((line = br.readLine()) != null) {
                lineCount++;
            }

            this.LOC= lineCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
		
    }
    
    // class kontrolü yapar
    private boolean isClass(File file) throws FileNotFoundException, IOException {
    	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    		Pattern pattern = Pattern.compile("^(\\s*)?(public|private)?\\s+class\\s+(\\w+)");
    		String line;
    		
    		while ((line = reader.readLine()) != null) {
    			Matcher matcher = pattern.matcher(line);
    			
    			if (matcher.find()) {
    				this.className = file.getName();
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    // fonksiyon sayıcı
    private void countFunctions(File file) {
    	
    	int countFunction=0;
    	try (BufferedReader br = new BufferedReader(new FileReader(file))) {            
            String line;
                        
            while ((line = br.readLine()) != null) {

                Pattern pattern = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
                Pattern pattern2 = Pattern.compile("\\w+\\s*=\\s*new\\s+\\w+\\([^)]*\\);");
                Matcher matcher = pattern.matcher(line);
                Matcher matcher2 = pattern2.matcher(line);
                if(matcher.find()&& !matcher2.find()) {
                	countFunction++;
                	continue;
                }
                if(line.contains(className.substring(0,className.indexOf(".java")))) {
                	pattern = Pattern.compile("\\b[a-zA-Z]+\\s+\\b\\w+\\s*\\([^)]*\\)\\s*");
                    matcher = pattern.matcher(line);
                    if(matcher.find()&& !matcher2.find()) {
                    	countFunction++;
                    	continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	this.functionNumber= countFunction;
    }
    private void commentDeviation() {
    	double YG = this.functionNumber==0 ? 0: ((double)(this.javadocLineNumber+this.commentLineNumber)*(double)0.8)/this.functionNumber;
    	double YH = this.functionNumber==0 ? 0:((double)this.codeLineNumber/(double)this.functionNumber)*(double)0.3;
    	this.commentDeviation = YH==0 ? 0 : ((double)(100*YG)/(double)YH)-100;
    } 
}
