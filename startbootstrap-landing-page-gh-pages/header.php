<!DOCTYPE html>
<html lang="en">
  <?php
    if(!isset($db)){
			 require_once('connect.php');
		}
		
    session_start();
		
	  $id = $_SESSION['id'];
	  $current_user = $_SESSION['username'];
		
		$query = "SELECT * FROM users WHERE id=$id";
		
		$response = @mysqli_query($db, $query);
                
    if ($response) {
        while($row = mysqli_fetch_array($response)) {
            if($row['username'] == $login_username) {
                $_SESSION['username'] = $row['username'];
                $current_user = $row['username'];
                echo $current_user;
            } 
        }
    }
		//
      echo'
      <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
  
        <title>CardGuard</title>
  
        <!-- Bootstrap core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="css/landing-page.min.css" rel="stylesheet">
        
        <!-- Custom fonts for this template -->
        <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="vendor/simple-line-icons/css/simple-line-icons.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
  
        <!-- Custom styles for this template -->
        <link href="css/landing-page.min.css" rel="stylesheet">
        <link href="style.css" rel="stylesheet">
      </head>';
    if($_SESSION['logged_in'] == false || $_SESSION['logged_in'] == null) {
      echo'
      <body>
        <!-- Navigation -->
        <nav class="navbar navbar-light bg-light static-top navbar-expand-md">
          <div class="container">
         
            <a class="navbar-brand" href="index.php">CardGuard</a>
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
             <span class="navbar-toggler-icon"></span>
           </button>
            <div class="nav-menu collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav>
                <li><a href="about.php"></a></li>
                <li><a href="about.php">About</a></li>
                <li><a href="contact.php">Contact</a></li>
              </ul>
              <a class="btn btn-primary" href="register.php">Register</a>
            	<a class="btn btn-primary" href="signin.php">Sign In</a>
            </div>
          </div>
        </nav>
      </body>';
		} else {
		  echo'
		   <body>
        <!-- Navigation -->
        <nav class="navbar navbar-light bg-light static-top navbar-expand-md">
          <div class="container">
            <a class="navbar-brand" href="index.php">CardGuard</a>
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
             <span class="navbar-toggler-icon"></span>
            </button>
            <div class="nav-menu collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav>
                <li><a href="about.php"></a></li>
                <li><a href="about.php">About</a></li>
                <li><a href="contact.php">Contact</a></li>
              </ul>
            	<a class="btn btn-primary" href="profile.php">' . $current_user . '</a>
    			    <a class="btn btn-primary" href="signout.php">Sign Out </a>
            </div>
          </div>
        </nav>
      </body>';
		}
  ?>
</html>