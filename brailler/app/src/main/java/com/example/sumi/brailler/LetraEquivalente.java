package com.example.sumi.brailler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sumi on 23-Mar-18.
 */

public class LetraEquivalente {
    String resposta; //uma unica letra
    HashMap<String, String> equivalente = new HashMap<String, String>();

    public LetraEquivalente() {
        equivalente.put("A", "100000");
        equivalente.put("B", "101000");
        equivalente.put("C", "110000");
        equivalente.put("D", "110100");
        equivalente.put("E", "100100");
        equivalente.put("F", "111000");
        equivalente.put("G", "111100");
        equivalente.put("H", "101100");
        equivalente.put("I", "011000");
        equivalente.put("J", "011100");
        equivalente.put("K", "100010");
        equivalente.put("L", "101010");
        equivalente.put("M", "110010");
        equivalente.put("N", "110110");
        equivalente.put("O", "100110");
        equivalente.put("P", "111010");
        equivalente.put("Q", "111110");
        equivalente.put("R", "101110");
        equivalente.put("S", "011010");
        equivalente.put("T", "011110");
        equivalente.put("U", "100011");
        equivalente.put("V", "101011");
        equivalente.put("W", "011101");
        equivalente.put("X", "110011");
        equivalente.put("Y", "110111");
        equivalente.put("Z", "100111");
        equivalente.put(".", "001101");
        equivalente.put(",", "001000");
        equivalente.put("?", "001011");
        equivalente.put("!", "001110");
        equivalente.put(",", "000010");
        equivalente.put("_", "000011");
        equivalente.put("#", "010111");
        equivalente.put("0", "011100");
        equivalente.put("1", "100000");
        equivalente.put("2", "101000");
        equivalente.put("3", "110000");
        equivalente.put("4", "110100");
        equivalente.put("5", "100100");
        equivalente.put("6", "111000");
        equivalente.put("7", "111100");
        equivalente.put("8", "101100");
        equivalente.put("9", "011000");
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getBinario(String letra){
        return equivalente.get(letra);
    }

    public String getLetra(String braille){
        for(Map.Entry<String,String> entrada : equivalente.entrySet()){
            String letra = entrada.getKey();
            String binario = entrada.getValue();
            if(braille.equals(binario)){
                return letra;
            }
        }
        return "";
    }




}
