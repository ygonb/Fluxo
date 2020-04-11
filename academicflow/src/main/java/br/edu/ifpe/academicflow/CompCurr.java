package br.edu.ifpe.academicflow;
import java.io.PrintWriter;

public class CompCurr {
    public String nome, cod, preReq, coReq;
    public int cap,periodo,indComp,indPreReq,indCoReq,alunos,txAprov;
    public double notaCorteAbs;
    public int totMatr[] = new int[10];
    public double notasAbs[] = new double[100];
    public boolean aprovados[] = new boolean[100];
    public boolean matriculados[] = new boolean[100];

    public CompCurr (int indCompi, String codi, String nomei, int periodoi, int capi, int txAprovi, String preReqi, String coReqi,int indPreReqi, int indCoReqi){
        this.indComp = indCompi; this.cod = codi;    this.nome = nomei;
        this.periodo = periodoi;    this.cap = capi;
        this.txAprov = txAprovi;    this.preReq = preReqi;
        this.coReq = coReqi; this.indPreReq = indPreReqi; this.indCoReq = indCoReqi; this.alunos = 0;
        }


    public void contaAlunos(int periodo) {
        this.totMatr[periodo] = 0;
        for (int indAlunos = 0; indAlunos<=99; indAlunos++){
            if (this.matriculados[indAlunos]){
                this.totMatr[periodo]++;
            }
        }
    }


}