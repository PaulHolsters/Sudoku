package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author paulh
 */
public class Genereer  {

    private HashMap<String, Integer> sudMap = new HashMap<>();
    private ArrayList<String> pad = new ArrayList<>();
    private HashMap<String, Integer> sudKlein = new HashMap<>();
    private ArrayList<String> padKlein = new ArrayList<>();
    private HashMap<String, Integer> sudGroot = new HashMap<>();
    private ArrayList<String> padGroot = new ArrayList<>();
    private Random r = new Random();
    private boolean finished = false;
    private boolean running = false;
    private boolean OK;
    private String leegVak;
    private boolean oplosbaar = false;
    private boolean timeExpired = false;
    private long time = 0;
    private long timePassed = 0;
    private long start = 0;
    private int teller = 0;

    public void generateNow() {
        running = true;
        //invullen van sudMap met 17 willekeurige clues
        for (char i = 'a'; i < 'j'; i++) {
            for (int j = 1; j < 10; j++) {
                String key = "" + i + j;
                sudMap.put(key, 0);
                sudKlein.put(key, 0);
                sudGroot.put(key, 0);
            }
        }
        willekeurig();
        //een oplossing zoeken van deze willekeurige sudoku
        //de voorwaarde voor het zoeken is dat de tijd beperkt blijft
        //indien de sudoku niet oplosbaar is moet de sudoku leeg gemaakt
        //worden en opnieuw aangevuld worden en moet opnieuw een oplossing worden 
        //gezocht. Dit moet zich herhalen tot de sudoku opgelost is
        losop();
        while (!oplosbaar) {
            //resetten van de hele sudoku
            for (String v : sudMap.keySet()) {
                sudMap.put(v, 0);
            }
            willekeurig();
            losop();
        }
        //nu moeten er (81-36) clues worden weggenomen waarbij telkenmale
        //wordt gecheckt of de sudoku nog steeds een unieke oplossing heeft
        do {
            verwijderClue();
            teller++;
        } while (teller < (81 - 36));
        running = false;
        teller = 0;
    }

    public void reset() {
        sudMap.clear();
        pad.clear();
        sudKlein.clear();
        sudGroot.clear();
        padGroot.clear();
        padKlein.clear();
        finished = false;
        running = false;
        oplosbaar = false;
        timeExpired = false;
        time = 0;
        timePassed = 0;
        start = 0;

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public HashMap<String, Integer> getSudMap() {
        return sudMap;
    }

    public void setSudMap(HashMap<String, Integer> sudMap) {
        this.sudMap = sudMap;
    }

    public void drukAf() {
        for (int i = 1; i < 10; i++) {
            for (char j = 'a'; j < 'j'; j++) {
                String vak = "" + j + i;
                System.out.print(sudMap.get(vak) + "\t");
            }
            System.out.println("");
        }
    }

    public void verwijderClue() {
        boolean OK = false;
        while (!OK) {
            int random = r.nextInt(81) + 1;
            int teller = 1;
            for (String vak : sudMap.keySet()) {
                if (teller == random) {
                    if (sudMap.get(vak) != 0) {
                        OK = removeTrue(vak);
                    }
                    break;
                } else {
                    teller++;
                }
            }
        }
    }

    public boolean removeTrue(String vak) {
        int cijfer = sudMap.get(vak);
        boolean OK = false;
        sudMap.put(vak, 0);
        if (uniek()) {
            OK = true;
        } else {
            sudMap.put(vak, cijfer);
        }
        return OK;
    }

    public boolean uniek() {
        boolean uniek = false;
        for (String v : sudMap.keySet()) {
            sudKlein.put(v, sudMap.get(v));
            sudGroot.put(v, sudMap.get(v));
        }
        finished = false;
        losopKlein();
        finished = false;
        losopGroot();
        if (gelijk()) {
            uniek = true;
        }
        for (String v : sudMap.keySet()) {
            sudKlein.put(v, 0);
            sudGroot.put(v, 0);
        }
        padGroot.clear();
        padKlein.clear();
        return uniek;
    }

    public boolean gelijk() {
        for (String v : sudMap.keySet()) {
            if (sudGroot.get(v) != sudKlein.get(v)) {
                return false;
            }
        }
        return true;
    }

    public String leegVak() {
        leegVak = "";
        for (String s : sudMap.keySet()) {
            if (sudMap.get(s) == 0) {
                return s;
            }
        }
        return leegVak;
    }

    public String leegVakSpeciaal(HashMap<String, Integer> sudMap) {
        leegVak = "";
        for (int i = 1; i < 10; i++) {
            for (char j = 'a'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) == 0) {
                    return vak;
                }
            }
        }
        return leegVak;
    }

    public void losopKlein() {
        while (!finished) {
            leegVak = leegVakSpeciaal(sudKlein);
            if (leegVak.equals("")) {
                finished = true;
            } else {
                padKlein.add(leegVak);
                sudKlein.replace(leegVak, 1);
                OK = check(sudKlein);
                while (!OK) {
                    int indexLaatste = padKlein.size() - 1;
                    String key = padKlein.get(indexLaatste);
                    if (sudKlein.get(key) < 9) {
                        int waarde = sudKlein.get(key) + 1;
                        sudKlein.replace(key, waarde);
                        OK = check(sudKlein);
                    } else {
                        boolean nothingLeft = true;
                        sudKlein.replace(key, 0);
                        padKlein.remove(indexLaatste);
                        while (nothingLeft) {
                            int nieuweIndex = padKlein.size() - 1;
                            String nieuweKey = padKlein.get(nieuweIndex);
                            if (sudKlein.get(nieuweKey) < 9) {
                                int waarde = sudKlein.get(nieuweKey) + 1;
                                sudKlein.replace(nieuweKey, waarde);
                                OK = check(sudKlein);
                                nothingLeft = false;
                            } else {
                                sudKlein.replace(nieuweKey, 0);
                                padKlein.remove(nieuweIndex);
                            }
                        }
                    }
                }
            }
        }
    }

    public void losopGroot() {
        while (!finished) {
            leegVak = leegVakSpeciaal(sudGroot);
            if (leegVak == "") {
                finished = true;
            } else {
                padGroot.add(leegVak);
                sudGroot.replace(leegVak, 9);
                OK = check(sudGroot);
                while (!OK) {
                    int indexLaatste = padGroot.size() - 1;
                    String key = padGroot.get(indexLaatste);
                    if (sudGroot.get(key) > 1) {
                        int waarde = sudGroot.get(key) - 1;
                        sudGroot.replace(key, waarde);
                        OK = check(sudGroot);
                    } else {
                        boolean nothingLeft = true;
                        sudGroot.replace(key, 0);
                        padGroot.remove(indexLaatste);
                        while (nothingLeft) {
                            int nieuweIndex = padGroot.size() - 1;
                            String nieuweKey = padGroot.get(nieuweIndex);
                            if (sudGroot.get(nieuweKey) > 1) {
                                int waarde = sudGroot.get(nieuweKey) - 1;
                                sudGroot.replace(nieuweKey, waarde);
                                OK = check(sudGroot);
                                nothingLeft = false;
                            } else {
                                sudGroot.replace(nieuweKey, 0);
                                padGroot.remove(nieuweIndex);
                            }
                        }
                    }
                }
            }
        }
    }

    public void losop() {
        //starttijd van het algoritme
        start = System.currentTimeMillis();
        while (!finished) {
            //timecheck            
            time = System.currentTimeMillis();
            timePassed = time - start;
            if (timePassed > 300) {
                timeExpired = true;
            }
            leegVak = leegVak();
            if (leegVak == "") {
                finished = true;
                oplosbaar = check(sudMap);
            } else if (timeExpired) {
                //resetten van de hele sudoku
                for (String v : sudMap.keySet()) {
                    sudMap.put(v, 0);
                }
                willekeurig();
                timeExpired = false;
                start = System.currentTimeMillis();
            } else {
                pad.add(leegVak);
                sudMap.replace(leegVak, 1);
                OK = check(sudMap);
                while (!OK) {
                    int indexLaatste = pad.size() - 1;
                    if (indexLaatste < 0) {
                        oplosbaar = false;
                        finished = true;
                        OK = true;
                    } else {
                        String key = pad.get(indexLaatste);
                        if (sudMap.get(key) < 9) {
                            int waarde = sudMap.get(key) + 1;
                            sudMap.replace(key, waarde);
                            OK = check(sudMap);
                        } else {
                            boolean nothingLeft = true;
                            sudMap.replace(key, 0);
                            pad.remove(indexLaatste);
                            while (nothingLeft) {
                                int nieuweIndex = pad.size() - 1;
                                if (nieuweIndex < 0) {
                                    oplosbaar = false;
                                    finished = true;
                                    nothingLeft = false;
                                    OK = true;
                                } else {
                                    String nieuweKey = pad.get(nieuweIndex);
                                    if (sudMap.get(nieuweKey) < 9) {
                                        int waarde = sudMap.get(nieuweKey) + 1;
                                        sudMap.replace(nieuweKey, waarde);
                                        OK = check(sudMap);
                                        nothingLeft = false;
                                    } else {
                                        sudMap.replace(nieuweKey, 0);
                                        pad.remove(nieuweIndex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void willekeurig() {
        for (int i = 0; i < 18; i++) {
            pickRandom();
        }
    }

    public void pickRandom() {
        boolean OK = false;
        while (!OK) {
            int random = r.nextInt(81) + 1;
            int teller = 1;
            for (String vak : sudMap.keySet()) {
                if (teller == random) {
                    if (sudMap.get(vak) == 0) {
                        OK = true;
                        pickTrue(vak);
                    }
                    break;
                } else {
                    teller++;
                }
            }
        }
    }

    public void pickTrue(String vak) {
        boolean OK = false;
        while (!OK) {
            int random = r.nextInt(9) + 1;
            sudMap.put(vak, random);
            OK = check(sudMap);
        }
    }

    public boolean check(HashMap<String, Integer> sudMap) {
        boolean OK = checkRijen(sudMap);
        if (OK) {
            OK = checkKolommen(sudMap);
        }
        if (OK) {
            OK = checkBlokken(sudMap);
        }
        return OK;
    }

    public boolean checkRijen(HashMap<String, Integer> sudMap) {
        boolean OK = true;
        for (int i = 1; i < 10; i++) {
            HashSet<Integer> cijfers = new HashSet<>();
            int teller = 0;
            for (char j = 'a'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
            if (cijfers.size() < teller) {
                OK = false;
                return OK;
            }
        }
        return OK;
    }

    public boolean checkKolommen(HashMap<String, Integer> sudMap) {
        boolean OK = true;
        for (char j = 'a'; j < 'j'; j++) {
            HashSet<Integer> cijfers = new HashSet<>();
            int teller = 0;
            for (int i = 1; i < 10; i++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
            if (cijfers.size() < teller) {
                OK = false;
                return OK;
            }
        }
        return OK;
    }

    public boolean checkBlokken(HashMap<String, Integer> sudMap) {
        boolean OK = true;
        HashSet<Integer> cijfers = new HashSet<>();
        int teller = 0;

        //checken blokken met letters a,b of c
        for (int i = 1; i < 4; i++) {
            for (char j = 'a'; j < 'd'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 4; i < 7; i++) {
            for (char j = 'a'; j < 'd'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 7; i < 10; i++) {
            for (char j = 'a'; j < 'd'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;

        //checken blokken met letters d,e of f
        for (int i = 1; i < 4; i++) {
            for (char j = 'd'; j < 'g'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 4; i < 7; i++) {
            for (char j = 'd'; j < 'g'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 7; i < 10; i++) {
            for (char j = 'd'; j < 'g'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;

        //checken blokken met letters g,h of i
        for (int i = 1; i < 4; i++) {
            for (char j = 'g'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 4; i < 7; i++) {
            for (char j = 'g'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        cijfers.clear();
        teller = 0;
        for (int i = 7; i < 10; i++) {
            for (char j = 'g'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudMap.get(vak) != 0) {
                    cijfers.add(sudMap.get(vak));
                    teller++;
                }
            }
        }
        if (cijfers.size() < teller) {
            OK = false;
        }
        return OK;
    }
}
