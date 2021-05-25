package file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class Traitement {
	private ArrayList<String> t = new ArrayList<String>();
	private File treatedFile;
	
	public static void main(String[] argc) {
		String path;

		if (argc.length == 0) {
			path = "test";
		} else {
			path = argc[0];
		}

		try {
			Traitement test = new Traitement(path);
			System.out.println("\nTraitement du fichier " + path + "...");

			test.copier();
			System.out.println("\n-Contenu du fichier copié avec succès.");

			String dominantWord = test.recherche();
			System.out.println("-Le mot dominant dans " + path + " est " + dominantWord);

			System.out.println("-Le mot miroir de " + dominantWord + " est " + test.miroir(dominantWord));

			test.remplacer();
			System.out.println("-Mot dominant remplacé par son mot miroir avec succès.");

			test.ecrire();
			System.out.println("-Contenu de t écrit sur le fichier avec succés.");

			System.out.println("\nFichier traité avec succès.");

		} catch (Exception e) {
			System.out.println("-Le fichier " + path + " n'existe pas");
		}
		
	}
	
	public Traitement(String path) throws FileNotFoundException {
		treatedFile = new File(path);
		if(!(treatedFile.exists() && treatedFile.isFile())) {
			FileNotFoundException f = new FileNotFoundException();
			throw f;
		} 
	}
	
	public void copier() throws IOException {
		try {
			FileReader fr = new FileReader(treatedFile);
			
			char c;
			int byt;
			
			String word = "";
			
			while((byt = fr.read()) != -1) {
				c = (char) byt;
				if((c == ' ' || c == ';' || c == ',') && word != "") {		// Vérification si le caractère est un séparateur
					t.add(word);
					word = "";
				} else {
					word += c;
				}
			}
			fr.close();
		} catch(FileNotFoundException e) {
			System.out.println("Le Fichier n'existe pas");
		}
		
	}
	
	public String recherche() {
		Map<String, Integer> occurencePerWord = new HashMap<String, Integer>();
		int occ = 0;
		
		for(String mot : t) {																// Calcul de l'occurence de chaque mot dans t 
			occ = occurencePerWord.containsKey(mot) ? occurencePerWord.get(mot) + 1 : 1 ;	
			occurencePerWord.put(mot, occ);
		}
		
		int max = 0;
		String mot = "";
		
		for(Map.Entry<String, Integer> entry : occurencePerWord.entrySet()) {				// Recherche du mot dominant
			mot = entry.getValue()>max ? entry.getKey() : mot;
			max = entry.getValue()>max ? entry.getValue() : max;
		}
		
		return mot;
	}
	
	public static String miroir(String mot) {
		int i1 = 0;
		int i2 = mot.length() - 1;
		char tmp;
		
		char[] newWord = mot.toCharArray();		// Comme une variable String est immutable, on convertit en tableau de char
		
		while(i1<i2) {
			tmp = newWord[i1];
			newWord[i1] = newWord[i2];
			newWord[i2] = tmp;
			i1++;
			i2--;
		}
		
		return String.valueOf(newWord);			// Reconversion en String
	}
	
	public void remplacer () {
		String mostRepeated = recherche();
		String mirror = miroir(mostRepeated);
		
		for(int i=0; i<t.size(); i++) {
			if(t.get(i).equals(mostRepeated)) {
				t.set(i, mirror);
			}
		}
	}
	
	public void ecrire() throws IOException {
		FileWriter fw = new FileWriter(treatedFile);
		
		for(String word : t) {
			fw.write(word + " ");
		}
		
		fw.close();
	}

}
