<?php
    include("header.php");
    include("footer.php");
    
    $recovery_email = $_POST['recovery_email'];
    
    $query = "SELECT email_address
                FROM users
                WHERE email_address='$recovery_email'";
                
    $response = @mysqli_query($db, $query);
    
    $message = 'You did it!';
    $headers = 'From: webmaster@cardguard.com' . "\r\n" .
                'Reply-To: webmaster@cardguard.com';
    
    if($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['email_address'] == $recovery_email) {
                echo mail($recovery_email, $message, $message, $headers);
                exit;
            }
        }
        
        echo'Email address not found, please try again.';
    }
?>