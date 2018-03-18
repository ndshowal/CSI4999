<?php
    include("header.php");
    include("footer.php");
    
    if(!$_SESSION['admin_change']) {
	    $username = $_SESSION['username'];
    } else {
        $username = $_SESSION['searched_username'];
    }
    
    $old_password = $_POST['old_password'];
    $new_password = $_POST['new_password'];
    $confirm_new_password = $_POST['confirm_new_password'];
    
    if(!$_SESSION['admin_change']) {
         if($_SESSION['password'] != $old_password) {
            echo'The password you entered does not match your current password. Please try again.
            <br><br> <button onclick="goBack()">Go Back</button>';
            exit;
         }
    }
    
    if($old_password == $new_password) {
        echo'Please enter a new password different than your current one.
            <br><br> <button onclick="goBack()">Go Back</button>';
    } else if($new_password != $confirm_new_password) {
        echo'The confirmation password you entered does not match. Please retry.
            <br><br> <button onclick="goBack()">Go Back</button>';
    } else {
        
        $query = "SELECT username, password 
                FROM users
                WHERE username='$username'";
        $response = @mysqli_query($db, $query);
        if ($response) {
            while($row = mysqli_fetch_array($response)) {
                if($row['username'] == $username && $row['password'] == $old_password) {
                    $query = "UPDATE users 
                            SET password='$new_password' 
                            WHERE username='$username'";
                            
                    mysqli_query($db, $query);
                    
                    if(!$_SESSION['admin_change']) {
                        $_SESSION['password'] = $new_password;
                    } else {
                        $_SESSION['searched_password'] = $new_password;
                    }
                    
                    echo'<head><meta http-equiv="refresh" content="0;password_change_confirmation.php"></head>';
                } 
            }
        }  else {
           echo'uh oh';
        }
    }
?>

