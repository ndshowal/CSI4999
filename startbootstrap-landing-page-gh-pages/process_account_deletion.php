<?php
    include("header.php");
    include("footer.php");
    
    $password1 = $_POST['password'];
    $password2 = $_POST['confirm_password'];
    
    if(!$_SESSION['admin_change']) {
        if($password1 != $_SESSION['searched_password']) {
            echo'Incorrect password, please try again.
                <head><meta http-equiv="refresh" content="2;delete_account.php"></head>';
            exit;
        }
        $username = $_SESSION['username'];
    } else {
        if($password != $_SESSION['password']) {
            echo'Incorrect password, please try again.
                <head><meta http-equiv="refresh" content="2;delete_account.php"></head>';
            exit;
        }
        $username = $_SESSION['searched_username'];
    }
    
    if($password1 != $password2) {
        echo'Passwords do not match. Please try again.
            <head><meta http-equiv="refresh" content="2;delete_account.php"></head>';
        exit;
    }
    
    $query = "SELECT username
            FROM users
            WHERE username=$username";
            
    $response = @mysqli_query($db, $query);
    
    if($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['username'] == $username) {
                $query = "DELETE from users
                        WHERE username = $username";
                        
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Account deleted successfully.";
                    echo'<head><meta http-equiv="refresh" content="2;signout.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
            
            }
        }
    }
    
?>