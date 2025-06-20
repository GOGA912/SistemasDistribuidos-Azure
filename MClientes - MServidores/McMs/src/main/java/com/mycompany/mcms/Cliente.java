package com.mycompany.mcms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        int nip,opc = 0,montoR;
        double montoD;
        String cuentaD;
        
        try{
            //Conectando al servidor de autenticacion
            Socket sAut = new Socket("localhost",3000);
            BufferedReader entAut = new BufferedReader(new InputStreamReader(sAut.getInputStream()));
            PrintWriter salAut = new PrintWriter(sAut.getOutputStream(), true);
            Scanner scan = new Scanner(System.in);
            //Solicitando numero de cuenta
            System.out.print("Ingrese su numero de cuenta: ");
            String numCuenta = scan.nextLine();
            //Solicitando NIP 
            System.out.print("Ingrese su NIP: ");
            nip = scan.nextInt();
            //Enviar info al servidor
            salAut.println(numCuenta);
            salAut.println(nip);
            salAut.flush();
            //Recibiendo respueta de servidor
            String autenticacion = entAut.readLine();
            System.out.println("\n"+autenticacion);
            //Si se autentico correctamente el cliente
            if(!autenticacion.startsWith("Autenticacion Exitosa")){
                sAut.close();
                return;
            }
            sAut.close();
            
            while(true){
                if(opc == 5){
                    break;
                }
                System.out.println("\nSeleccione la opcion deseada:");
                System.out.println("1. Consultar saldo");
                System.out.println("2. Depositar dinero");
                System.out.println("3. Retirar dinero");
                System.out.println("4. Trasferencia");
                System.out.println("5. Salir");
                opc = scan.nextInt();
                switch(opc){
                    case 1: //Conectando al servidor de Saldo
                            try{
                                Socket sSaldo = new Socket("localhost",4000);
                                BufferedReader entSaldo = new BufferedReader(new InputStreamReader(sSaldo.getInputStream()));
                                PrintWriter salSaldo = new PrintWriter(sSaldo.getOutputStream(), true);
                                //Enviando numero de cuenta
                                salSaldo.println("C");
                                salSaldo.println(numCuenta);
                                salSaldo.flush();
                                System.out.print("\nSaldo Actual: $"+entSaldo.readLine()+"\n");
                            }catch(IOException e){
                                System.out.println("Error al consutar el saldo");
                            }
                            break;
                    case 2: //Conectando al servidor de transacciones (Deposito)
                            try{
                                Socket sDeposito = new Socket("localhost",5000);
                                BufferedReader entDeposito = new BufferedReader(new InputStreamReader(sDeposito.getInputStream()));
                                PrintWriter salDeposito = new PrintWriter(sDeposito.getOutputStream(), true);
                               
                                System.out.print("Ingrese el monto a depositar: $");
                                montoD = scan.nextDouble();
                                
                                salDeposito.println("D");
                                salDeposito.println(numCuenta);
                                salDeposito.println(montoD);
                                salDeposito.flush();
                                
                                System.out.println(entDeposito.readLine());
                            }catch(IOException e){
                                System.out.println("Error al depositar dinero"); 
                            }
                            break;
                    case 3: //Conectando a servidor de transacciones (Retiros)
                            try{
                                Socket sRetiro = new Socket("localhost",5000);
                                BufferedReader entRetiro = new BufferedReader(new InputStreamReader(sRetiro.getInputStream()));
                                PrintWriter salRetiro = new PrintWriter(sRetiro.getOutputStream(), true);
                                
                                System.out.print("Ingrese el monto a retirar: $");
                                montoR = scan.nextInt();
                                
                                salRetiro.println("R");
                                salRetiro.println(numCuenta);
                                salRetiro.println(montoR);
                                salRetiro.flush();
                                
                                System.out.println(entRetiro.readLine());
                            }catch(IOException e){
                                System.out.println("Error al retirar dinero");
                            }
                            break;
                    case 4: //Conectando a servidor de transacciones (Transferencias)
                            try{
                                Socket sTrans = new Socket("localhost",5000);
                                BufferedReader entTrans = new BufferedReader(new InputStreamReader(sTrans.getInputStream()));
                                PrintWriter salTrans = new PrintWriter(sTrans.getOutputStream(), true);
                                
                                salTrans.println("T");
                                salTrans.println(numCuenta);
                                salTrans.flush();
                                
                                System.out.print("\nSaldo disponible: $"+entTrans.readLine());
                                System.out.print("\nIngrese el numero de cuenta destino: ");
                                scan.nextLine();
                                cuentaD = scan.nextLine();
                                salTrans.println(cuentaD);
                                salTrans.flush();
                                
                                System.out.print("Ingresa el monto a depositar: $");
                                montoD = scan.nextInt();
                                salTrans.println(montoD);
                                salTrans.flush();
                                
                                System.out.println(entTrans.readLine());
                                
                            }catch(IOException e){
                                System.out.println("Error al retirar dinero");
                            }
                            break;
                    case 5: System.out.println("Sesion finalizada");
                            break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
