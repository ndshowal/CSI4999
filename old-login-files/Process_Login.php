<?php
    if(!isset($db)) {
		require_once('Connect.php');
	} 
	
	session_start();
		
    $login_username = $_POST['login_username'];
    $login_password = $_POST['login_password'];
    
    $query = "SELECT username, password FROM Customer
                WHERE username='$login_username' AND password='$login_password'";
    
    $response = @mysqli_query($db, $query);
                
    if ($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['username'] == $login_username && $row['password'] == $login_password) {
                $_SESSION['username'] = $login_username;
                $_SESSION['logged_in'] = true;
                
                if($_SESSION['from_review_not_logged_in']) {
                    echo 'Login successful! Welcome back, ' . $login_username . '!';
                    echo '<br><br> <a href="Review.php">Back to review</a>';
                    $_SESSION['from_review_not_logged_in'] = false;
                    $_SESSION['profile_message'] = "";
                } else {
                    echo 'Login successful! Welcome back, ' . $login_username . '!';
                    echo '<br><br> <a href="Profile.php">Proceed</a>';
                }
            } 
        }
        
        if(!$_SESSION['logged_in']) {
            echo 'User/password not found, please try again.';
            echo '<br><br> <a href="Login.php">Back</a>';
        } 
        
        exit();
    } else {
            echo "Failed to login.";
            echo '<br><br> <a href="Login.php">Back</a>';
    }
?>