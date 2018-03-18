<?php
    unset($_SESSION);
    session_start();

    $servername = "cardguard-db.mysql.database.azure.com";
    $username = "cardguard-admin@cardguard-db";
    $password = "password12345!";
    $database = "cardguard_db";
    $dbport = 3306;

    // Create connection
    $db = new mysqli($servername, $username, $password, $database, $dbport);

    // Check connection
    if ($db->connect_error) {
        die("Connection failed: " . $db->connect_error);
    }
?>