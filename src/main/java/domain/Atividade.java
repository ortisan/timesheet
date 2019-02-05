//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

public class Atividade {
    private Calendar dataInicio;
    private Calendar dataFim;
    private String descricao;

    public Atividade() {
    }

    public String getData() {
        return (new SimpleDateFormat("dd/MM/yyyy")).format(this.dataInicio.getTime());
    }

    public long getDiffDataInicialFinal() {
        return this.dataFim.getTimeInMillis() - this.dataInicio.getTimeInMillis();
    }

    public String getHorarioInicial() {
        return (new SimpleDateFormat("HH:mm")).format(this.dataInicio.getTime());
    }

    public String getHorarioFinal() {
        return (new SimpleDateFormat("HH:mm")).format(this.dataFim.getTime());
    }

    public void addDescricao(String descricao) {
        if (!StringUtils.isEmpty(this.descricao)) {
            this.descricao = this.descricao + "\n" + descricao;
        } else {
            this.descricao = descricao;
        }

        this.descricao = this.descricao.replaceAll("\"", "");
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendar getDataInicio() {
        return this.dataInicio;
    }

    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendar getDataFim() {
        return this.dataFim;
    }

    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }
}
