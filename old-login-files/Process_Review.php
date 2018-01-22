<?php
    if(!isset($db)) {
		require_once('Connect.php');
	} 
	
	session_start();
		
    $username = $_SESSION['username'];
    $service_date = $_POST['service_date'];
    $review_date = date("Y-m-d");
    $review_details = mysqli_real_escape_string($db, ($_POST['review_details']));
    $review_rating = $_POST['review_rating'];
    $service_id = $_SESSION['plumber_id'];
    
    $query = "INSERT INTO Review(review_id, service_date, review_date, 
            review_details, review_rating, username, service_id)
            VALUES(NULL, '$service_date', '$review_date', 
            '$review_details', '$review_rating', '$username', '$service_id')";
            
    $query2 = "SELECT username FROM Review";
    
    $response = @mysqli_query($db, $query);
                
    if ($response) {
        echo 'Thank you for your review!<br><br>';
        echo '<a href="Profile.php">Proceed</a>';
    } else {
        echo $username . '<br><br>';
        echo'Error description: ' . mysqli_error($db) . '<br><a href="Profile.php">Back</a>';
        
    }
?>