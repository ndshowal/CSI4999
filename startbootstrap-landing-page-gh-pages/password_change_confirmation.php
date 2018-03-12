<?php
    include("header.php");
    include("footer.php");
    
    if(!$_SESSION['admin_change']) {
      echo' 
      <section class="confirmation">
	      <div class="container">
            <div class="row">
              <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
              <br><br><br><br><br><br>
                <h1>Password changed succesfully!</h1>
                <head><meta http-equiv="refresh" content="2;profile.php"></head>
              </div>
            </div>
          </div>
        </section>';
    } else {
       echo' 
        <section class="confirmation">
	      <div class="container">
            <div class="row">
              <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
              <br><br><br><br><br><br>
                <h1>Password changed succesfully!</h1>
                <head><meta http-equiv="refresh" content="2;admin_process_user_search.php"></head>
              </div>
            </div>
          </div>
        </section>';
    }