# Localisation GPS — Android + PHP

Application Android qui enregistre la position GPS de l'appareil et l'affiche sur une carte Google Maps via une API PHP.

Salma AIT ZIDAN
---

## Structure du projet

```
localisation-gps/
├── backend/
│   └── localisation/          ← dossier à déposer dans htdocs/www
│       ├── classe/
│       │   └── Position.php
│       ├── connexion/
│       │   └── Connexion.php
│       ├── dao/
│       │   └── IDao.php
│       ├── service/
│       │   └── PositionService.php
│       ├── createPosition.php
│       ├── showPositions.php
│       └── database.sql
└── android/
    └── app/
        └── src/main/
            ├── java/com/example/localisation/
            │   ├── Config.java          ← configuration centrale
            │   ├── MainActivity.java
            │   └── MapsActivity.java
            ├── res/
            │   ├── layout/
            │   │   ├── activity_main.xml
            │   │   └── activity_maps.xml
            │   └── values/
            │       ├── strings.xml
            │       ├── colors.xml
            │       └── styles.xml
            └── AndroidManifest.xml
```

---

## Prérequis

- **Serveur** : XAMPP / WAMP avec Apache, PHP 7+ et MySQL
- **Android** : Android Studio, téléphone avec GPS activé (ou émulateur)
- **Réseau** : téléphone et PC sur le **même réseau Wi-Fi ou hotspot**
- **Clé Google Maps** : obtenir une clé sur [Google Cloud Console](https://console.cloud.google.com/)

---

## Installation

### 1. Base de données

1. Démarrer Apache et MySQL dans XAMPP
2. Ouvrir phpMyAdmin (`http://localhost/phpmyadmin`)
3. Exécuter le script `backend/localisation/database.sql`

### 2. Backend PHP

1. Copier le dossier `backend/localisation/` dans `C:/xampp/htdocs/` (Windows) ou `/var/www/html/` (Linux)
2. Vérifier l'accès : `http://localhost/localisation/createPosition.php`
3. Si nécessaire, modifier les identifiants dans `connexion/Connexion.php` :
   ```php
   private static $HOST     = 'localhost';
   private static $DB_NAME  = 'localisation';
   private static $USER     = 'root';
   private static $PASSWORD = '';
   ```





## Test rapide

1. Lancer l'app → accepter la permission de localisation
2. Sortir à l'extérieur ou activer la localisation via le menu développeur (émulateur)
3. Vérifier dans phpMyAdmin que des lignes apparaissent dans la table `position`
4. Appuyer sur "Afficher la carte" → les marqueurs doivent être visibles

---

## Dépannage

| Problème | Solution |
|---|---|
| `Erreur réseau` sur Android | Vérifier que le PC et le téléphone sont sur le même réseau, et que l'IP dans `Config.java` est correcte |
| Carte vide | Vérifier que la table `position` contient des données et que la clé Google Maps est valide |
| `CLEARTEXT not permitted` | Vérifier `android:usesCleartextTraffic="true"` dans le Manifest |
| Permission refusée | Accepter manuellement dans Paramètres → Applications → Localisation |
