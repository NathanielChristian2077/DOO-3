package me.model.enums;

public enum Qualidade {
    HD,
    FullHD,
    QHD,
    ULTRAHD;

    @Override
    public String toString() {
        switch (this) {
            case HD: return "HD";
            case FullHD: return "FullHD";
            case QHD: return "QHD";
            case ULTRAHD: return "4K";
            default: return "";
        }
    }
}
