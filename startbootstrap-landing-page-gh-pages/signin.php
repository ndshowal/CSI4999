<?php include('header.php');
  echo'
    <header class="masthead text-white text-center">
    <div class="overlay"></div>
    <div class="container">
      <div class="row">
        <div class="col-xl-9 mx-auto">
          <h1 class="mb-5">Sign in to your CardGuard Account</h1>
	      </div>
      </div>
    </div>
  </header>
  	
  	<section class="register-form">
		  <div class="container">
        <div class="row">
          <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
            <form method="post" action="process_login.php">
              <div class="">
  							<label><b>Username</b></label>
                <input type="text" class="form-control form-control-lg" name="login_username" placeholder="Username">
				        <br />
              </div>
				      <div class="">
  				    	<label><b>Password</b></label>
                <input type="password" class="form-control form-control-lg" name="login_password" placeholder="Password">
				        <br />
              </div>
              <div class="form-row">
                <button type="submit" align="middle" class="btn btn-lg btn-basic" name="login_btn">Sign in</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>';
include('footer.php');?>