<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Home</title>
	<link rel="stylesheet" href="style.css">
	</head>
	<body>
	<h1 align="left">PlumbersByU</h1>
	<table id="navbar">
		<tr>
			<th scope="col"><a href="Home.php">Home</a></th>
			<th scope="col"><a href="Search.php">Search</a></th>
			<th scope="col"><a href="Profile.php">Profile</a></th>
			<th scope="col"><a href="Login.php">Login</a></th>
		</tr>
	</table>
	
	<br>
	
	<?php
		session_start();
		
		$_SESSION['profile_message'] = "";
		
		if($_SESSION['logged_in'] == true) {
			echo '<h3>Leave a review of ' . $_SESSION['current_plumber'] . '</h3>';
			
			echo '<form method="post" action="Process_Review.php">
				<table class="review">
					<tr><td>Please rate your experience from 1(poor) to 5(excellent):<br>
					<input type="number" name="review_rating" min="1" max="5"></td></tr><br>
					<tr><td>When did you receive service? (yyyy-mm-dd)<br>
					<input type="date" name="service_date"></td></tr><br>
					<tr><td>Please describe your experience:<br>
					<textarea name="review_details" class="review_box" rows="10" cols="10" value="Leave a comment..."></textarea></td></tr><br>
					<tr><td><input type="submit" name="submit_btn" value="Submit"></td></tr>
				</table>
			</form>';
		} else {
			echo'<h3>Please <a href="Login.php?id='.$_SESSION['plumber_id'].'">sign in</a> to leave a review.</h3>';
			$_SESSION['from_review_not_logged_in'] = true;
		}
	?>
	
	
	
	</body>	
</html>