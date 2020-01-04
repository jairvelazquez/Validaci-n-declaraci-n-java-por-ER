
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *Actualmente solo funciona el diccionario creado con los dos arraylist
 */

/**
 *
 * @author Jair Velazquez
 */
public class LecturaArchivo {
    String archivo;
    String cadenaCompleta;
    Map<String,String> identificadores;
    String fraseCompleta;
    boolean debeContinuar=true;
    int contadorPositivo = 0;
    int contadorNegativo = 0;
    int contadorInmersion = 0;
    int contadorTotal = 0;
    int contadorPalabra = 0;
    int contadorModificador = 0;
    public LecturaArchivo(){
        archivo = "C:\\Users\\ejv99\\Documents\\NetBeansProjects\\ProyectoAutomatas\\src\\Automatas.txt";//Aqui va la ruta donde Automatas.txt esta guardada
        cadenaCompleta = "";
        identificadores = new HashMap<>();
        fraseCompleta = "";
    }
    
    public void capturaContenido() throws IOException{
        String cadena;
        int contador = 2;//Si el contador es par, se guarda la linea, esto para eliminar los espacios entre renglones
        try {
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null){
                if(contador%2==0){//Se elimnan los espaciados entre renglones
                    String[] valores = cadena.split("::=");
                    identificadores.put(valores[0].trim(), valores[1].trim());// se guarda el identificador junto con su clave  
                }
                contador++;
            }
            b.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecturaArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean busquedaExhaustiva(String input,String noTerminal){//Aqui se implementa la recursividad
        noTerminal = noTerminal.trim();
        System.out.println(input+":"+noTerminal);
        if(input.compareTo(noTerminal)==0){
            fraseCompleta = fraseCompleta.concat(" "+input);
            System.out.println("Coincide!!");
            return true;
        }
        
        if(noTerminal.compareTo("<class*modifiers>")==0){
            contadorModificador++;
            if(contadorModificador>2){
                return false;
            }
        }
        
        if(identificadores.containsKey(noTerminal)&&identificadores.get(noTerminal).contains(" ")){
            for(String caminos:identificadores.get(noTerminal).split(" ")){
                contadorPositivo++;
                if(caminos.contains("?")&&(contadorPalabra==1||contadorPalabra==4||contadorPalabra==6||contadorPalabra==2))caminos = caminos.replace("?","");
                if(busquedaExhaustiva(input,caminos))return true; 
            }
        }
        if (identificadores.containsKey(noTerminal)&&identificadores.get(noTerminal).contains("\\|")){//Tiene multples opciones
            for(String caminos:identificadores.get(noTerminal).split("\\|")){
                busquedaExhaustiva(input,caminos);
            }
        }
        if(identificadores.containsKey(noTerminal)&&identificadores.get(noTerminal).contains("?")){//Tiene signos de pregunta
            busquedaExhaustiva(input,identificadores.get(noTerminal).substring(0, identificadores.get(noTerminal).length()-1));
        }
        if(identificadores.containsKey(noTerminal)){//es sencilla
            busquedaExhaustiva(input,identificadores.get(noTerminal));
        }
        return false;
    }
    public void busquedaCompleta(String input){
        String noTerminal ="<class*declaration>";//es lo que estamos buscando
        String[] inputDividido = input.split(" ");
        contadorPalabra = 0;
        for (String inputDividido1 : inputDividido) {
            contadorPalabra++;
            if(inputDividido1.contains("?"))inputDividido1= inputDividido1.replace("?", "");
            if(busquedaExhaustiva(inputDividido1, noTerminal)){
                System.out.println("Encontrado");
            }
        }
            fraseCompleta = fraseCompleta.trim();
    }
    public void imprimeDiccionario(){
        Iterator iter = identificadores.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) iter.next();
            System.out.println(mEntry.getKey() + ":" + mEntry.getValue());
            
        }
    }
    public static void main(String[] args){
        LecturaArchivo leer = new LecturaArchivo();
        try {
            leer.capturaContenido();
            String validacion4 = "public class x {}"; 
            String validacion2 = "class y {}";
            String validacion3 = "public final class extends y implements a , b , c {}";

            System.out.println("Validacion 3");
            leer.busquedaCompleta(validacion3); 
            System.out.println(leer.fraseCompleta+"\n"+validacion3);
            if(leer.fraseCompleta.equals(validacion3)){
                System.out.println("Es valida");
            }
            else{
                System.out.println("Es invalida");
            }
            leer.fraseCompleta = "";
            System.out.println("Validacion 1");
            leer.busquedaCompleta(validacion4); 
            System.out.println(leer.fraseCompleta+"\n"+validacion4);
            if(leer.fraseCompleta.equals(validacion4)){
                System.out.println("Es valida");
            }
            else{
                System.out.println("Es invalida");
            }
            leer.fraseCompleta = "";
            System.out.println("Validacion 2");
            leer.busquedaCompleta(validacion2); 
            System.out.println(leer.fraseCompleta+"\n"+validacion2);
            if(leer.fraseCompleta.equals(validacion2)){
                System.out.println("Es valida");
            }
            else{
                System.out.println("Es invalida");
            }
            
            
        }catch (IOException ex) {
            Logger.getLogger(LecturaArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
