<?php
    include("header.php");
    include("footer.php");
    $firstname = $_SESSION['first_name'];
    $account_type = $_SESSION['account_type'];
    
    if($account_type == 'Admin') {
      echo '<head><meta http-equiv="refresh" content="0;admin.php"></head>';
    } else {
      echo' 
      <section class="register-form">
	    <div class="container">
          <div class="row">
            <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
              <h1>Signed in succesfully! Welcome back, '. $firstname .'!</h1>
              <head><meta http-equiv="refresh" content="2;profile.php"></head>
            </div>
          </div>
        </div>
      </section>';
    }
    
?>