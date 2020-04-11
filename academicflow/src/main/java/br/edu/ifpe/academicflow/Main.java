package br.edu.ifpe.academicflow;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

class Main {
    public static void main(String[] args) throws IOException {
        Random r = new Random();
        Aluno alunos[] = new Aluno[100];
        double notaCorteAbs[] = new double[6];
        boolean matriculados[][][] = new boolean[10][6][100];
        int totalMatriculados[] = new int[10];
        int totalEgressos[] = new int[10];
        int totalDesistentes[] = new int[10];
        boolean tmpEgressos;
        //FileWriter fw = new FileWriter("out.txt");
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream("out.txt"),"ISO-8859-1");
        PrintWriter pw = new PrintWriter(fw);


        for (int indAlunos=0; indAlunos<=99; indAlunos++){
            alunos[indAlunos] = new Aluno(indAlunos,(r.nextGaussian()*2)+6,(r.nextGaussian()*0.5)+1);
        }

        pw.println("===================");
        pw.println("CADASTRO DAS COMPONENTES CURRICULARES");
        CompCurr compsCurr[] = geraMatriz();

        for(int indComp=0; indComp<=5; indComp++){
        pw.printf("%d %s - %s ( %d / %d / %d / %d %s / %d %s )\n", compsCurr[indComp].indComp, compsCurr[indComp].cod, compsCurr[indComp].nome, compsCurr[indComp].periodo, compsCurr[indComp].cap, compsCurr[indComp].txAprov,compsCurr[indComp].indPreReq, compsCurr[indComp].preReq,compsCurr[indComp].indCoReq, compsCurr[indComp].coReq);
        }



        for (int periodo = 1; periodo <= 9; periodo++){

            // Matricula
            for (int indAlunos=0; indAlunos<=99; indAlunos++){
                alunos[indAlunos].matricula(periodo,compsCurr);}

            for (int indComp=0; indComp<=5; indComp++){
                for (int indAlunos=0; indAlunos<=99; indAlunos++){
                    matriculados[periodo-1][indComp][indAlunos]=compsCurr[indComp].matriculados[indAlunos];
                }
                compsCurr[indComp].contaAlunos(periodo-1);
            }

            for (int indAlunos=0; indAlunos<= 99; indAlunos++){
                // conta  matriculados
                for (int indComp=0; indComp<=5; indComp++){
                    if (alunos[indAlunos].matricular[indComp]){
                        totalMatriculados[periodo-1]++;
                        break;}
                }

                // conta egressos
                tmpEgressos = true;
                for (int indComp=0; indComp<=5; indComp++){
                    if (!alunos[indAlunos].aprovado[indComp]){
                        tmpEgressos = false;
                    }
                }
                if (tmpEgressos){totalEgressos[periodo-1]++;
                    alunos[indAlunos].egresso=true;}

                // conta Desistentes
                if (!alunos[indAlunos].ativo[periodo-1] && !alunos[indAlunos].egresso){
                    totalDesistentes[periodo-1]++;
                }

            }



            // Cursa
            for (int indAlunos=0; indAlunos<=99; indAlunos++){
                alunos[indAlunos].cursaPeriodo(compsCurr);}

            // Verifica aprovação
            ptoCorte(periodo-1,compsCurr);
            for (int indAlunos=0; indAlunos<=99; indAlunos++){
                alunos[indAlunos].verificaAprov(compsCurr);}

        }


        pw.println("===================");
        pw.println("MATRÍCULAS POR COMPONENTE ( <-PERÍODOS-> )");

        for (int indComp=0; indComp<=5; indComp++){
            pw.printf("%s ",compsCurr[indComp].cod);
            for (int periodo = 1; periodo <= 9; periodo++){
                pw.printf ("%d\t",compsCurr[indComp].totMatr[periodo-1]);}
            pw.printf ("\n");
        }

        pw.println("===================");
        pw.println("TOTAIS");

        pw.printf ("Ativos: \t");
        for (int periodo = 1; periodo <= 9; periodo++){
            pw.printf ("%d\t", totalMatriculados[periodo-1]);
        }
        pw.printf ("\nEgressos: \t");
        for (int periodo = 1; periodo <= 9; periodo++){
            pw.printf ("%d\t", totalEgressos[periodo-1]);
        }

        pw.printf ("\nDesist.:\t");
        for (int periodo = 1; periodo <= 9; periodo++){
            pw.printf ("%d\t", totalDesistentes[periodo-1]);
        }

        pw.close();

        InputStreamReader fr = new InputStreamReader(new FileInputStream("out.txt"), "ISO-8859-1");
        BufferedReader br = new BufferedReader(fr);

        String linha = br.readLine();
        while (linha!=null){
        System.out.println(linha);
        linha = br.readLine();}
    }


    static CompCurr[] geraMatriz(){
        CompCurr FEL = new CompCurr(0,"FEL","Fundamentos de Eletrotécnica",1,40,80,"","",-1,-1);
        CompCurr CA1 = new CompCurr(1,"CA1","Cálculo I",1,40,60,"","",-1,-1);
        CompCurr FI2 = new CompCurr(2,"FI2","Física II",2,40,70,"FEL","CA2",0,3);
        CompCurr CA2 = new CompCurr(3,"CA2","Cálculo II",2,40,50,"CA1","",1,-1);
        CompCurr ET1 = new CompCurr(4,"ET1","Eletrônica I",3,40,80,"FI2","",2,-1);
        CompCurr LE1 = new CompCurr(5,"LE1","Laboratório de Eletrônica I",3,20,90,"","ET1",-1,4);
        CompCurr compsCurr[] = {FEL,CA1,FI2,CA2,ET1,LE1};
        return compsCurr;
    }

    static void ptoCorte(int periodo, CompCurr[] compsCurr){
        double notasAbsOrd[] = new double[100]; // capacidade maxima da turma
        for (int indComp=0; indComp<=5; indComp++){
            for (int indAlunos=0; indAlunos<=99; indAlunos++){
                if (compsCurr[indComp].matriculados[indAlunos]){
                    notasAbsOrd[indAlunos] = compsCurr[indComp].notasAbs[indAlunos];}else{
                    notasAbsOrd[indAlunos] = 1000;
                }}
            Arrays.sort(notasAbsOrd);
            compsCurr[indComp].notaCorteAbs=notasAbsOrd[(int)((100-(double)compsCurr[indComp].txAprov)/100*compsCurr[indComp].totMatr[periodo])];
        }}



}

