package xyz.ibudai.process.common;

public enum Language {

    EN("en"),
    ZH("zh");

    private final String language;

    Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
