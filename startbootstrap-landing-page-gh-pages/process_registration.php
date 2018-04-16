<?php
	include("header.php");
	include("footer.php");
	
	$reg_first_name = $_POST['reg_first_name'];
	$reg_last_name = $_POST['reg_last_name'];
	$reg_username = $_POST['reg_username'];
	$reg_email = $_POST['reg_email'];
    $reg_password = $_POST['reg_password'];
    $reg_confirm_password = $_POST['reg_confirm_password'];
    $reg_account_type = "Standard";
    $reg_user_hash = hash('MD5', $reg_first_name + $reg_last_name + $reg_username);
    
    
    if($reg_password === $reg_confirm_password) {
	    $query = "INSERT INTO users(user_ID, first_name, last_name, username, email_address, password, account_type)
                VALUES('$reg_user_hash', '$reg_first_name', '$reg_last_name', '$reg_username', '$reg_email', '$reg_password', '$reg_account_type')";	
        $query2 = "SELECT * FROM users where username='$reg_username' OR email_address='$reg_email'";
        
		$response = @mysqli_query($db, $query2);
		
		if($response) {
		    while($row = mysqli_fetch_array($response)) {
		    	if ($row['username'] === $reg_username) {
		        	echo 'An account with the username ' . $reg_username . ' already exists.';
		        	echo '<head><meta http-equiv="refresh" content="2;register.php"></head>';
		        	exit();
		        } else if ($reg_email === $row['email_address']) {
		        	echo 'A user with the email ' . $reg_email . ' already exists.';
		        	echo '<head><meta http-equiv="refresh" content="2;register.php"></head>';
		        	exit();
		        }
		    }
		    
		    try {
		    	mysqli_query($db, $query);
		    	
	    		$_SESSION['user_hash'] = $reg_user_hash;		            
			    $_SESSION['username'] = $reg_username;
		    	$_SESSION['password'] = $reg_password;
 		    	$_SESSION['logged_in'] = true;
		    	$_SESSION['first_name'] = $reg_first_name;
            	$_SESSION['last_name'] = $reg_last_name;
            	$_SESSION['email_address'] = $reg_email;
			        
				echo'<head><meta http-equiv="refresh" content="0;confirmation.php"></head>';
		        
		    	exit();
		    } catch (Exception $e) {
		    	echo $e;
		    }
		        
		
		} 
	} else {
		echo 'The two passwords do not match. Please re-enter.';
		echo '<br><br> <a href="register.php">Back</a>';
		exit();
	}
?>