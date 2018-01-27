<?php
    include("header.php");
    include("footer.php");

    echo 'Login successful! Welcome back, ' . $_SESSION['username'] . '!';
    echo '<br><br> <a href="profile.php">Proceed</a>';
?>