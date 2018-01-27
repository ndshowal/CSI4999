<?php
    include("header.php");
    include("footer.php");
	$username = $_SESSION['username'];
	$password = $_SESSION['password'];
		
    $entered_password = $_POST['entered_password'];
    $new_password = $_POST['new_password'];
    $confirm_new_password = $_POST['confirm_new_password'];
    
    if($entered_password != $password) {
        echo'The password you provided does not match your current password. Please retry.
        '. $password . '' . $entered_password .'
        <br><br> <a href="profile.php#password">Return</a>';
    } else if($entered_password == $new_password) {
        echo'Please enter a new password different than your current one.
        <br><br> <a href="profile.php#password">Return</a>';
    } else if($new_password != $confirm_new_password) {
        echo'The confirmation password you entered does not match. Please retry.
        <br><br> <a href="profile.php#password">Return</a>';
    } else {
        $query = "SELECT username, password FROM users
                WHERE username='$username' AND password='$password'";
        $response = @mysqli_query($db, $query);
        if ($response) {
            while($row = mysqli_fetch_array($response)) {
                if($row['username'] == $username && $row['password'] == $password) {
                    $query = "UPDATE users SET password='$new_password' WHERE username='$username'";
                    mysqli_query($db, $query);
                    $_SESSION['password'] = $new_password;
                    echo'<head><meta http-equiv="refresh" content="0;password_change_confirmation.php"></head>';
                } 
            }
        } 
    }
?>