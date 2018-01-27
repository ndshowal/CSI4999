<?php
    include("header.php");
    include("footer.php");
    $firstname = $_SESSION['first_name'];
    
    echo' 
    <section class="register-form">
	  <div class="container">
        <div class="row">
          <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
            <h1>Registered succesfully! Welcome, '. $firstname .'!</h1>
            <head><meta http-equiv="refresh" content="2;profile.php"></head>
          </div>
        </div>
      </div>
    </section>';
?>