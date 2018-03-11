<?php
    include("header.php");
    include("footer.php");
    
    $to_be_modified = $_GET['to_be_modified'];
    $password = $_POST['entered_password'];
    $admin_change = $_SESSION['admin_change'];
    
    if(!$admin_change) {
        switch($to_be_modified) {
            case 'username':
                $username = $_SESSION['username'];
                $new_username = $_POST['new_username'];
                if($username == $new_username) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=username"></head>';
                    exit;
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=username"></head>';
                    exit;
                }
                
                $query = "UPDATE users 
                            SET username='$new_username' 
                            WHERE username='$username'";
                            
                mysqli_query($db, $query);
            
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['username'] = $new_username;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                break;
    
            case 'email_address':
                $email = $_SESSION['email'];
                $new_email = $_GET['new_email'];
                
                if($email == $new_email) {
                    echo'New email address must be different from your existing one. Please try again.';
                    echo'<head><meta http-equiv="refresh" content="2;modify_account_settings.php?to_be_modified=email_address"></head>';
                    exit;
                } else if($password != $_SESSION['password']) {
                    echo'Incorrect password, please try again.';
                    echo'<head><meta http-equiv="refresh" content="2;modify_account_settings.php?to_be_modified=email_address"></head>';
                    exit;
                }
            
                $query = "UPDATE users 
                            SET email_address='$new_email'
                            WHERE email_address='$email'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['email_address'] = $new_email;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
            
                break;
            
            case 'first_name':
                $first_name = $_SESSION['first_name'];
                $new_first_name = $_POST['new_first_name'];
                
                if($first_name == $new_first_name) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=first_name></head>';
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=first_name"></head>';
                }
                 
                $query = "UPDATE users 
                            SET first_name='$new_first_name'
                            WHERE first_name='$first_name'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['first_name'] = $new_first_name;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                
                break;
                
            case 'last_name':
                $last_name = $_SESSION['last_name'];
                $new_last_name = $_POST['new_last_name'];
                
                if($last_name == $new_last_name) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=last_name"></head>';
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=last_name"></head>';
                }
                
                $query = "UPDATE users 
                            SET last_name='$new_last_name'
                            WHERE last_name='$last_name'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['last_name'] = $new_last_name;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                
                break;
            }
    } else {
        switch($to_be_modified) {
            case 'username':
                $username = $_SESSION['searched_username'];
                $new_username = $_POST['new_username'];
                if($username == $new_username) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=username"></head>';
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=username"></head>';
                }
                
                $query = "UPDATE users 
                            SET username='$new_username' 
                            WHERE username='$username'";
                            
                mysqli_query($db, $query);
            
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['searched_username'] = $new_username;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                break;
    
            case 'email_address':
                $email = $_SESSION['searched_email_address'];
                $new_email = $_GET['new_email'];
                
                if($email == $new_email) {
                    echo'Your new email address must be different than your existing one. Please try again.';
                    echo'<head><meta http-equiv="refresh" content="2;modify_account_settings.php?to_be_modified=email_address"></head>';
                } else if($password != $_SESSION['password']) {
                    echo'Incorrect password. Please try again.';
                    echo'<head><meta http-equiv="refresh" content="2;modify_account_settings.php?to_be_modified=email_address"></head>';
                }
            
                $query = "UPDATE users 
                            SET email_address='$new_email'
                            WHERE email_address='$email'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['searched_email_address'] = $new_email;
                    echo'<head><meta http-equiv="refresh" content=";profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
            
                break;
            
            case 'first_name':
                $first_name = $_SESSION['searched_first_name'];
                $new_first_name = $_POST['new_first_name'];
                
                if($first_name == $new_first_name) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=first_name"></head>';
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=first_name"></head>';
                }
                
                $query = "UPDATE users 
                            SET first_name='$new_first_name'
                            WHERE first_name='$first_name'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['searched_first_name'] = $new_first_name;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                
                break;
                
            case 'last_name':
                $last_name = $_SESSION['searched_last_name'];
                $new_last_name = $_POST['new_last_name'];
                
                if($last_name == $new_last_name) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=last_name"></head>';
                } else if($password != $_SESSION['password']) {
                    echo'<head><meta http-equiv="refresh" content="0;modify_account_settings.php?to_be_modified=last_name"></head>';
                }
                
                $query = "UPDATE users 
                            SET last_name='$new_last_name'
                            WHERE last_name='$last_name'";
                
                mysqli_query($db, $query);
                
                if ($db->query($query) === TRUE) {
                    echo "Record updated successfully";
                    $_SESSION['searched_last_name'] = $new_last_name;
                    echo'<head><meta http-equiv="refresh" content="1;profile.php"></head>';
                } else {
                    echo "Error updating record: " . $db->error;
                }
                
                break;
            }
    }
?>