package org.example;

import java.util.Map;

public interface Serializable {
    static void savePharmacy(Pharmacy pharmacy) {}
    static Pharmacy loadPharmacy() {return null;}
    private static void loadUsers() {}
    private static void saveUsers() {}
}
