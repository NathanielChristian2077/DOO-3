package me.model.enums;

public enum Genero {
    ACAO,
    AVENTURA,
    COMEDIA,
    DRAMA,
    FANTASIA,
    SCI_FI,
    ROMANCE,
    TERROR,
    SUSPENSE,
    DOCUMENTARIO,
    MUSICAL,
    GUERRA,
    FAROESTE,
    BIOGRAFIA,
    POLICIAL,
    MISTERIO,
    HISTORICO,
    ESPORTE,
    FAMILIA,
    THRILLER_PSICOLOGICO,
    CURTA_METRAGEM;

    @Override
    public String toString() {
        switch (this) {
            case ACAO: return "Ação ";
            case AVENTURA: return "Aventura ";
            case COMEDIA: return "Comédia ";
            case DRAMA: return "Drama ";
            case FANTASIA: return "Fantasia ";
            case SCI_FI: return "Sci-Fi ";
            case ROMANCE: return "Românce ";
            case TERROR: return "Terror ";
            case SUSPENSE: return "Suspense ";
            case DOCUMENTARIO: return "Documentário ";
            case MUSICAL: return "Musical ";
            case GUERRA: return "Guerra ";
            case FAROESTE: return "Faroeste ";
            case BIOGRAFIA: return "Biografia ";
            case POLICIAL: return "Policial ";
            case MISTERIO: return "Mistério ";
            case HISTORICO: return "Histórico ";
            case ESPORTE: return "Esporte ";
            case FAMILIA: return "Família ";
            case THRILLER_PSICOLOGICO: return "Thriller Psicológico ";
            case CURTA_METRAGEM: return "Curta Metragem ";
            default: return "";
        }
    }
}
