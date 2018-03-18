<!DOCTYPE html>
<html lang="en">
  <?php include('header.php');
    if(!isset($db)){
			 require_once('connect.php');
		}
    session_start();
		$current_user = $_SESSION['username'];
		
		
		  echo'
      <!-- Masthead -->
      <header class="masthead text-white text-center">
  	    <div class="overlay"></div>   
	      <h1>Register for CardGuard</h1>
      </header>
    
      <section class="register-form">
        <div class="container">
          <div class="row">
            <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
              <form method="post" action="process_registration.php">
			          <div class="">
				          <label><b>First Name</b></label>
                  <input type="name" class="form-control form-control-lg" placeholder="First name" name="reg_first_name" required>
				          <br />
                </div>
				      
  				      <div class="">
				          <label><b>Last Name</b></label>
                  <input type="name" class="form-control form-control-lg" placeholder="Last name" name="reg_last_name" required>
				          <br />
                </div>
                
                <div class="">
				          <label><b>Username</b></label>
                  <input type="name" class="form-control form-control-lg" placeholder="Username" name="reg_username" required>
				          <br />
                </div>
                
                <div class="">
  				        <label><b>Email</b></label>
                  <input type="email" class="form-control form-control-lg" placeholder="Email" name="reg_email" required>
      				    <br />
                </div>
				
				        <div class="">
  				        <label><b>Password</b></label>
                  <input type="password" class="form-control form-control-lg" placeholder="Password" name="reg_password" required>
				          <br />
                </div>
                
                <div class="">
  				        <label><b>Confirm Password</b></label>
                  <input type="password" class="form-control form-control-lg" placeholder="Confirm Password" name="reg_confirm_password" required>
				          <br />
                </div>
				
				        <div class="form-row">
  				        <button type="submit" align="middle" class="btn  btn-lg btn-basic">Register</button>
                </div>
              </form>
            </div>
          </div>
        </div>
  	    <!--- <div class="container-fluid">
	        <div class="form-row">
            <button type="submit" class="btn btn-block btn-lg btn-basic" name="reg_btn">Register</button>
          </div> --->
    	  </div>
	    </section>';
	
	  echo'
      <!-- Footer -->
      <footer class="footer bg-light">
        <div class="container">
          <div class="row">
            <div class="col-lg-6 h-100 text-center text-lg-left my-auto">
              <ul class="list-inline mb-2">
                <li class="list-inline-item">
                  <a href="about.php">About</a>
                </li>
                <li class="list-inline-item">&sdot;</li>
                <li class="list-inline-item">
                  <a href="contact.php">Contact</a>
                </li>
                <li class="list-inline-item">&sdot;</li>
                <li class="list-inline-item">
                  <a href="termsfeed-terms-service-html-english.php">Terms of Use</a>
                </li>
                <li class="list-inline-item">&sdot;</li>
                <li class="list-inline-item">
                  <a href="privacypolicy.php">Privacy Policy</a>
                </li>
              </ul>
              <p class="text-muted small mb-4 mb-lg-0">&copy; Start Bootstrap 2017. All Rights Reserved.</p>
            </div>
            <div class="col-lg-6 h-100 text-center text-lg-right my-auto">
              <ul class="list-inline mb-0">
                <li class="list-inline-item mr-3">
                  <a href="#">
                    <i class="fa fa-facebook fa-2x fa-fw"></i>
                  </a>
                </li>
                <li class="list-inline-item mr-3">
                  <a href="#">
                    <i class="fa fa-twitter fa-2x fa-fw"></i>
                  </a>
                </li>
                <li class="list-inline-item">
                  <a href="#">
                    <i class="fa fa-instagram fa-2x fa-fw"></i>
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </footer>

    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>'
  ?>
  </body>

</html>
