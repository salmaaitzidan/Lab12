<?php

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    include_once __DIR__ . '/service/PositionService.php';
    afficherPositions();
}

function afficherPositions() {
    $service = new PositionService();
    echo json_encode(["positions" => $service->getAll()]);
}
