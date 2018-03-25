package com.example.sumi.brailler;

import java.util.HashMap;
import java.util.Map;

import static com.example.sumi.brailler.MenuPrincipal.braille_to_text;
import static com.example.sumi.brailler.MenuPrincipal.text_to_braille;


/**
 * Created by Sumi on 23-Mar-18.
 */

public class LetraEquivalente {
    String resposta; //uma unica letra

    public LetraEquivalente() {
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getBinario(String letra){
        return braille_to_text.get(letra);
    }

    public String getLetra(String braille){
        return text_to_braille.get(braille);
    }


}
