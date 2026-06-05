<?php

class Connexion {

    // Modifier ces valeurs selon votre environnement
    private static $HOST     = 'localhost';
    private static $DB_NAME  = 'localisation';
    private static $USER     = 'root';
    private static $PASSWORD = '';

    private $pdo;

    public function __construct() {
        $dsn = "mysql:host=" . self::$HOST . ";dbname=" . self::$DB_NAME . ";charset=utf8";

        try {
            $this->pdo = new PDO($dsn, self::$USER, self::$PASSWORD, [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
            ]);
        } catch (Exception $e) {
            die(json_encode(["ok" => false, "error" => $e->getMessage()]));
        }
    }

    public function getConnextion() {
        return $this->pdo;
    }
}
