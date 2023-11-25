/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author paulh
 */
public class Info {
    
    private String tekst = "Om een willekeurige hint te verkrijgen, klik op een veld waarin een cijfer van de opgave"+"\n"
            +"staat en vervolgens op 'Geef hint'."+"\n"+"\n"
            +"Om een eigen opgave te maken vul je de gegeven cijfers in de velden in en klikt vervolgens op 'Invoeren sudoku'."+"\n"
            +"Pas daarna is het mogelijk om via 'Oplossing' de oplossing van je zelf ingevoerde sudoku te verkrijgen."+"\n"+"\n"
            +"Wanneer er geen opgave beschikbaar is krijg je de laatste opgave terug op het scherm door op 'Vorige opgave' te klikken."+"\n"
            +"Een opgave wordt aangemaakt door een sudoku te genereren of er zelf één in te voeren.";

    public String getTekst() {
        return tekst;
    }
    
}
