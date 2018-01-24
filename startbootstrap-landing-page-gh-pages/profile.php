<?php include("header.php");
    $query_user_info = "SELECT * 
						FROM users 
						WHERE username = '" . $username . "'";
    $user_info_response = @mysqli_query($db, $query_user_info);
    
    if($user_info_response) {  
		while($row = mysqli_fetch_array($user_info_response)) {
		    echo 'Hello';
            echo '<h3>Logged in as: <strong>' . $username . '</strong></h3>
            <br>
        	Not you? Click <a href="Logout.php">here</a> to switch users.<br><br>' .
        	'<h3>User info: </h3>' .
        	'<h4>Name: ' . $row['cust_first_name'] . ' ' . $row['cust_last_name'] . '</h4>' .
        	'<h4>E-mail: ' . $row['email_address'] . '<br><br>';
		}
    }
    
    $query_user_info = "SELECT * 
						FROM Customer 
						WHERE username = '" . $username . "'";
		
		$user_info_response = @mysqli_query($db, $query_user_info); 
		
		if($user_info_response) {
			while($row = mysqli_fetch_array($user_info_response)) {
        		echo '<h3>Logged in as: <strong>' . $username . '</strong></h3>
        		Not you? Click <a href="Logout.php">here</a> to switch users.<br><br>' .
        		'<h3>User info: </h3>' .
        		'<h4>Name: ' . $row['cust_first_name'] . ' ' . $row['cust_last_name'] . '</h4>' .
        		'<h4>E-mail: ' . $row['email_address'] . '<br><br>';
			}
	        
	        echo '</tr>';
		} else {
			echo "Couldn't issue database query<br />";
			echo mysqli_error($db);
		}
		
		$query = "SELECT Review.username, Review.review_date, Review.service_date, 
				Review.review_rating, Review.review_details, Review.service_id,
				Plumber.company_name, Plumber.internal_url
				FROM Review
				INNER JOIN Plumber
				ON Review.service_id = Plumber.service_id
				WHERE Review.username = '$username'
				ORDER BY Plumber.company_name DESC";

		$response = @mysqli_query($db, $query);

		if($response){
			echo '<h3> Your reviews:</h3>' .
	    	'<table class="reviews" align="left" cellspacing="5" cellpadding="8" width="50%">
				<td align="left"><b>Company Name</b></td>
				<td align="left"><b>Review Date</b></td>
				<td align="left"><b>Service Date</b></td>
				<td align="left"><b>Review Rating</b></td>
				<td align="left"><b>Review Details</b></td>';
	
    		while($row = mysqli_fetch_array($response)) {
        		echo '<tr>
        		<td align="left"><a color="black" href="Plumber.php?id='. 
        			$row['service_id'] .'">' . $row['company_name'].'</a></td>
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
    
include("footer.php");?>