package br.edu.ifpe.academicflow;

import java.util.Random;
import java.lang.Math;

public class Aluno {
    public int indAluno;
    public double notaAbs[] = new double[6];
    public double mNotas;
    public double varNotas;
    public boolean matricular[] = new boolean[6];
    public boolean aprovado[] = new boolean[6];
    public double desistencia;
    public boolean ativo[] = {true, true, true, true, true, true, true, true, true, true};
    boolean egresso;

    public Aluno(int indAlunoi, double mNotasi, double varNotasi){
        this.indAluno = indAlunoi;
        if (varNotasi < 0.5) varNotasi = 0.5;
        if (varNotasi > 3.0) varNotasi = 3.0;
        this.varNotas = varNotasi;
        this.mNotas = mNotasi;
        Random r = new Random();
        this.desistencia = Math.abs(r.nextGaussian()*3*(10-this.mNotas)); // Mediana 7  +/- 2
    }

    public void cursa(int indComp,CompCurr[] compsCurr){
        Random r = new Random();
        double N=(r.nextGaussian()*this.varNotas)+this.mNotas;
        this.notaAbs[indComp] = N;
        compsCurr[indComp].notasAbs[this.indAluno]=N;
    }

    public void cursaPeriodo(CompCurr[] compsCurr){
        for (int indComp = 0; indComp<=5; indComp++){
            if (this.matricular[indComp]){
                this.cursa(indComp,compsCurr);}
        }

    }

    public void verificaAprov(CompCurr[] compsCurr){
        for (int indComp=0;indComp<=5;indComp++){
            if ((this.notaAbs[indComp]>=compsCurr[indComp].notaCorteAbs)&&(this.matricular[indComp])){
                this.aprovado[indComp]=true;
                compsCurr[indComp].aprovados[this.indAluno]=true;
            }
        }
    }

    public void matricula(int periodo,CompCurr[] compsCurr){

        // inicializa o vetor matricular
        for (int indComp=0;indComp<=5;indComp++){
            this.matricular[indComp]=false;
        }


        // estima desistência (mediana 7+/-2)
        Random r = new Random();
        if (this.desistencia > (1/(Math.abs((r.nextGaussian()/2+2)-periodo))*r.nextDouble()*100)){
            for (int per = periodo; per <= 9; per++){
                this.ativo[per-1]=false;
            }
        }

        // verifica se é aluno ativo
        if (this.ativo[periodo-1]){

            for (int indComp=0;indComp<=5;indComp++){

                // verifica se a disciplina é do período e se já foi aprovado nela
                if ((compsCurr[indComp].periodo<=periodo) && (this.aprovado[indComp]==false)){
                    this.matricular[indComp]=true;
                }

                // verifica se tem pré-requisitos e se já foi aprovado nela
                if ((this.matricular[indComp]==true)&&(compsCurr[indComp].indPreReq!=-1)){
                    if((this.aprovado[compsCurr[indComp].indPreReq])==false){
                        this.matricular[indComp]=false;
                    }
                }
            }
            for (int indComp=0;indComp<=5;indComp++){
                // verifica se tem co-requisito e se já está matriculado nela ou se já foi aprovado nela. Segundo loop.

                if ((this.matricular[indComp]==true)&&(compsCurr[indComp].indCoReq!=-1)){
                    if((!this.matricular[compsCurr[indComp].indCoReq])&&(!this.aprovado[compsCurr[indComp].indCoReq])){
                        this.matricular[indComp]=false;
                    }
                }
            }

        }
        for (int indComp=0;indComp<=5;indComp++){
            // Copia matriculas para os componentes
            compsCurr[indComp].matriculados[this.indAluno]=this.matricular[indComp]; }

    }

}