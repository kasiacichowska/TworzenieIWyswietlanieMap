import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;

public class ProgLang {
    String [][] liniePliku; //zeby byly dostepne w kazdej klasie - inne rozwiazanie
    String [] wiersze;  //to samo
    Map <String,List<String>> programisciJezyka;  //to samo
    Map<String, List<String>> programisciJezykow; //to samo
    public ProgLang(String nazwaPliku) throws FileNotFoundException { //w swoim łancuchu dziedziczenia ma klasę java.lang.Throwable. Instancje, które w swojej hierarchii dziedziczenia mają tę klasę mogą zostać „rzucone” (ang. throw) przerywając standardowe wykonanie programu.
        Scanner odczyt = new Scanner(new File(nazwaPliku));

        int licznik = 0;
        while (odczyt.hasNextLine()) {
            odczyt.nextLine();
            licznik++;
        }
        wiersze = new String[licznik];

        // otwieramy ponownie, aby odczytac od poczatku
        odczyt = new Scanner(new File(nazwaPliku));

        int i = 0;
        while (odczyt.hasNextLine()) {
            wiersze[i++] = odczyt.nextLine();
        }


        liniePliku = new String[wiersze.length][];

        for (int j = 0; j < wiersze.length; j++) {
            liniePliku[j] = wiersze[j].split("\\t");
        }

    }

    public Map <String,List<String>> getLangsMap() {

        programisciJezyka=new LinkedHashMap<>();  //LinkedHashMap po to, zeby zostala zachowana kolejnosc


        for (int i = 0; i < wiersze.length; i++) {
            String key = liniePliku[i][0];
            List<String> listaProgramistow = new ArrayList<>();

            for (int j = 1; j < liniePliku[i].length; j++) {
                if(!listaProgramistow.contains(liniePliku[i][j]))
                listaProgramistow.add(liniePliku[i][j]);
            }
            programisciJezyka.put(key, listaProgramistow);
        }
        return programisciJezyka;
    }

    public Map<String, List<String>> getProgsMap() {
        programisciJezykow = new LinkedHashMap<>();

        //dla kazdego elementu
        for (int i = 0; i < wiersze.length; i++) {
            for (int j = 1; j < liniePliku[i].length; j++) {
                //sprawdz czy w wierszu wystepuje ten element
                List <String> listaJezykowProgramisty = new ArrayList<>();
                for (int k = 0; k < wiersze.length; k++) {
                    for (int l = 1; l < liniePliku[k].length; l++) {
                        //jesli tak, dodaj pierwszy [0]
                        if(liniePliku[i][j].equals(liniePliku[k][l]) && !listaJezykowProgramisty.contains(liniePliku[k][0])){
                            listaJezykowProgramisty.add(liniePliku[k][0]);
                        }
                    }
                }
                programisciJezykow.put(liniePliku[i][j],listaJezykowProgramisty);
            }
        }
        return programisciJezykow;
    }

    public Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        Map<String, List<String>> jezProgByProgWynik = getLangsMap();
        return sorted(jezProgByProgWynik, Comparator.comparing(entry -> -entry.getValue().size()));
    }


    public Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        Map<String, List<String>> jezProgByNumWynik = getProgsMap();
        return sorted(jezProgByNumWynik, Comparator.comparing(entry -> -entry.getValue().size()));

    }

    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        Map<String, List<String>> programisciJezykaWiecejNiz = getProgsMap();
        return filtered(programisciJezykaWiecejNiz, entry -> entry.getValue().size() > n);

    }

    public <K, V> Map<K, List<V>> filtered(Map<K, List<V>> mapka, Predicate<? super Map.Entry<K, List<V>>> predicate) {
        Map<K, List<V>> filtrowana = new LinkedHashMap<>();

        for (Map.Entry<K, List<V>> entry : mapka.entrySet()) {
            if (predicate.test(entry)) {
                filtrowana.put(entry.getKey(), entry.getValue());
            }
        }
        return filtrowana;
    }
    public <K, V> Map<K, List<V>> sorted(Map<K, List<V>> mapka, Comparator<? super Map.Entry<K, List<V>>> comparator) {

        List<Map.Entry<K, List<V>>> entryList = new ArrayList<>(mapka.entrySet());
        entryList.sort(comparator);

        Map<K, List<V>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, List<V>> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
