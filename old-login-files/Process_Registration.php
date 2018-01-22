<?php
	if(!isset($db)){
		require_once('Connect.php');
	}
	session_start;
	
	$reg_first_name = $_POST['reg_first_name'];
	$reg_last_name = $_POST['reg_last_name'];
	$reg_username = $_POST['reg_username'];
	$reg_email = $_POST['email_address'];
    $reg_password = $_POST['reg_password'];
    $reg_password2 = $_POST['reg_password2'];
    
    if($reg_password == $reg_password2) {
	    $query = "INSERT INTO Customer(cust_first_name, cust_last_name, username, email_address, password)
                VALUES('$reg_first_name', '$reg_last_name', '$reg_username', '$reg_email', '$reg_password')";	
        $query2 = "SELECT cust_first_name, cust_last_name, username, email_address FROM Customer";
        
		$response = @mysqli_query($db, $query2);
		
		if($response) {
		    while($row = mysqli_fetch_array($response)) {
		        if(($reg_first_name == $row['cust_first_name'] && $reg_last_name == $row['cust_last_name'])) {
		           	echo 'A user with that name already exists.';
		           	echo '<br><br> <a href="Login.php">Back</a>';
		           	exit();
		        } else if ($row['username'] == $reg_username) {
		        	echo 'A user with that username already exists.';
		        	echo '<br><br> <a href="Login.php">Back</a>';
		        	exit();
		        } else if ($reg_email == $row['email_address']) {
		        	echo 'A user with that email already exists.';
		        	echo '<br><br> <a href="Login.php">Back</a>';
		        	exit();
		        } else {
		        	echo 'Registration successful! Welcome, ' . $reg_username . '!';
		            echo '<br><br> <a href="Profile.php">Proceed</a>';
		            mysqli_query($db, $query);
		            $_SESSION['username'] = $reg_username;
		            $_SESSION['logged_in'] = true;
		            exit();
		        }
		    }
		}
	} else {
		echo 'The two passwords do not match. Please re-enter.';
		echo '<br><br> <a href="Login.php">Back</a>';
		exit();
	}
    
    
    
    $response = @mysqli_query($db, $query);
	
	
?>