<?php

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(["ok" => false, "error" => "POST requis"]);
    exit;
}

include_once __DIR__ . '/service/PositionService.php';
include_once __DIR__ . '/classe/Position.php';

$latitude  = $_POST['latitude']  ?? null;
$longitude = $_POST['longitude'] ?? null;
$date      = $_POST['date']      ?? null;
$imei      = $_POST['imei']      ?? null;

$clientIp = $_SERVER['REMOTE_ADDR'];

if ($latitude === null || $longitude === null || $date === null || $imei === null) {
    http_response_code(400);
    echo json_encode(["ok" => false, "error" => "Paramètres manquants", "ip" => $clientIp]);
    exit;
}

try {
    $service  = new PositionService();
    $position = new Position(null, $latitude, $longitude, $date, $imei);
    $service->create($position);

    echo json_encode(["ok" => true, "ip" => $clientIp]);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["ok" => false, "error" => $e->getMessage(), "ip" => $clientIp]);
}
