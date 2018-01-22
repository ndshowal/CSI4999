<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<?php
        if(!isset($db)){
			require_once('Connect.php');
		}
		session_start();
		
		$_SESSION['profile_message'] = "";
		
		$plumber_id = $_GET['id'];
		
		$query = "SELECT * FROM Plumber
				WHERE Plumber.service_id='$plumber_id'";
		$response = @mysqli_query($db, $query);
		
		if($response) {
			while($row = mysqli_fetch_array($response)) {
				echo '<html xmlns="http://www.w3.org/1999/xhtml">
				<head>
					<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
					<link rel="stylesheet" href="style.css">
				</head>
				<title>'.$row['company_name'].'</title>
				<body>
					<h1 align="left">PlumbersByU</h1>
					<table class="navbar">
						<tr>
							<th scope="col"><a href="Home.php">Home</a></th>
							<th scope="col"><a href="Search.php">Search</a></th>
							<th scope="col"><a href="Profile.php">Profile</a></th>
							<th scope="col"><a href="Login.php">Login</a></th>
						</tr>
					</table>';
				echo'
				<h2>'. $row['company_name'].'</h2>
				<h4><strong>Website: </strong><a href="'.$row['website_url'].'">'.$row['website_url'].'</a></h4>
				<h4><strong>Phone number: </strong>'.$row['phone_number'].'</h4>
				<h4><strong>Address: </strong>'.$row['full_address'].'</h4>
				
				<br>
				
				<a href="Review.php">Click here to review this plumber</a>
				
				<br><br>';
			}
			
			
        	$_SESSION['plumber_id'] = $plumber_id;
		}
		
		$query2 = "SELECT Review.username, Review.service_date, Review.review_date, 
        		Review.review_rating, Review.review_details, Review.service_id,
			    Plumber.company_name
		        FROM Review
		        INNER JOIN Plumber
		        ON Review.service_id = Plumber.service_id
		        WHERE Plumber.service_id='$plumber_id'";
		
		switch($_GET['sort']) {
			case 'username':
    			$query2 .= " ORDER BY username";
    			break;
			case 'service_date': 
    			$query2 .= " ORDER BY service_date DESC";
    			break;
			case 'review_date':
    			$query2 .= " ORDER BY review_date DESC";
    			break;
			case 'review_rating':
    			$query2 .= " ORDER BY review_rating DESC";
    			break;
		}
		
		$response2 = @mysqli_query($db, $query2);
		
        if ($response2) {
        	
        	echo '<table class="reviews" align="left" table-striped cellspacing="5" cellpadding="8" width="50%">
			<th align="left"><b><a href="Plumber.php?id='.$plumber_id.'&sort=username">Username</a></b></td>
			<th align="left"><b><a href="Plumber.php?id='.$plumber_id.'&sort=service_date">Service Date</a></b></td>
			<th align="left"><b><a href="Plumber.php?id='.$plumber_id.'&sort=review_date">Review Date</a></b></td>
			<th align="left"><b><a href="Plumber.php?id='.$plumber_id.'&sort=review_rating">Review Rating</a></b></td>
			<td align="left" width="50%"><b>Review Details</b></td>';
	        
	        while($row = mysqli_fetch_array($response2)) {
                echo '<tr><td align="left">' .
                $row['username'] . '</td>
                <td align="left">' . $row['service_date'] . '</td>
                <td align="left">' . $row['review_date'] . '</td>
                <td align="left">' . $row['review_rating'] . '</td>
	            <td align="left">' . $row['review_details'] . '</td>';
        
                echo '</tr>';
                
                $plumber = $row['company_name'];
                $_SESSION['current_plumber'] = $plumber;
            }
            
            echo '</table>';
        } else {
    	    echo "Couldn't issue database query<br />";
    	    echo mysqli_error($db);
        }
    
	
	echo'
		</body>	
	</html>'
?>