<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Login</title>
	<link rel="stylesheet" href="style.css">
	</head>
	<body>
	<h1 align="left">PlumbersByU</h1>
	<table class="navbar">
		<tr>
			<th scope="col"><a href="Home.php">Home</a></th>
			<th scope="col"><a href="Search.php">Search</a></th>
			<th scope="col"><a href="Profile.php">Profile</a></th>
			<th scope="col" class="active"><a href="Login.php">Login</a></th>
		</tr>
	</table>
	
	<br>
	
	<?php
		session_start();
		$current_user = $_SESSION['username'];
		
		if($_SESSION['logged_in'] == false || $_SESSION['logged_in'] == null) {
			echo'
			
			<h3>'. $_SESSION['profile_message'] . '</h3>
			
			<br>
			
			<form method="post" action="Process_Login.php">
				<table class="login">
					<tr><td><h3>Enter your username and password:</h3></td>
					<tr><td><input type="text" name="login_username" value="Username"></td></tr>
					<tr><td><input type="password" name="login_password" value="aaaaaa"></td></tr>
					<tr><td><input type="submit" name="login_btn" value="Submit"></td></tr>
				</table>
			</form>
	
			<br><br>
	
			<form method="post" action="Process_Registration.php">
				<table class="register">
					<tr><td><h3>Please register an account:</h3></tr></td>
					<tr><td>First name:</td></tr>
					<tr><td><input type="text" name="reg_first_name" value="First name"></td></tr>
					<tr><td>Last name:</td></tr>
					<tr><td><input type="text" name="reg_last_name" value="Last name"></td></tr>
					<tr><td>Please enter a valid email address:</td></tr>
					<tr><td><input type="email" name="email_address" value="Email"></td></tr>
					<tr><td>Please enter a username:</td></tr>
					<tr><td><input type="text" name="reg_username" value="Username"></td></tr>
					<tr><td>Please enter a password:</td></tr>
					<tr><td><input type="password" name="reg_password" value="aaaaaa"></td></tr>
					<tr><td>Please confirm your password:</td></tr>
					<tr><td><input type="password" name="reg_password2" value="aaaaaa"></td></tr>
					<tr><td><input type="submit" name="register_btn" value="Submit"></td></tr>
				</table>
			</form>';
		} else {
			echo '<h3>You are already logged in! if you are not <a href="Profile.php"><strong> ' . $current_user . 
				 ' </strong></a>or simply wish to log out, please click <a href="Logout.php">here.</a><br></h3>';
		}
	?>
	</body>	
</html>