<?php
    session_start();
    session_destroy();
    session_start();
    $_SESSION['logged_in'] = false;
    header("location: Login.php");
    exit();
?>