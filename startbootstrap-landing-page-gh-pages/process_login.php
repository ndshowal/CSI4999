<?php
    include("header.php");
    include("footer.php");
    
    $login_username = $_POST['login_username'];
    $login_password = $_POST['login_password'];
    
    $query = "SELECT * FROM users
            WHERE username='$login_username' AND password='$login_password'";
                
    $response = @mysqli_query($db, $query);
    
    if ($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['username'] == $login_username && $row['password'] == $login_password) {
                $_SESSION['logged_in'] = true;
                $_SESSION['username'] = $login_username;
                $_SESSION['password'] = $login_password;
                $_SESSION['first_name'] = $row['first_name'];
                $_SESSION['last_name'] = $row['last_name'];
                $_SESSION['email_address'] = $row['email_address'];
                $_SESSION['account_type'] = $row['account_type'];
                $_SESSION['id'] = $row['id'];
                $user_firstname = $row['first_name'];
                echo'<head><meta http-equiv="refresh" content="0;login_confirmation.php"></head>';
            } 
        }
        
        if(!$_SESSION['logged_in']) {
            echo "<div style='text-align:center'><br>User/password not found, please try again.</div>";
            echo "<div style='text-align:center'><br><a href='signin.php'>Back</a></div>";
        } 
        
        exit();
    } else {
        echo "Failed to login.";
        echo '<br><br> <a href="signin.php">Back</a>';
    }
?>