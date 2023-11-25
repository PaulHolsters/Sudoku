/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;

/*
 * @author Paul Holsters
 */
public class PaneelSudoku extends javax.swing.JPanel {

    private HashMap<String, Integer> opgave = new HashMap<>();
    private HashMap<String, Integer> oplossing = new HashMap<>();
    private HashMap<String, Integer> opgaveKopie = new HashMap<>();
    private HashMap<String, Integer> oplossingKopie = new HashMap<>();
    private HashMap<javax.swing.JTextField, String[]> velden = new HashMap<>();
    private ArrayList<String> foutieveVelden = new ArrayList<>();

    private Oplossing o = new Oplossing();
    private Genereer g = new Genereer();
    private Info i = new Info();

    private boolean buttonOplossing = false;
    private boolean buttonGenereer = false;
    private boolean buttonHint = false;
    private boolean buttonControleer = false;
    private boolean buttonInvoeren = false;
    private boolean buttonReset = false;
    private boolean buttonVorige = false;
    private boolean buttonInfo = false;

    private boolean conform = true;
    private boolean voldoendeCijfers = true;
    private boolean geenOplossing = false;
    private boolean foutief = false;
    private boolean oplosbaar = true;
    private boolean threadGenereer = false;
    private boolean threadInvoeren = false;
    private boolean geenKopie = false;
    private boolean vol = false;

    public PaneelSudoku() {
        //initialisatie
        initComponents();
        maakVelden();
        jButtonControleer.setFocusable(false);
        jButtonGenereer.setFocusable(false);
        jButtonHint.setFocusable(false);
        jButtonInvoeren.setFocusable(false);
        jButtonOplossing.setFocusable(false);
        jButtonVorige.setFocusable(false);
        jButtonInfo.setFocusable(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //achtergrond
        Graphics2D g2d = (Graphics2D) g;
        float strokeThickness = 3.0f;
        BasicStroke stroke = new BasicStroke(strokeThickness);
        g2d.setStroke(stroke);
        g2d.drawLine(30, 25, 383, 25);
        g2d.drawLine(30, 142, 383, 142);
        g2d.drawLine(30, 264, 383, 264);
        g2d.drawLine(30, 385, 383, 385);
        g2d.drawLine(30, 25, 30, 385);
        g2d.drawLine(147, 25, 147, 385);
        g2d.drawLine(267, 25, 267, 385);
        g2d.drawLine(383, 25, 383, 385);
        //boodschappen tonen
        if (buttonOplossing) {
            if (geenOplossing) {
                g.drawString("Er is geen deugdelijke opgave beschikbaar.", 30, 450);
                geenOplossing = false;
                buttonOplossing = false;
            } else {
                g.drawString("De oplossing!", 30, 450);
                buttonOplossing = false;
            }
        } else if (buttonControleer) {
            if (geenOplossing) {
                g.drawString("Er is geen deugdelijke opgave beschikbaar.", 30, 450);
                geenOplossing = false;
                buttonControleer = false;
            } else if (foutief) {
                g.drawString("De rode cijfers zijn verkeerd.", 30, 450);
                foutief = false;
                buttonControleer = false;
            } else {
                g.drawString("De sudoku werd tot nog toe correct aangevuld.", 30, 450);
                buttonControleer = false;
            }
        } else if (buttonInvoeren) {
            if (threadInvoeren) {
                g.drawString("De opgave wordt gecontroleerd. Even geduld...", 30, 450);
            } else {
                if (!oplosbaar) {
                    g.drawString("Deze opgave heeft geen (unieke) oplossing.", 30, 450);
                    oplosbaar = true;
                    voldoendeCijfers = true;
                    conform = true;
                    buttonInvoeren = false;
                } else if (!voldoendeCijfers) {
                    g.drawString("Een opgave bevat minstens 17 cijfers. De door u voorgestelde opgave werd derhalve niet ingevoerd.", 30, 450);
                    oplosbaar = true;
                    voldoendeCijfers = true;
                    conform = true;
                    buttonInvoeren = false;
                } else if (!conform) {
                    g.drawString("De door u voorgestelde opgave is niet conform de regels van het spel.", 30, 450);
                    oplosbaar = true;
                    voldoendeCijfers = true;
                    conform = true;
                    buttonInvoeren = false;
                } else {
                    g.drawString("De door u voorgestelde opgave werd succesvol aangemaakt.", 30, 450);
                    buttonInvoeren = false;
                }
            }
        } else if (buttonHint) {
            if (geenOplossing) {
                g.drawString("Er werd geen opgave aangemaakt.", 30, 450);
                geenOplossing = false;
                buttonHint = false;
            } else if (vol) {
                g.drawString("Deze sudoku is al opgelost.", 30, 450);
                vol = false;
                buttonHint = false;
            } else {
                g.drawString("De hint staat in het blauw. Veel succes!", 30, 450);
                buttonHint = false;
            }
        } else if (buttonGenereer) {
            if (threadGenereer) {
                g.drawString("Een nieuwe opgave wordt aangemaakt. Even geduld...", 30, 450);
            }
        } else if (buttonVorige) {
            if (geenKopie) {
                buttonVorige = false;
                geenKopie = false;
                g.drawString("Resetten is op dit moment nog niet mogelijk.", 30, 450);
            } else {
                buttonVorige = false;
                g.drawString("Klaar met resetten.", 30, 450);
            }
        }
    }

    //BEGIN methoden INITIALISATIE
    public void maakVelden() {
        jTextField_A1.setName("a1");
        velden.put(this.jTextField_A1, null);
        jTextField_B1.setName("b1");
        velden.put(this.jTextField_B1, null);
        jTextField_C1.setName("c1");
        velden.put(this.jTextField_C1, null);
        jTextField_D1.setName("d1");
        velden.put(this.jTextField_D1, null);
        jTextField_E1.setName("e1");
        velden.put(this.jTextField_E1, null);
        jTextField_F1.setName("f1");
        velden.put(this.jTextField_F1, null);
        jTextField_G1.setName("g1");
        velden.put(this.jTextField_G1, null);
        jTextField_H1.setName("h1");
        velden.put(this.jTextField_H1, null);
        jTextField_I1.setName("i1");
        velden.put(this.jTextField_I1, null);
        jTextField_A2.setName("a2");
        velden.put(this.jTextField_A2, null);
        jTextField_B2.setName("b2");
        velden.put(this.jTextField_B2, null);
        jTextField_C2.setName("c2");
        velden.put(this.jTextField_C2, null);
        jTextField_D2.setName("d2");
        velden.put(this.jTextField_D2, null);
        jTextField_E2.setName("e2");
        velden.put(this.jTextField_E2, null);
        jTextField_F2.setName("f2");
        velden.put(this.jTextField_F2, null);
        jTextField_G2.setName("g2");
        velden.put(this.jTextField_G2, null);
        jTextField_H2.setName("h2");
        velden.put(this.jTextField_H2, null);
        jTextField_I2.setName("i2");
        velden.put(this.jTextField_I2, null);
        jTextField_F3.setName("f3");
        velden.put(this.jTextField_F3, null);
        jTextField_A3.setName("a3");
        velden.put(this.jTextField_A3, null);
        jTextField_C3.setName("c3");
        velden.put(this.jTextField_C3, null);
        jTextField_D3.setName("d3");
        velden.put(this.jTextField_D3, null);
        jTextField_G3.setName("g3");
        velden.put(this.jTextField_G3, null);
        jTextField_I3.setName("i3");
        velden.put(this.jTextField_I3, null);
        jTextField_H3.setName("h3");
        velden.put(this.jTextField_H3, null);
        jTextField_B3.setName("b3");
        velden.put(this.jTextField_B3, null);
        jTextField_E3.setName("e3");
        velden.put(this.jTextField_E3, null);
        jTextField_A4.setName("a4");
        velden.put(this.jTextField_A4, null);
        jTextField_B4.setName("b4");
        velden.put(this.jTextField_B4, null);
        jTextField_C4.setName("c4");
        velden.put(this.jTextField_C4, null);
        jTextField_D4.setName("d4");
        velden.put(this.jTextField_D4, null);
        jTextField_E4.setName("e4");
        velden.put(this.jTextField_E4, null);
        jTextField_F4.setName("f4");
        velden.put(this.jTextField_F4, null);
        jTextField_G4.setName("g4");
        velden.put(this.jTextField_G4, null);
        jTextField_H4.setName("h4");
        velden.put(this.jTextField_H4, null);
        jTextField_I4.setName("i4");
        velden.put(this.jTextField_I4, null);
        jTextField_A5.setName("a5");
        velden.put(this.jTextField_A5, null);
        jTextField_H5.setName("h5");
        velden.put(this.jTextField_H5, null);
        jTextField_C5.setName("c5");
        velden.put(this.jTextField_C5, null);
        jTextField_E5.setName("e5");
        velden.put(this.jTextField_E5, null);
        jTextField_F5.setName("f5");
        velden.put(this.jTextField_F5, null);
        jTextField_D5.setName("d5");
        velden.put(this.jTextField_D5, null);
        jTextField_I5.setName("i5");
        velden.put(this.jTextField_I5, null);
        jTextField_B5.setName("b5");
        velden.put(this.jTextField_B5, null);
        jTextField_G5.setName("g5");
        velden.put(this.jTextField_G5, null);
        jTextField_A6.setName("a6");
        velden.put(this.jTextField_A6, null);
        jTextField_H6.setName("h6");
        velden.put(this.jTextField_H6, null);
        jTextField_G6.setName("g6");
        velden.put(this.jTextField_G6, null);
        jTextField_E6.setName("e6");
        velden.put(this.jTextField_E6, null);
        jTextField_F6.setName("f6");
        velden.put(this.jTextField_F6, null);
        jTextField_D6.setName("d6");
        velden.put(this.jTextField_D6, null);
        jTextField_I6.setName("i6");
        velden.put(this.jTextField_I6, null);
        jTextField_B6.setName("b6");
        velden.put(this.jTextField_B6, null);
        jTextField_C6.setName("c6");
        velden.put(this.jTextField_C6, null);
        jTextField_C7.setName("c7");
        velden.put(this.jTextField_C7, null);
        jTextField_G7.setName("g7");
        velden.put(this.jTextField_G7, null);
        jTextField_E7.setName("e7");
        velden.put(this.jTextField_E7, null);
        jTextField_D7.setName("d7");
        velden.put(this.jTextField_D7, null);
        jTextField_I7.setName("i7");
        velden.put(this.jTextField_I7, null);
        jTextField_A7.setName("a7");
        velden.put(this.jTextField_A7, null);
        jTextField_H7.setName("h7");
        velden.put(this.jTextField_H7, null);
        jTextField_F7.setName("f7");
        velden.put(this.jTextField_F7, null);
        jTextField_B7.setName("b7");
        velden.put(this.jTextField_B7, null);
        jTextField_H8.setName("h8");
        velden.put(this.jTextField_H8, null);
        jTextField_G8.setName("g8");
        velden.put(this.jTextField_G8, null);
        jTextField_B8.setName("b8");
        velden.put(this.jTextField_B8, null);
        jTextField_C8.setName("c8");
        velden.put(this.jTextField_C8, null);
        jTextField_F8.setName("f8");
        velden.put(this.jTextField_F8, null);
        jTextField_A8.setName("a8");
        velden.put(this.jTextField_A8, null);
        jTextField_E8.setName("e8");
        velden.put(this.jTextField_E8, null);
        jTextField_D8.setName("d8");
        velden.put(this.jTextField_D8, null);
        jTextField_I8.setName("i8");
        velden.put(this.jTextField_I8, null);
        jTextField_I9.setName("i9");
        velden.put(this.jTextField_I9, null);
        jTextField_D9.setName("d9");
        velden.put(this.jTextField_D9, null);
        jTextField_H9.setName("h9");
        velden.put(this.jTextField_H9, null);
        jTextField_G9.setName("g9");
        velden.put(this.jTextField_G9, null);
        jTextField_F9.setName("f9");
        velden.put(this.jTextField_F9, null);
        jTextField_A9.setName("a9");
        velden.put(this.jTextField_A9, null);
        jTextField_C9.setName("c9");
        velden.put(this.jTextField_C9, null);
        jTextField_E9.setName("e9");
        velden.put(this.jTextField_E9, null);
        jTextField_B9.setName("b9");
        velden.put(this.jTextField_B9, null);
        maakLeeg();
    }

    public void maakLeeg() {
        for (javax.swing.JTextField c : velden.keySet()) {
            c.setText("");
            c.setEditable(true);
            c.setForeground(Color.black);
            c.setBackground(Color.white);
            Font f = new Font("TimesRoman", Font.PLAIN, 20);
            c.setFont(f);
            String[] layout = new String[]{"TimesRoman", "plain", "20", "VGzwart", "AGwit", "aanpasbaar"};
            velden.put(c, layout);
        }
    }
    //EINDE methoden INITIALISATIE

    //BEGIN methoden voor de BEREKENINGEN nodig voor de buttons
    public void genereerSudoku() {
        //initialisatie
        maakLeeg();
        g.reset();
        o.reset();
        foutieveVelden.clear();
        oplossing.clear();
        opgave.clear();
        //berekening 'genereren'
        g.generateNow();
        //resultaat plaatsen in "opgave"
        for (String veld : g.getSudMap().keySet()) {
            opgave.put(veld, g.getSudMap().get(veld));
            opgaveKopie.put(veld, g.getSudMap().get(veld));
        }
        //oplossing berekenen en plaatsen in "oplossing"
        o.losop(opgave);
        for (String v : o.getVoorlopig().keySet()) {
            oplossing.put(v, o.getVoorlopig().get(v));
            oplossingKopie.put(v, o.getVoorlopig().get(v));
        }
        //"opgave" tonen
        threadGenereer = false;
        tonenSudoku(opgave);
        buttonGenereer = false;
    }

    public void invoerenSudoku() {
        //initialisatie
        opgave.clear();
        oplossing.clear();
        o.reset();
        g.reset();
        foutieveVelden.clear();
        //controle
        for (javax.swing.JTextField c : velden.keySet()) {
            if (!c.getText().trim().equals("") && !c.getText().trim().equals("1") && !c.getText().trim().equals("2") && !c.getText().trim().equals("3") && !c.getText().trim().equals("4") && !c.getText().trim().equals("5") && !c.getText().trim().equals("6") && !c.getText().trim().equals("7") && !c.getText().trim().equals("8") && !c.getText().trim().equals("9")) {
                conform = false;
                break;
            }
        }
        if (conform) {
            for (javax.swing.JTextField c : velden.keySet()) {
                String name = c.getName();
                if (c.getText().equals("")) {
                    opgave.put(name, 0);
                } else {
                    opgave.put(name, Integer.parseInt(c.getText()));
                }
            }
            conform = o.check(opgave);
        }
        if (conform) {
            int teller = 0;
            for (String v : opgave.keySet()) {
                if (opgave.get(v) != 0) {
                    teller++;
                }
            }
            if (teller < 17) {
                voldoendeCijfers = false;
            }
        }
        if (voldoendeCijfers && conform) {
            o.losop(opgave);
            if (!o.isOplosbaar()) {
                oplosbaar = false;
            }
        }
        //uitvoeren
        if (!conform || !voldoendeCijfers || !oplosbaar) {
            opgave.clear();
            threadInvoeren = false;
            repaint();
        } else {
            for (String v : o.getVoorlopig().keySet()) {
                oplossing.put(v, o.getVoorlopig().get(v));
                oplossingKopie.put(v, o.getVoorlopig().get(v));
            }
            for (String v2 : opgave.keySet()) {
                opgaveKopie.put(v2, opgave.get(v2));
            }
            threadInvoeren = false;
            tonenSudoku(opgave);
        }
    }

    public void controleerSudoku() {
        o.reset();
        g.reset();
        foutieveVelden.clear();
        if (opgave.size() == 0) {
            geenOplossing = true;
        } else {
            HashMap<String, String> tempSudoku = new HashMap<>();
            for (javax.swing.JTextField c : velden.keySet()) {
                tempSudoku.put(c.getName(), c.getText());
            }
            for (String veld : tempSudoku.keySet()) {
                if (!tempSudoku.get(veld).equals(Integer.toString(oplossing.get(veld))) && !tempSudoku.get(veld).equals("") && !tempSudoku.get(veld).isEmpty()) {
                    foutieveVelden.add(veld);
                    foutief = true;
                }
            }
            if (foutief) {
                for (javax.swing.JTextField c : velden.keySet()) {
                    if (foutieveVelden.contains(c.getName())) {
                        c.setForeground(Color.red);
                        velden.get(c)[3] = "VGrood";
                    } else {
                        c.setForeground(Color.black);
                        velden.get(c)[3] = "VGzwart";
                    }
                }
            } else {
                for (javax.swing.JTextField c : velden.keySet()) {
                    c.setForeground(Color.black);
                    velden.get(c)[3] = "VGzwart";
                }
            }
            foutieveVelden.clear();
        }
        repaint();
    }

    public void geefHint() {
        if (opgave.size() == 0) {
            geenOplossing = true;
            repaint();
        } else {
            boolean hint = false;
            for (javax.swing.JTextField c : velden.keySet()) {
                if (c.isFocusOwner()) {
                    if (opgave.get(c.getName()) == 0) {
                        if (!c.getText().trim().equals(Integer.toString(oplossing.get(c.getName())))) {
                            hint = true;
                            c.setText(Integer.toString(oplossing.get(c.getName())));
                            c.setForeground(Color.blue);
                            velden.get(c)[3] = "VGblauw";
                            break;
                        }
                    }
                }
            }
            if (!hint) {
                vol = true;
                for (javax.swing.JTextField c : velden.keySet()) {
                    if (opgave.get(c.getName()) == 0) {
                        if (!c.getText().trim().equals(Integer.toString(oplossing.get(c.getName())))) {
                            c.setText(Integer.toString(oplossing.get(c.getName())));
                            c.setForeground(Color.blue);
                            velden.get(c)[3] = "VGblauw";
                            vol = false;
                            break;
                        }
                    }
                }
            }
        }
        repaint();
    }

    public void vorige() {
        if (opgaveKopie.size() == 0) {
            geenKopie = true;
            repaint();
        } else {
            for (String v : opgaveKopie.keySet()) {
                opgave.put(v, opgaveKopie.get(v));
            }
            for (String v : oplossingKopie.keySet()) {
                oplossing.put(v, oplossingKopie.get(v));
            }
            maakLeeg();
            tonenSudoku(opgave);
        }
    }

    public void info() {
        JOptionPane.showMessageDialog(jButtonInfo, i.getTekst());
        buttonInfo = false;
    }

    public void oplossenSudoku() {
        if (oplossing.size() == 0) {
            geenOplossing = true;
            repaint();
        } else {
            tonenSudoku(oplossing);
        }
    }
    //EINDE methoden voor de BEREKENINGEN nodig voor de buttons

    //BEGIN methoden voor het TONEN van de gegevens op het scherm
    public void tonenSudoku(HashMap<String, Integer> sudoku) {
        //tonen sudoku na genereren
        if (buttonGenereer || buttonInvoeren || buttonVorige) {
            if (opgave.size() != 0) {
                for (String v1 : opgave.keySet()) {
                    for (javax.swing.JTextField c : velden.keySet()) {
                        if (c.getName().equals(v1)) {
                            if (opgave.get(v1) != 0) {
                                c.setText(Integer.toString(opgave.get(v1)));
                                velden.get(c)[0] = "TimesRoman";
                                velden.get(c)[1] = "bold";
                                velden.get(c)[2] = "20";
                                velden.get(c)[3] = "VGzwart";
                                velden.get(c)[4] = "AGlichtgrijs";
                                velden.get(c)[5] = "nietAanpasbaar";
                            } else {
                                c.setText("");
                                velden.get(c)[0] = "TimesRoman";
                                velden.get(c)[1] = "plain";
                                velden.get(c)[2] = "20";
                                velden.get(c)[3] = "VGzwart";
                                velden.get(c)[4] = "AGwit";
                                velden.get(c)[5] = "aanpasbaar";
                            }
                        }
                    }
                }
            }
        } //tonen van de oplossing
        else if (buttonOplossing) {
            for (String v1 : oplossing.keySet()) {
                for (javax.swing.JTextField c : velden.keySet()) {
                    if (c.getName().equals(v1)) {
                        c.setText(Integer.toString(oplossing.get(v1)));
                        if (opgave.get(v1) != 0) {
                            velden.get(c)[0] = "TimesRoman";
                            velden.get(c)[1] = "bold";
                            velden.get(c)[2] = "20";
                            velden.get(c)[3] = "VGzwart";
                            velden.get(c)[4] = "AGlichtgrijs";
                            velden.get(c)[5] = "nietAanpasbaar";
                        } else {
                            velden.get(c)[0] = "TimesRoman";
                            velden.get(c)[1] = "plain";
                            velden.get(c)[2] = "20";
                            velden.get(c)[3] = "VGzwart";
                            velden.get(c)[4] = "AGwit";
                            velden.get(c)[5] = "nietAanpasbaar";
                        }
                    }
                }
            }
        }
        toon();
        repaint();
    }

    public void toon() {
        for (javax.swing.JTextField c : velden.keySet()) {
            String stijl = velden.get(c)[1];
            String voorgrond = velden.get(c)[3];
            String achtergrond = velden.get(c)[4];
            String editable = velden.get(c)[5];
            if (stijl.equals("plain")) {
                Font f = new Font("TimesRoman", Font.PLAIN, 20);
                c.setFont(f);
            } else {
                Font f = new Font("TimesRoman", Font.BOLD, 20);
                c.setFont(f);
            }
            if (voorgrond.equals("VGzwart")) {
                c.setForeground(Color.black);
            } else {
                c.setForeground(Color.red);
            }
            if (achtergrond.equals("AGwit")) {
                c.setBackground(Color.white);
            } else {
                c.setBackground(Color.lightGray);
            }
            if (editable.equals("aanpasbaar")) {
                c.setEditable(true);
            } else {
                c.setEditable(false);
            }
        }
    }
    //EINDE methoden voor het TONEN van de gegevens op het scherm

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField_A1 = new javax.swing.JTextField();
        jTextField_B1 = new javax.swing.JTextField();
        jTextField_C1 = new javax.swing.JTextField();
        jTextField_D1 = new javax.swing.JTextField();
        jTextField_E1 = new javax.swing.JTextField();
        jTextField_F1 = new javax.swing.JTextField();
        jTextField_G1 = new javax.swing.JTextField();
        jTextField_H1 = new javax.swing.JTextField();
        jTextField_I1 = new javax.swing.JTextField();
        jTextField_A2 = new javax.swing.JTextField();
        jTextField_B2 = new javax.swing.JTextField();
        jTextField_C2 = new javax.swing.JTextField();
        jTextField_D2 = new javax.swing.JTextField();
        jTextField_E2 = new javax.swing.JTextField();
        jTextField_F2 = new javax.swing.JTextField();
        jTextField_G2 = new javax.swing.JTextField();
        jTextField_H2 = new javax.swing.JTextField();
        jTextField_I2 = new javax.swing.JTextField();
        jTextField_F3 = new javax.swing.JTextField();
        jTextField_A3 = new javax.swing.JTextField();
        jTextField_C3 = new javax.swing.JTextField();
        jTextField_D3 = new javax.swing.JTextField();
        jTextField_G3 = new javax.swing.JTextField();
        jTextField_I3 = new javax.swing.JTextField();
        jTextField_H3 = new javax.swing.JTextField();
        jTextField_B3 = new javax.swing.JTextField();
        jTextField_E3 = new javax.swing.JTextField();
        jTextField_A4 = new javax.swing.JTextField();
        jTextField_B4 = new javax.swing.JTextField();
        jTextField_C4 = new javax.swing.JTextField();
        jTextField_D4 = new javax.swing.JTextField();
        jTextField_E4 = new javax.swing.JTextField();
        jTextField_F4 = new javax.swing.JTextField();
        jTextField_G4 = new javax.swing.JTextField();
        jTextField_H4 = new javax.swing.JTextField();
        jTextField_I4 = new javax.swing.JTextField();
        jTextField_A5 = new javax.swing.JTextField();
        jTextField_H5 = new javax.swing.JTextField();
        jTextField_C5 = new javax.swing.JTextField();
        jTextField_E5 = new javax.swing.JTextField();
        jTextField_F5 = new javax.swing.JTextField();
        jTextField_D5 = new javax.swing.JTextField();
        jTextField_I5 = new javax.swing.JTextField();
        jTextField_B5 = new javax.swing.JTextField();
        jTextField_G5 = new javax.swing.JTextField();
        jTextField_A6 = new javax.swing.JTextField();
        jTextField_H6 = new javax.swing.JTextField();
        jTextField_G6 = new javax.swing.JTextField();
        jTextField_E6 = new javax.swing.JTextField();
        jTextField_F6 = new javax.swing.JTextField();
        jTextField_D6 = new javax.swing.JTextField();
        jTextField_I6 = new javax.swing.JTextField();
        jTextField_B6 = new javax.swing.JTextField();
        jTextField_C6 = new javax.swing.JTextField();
        jTextField_C7 = new javax.swing.JTextField();
        jTextField_G7 = new javax.swing.JTextField();
        jTextField_E7 = new javax.swing.JTextField();
        jTextField_D7 = new javax.swing.JTextField();
        jTextField_I7 = new javax.swing.JTextField();
        jTextField_A7 = new javax.swing.JTextField();
        jTextField_H7 = new javax.swing.JTextField();
        jTextField_F7 = new javax.swing.JTextField();
        jTextField_B7 = new javax.swing.JTextField();
        jTextField_H8 = new javax.swing.JTextField();
        jTextField_G8 = new javax.swing.JTextField();
        jTextField_B8 = new javax.swing.JTextField();
        jTextField_C8 = new javax.swing.JTextField();
        jTextField_F8 = new javax.swing.JTextField();
        jTextField_A8 = new javax.swing.JTextField();
        jTextField_E8 = new javax.swing.JTextField();
        jTextField_D8 = new javax.swing.JTextField();
        jTextField_I8 = new javax.swing.JTextField();
        jTextField_I9 = new javax.swing.JTextField();
        jTextField_D9 = new javax.swing.JTextField();
        jTextField_H9 = new javax.swing.JTextField();
        jTextField_G9 = new javax.swing.JTextField();
        jTextField_F9 = new javax.swing.JTextField();
        jTextField_A9 = new javax.swing.JTextField();
        jTextField_C9 = new javax.swing.JTextField();
        jTextField_E9 = new javax.swing.JTextField();
        jTextField_B9 = new javax.swing.JTextField();
        jButtonOplossing = new javax.swing.JButton();
        jButtonGenereer = new javax.swing.JButton();
        jButtonInvoeren = new javax.swing.JButton();
        jButtonControleer = new javax.swing.JButton();
        jButtonHint = new javax.swing.JButton();
        jButtonVorige = new javax.swing.JButton();
        jButtonInfo = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();

        jTextField_A1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_A1.setName("A1"); // NOI18N

        jTextField_B1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_B1.setName("B1"); // NOI18N

        jTextField_C1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_C1.setName("C1"); // NOI18N

        jTextField_D1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_D1.setName("D1"); // NOI18N

        jTextField_E1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_E1.setName("E1"); // NOI18N

        jTextField_F1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_F1.setName("F1"); // NOI18N

        jTextField_G1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_G1.setName("G1"); // NOI18N

        jTextField_H1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_H1.setName("H1"); // NOI18N

        jTextField_I1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_I9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_I9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_D9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_D9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_H9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_H9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_G9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_G9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_F9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_F9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_A9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_A9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_C9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_C9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_E9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_E9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextField_B9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField_B9.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButtonOplossing.setText("Oplossing");
        jButtonOplossing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOplossingActionPerformed(evt);
            }
        });

        jButtonGenereer.setText("Genereer sudoku");
        jButtonGenereer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenereerActionPerformed(evt);
            }
        });

        jButtonInvoeren.setText("Invoeren sudoku");
        jButtonInvoeren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInvoerenActionPerformed(evt);
            }
        });

        jButtonControleer.setText("Controleer sudoku");
        jButtonControleer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonControleerActionPerformed(evt);
            }
        });

        jButtonHint.setText("Geef hint");
        jButtonHint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHintActionPerformed(evt);
            }
        });

        jButtonVorige.setText("Vorige opgave");
        jButtonVorige.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVorigeActionPerformed(evt);
            }
        });

        jButtonInfo.setText("Info");
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        jButtonReset.setText("Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField_A7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_B7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_C7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_D7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_E7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_F7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField_G7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_H7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_I7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(78, 78, 78)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonOplossing, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jButtonGenereer, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jButtonInvoeren, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jButtonControleer, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jButtonHint, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jButtonVorige, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOplossing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGenereer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonInvoeren, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonControleer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonHint, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonVorige, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_A9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_B9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_C9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_D9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_E9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_F9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_G9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_H9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_I9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(311, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    //BEGIN BUTTONS 
    private void jButtonOplossingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOplossingActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonOplossing = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    oplossenSudoku();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonOplossingActionPerformed

    private void jButtonGenereerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenereerActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonGenereer = true;
            threadGenereer = true;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {

                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            genereerSudoku();
                        }
                    });
                    t2.start();
                    repaint();
                }
            });
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonGenereerActionPerformed

    private void jButtonInvoerenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInvoerenActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonInvoeren = true;
            threadInvoeren = true;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            invoerenSudoku();
                        }
                    });
                    t2.start();
                    repaint();
                }
            });
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonInvoerenActionPerformed

    private void jButtonControleerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonControleerActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonControleer = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    controleerSudoku();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonControleerActionPerformed

    private void jButtonHintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHintActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonHint = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    geefHint();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonHintActionPerformed

    private void jButtonVorigeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVorigeActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonVorige = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    vorige();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonVorigeActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        if (!buttonControleer && !buttonVorige && !buttonGenereer && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonInfo = true;
            info();
        }
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        if (!buttonControleer && !buttonGenereer && !buttonVorige && !buttonHint && !buttonInvoeren && !buttonOplossing && !buttonReset && !buttonInfo) {
            buttonReset = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    maakLeeg();
                    opgave.clear();
                    oplossing.clear();
                    buttonReset = false;
                    repaint();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PaneelSudoku.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonResetActionPerformed
    //EINDE BUTTONS

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonControleer;
    private javax.swing.JButton jButtonGenereer;
    private javax.swing.JButton jButtonHint;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonInvoeren;
    private javax.swing.JButton jButtonOplossing;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonVorige;
    private javax.swing.JTextField jTextField_A1;
    private javax.swing.JTextField jTextField_A2;
    private javax.swing.JTextField jTextField_A3;
    private javax.swing.JTextField jTextField_A4;
    private javax.swing.JTextField jTextField_A5;
    private javax.swing.JTextField jTextField_A6;
    private javax.swing.JTextField jTextField_A7;
    private javax.swing.JTextField jTextField_A8;
    private javax.swing.JTextField jTextField_A9;
    private javax.swing.JTextField jTextField_B1;
    private javax.swing.JTextField jTextField_B2;
    private javax.swing.JTextField jTextField_B3;
    private javax.swing.JTextField jTextField_B4;
    private javax.swing.JTextField jTextField_B5;
    private javax.swing.JTextField jTextField_B6;
    private javax.swing.JTextField jTextField_B7;
    private javax.swing.JTextField jTextField_B8;
    private javax.swing.JTextField jTextField_B9;
    private javax.swing.JTextField jTextField_C1;
    private javax.swing.JTextField jTextField_C2;
    private javax.swing.JTextField jTextField_C3;
    private javax.swing.JTextField jTextField_C4;
    private javax.swing.JTextField jTextField_C5;
    private javax.swing.JTextField jTextField_C6;
    private javax.swing.JTextField jTextField_C7;
    private javax.swing.JTextField jTextField_C8;
    private javax.swing.JTextField jTextField_C9;
    private javax.swing.JTextField jTextField_D1;
    private javax.swing.JTextField jTextField_D2;
    private javax.swing.JTextField jTextField_D3;
    private javax.swing.JTextField jTextField_D4;
    private javax.swing.JTextField jTextField_D5;
    private javax.swing.JTextField jTextField_D6;
    private javax.swing.JTextField jTextField_D7;
    private javax.swing.JTextField jTextField_D8;
    private javax.swing.JTextField jTextField_D9;
    private javax.swing.JTextField jTextField_E1;
    private javax.swing.JTextField jTextField_E2;
    private javax.swing.JTextField jTextField_E3;
    private javax.swing.JTextField jTextField_E4;
    private javax.swing.JTextField jTextField_E5;
    private javax.swing.JTextField jTextField_E6;
    private javax.swing.JTextField jTextField_E7;
    private javax.swing.JTextField jTextField_E8;
    private javax.swing.JTextField jTextField_E9;
    private javax.swing.JTextField jTextField_F1;
    private javax.swing.JTextField jTextField_F2;
    private javax.swing.JTextField jTextField_F3;
    private javax.swing.JTextField jTextField_F4;
    private javax.swing.JTextField jTextField_F5;
    private javax.swing.JTextField jTextField_F6;
    private javax.swing.JTextField jTextField_F7;
    private javax.swing.JTextField jTextField_F8;
    private javax.swing.JTextField jTextField_F9;
    private javax.swing.JTextField jTextField_G1;
    private javax.swing.JTextField jTextField_G2;
    private javax.swing.JTextField jTextField_G3;
    private javax.swing.JTextField jTextField_G4;
    private javax.swing.JTextField jTextField_G5;
    private javax.swing.JTextField jTextField_G6;
    private javax.swing.JTextField jTextField_G7;
    private javax.swing.JTextField jTextField_G8;
    private javax.swing.JTextField jTextField_G9;
    private javax.swing.JTextField jTextField_H1;
    private javax.swing.JTextField jTextField_H2;
    private javax.swing.JTextField jTextField_H3;
    private javax.swing.JTextField jTextField_H4;
    private javax.swing.JTextField jTextField_H5;
    private javax.swing.JTextField jTextField_H6;
    private javax.swing.JTextField jTextField_H7;
    private javax.swing.JTextField jTextField_H8;
    private javax.swing.JTextField jTextField_H9;
    private javax.swing.JTextField jTextField_I1;
    private javax.swing.JTextField jTextField_I2;
    private javax.swing.JTextField jTextField_I3;
    private javax.swing.JTextField jTextField_I4;
    private javax.swing.JTextField jTextField_I5;
    private javax.swing.JTextField jTextField_I6;
    private javax.swing.JTextField jTextField_I7;
    private javax.swing.JTextField jTextField_I8;
    private javax.swing.JTextField jTextField_I9;
    // End of variables declaration//GEN-END:variables

}
