package edu.java.bot.links.classes;

// Для URLInfo верю, что toString должен возвращать представление,
// по которому пользователь сможет определить что мониторится
// При этом toString не обязан быть ссылкой
// Например, если в каком-то сообществе ВК мониторятся определенные хэштеги,
// то toString будет ссылка на сообщество + хэштег

public abstract class URLInfo {
    public abstract String getResourceType();

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof URLInfo urlObj)) {
            return false;
        }
        return toString().equals(urlObj.toString());
    }
}
