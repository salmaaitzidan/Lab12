package com.example.localisation;

/**
 * Fichier de configuration central du projet.
 * Modifier ici les paramètres sans toucher aux activités.
 */
public class Config {

    // ─── Serveur PHP ───────────────────────────────────────────────
    private static final String SERVER_BASE_URL = "http://192.168.1.7/localisation";

    public static final String URL_CREATE   = SERVER_BASE_URL + "/createPosition.php";
    public static final String URL_SHOW_ALL = SERVER_BASE_URL + "/showPositions.php";

    // ─── GPS ───────────────────────────────────────────────────────
    public static final long  GPS_MIN_TIME_MS   = 60_000;
    public static final float GPS_MIN_DISTANCE_M = 150.0f;

    // ─── Requête réseau ────────────────────────────────────────────
    public static final int REQUEST_PERMISSION_LOCATION = 100;

    // ─── Format de date envoyé au serveur ──────────────────────────
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
