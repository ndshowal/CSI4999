<?php
    if(!isset($db)) {
		require_once('connect.php');
	} 
	
	session_start();
		
    $login_username = $_POST['login_username'];
    $login_password = $_POST['login_password'];
    $user_firstname;
    
    $query = "SELECT username, password, first_name, last_name, email_address FROM users
                WHERE username='$login_username' AND password='$login_password'";
    
    $response = @mysqli_query($db, $query);
                
    if ($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['username'] == $login_username && $row['password'] == $login_password) {
                $_SESSION['username'] = $login_username;
                $_SESSION['logged_in'] = true;
                $_SESSION['first_name'] = $row['first_name'];
                $_SESSION['last_name'] = $row['last_name'];
                $_SESSION['email_address'] = $row['email_address'];
                $user_firstname = $row['first_name'];
                
                echo 'Login successful! Welcome back, ' . $user_firstname . '!';
                echo '<br><br> <a href="profile.php">Proceed</a>';
            } 
        }
        
        if(!$_SESSION['logged_in']) {
            echo 'User/password not found, please try again.';
            echo '<br><br> <a href="signin.php">Back</a>';
        } 
        
        exit();
    } else {
        echo "Failed to login.";
        echo '<br><br> <a href="login.php">Back</a>';
    }
?>