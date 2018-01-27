<?php
	if(!isset($db)){
		require_once('connect.php');
	}
	session_start;
	
	$reg_first_name = $_POST['reg_first_name'];
	$reg_last_name = $_POST['reg_last_name'];
	$reg_username = $_POST['reg_username'];
	$reg_email = $_POST['reg_email'];
    $reg_password = $_POST['reg_password'];
    $reg_confirm_password = $_POST['reg_confirm_password'];
    $reg_account_type = $_POST['reg_account_type'];
    
    if($reg_password == $reg_confirm_password) {
	    $query = "INSERT INTO users(first_name, last_name, username, email_address, password, account_type)
                VALUES('$reg_first_name', '$reg_last_name', '$reg_username', '$reg_email', '$reg_password', '$reg_account_type')";	
        $query2 = "SELECT * FROM users";
        
		$response = @mysqli_query($db, $query2);
		
		if($response) {
		    while($row = mysqli_fetch_array($response)) {
		        if($reg_first_name == $row['first_name'] && $reg_last_name == $row['last_name']) {
		           	echo 'An account with the name "' . $reg_first_name . ' ' . $reg_last_name . '" already exists.';
		           	echo '<br><br> <a href="register.php">Back</a>';
		           	exit();
		        } else if ($row['username'] == $reg_username) {
		        	echo 'An account with the username ' . $reg_username . ' already exists.';
		        	echo '<br><br> <a href="register.php">Back</a>';
		        	exit();
		        } else if ($reg_email == $row['email_address']) {
		        	echo 'A user with the email ' . $reg_email . ' already exists.';
		        	echo '<br><br> <a href="register.php">Back</a>';
		        	exit();
		        } else {
		        	echo'<head><meta http-equiv="refresh" content="0;confirmation.php"></head>';
		        	
		            mysqli_query($db, $query);
		            
		            $_SESSION['username'] = $reg_username;
		            $_SESSION['password'] = $reg_password;
 		            $_SESSION['logged_in'] = true;
		            $_SESSION['first_name'] = $reg_first_name;
                	$_SESSION['last_name'] = $reg_last_name;
                	$_SESSION['email_address'] = $reg_email;
		            exit();
		        }
		    }
		}
	} else {
		echo 'The two passwords do not match. Please re-enter.';
		echo '<br><br> <a href="register.php">Back</a>';
		exit();
	}
	echo'<head><meta http-equiv="refresh" content="0;confirmation.php"></head>';
		
	mysqli_query($db, $query);
	   
	$_SESSION['username'] = $reg_username;
	$_SESSION['password'] = $reg_password;
	$_SESSION['logged_in'] = true;
	$_SESSION['first_name'] = $reg_first_name;
    $_SESSION['last_name'] = $reg_last_name;
    $_SESSION['email_address'] = $reg_email;
	exit();
?>