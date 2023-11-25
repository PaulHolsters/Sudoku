package sudoku;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Oplossing  {

    /*
 * Het sudoku-bord is een vierkante matrix. De rijen worden voorgesteld door cijfers 1 t/m 9.
 * De kolommen door de letters a t/m i. Een veld wordt dus aangegeven door een letter gevolgd door een cijfer,
 * net zoals bij een schaakbord. Een veld is tevens key in de HashMappen "oplossing" en "voorlopig".
 * De overeenkomstige value is een cijfer van 1 t/m 9, zijnde de waarde ingevuld op de sudoku.
 * De ArrayList "pad" houdt bij welke velden reeds werden ingevuld.
     */
    private ArrayList<String> pad;
    private HashMap<String, Integer> voorlopig;
    private boolean finished;
    private boolean OK;
    private String leegVak;
    private boolean oplosbaar = false;
    private boolean running = false;

    public Oplossing() {
        pad = new ArrayList<>();
        voorlopig = new HashMap<>();
        finished = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    public void reset() {
        for (char i = 'a'; i < 'j'; i++) {
            for (int j = 1; j < 10; j++) {
                String key = "" + i + j;
                voorlopig.put(key, 0);
            }
        }
        finished = false;
        running = false;
        oplosbaar = false;
        pad.clear();
    }
    
    public boolean aantal(){
        boolean onvoldoende=false;
        int aantal=0;
        for (String s : voorlopig.keySet()) {
            if(voorlopig.get(s)!=0){
                aantal++;
            }
        }
        if(aantal<17){
            onvoldoende=true;
        }
        return onvoldoende;
    }

    //getters en setters
    public ArrayList<String> getPad() {
        return pad;
    }

    public void setPad(ArrayList<String> pad) {
        this.pad = pad;
    }
    
    public HashMap<String, Integer> getVoorlopig() {
        return voorlopig;
    }

    public void setVoorlopig(HashMap<String, Integer> voorlopig) {
        this.voorlopig = voorlopig;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isOK() {
        return OK;
    }

    public void setOK(boolean OK) {
        this.OK = OK;
    }

    public String getLeegVak() {
        return leegVak;
    }

    public void setLeegVak(String leegVak) {
        this.leegVak = leegVak;
    }
    
    public boolean isOplosbaar() {
        return oplosbaar;
    }

    public void setOplosbaar(boolean oplosbaar) {
        this.oplosbaar = oplosbaar;
    }

    //CODE ALGORITME
    public void losop(HashMap<String,Integer>opgave) {
        //kopieer opgave naar voorlopig
        for (String veld : opgave.keySet()) {
            voorlopig.put(veld, opgave.get(veld));
        }
        running = true;       
        while (!finished && running) {
            leegVak = leegVak();
            if (leegVak == "") {
                finished = true;
                running = false;
                oplosbaar = check(voorlopig);
            } else {
                pad.add(leegVak);
                voorlopig.replace(leegVak, 1);
                OK = check(voorlopig);
                while (!OK) {
                    int indexLaatste = pad.size() - 1;
                    if (indexLaatste < 0) {
                        oplosbaar = false;
                        finished = true;
                        running = false;
                        OK = true;
                    } else {
                        String key = pad.get(indexLaatste);
                        if (voorlopig.get(key) < 9) {
                            int waarde = voorlopig.get(key) + 1;
                            voorlopig.replace(key, waarde);
                            OK = check(voorlopig);
                        } else {
                            boolean nothingLeft = true;
                            voorlopig.replace(key, 0);
                            pad.remove(indexLaatste);
                            while (nothingLeft) {
                                int nieuweIndex = pad.size() - 1;
                                if (nieuweIndex < 0) {
                                    oplosbaar = false;
                                    finished = true;
                                    running = false;
                                    nothingLeft = false;
                                    OK = true;
                                } else {
                                    String nieuweKey = pad.get(nieuweIndex);
                                    if (voorlopig.get(nieuweKey) < 9) {
                                        int waarde = voorlopig.get(nieuweKey) + 1;
                                        voorlopig.replace(nieuweKey, waarde);
                                        OK = check(voorlopig);
                                        nothingLeft = false;
                                    } else {
                                        voorlopig.replace(nieuweKey, 0);
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

    //het algoritme maakt gebruik van de hierna volgende deelfuncties
    //de methode leegVak geeft de naam van het eerstvolgende lege
    //vak terug; indien er geen is geeft de methode de waarde 'null' terug
    public String leegVak() {
        leegVak = "";
        for (String s : voorlopig.keySet()) {
            if (voorlopig.get(s) == 0) {
                return s;
            }
        }
        return leegVak;
    }

    //de methode check geeft de boolean OK terug;
    //indien true dan is de sudoku voorlopig goed ingevuld
    //indien false dan is de sudoku niet correct ingevuld
    public boolean check(HashMap<String,Integer>sudoku) {
        boolean OK = checkRijen(sudoku);
        if (OK) {
            OK = checkKolommen(sudoku);
        }
        if (OK) {
            OK = checkBlokken(sudoku);
        }
        return OK;
    }

    //bijkomende methodes horende bij de methode check()
    //de bedoeling van deze methodes spreekt voor zich
    public boolean checkRijen(HashMap<String,Integer>sudoku) {
        boolean OK = true;
        for (int i = 1; i < 10; i++) {
            HashSet<Integer> cijfers = new HashSet<>();
            int teller = 0;
            for (char j = 'a'; j < 'j'; j++) {
                String vak = "" + j + i;
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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

    public boolean checkKolommen(HashMap<String,Integer>sudoku) {
        boolean OK = true;
        for (char j = 'a'; j < 'j'; j++) {
            HashSet<Integer> cijfers = new HashSet<>();
            int teller = 0;
            for (int i = 1; i < 10; i++) {
                String vak = "" + j + i;
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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

    public boolean checkBlokken(HashMap<String,Integer>sudoku) {
        boolean OK = true;
        HashSet<Integer> cijfers = new HashSet<>();
        int teller = 0;

        //checken blokken met letters a,b of c
        for (int i = 1; i < 4; i++) {
            for (char j = 'a'; j < 'd'; j++) {
                String vak = "" + j + i;
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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
                if (sudoku.get(vak) != 0) {
                    cijfers.add(sudoku.get(vak));
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

