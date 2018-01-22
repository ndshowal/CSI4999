<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Home</title>
	<link rel="stylesheet" href="style.css">
	</head>
	<body>
	<h1 align="left">PlumbersByU</h1>
	<table class="navbar">
		<tr>
			<th scope="col" class="active"><a href="Home.php">Home</a></th>
			<th scope="col"><a href="Search.php">Search</a></th>
			<th scope="col"><a href="Profile.php">Profile</a></th>
			<th scope="col"><a href="Login.php">Login</a></th>
		</tr>
	</table>
	
	<br><br>
	
	<h4>Welcome to PlumbersByU! Here you will find an easy and convenient way to 
	locate, research, and rate plumbers in your area!
				
	<br><br>
				
	New users are encouraged to register with us - only registered users are 
	able to leave reviews, though anyone can view them! </h4>
	
	<br><br>
	
	<h3> Most recent customer reviews: </h3>
	
	<?php
		if(!isset($db)){
			require_once('Connect.php');
		}
		session_start();
		
		$username = $_SESSION['username'];
		$_SESSION['profile_message'] = "";
		
		$query = "SELECT Review.username, Review.review_date, Review.service_date, 
				Review.review_rating, Review.review_details, Review.service_id,
				Plumber.company_name, Plumber.internal_url
				FROM Review
				INNER JOIN Plumber
				ON Review.service_id = Plumber.service_id
				WHERE Review.service_id = Plumber.service_id
				ORDER BY review_date DESC
				LIMIT 5";

		$response = @mysqli_query($db, $query);

		if($response){
	    	echo '<table class="reviews" align="left" cellspacing="5" cellpadding="8" width="50%">
			
			<td align="left" width="120px"><b>User</b></td>
			<td align="left"><b>Company Name</b></td>
			<td align="left"><b>Review Date</b></td>
			<td align="left"><b>Service Date</b></td>
			<td align="left"><b>Review Rating</b></td>
			<td align="left"><b>Review Details</b></td>';
	
    		while($row = mysqli_fetch_array($response)) {
        		echo '<tr><td align="left">' . $row['username'] . '</td>
        		<td align="left"><a href="Plumber.php?id='.$row['service_id'].'">'.$row['company_name'].'</a></td>
        		<td align="left">' . $row['review_date'] . '</td>
        		<td align="left">' . $row['service_date'] . '</td>
	    		<td align="left">' . $row['review_rating'] . '</td>
		    	<td align="left">' . $row['review_details'] . '</td>';
        
        	echo '</tr>';
    		}

    	echo '</table>';
		} else {
			echo "Couldn't issue database query<br />";
			echo mysqli_error($db);
		}
	?>
	
	</body>	
</html>