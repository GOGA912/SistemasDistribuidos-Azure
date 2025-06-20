package com.mycompany.atm.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        int nip,opc = 0,montoR,montoD;
        try{
            Socket cl = new Socket("localhost",3000);
            BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            PrintWriter sal = new PrintWriter(cl.getOutputStream(), true);
            Scanner scan = new Scanner(System.in);
            //Solicitando numero de cuenta
            System.out.print("Ingrese su numero de cuenta: ");
            String numCuenta = scan.nextLine();
            //Solicitando NIP 
            System.out.print("Ingrese su NIP: ");
            nip = scan.nextInt();
            //Enviar info al servidor
            sal.println(numCuenta);
            sal.println(nip);
            sal.flush();
            //Recibiendo respueta de servidor
            String autenticacion = ent.readLine();
            System.out.println("\n"+autenticacion);
            //
            if(!autenticacion.startsWith("Bienvenido"))
                return;
            
            while(true){
                if(opc == 4){
                    break;
                }
                System.out.println("Seleccione la opcion deseada:");
                System.out.println("1. Consultar saldo");
                System.out.println("2. Depositar dinero");
                System.out.println("3. Retirar dinero");
                System.out.println("4. Salir");
                opc = scan.nextInt();
                
                switch(opc){
                    case 1: sal.println("C");
                            sal.flush();
                            System.out.print("\n");
                            break;
                    case 2: System.out.print("\nIngrese el monto a depositar: $");
                            montoD = scan.nextInt();
                            sal.println("D");
                            sal.println(montoD);
                            sal.flush();
                            break;
                    case 3: System.out.print("Ingrese el monto a retirar: $");
                            montoR = scan.nextInt();
                            sal.println("R");
                            sal.println(montoR);
                            sal.flush();
                            break;
                    case 4: sal.println("S");
                            sal.flush();
                            break;
                }
                System.out.println(ent.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
